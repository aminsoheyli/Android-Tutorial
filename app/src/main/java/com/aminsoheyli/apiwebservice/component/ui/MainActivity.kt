package com.aminsoheyli.apiwebservice.component.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.apiwebservice.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


const val TAG_LOG = "JOBS"

// TODO: Add this string to the info.json file in the raw directory and retrieve it using getResources()
const val JSON_FROM_URL = "{" +
        "'info':{'name':'John','age':27 }," +
        "'jobs': [" +
        "{'id':1, 'title':'developer','desc':'nyc'}," +
        "{'id':2, 'title':'programmer','desc':'wa'}," +
        "{'id':3, 'title':'monkey-coder','desc':'ca'}" +
        "]" +
        "}"

const val BTC_PRICE_URL = "https://api.coinlore.net/api/coin/markets/?id=90"


class MainActivity : AppCompatActivity() {
    private var coinFullName = "bitcoin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        parseJson()
    }

    private fun initUi() {
        findViewById<Button>(R.id.button_show_btc_price).setOnClickListener {
            getPrice(BTC_PRICE_URL)
        }
    }

    private fun getPrice(priceUrl: String): String {
        val client = AsyncHttpClient()
        client[priceUrl, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                // called before request is started
            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header?>?,
                response: ByteArray?
            ) {
                val json = JSONArray(String(response!!))
                val exchange = json.getJSONObject(0)
                val exchangeName = exchange.getString("name")
                val btcPrice = exchange.getString("price_usd")
                Toast.makeText(
                    this@MainActivity,
                    "BTC price($exchangeName): $btcPrice",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header?>?,
                errorResponse: ByteArray?,
                e: Throwable?
            ) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            override fun onRetry(retryNo: Int) {
                // called when request is retried
            }
        }]
        return ""
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