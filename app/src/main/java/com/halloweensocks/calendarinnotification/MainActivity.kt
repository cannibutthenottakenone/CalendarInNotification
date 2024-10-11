package com.halloweensocks.calendarinnotification

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.provider.CalendarContract
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var calendarList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //instantiate layout
        calendarList = findViewById(R.id.calendarsDisplay)

        //requesting permissions
        if (checkSelfPermission(android.Manifest.permission.READ_CALENDAR)==PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_CALENDAR), 0)
        }
        else {
            loadCalendars()
        }
    }

    private fun loadCalendars(){
        val calendars: Cursor?=contentResolver.query(CalendarContract.Calendars.CONTENT_URI, arrayOf(CalendarContract.Calendars.NAME, CalendarContract.Calendars._ID), null, null)
        if(calendars!=null && calendars.moveToFirst()){
            val nameColumn=calendars.getColumnIndex(CalendarContract.Calendars.NAME)
            val idColumn=calendars.getColumnIndex(CalendarContract.Calendars._ID)
            do {
                val calendarName=calendars.getString(nameColumn)
                val calendarid=calendars.getString(idColumn)
                val check=checkCreator(calendarName)
                calendarList.addView(check)

                //tv.text="${tv.text}\n$calendarName"
            }while (calendars.moveToNext())
            calendars.close()
        }
    }

    private fun checkCreator(text: String?): CheckBox{
        val check = CheckBox(this)
        check.text=text

        return check
    }
}