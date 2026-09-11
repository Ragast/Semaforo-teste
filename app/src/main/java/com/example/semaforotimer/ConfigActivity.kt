package com.example.semaforotimer

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ConfigActivity : Activity() {
    private val defaultPeriodSeconds = 180L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val prefs = getSharedPreferences("semaforo", MODE_PRIVATE)
        val field = findViewById<EditText>(R.id.periodField)
        val save = findViewById<Button>(R.id.saveButton)
        val cancel = findViewById<Button>(R.id.cancelButton)

        field.setText((prefs.getLong("periodSeconds", defaultPeriodSeconds) / 60L).toString())

        save.setOnClickListener {
            val minutes = field.text.toString().trim().toLongOrNull()
            if (minutes != null && minutes > 0) {
                prefs.edit().putLong("periodSeconds", minutes * 60L).apply()
                finish()
            } else {
                field.error = "Introduz um número de minutos válido"
            }
        }
        cancel.setOnClickListener { finish() }
    }
}
