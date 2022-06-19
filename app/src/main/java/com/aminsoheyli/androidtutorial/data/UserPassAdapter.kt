package com.aminsoheyli.androidtutorial.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R

class UserPassAdapter(private val dataSet: ArrayList<UserInfo>) :
    RecyclerView.Adapter<UserPassAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewUserId: TextView
        val textViewUserName: TextView
        val textViewUserPassword: TextView

        init {
            textViewUserId = view.findViewById(R.id.textView_userCard_id)
            textViewUserName = view.findViewById(R.id.textView_userCard_username)
            textViewUserPassword = view.findViewById(R.id.textView_userCard_password)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_user, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = dataSet[position]
        viewHolder.textViewUserId.text = user.id.toString()
        viewHolder.textViewUserName.text = user.username
        viewHolder.textViewUserPassword.text = user.password
    }

    override fun getItemCount() = dataSet.size
}