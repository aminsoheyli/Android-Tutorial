package com.aminsoheyli.androidtutorial.ui

import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.data.Contact
import com.aminsoheyli.androidtutorial.data.ContactAdapter
import com.aminsoheyli.androidtutorial.utilities.Utility

private const val REQUEST_CODE_READ_CONTACT_PERMISSION = 1

class ContentProviderActivity : AppCompatActivity() {
    private val contactsList = ArrayList<Contact>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        recyclerView = findViewById(R.id.recyclerView_contacts)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        readContacts()
    }

    @SuppressLint("Range")
    private fun readContacts() {
        if (checkSelfPermission(READ_CONTACTS) == PERMISSION_GRANTED) {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            while (cursor?.moveToNext() == true) {
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(NUMBER))
                contactsList.add(Contact(name, phoneNumber))
            }
            recyclerView.adapter = ContactAdapter(contactsList)
            cursor?.close()
        } else if (!shouldShowRequestPermissionRationale(READ_CONTACTS))
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_CODE_READ_CONTACT_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_READ_CONTACT_PERMISSION ->
                if (grantResults[0] == PERMISSION_GRANTED)
                    readContacts()
                else
                    Utility.showSnackBar(
                        findViewById(R.id.recyclerView_contacts),
                        "You denied the contacts access"
                    )
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


}