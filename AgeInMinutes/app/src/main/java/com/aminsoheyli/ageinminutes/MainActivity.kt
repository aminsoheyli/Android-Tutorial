package com.aminsoheyli.ageinminutes

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDatePicker: Button
    private lateinit var textViewSelectedDate: TextView
    private lateinit var textViewSelectedDateInMinutes: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        buttonDatePicker = findViewById(R.id.button_date_picker)
        textViewSelectedDate = findViewById(R.id.textView_selected_date)
        textViewSelectedDateInMinutes = findViewById(R.id.textView_in_minutes)
        buttonDatePicker.setOnClickListener {
            pickDate()
        }
    }

    private fun pickDate() {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this, { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                textViewSelectedDate.text = selectedDate
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val date = sdf.parse(selectedDate)
                val selectedDateInMinutes = date!!.time / 1000 / 60
                val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                val currentDateToMinutes = currentDate!!.time / 1000 / 60
                val differenceInMinutes = currentDateToMinutes - selectedDateInMinutes
                textViewSelectedDateInMinutes.text = differenceInMinutes.toString()
            }, year, month, day
        )
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
    }
}