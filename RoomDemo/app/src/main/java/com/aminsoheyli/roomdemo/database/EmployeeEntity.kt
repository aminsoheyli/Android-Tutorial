package com.aminsoheyli.roomdemo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val EMPLOYEE_TABLE_NAME = "employee-table"

@Entity(tableName = EMPLOYEE_TABLE_NAME)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    // ColumnInfo -> name is the internal name for the column name of this table and it's optional
    @ColumnInfo(name = "email-id")
    var email: String = ""
)