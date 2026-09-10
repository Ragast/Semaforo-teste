package com.example.semaforotimer

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    // TEMPOS DO SEMÁFORO — serão afinados quando medires os tempos reais.
    private val greenSeconds = 60L
    private val redSeconds = 60L

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var root: View
    private lateinit var stateText: TextView
    private lateinit var countdownText: TextView
    private lateinit var nextText: TextView
    private lateinit var syncButton: Button

    private val prefs by lazy {
        getSharedPreferences("semaforo", MODE_PRIVATE)
    }

    private var cycleStartMillis: Long = 0L

    private val ticker = object : Runnable {
        override fun run() {
            update()
            handler.postDelayed(this, 200L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.root)
        stateText = findViewById(R.id.stateText)
        countdownText = findViewById(R.id.countdownText)
        nextText = findViewById(R.id.nextText)
        syncButton = findViewById(R.id.syncButton)

        // Recupera a sincronização anterior. Assim, fechar e reabrir
        // a app não reinicia o ciclo.
        cycleStartMillis = prefs.getLong("cycleStartMillis", 0L)

        // Primeira utilização: se ainda não houver sincronização,
        // assume temporariamente que o ciclo começa em verde agora.
        if (cycleStartMillis == 0L) {
            cycleStartMillis = System.currentTimeMillis()
        }

        syncButton.setOnClickListener {
            // Premir "sync" exatamente no instante em que o semáforo
            // passa para VERDE. A hora fica guardada no telemóvel.
            cycleStartMillis = System.currentTimeMillis()
            prefs.edit().putLong("cycleStartMillis", cycleStartMillis).apply()
            update()
        }
    }

    override fun onResume() {
        super.onResume()
        update()
        handler.post(ticker)
    }

    override fun onPause() {
        handler.removeCallbacks(ticker)
        super.onPause()
    }

    private fun update() {
        val greenMs = greenSeconds * 1000L
        val redMs = redSeconds * 1000L
        val cycleMs = greenMs + redMs

        // Usa sempre o relógio atual. O ciclo continua mesmo com a app fechada.
        val elapsed = ((System.currentTimeMillis() - cycleStartMillis) % cycleMs + cycleMs) % cycleMs

        val green = elapsed < greenMs
        val remaining = if (green) greenMs - elapsed else cycleMs - elapsed

        root.setBackgroundColor(
            if (green) android.graphics.Color.rgb(35, 150, 65)
            else android.graphics.Color.rgb(205, 45, 45)
        )

        stateText.text = if (green) "VERDE" else "VERMELHO"
        countdownText.text = format(remaining)
        nextText.text = if (green) "até vermelho" else "até verde"
    }

    private fun format(ms: Long): String {
        val totalSeconds = (ms + 999L) / 1000L
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        return String.format("%02d:%02d", minutes, seconds)
    }
}
