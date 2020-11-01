package com.mfa.carikerjapariwisata.views.ui.create_job

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.mfa.carikerjapariwisata.R
import kotlinx.android.synthetic.main.activity_create_job.*
import java.text.SimpleDateFormat
import java.util.*


class CreateJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)

        val arrayList: ArrayList<String> = ArrayList()
        arrayList.add("JAVA")
        arrayList.add("ANDROID")
        arrayList.add("C Language")
        arrayList.add("CPP Language")
        arrayList.add("Go Language")
        arrayList.add("AVN SYSTEMS")

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.setAdapter(arrayAdapter)
        
        etDateEnd.setOnClickListener {
            val newCalendar: Calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate: Calendar = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                etDateEnd.setText(dateFormatter.format(newDate.getTime()))
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }
    }
}