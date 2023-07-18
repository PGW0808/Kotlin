package com.kotlin_assignment.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var tvSelectedDate : TextView? = null
    private var tvResultInMin : TextView? = null
    private var tvResultInHr : TextView? = null
    private var tvResultInDays : TextView? = null
    private var tvResultInWeeks : TextView? = null
    private var tvResultInYear : TextView? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //var result = tvSelectedDate ?: "01/01/2000"
        tvSelectedDate = findViewById(R.id.showDate)
        tvResultInMin = findViewById(R.id.resultAge)
        tvResultInHr = findViewById(R.id.resultAgeInHr)
        tvResultInDays = findViewById(R.id.resultAgeInDays)
        tvResultInWeeks = findViewById(R.id.resultAgeInWeeks)
        tvResultInYear = findViewById(R.id.resultAgeInYear)

        val btnDatePicker : Button = findViewById(R.id.btnDatePicker)

        btnDatePicker.setOnClickListener{
            clickDatePicker()
        }
    }

   private fun clickDatePicker(){

        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
       val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                tvSelectedDate?.text = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)

                theDate?.let{
                    val selectedDateInMinutes = theDate.time / 60000
                    val selectedDateInHr = selectedDateInMinutes / 60
                    val selectedDateInDays = selectedDateInHr / 24
                    val selectedDateInWeeks = selectedDateInDays / 7
                    val selectedDateInYear = selectedDateInWeeks / 52

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

                    currentDate?.let {
                        val currentDateInMinutes = currentDate.time / 60000
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                        val currentDateInHr = currentDateInMinutes / 60
                        val differenceInHr = currentDateInHr - selectedDateInHr

                        val currentDateInDays = currentDateInHr / 24
                        val differenceInDays = currentDateInDays - selectedDateInDays

                        val currentDateInWeeks = currentDateInDays / 7
                        val differenceInWeeks = currentDateInWeeks - selectedDateInWeeks

                        val currentDateInYear = currentDateInWeeks / 52
                        val differenceInYear = currentDateInYear - selectedDateInYear

                        tvResultInMin?.text = differenceInMinutes.toString()
                        tvResultInHr?.text = differenceInHr.toString()
                        tvResultInDays?.text = differenceInDays.toString()
                        tvResultInWeeks?.text = differenceInWeeks.toString()
                        tvResultInYear?.text = differenceInYear.toString()
                    }

                }
            },
            year,
            month,
            day
        )
            dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
            dpd.show()

        Toast.makeText(this, "Select Date", Toast.LENGTH_LONG).show()

    }
}


