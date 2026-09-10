package com.example.semaforotimer

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.app.Activity
import java.util.Locale

class MainActivity : Activity() {

    // ALTERA AQUI OS TEMPOS PARA O TEU SEMÁFORO:
    private val greenSeconds = 60L
    private val redSeconds = 60L

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var root: View
    private lateinit var stateText: TextView
    private lateinit var countdownText: TextView
    private lateinit var nextText: TextView
    private lateinit var syncButton: Button

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

        // Para o primeiro teste, assume-se que a app foi aberta no início de um verde.
        cycleStartMillis = System.currentTimeMillis()

        syncButton.setOnClickListener {
            // Premir "sync" exatamente quando o semáforo fica verde.
            cycleStartMillis = System.currentTimeMillis()
            update()
        }
    }

    override fun onResume() {
        super.onResume()
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
        val elapsed = ((System.currentTimeMillis() - cycleStartMillis) % cycleMs + cycleMs) % cycleMs

        val green = elapsed < greenMs
        val remaining = if (green) greenMs - elapsed else cycleMs - elapsed

        root.setBackgroundColor(if (green) Color.rgb(35, 150, 65) else Color.rgb(205, 45, 45))
        stateText.text = if (green) "VERDE" else "VERMELHO"
        countdownText.text = format(remaining)
        nextText.text = if (green) "até vermelho" else "até verde"
    }

    private fun format(ms: Long): String {
        val totalSeconds = (ms + 999L) / 1000L
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
