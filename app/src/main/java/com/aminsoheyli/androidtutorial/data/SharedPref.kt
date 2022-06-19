package com.aminsoheyli.androidtutorial.data

import android.content.Context
import android.content.SharedPreferences
import java.lang.StringBuilder

private const val USERNAME_KEY = "username"
private const val PASSWORD_KEY = "password"

class SharedPref(context: Context) {
    private var sharedPreference: SharedPreferences =
        context.getSharedPreferences("shared_preferences_section", Context.MODE_PRIVATE)

    fun saveData(username: String, password: String) {
        val editor = sharedPreference.edit()
        editor.putString(USERNAME_KEY, username)
        editor.putString(PASSWORD_KEY, password)
        editor.apply()
    }

    fun loadData(): String {
        val fileContent = StringBuilder()
        fileContent.append("UserName:")
        fileContent.append(sharedPreference.getString(USERNAME_KEY, "No Name"))
        fileContent.append(",Password:")
        fileContent.append(sharedPreference.getString(PASSWORD_KEY, "No Password"))
        return fileContent.toString()
    }
}