package com.aminsoheyli.androidtutorial.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.ui.ItemChangedInterface
import com.aminsoheyli.androidtutorial.utilities.Utility

class UserPassAdapter(
    private val dataSet: ArrayList<UserInfo>,
    private val itemChangedInterface: ItemChangedInterface,
    private val view: View
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
        val userInfo = dataSet[position]
        viewHolder.textViewUserId.text = userInfo.id.toString()
        viewHolder.textViewUserName.text = userInfo.username
        viewHolder.textViewUserPassword.text = userInfo.password
        viewHolder.itemView.setOnLongClickListener {
            val alert = AlertDialog.Builder(view.context)
            alert.setTitle("Remove")
                .setMessage("Do you want to delete ${userInfo.username}")
                .setPositiveButton("Yes") { _, _ ->
                    Utility.showSnackBar(view, "✅ Deleted $position")
                    dataSet.remove(userInfo)
                    if (itemChangedInterface.onItemDeleted(userInfo.id, position)) {
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                    }
                }
                .setNegativeButton("No") { _, _ ->
                    Utility.showSnackBar(view, "❎ Canceled")
                }
            alert.show()
            false
        }
        viewHolder.itemView.setOnClickListener {
            itemChangedInterface.onItemUpdate(userInfo, position)
        }
    }

    override fun getItemCount() = dataSet.size
}