package com.aminsoheyli.apiwebservice.component.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.apiwebservice.R
import org.json.JSONObject

const val TAG_LOG = "JOBS"

const val JSON_FROM_URL = "{" +
        "'info':{'name':'John','age':27 }," +
        "'jobs': [" +
        "{'id':1, 'title':'developer','desc':'nyc'}," +
        "{'id':2, 'title':'programmer','desc':'wa'}," +
        "{'id':3, 'title':'monkey-coder','desc':'ca'}" +
        "]" +
        "}"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parseJson()
    }

    private fun parseJson() {
        val json = JSONObject(JSON_FROM_URL)
        val info = json.getJSONObject("info")
        val name = info.getString("name")
        val age = info.getInt("age")
        val jobs = json.getJSONArray("jobs")
        Log.d(TAG_LOG, "Info= name: $name, age: $age")
        Log.d(TAG_LOG, "----Jobs----")
        for (i in 0 until jobs.length()) {
            val job = jobs.getJSONObject(i)
            val id = job.getInt("id")
            val title = job.getString("title")
            val desc = job.getString("desc")
            Log.d(TAG_LOG, "Job= id: $id, title: $title, desc: $desc")
        }
    }
}