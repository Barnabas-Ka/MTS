package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var currentInput = ""
    private var firstOperand = 0.0
    private var currentOperator = ""
    private var isNewOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        setupButtons()
    }

    private fun setupButtons() {
        val numberButtons = listOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to "."
        )

        for ((id, value) in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                appendInput(value)
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { applyPercent() }

        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteLastDigit() }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
    }

    private fun appendInput(value: String) {
        if (isNewOperator) {
            currentInput = ""
            isNewOperator = false
        }

        if (value == "." && currentInput.contains(".")) return

        currentInput += value
        tvResult.text = currentInput
    }

    private fun setOperator(operator: String) {
        if (currentInput.isNotEmpty()) {
            firstOperand = currentInput.toDoubleOrNull() ?: 0.0
            currentOperator = operator
            isNewOperator = true
            tvExpression.text = "${formatNumber(firstOperand)} $operator"
        }
    }

    private fun applyPercent() {
        if (currentInput.isNotEmpty()) {
            val number = currentInput.toDoubleOrNull() ?: 0.0
            val percentResult = number / 100.0
            currentInput = percentResult.toString()
            tvResult.text = formatNumber(percentResult)
        }
    }

    private fun clearAll() {
        currentInput = ""
        firstOperand = 0.0
        currentOperator = ""
        isNewOperator = false
        tvExpression.text = ""
        tvResult.text = "0"
    }

    private fun deleteLastDigit() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            tvResult.text = if (currentInput.isEmpty()) "0" else currentInput
        }
    }

    private fun calculateResult() {
        if (currentOperator.isEmpty() || currentInput.isEmpty()) return

        val secondOperand = currentInput.toDoubleOrNull() ?: 0.0
        var result = 0.0

        when (currentOperator) {
            "+" -> result = firstOperand + secondOperand
            "-" -> result = firstOperand - secondOperand
            "×" -> result = firstOperand * secondOperand
            "÷" -> {
                if (secondOperand == 0.0) {
                    tvResult.text = "Erreur"
                    currentInput = ""
                    currentOperator = ""
                    return
                }
                result = firstOperand / secondOperand
            }
        }

        tvExpression.text = "${formatNumber(firstOperand)} $currentOperator ${formatNumber(secondOperand)} ="
        tvResult.text = formatNumber(result)
        currentInput = result.toString()
        currentOperator = ""
        isNewOperator = true
    }

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }
}
