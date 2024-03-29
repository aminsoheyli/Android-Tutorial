package com.aminsoheyli.androidtutorial.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.data.*
import com.aminsoheyli.androidtutorial.utilities.Utility


class StorageActivity : AppCompatActivity(), ItemChangedInterface {
    private lateinit var buttonDialog: Button
    private lateinit var buttonAlert: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var sharedPref: SharedPref
    private lateinit var dbManager: DBManager
    private lateinit var recyclerView: RecyclerView
    private var usersInfo = arrayListOf<UserInfo>()
    private var isUpdating = false
    private lateinit var currentUserInfo: UserInfo
    private var currentUserPosition: Int = -1
    private lateinit var userPassAdapter: UserPassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        sharedPref = SharedPref(this)
        dbManager = DBManager(this)
        initUi()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUi() {
        buttonDialog = findViewById(R.id.button_show_dialog)
        buttonAlert = findViewById(R.id.button_show_alert)
        buttonSave = findViewById(R.id.button_save)
        buttonLoad = findViewById(R.id.button_load)
        editTextUsername = findViewById(R.id.editText_username)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        recyclerView = findViewById(R.id.recyclerView_users)


        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userPassAdapter = UserPassAdapter(usersInfo, this, buttonAlert)
        recyclerView.adapter = userPassAdapter

        buttonDialog.setOnClickListener {
            val popTime = PopTime()
            popTime.show(supportFragmentManager, "Show dialog fragment")
        }

        buttonAlert.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Delete document?")
                .setTitle("Confirmation")
                .setPositiveButton("Yes") { _, _ ->
                    showPopUpMessage("✅ Deleted")
                }
                .setNegativeButton("No") { _, _ ->
                    showPopUpMessage("❎ Canceled")
                }
            alert.show()
        }

        editTextUsername.doOnTextChanged { text, _, _, _ ->
            val namePattern = text.toString()

            var selection: String? = null
            var selectionArgs: Array<String>? = null
            if (namePattern.isNotEmpty()) {
                selection = "username like ?"
                selectionArgs = arrayOf("%$namePattern%")
            }
            val cursor = dbManager.query(null, selection, selectionArgs, null)
            val idColumnIndex = cursor.getColumnIndex("id")
            val usernameColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_USERNAME)
            val passwordColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_PASSWORD)
            if (cursor.moveToFirst()) {
                usersInfo.clear()
                do {
                    usersInfo.add(
                        UserInfo(
                            cursor.getInt(idColumnIndex),
                            cursor.getString(usernameColumnIndex),
                            cursor.getString(passwordColumnIndex)
                        )
                    )
                } while (cursor.moveToNext())
                (recyclerView.adapter as UserPassAdapter).notifyDataSetChanged()
            }
        }

        findViewById<Button>(R.id.button_update).setOnClickListener {
            if (isUpdating) {
                val values = ContentValues()
                values.put(DBManager.COLUMN_ID, currentUserInfo.id)
                values.put(DBManager.COLUMN_USERNAME, editTextUsername.text.toString())
                values.put(DBManager.COLUMN_PASSWORD, editTextPassword.text.toString())
                val selectionArgs = arrayOf(currentUserInfo.id.toString())
                dbManager.update(values, "id=?", selectionArgs)
                (recyclerView.adapter as UserPassAdapter).notifyDataSetChanged()
                editTextPassword.setText("")
                editTextUsername.setText("")
                editTextUsername.clearFocus()
                isUpdating = false
            }
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
            editTextUsername.setText("")
            editTextPassword.setText("")
            /*
            sharedPref.saveData(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
            */
        }

        buttonLoad.setOnClickListener {
            val namePattern = editTextUsername.text.toString()
            var selection: String? = null
            var selectionArgs: Array<String>? = null
            if (namePattern.isNotEmpty()) {
                selection = "username like ?"
                selectionArgs = arrayOf("%$namePattern%")
            }
            val cursor = dbManager.query(null, selection, selectionArgs, null)
            val idColumnIndex = cursor.getColumnIndex("id")
            val usernameColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_USERNAME)
            val passwordColumnIndex = cursor.getColumnIndex(DBManager.COLUMN_PASSWORD)
            if (cursor.moveToFirst()) {
                usersInfo.clear()
                do {
                    usersInfo.add(
                        UserInfo(
                            cursor.getInt(idColumnIndex),
                            cursor.getString(usernameColumnIndex),
                            cursor.getString(passwordColumnIndex)
                        )
                    )
                } while (cursor.moveToNext())
                (recyclerView.adapter as UserPassAdapter).notifyDataSetChanged()
            }
            /*
            val data = sharedPref.loadData()
            showPopUpMessage(data)
            */
        }
    }

    fun setTime(hour: Int, minute: Int) {
        val alarm = Alarm(applicationContext)
        alarm.setAlarm(hour, minute)
        alarm.saveAlarmData(hour, minute)
    }

    private fun showPopUpMessage(text: String) {
        Utility.showSnackBar(buttonSave, text)
    }

    override fun onItemDeleted(id: Int, position: Int): Boolean {
        val whereArgs = arrayOf(id.toString())
        return dbManager.delete("id=?", whereArgs) > 0
    }

    override fun onItemUpdate(userInfo: UserInfo, position: Int) {
        isUpdating = true
        editTextUsername.setText(userInfo.username)
        editTextPassword.setText(userInfo.password)
        currentUserInfo = userInfo
        currentUserPosition = position
    }


}

interface ItemChangedInterface {
    fun onItemDeleted(id: Int, position: Int): Boolean
    fun onItemUpdate(userInfo: UserInfo, position: Int)
}
