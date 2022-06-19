package com.aminsoheyli.androidtutorial.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.ui.ItemChangedInterface
import com.aminsoheyli.androidtutorial.utilities.Utility

class UserPassAdapter(
    private val dataSet: ArrayList<UserInfo>,
    private val itemChangedInterface: ItemChangedInterface
) :
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
        (viewHolder.textViewUserId.parent as LinearLayout).setOnLongClickListener {
            val alert = AlertDialog.Builder(it.context)
            alert.setTitle("Remove")
                .setMessage("Do you want to delete ${user.username}")
                .setPositiveButton("Yes") { dialog, which ->
                    dataSet.removeAt(position)
                    notifyDataSetChanged()
                    itemChangedInterface.onItemDeleted(user.id)
                    Utility.showSnackBar(it, "✅ Deleted $position")
                }
                .setNegativeButton("No") { dialog, which ->
                    Utility.showSnackBar(it, "❎ Canceled")
                }
            alert.show()
            false
        }
    }

    override fun getItemCount() = dataSet.size
}