package com.example.semaforotimer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {
    private val defaultPeriodSeconds = 180L
    private val prefs by lazy { getSharedPreferences("semaforo", MODE_PRIVATE) }
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var root: View
    private lateinit var stateText: TextView
    private lateinit var countdownText: TextView
    private lateinit var nextText: TextView
    private lateinit var configButton: Button
    private lateinit var syncButton: Button
    private var cycleStartMillis = 0L

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
        configButton = findViewById(R.id.configButton)
        syncButton = findViewById(R.id.syncButton)

        cycleStartMillis = prefs.getLong("cycleStartMillis", 0L)
        if (cycleStartMillis == 0L) cycleStartMillis = System.currentTimeMillis()

        configButton.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
        syncButton.setOnClickListener {
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
        val periodSeconds = prefs.getLong("periodSeconds", defaultPeriodSeconds)
        val periodMs = periodSeconds * 1000L
        val cycleMs = periodMs * 2L
        val elapsed = ((System.currentTimeMillis() - cycleStartMillis) % cycleMs + cycleMs) % cycleMs
        val green = elapsed < periodMs
        val remaining = if (green) periodMs - elapsed else cycleMs - elapsed

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
        return String.format("%02d:%02d", totalSeconds / 60L, totalSeconds % 60L)
    }
}
