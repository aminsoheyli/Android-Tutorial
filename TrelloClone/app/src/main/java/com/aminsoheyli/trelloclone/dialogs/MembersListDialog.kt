package com.aminsoheyli.trelloclone.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminsoheyli.trelloclone.adapters.MemberItemsAdapter
import com.aminsoheyli.trelloclone.databinding.DialogListBinding
import com.aminsoheyli.trelloclone.models.User

abstract class MembersListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
) : Dialog(context) {
    private lateinit var binding: DialogListBinding

    private var adapter: MemberItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
//        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)
        binding = DialogListBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.tvTitle.text = title
        if (list.size > 0) {
            binding.rvList.layoutManager = LinearLayoutManager(context)
            adapter = MemberItemsAdapter(context, list)
            binding.rvList.adapter = adapter
            adapter!!.setOnClickListener(object :
                MemberItemsAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, action: String)
}