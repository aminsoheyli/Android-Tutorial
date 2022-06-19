package com.aminsoheyli.androidtutorial.ui

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.data.DBManager
import com.aminsoheyli.androidtutorial.data.SharedPref
import com.aminsoheyli.androidtutorial.data.UserInfo
import com.aminsoheyli.androidtutorial.data.UserPassAdapter
import com.aminsoheyli.androidtutorial.utilities.Utility

class MainActivity : AppCompatActivity() {
    private lateinit var buttonDialog: Button
    private lateinit var buttonAlert: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var sharedPref: SharedPref
    private lateinit var dbManager: DBManager
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = SharedPref(this)
        dbManager = DBManager(this)
        initUi()
    }

    private fun initUi() {
        buttonDialog = findViewById(R.id.button_show_dialog)
        buttonAlert = findViewById(R.id.button_show_alert)
        buttonSave = findViewById(R.id.button_save)
        buttonLoad = findViewById(R.id.button_load)
        editTextUsername = findViewById(R.id.editText_username)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        recyclerView = findViewById(R.id.recyclerView_users)

        buttonDialog.setOnClickListener {
            val popTime = PopTime()
            popTime.show(supportFragmentManager, "Show dialog fragment")
        }

        buttonAlert.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Delete document?")
                .setTitle("Confirmation")
                .setPositiveButton("Yes") { dialog, which ->
                    showPopUpMessage("✅ Deleted")
                }
                .setNegativeButton("No") { dialog, which ->
                    showPopUpMessage("❎ Canceled")
                }
            alert.show()
        }

        buttonSave.setOnClickListener {
            val values = ContentValues()
            values.put(DBManager.COLUMN_USERNAME, editTextUsername.text.toString())
            values.put(DBManager.COLUMN_PASSWORD, editTextPassword.text.toString())
            val id = dbManager.insert(values)
            if (id > -1)
                Utility.showSnackBar(it, "User id: $id")
            else
                Utility.showSnackBar(it, "Can't insert")

            /*sharedPref.saveData(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )*/
        }

        buttonLoad.setOnClickListener {
            val projection = { "username, password" }
            val cursor = dbManager.query(null, null, null, null)
            val idColumnIndex = cursor.getColumnIndex("id")
            val usernameColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_USERNAME)
            val passwordColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_PASSWORD)
            if (cursor.moveToFirst()) {
                val usersInfo = arrayListOf<UserInfo>()
                do {
                    usersInfo.add(
                        UserInfo(
                            cursor.getInt(idColumnIndex),
                            cursor.getString(usernameColumnIndex),
                            cursor.getString(passwordColumnIndex)
                        )
                    )
                } while (cursor.moveToNext())
                if (usersInfo.isNotEmpty()) {
                    recyclerView.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recyclerView.adapter = UserPassAdapter(usersInfo)
                }
            }

            /*val data = sharedPref.loadData()
            showPopUpMessage(data)*/
        }
    }

    fun setTime(time: String) {
        val text = "Time: $time";
        showPopUpMessage(text)
    }

    private fun showPopUpMessage(text: String) {
        Utility.showSnackBar(buttonSave, text)
    }
}