package com.aminsoheyli.trelloclone.activities

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.adapters.MemberItemsAdapter
import com.aminsoheyli.trelloclone.databinding.ActivityMembersBinding
import com.aminsoheyli.trelloclone.databinding.DialogSearchMemberBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants

class MembersActivity : BaseActivity() {
    private lateinit var boardDetails: Board
    private lateinit var assignedMemberList: ArrayList<User>
    private var anyChangesMade = false
    private lateinit var binding: ActivityMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        setupFirebase()
    }

    private fun setupFirebase() {
        showProgressDialog()
        Firestore().getAssignedMembersListDetails(this, boardDetails.assignedTo)
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbarMembersActivity
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white_color_24dp)
            title = resources.getString(R.string.members)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initUi() {
        if (intent.hasExtra(Constants.BOARD_DETAIL))
            boardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        setupActionBar()
    }

    fun setupMembersList(list: ArrayList<User>) {
        assignedMemberList = list
        hideProgressDialog()
        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)
        val adapter = MemberItemsAdapter(this, list)
        binding.rvMembersList.adapter = adapter
    }

    fun memberDetails(user: User) {
        boardDetails.assignedTo.add(user.id)
        Firestore().assignMemberToBoard(this, boardDetails, user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_member -> {
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember() {
        val dialog = android.app.Dialog(this)
        val dialogBinding = DialogSearchMemberBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvAdd.setOnClickListener(View.OnClickListener {
            val email = dialogBinding.etEmailSearchMember.text.toString()
            if (email.isNotEmpty()) {
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                Firestore().getMemberDetails(this, email)
            } else {
                showErrorSnackBar("Please enter members email address.")
            }
        })
        dialogBinding.tvCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun memberAssignSuccess(user: User) {
        hideProgressDialog()
        assignedMemberList.add(user)
        anyChangesMade = true
        setupMembersList(assignedMemberList)
    }

    override fun onBackPressed() {
        if(anyChangesMade)
            setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}