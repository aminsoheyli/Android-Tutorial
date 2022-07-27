package com.aminsoheyli.roomdemo

import android.app.Application
import com.aminsoheyli.roomdemo.database.EmployeeDatabase

class EmployeeApp : Application() {
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }
}