package com.aminsoheyli.androidtutorial.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R

class ContactAdapter(private val dataSet: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewPhoneNumber: TextView

        init {
            textViewName = view.findViewById(R.id.textView_name)
            textViewPhoneNumber = view.findViewById(R.id.textView_number)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_contact, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val userInfo = dataSet[position]
        viewHolder.textViewName.text = userInfo.name
        viewHolder.textViewPhoneNumber.text = userInfo.phoneNumber
    }

    override fun getItemCount() = dataSet.size
}