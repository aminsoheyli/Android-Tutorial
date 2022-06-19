package com.aminsoheyli.androidtutorial.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import com.aminsoheyli.androidtutorial.utilities.Utility

class DBManager(context: Context) {
    private val db = DatabaseHelperUser(context)
    private val sqlDB = db.writableDatabase

    companion object {
        const val DB_NAME = "Students"
        const val TABLE_NAME = "Logins"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val DB_VERSION = 1

        // CREATE TABLE IF NOT EXIST Logins (id	INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password	TEXT);
        const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $COLUMN_USERNAME TEXT," +
                    "$COLUMN_PASSWORD TEXT);"

        class DatabaseHelperUser(private val context: Context) :
            SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL(CREATE_TABLE)
                Utility.showToast(context, "Table is created")
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
                onCreate(db)
            }

        }
    }

    fun insert(values: ContentValues): Long {
        val id = sqlDB.insert(TABLE_NAME, "", values)
        return id
    }

    // Select username, password from Logins where ID=1
    fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME

        return qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
    }

    fun delete(whereClause: String, whereArgs: Array<String>?): Int {
        return sqlDB.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun update(values: ContentValues, selection: String?, selectionArgs: Array<String>?): Int {
        return sqlDB.update(TABLE_NAME, values, selection, selectionArgs)
    }

}