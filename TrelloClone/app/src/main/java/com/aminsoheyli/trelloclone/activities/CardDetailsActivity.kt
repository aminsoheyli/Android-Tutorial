package com.aminsoheyli.trelloclone.activities

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityCardDetailsBinding
import com.aminsoheyli.trelloclone.dialogs.LabelColorListDialog
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.Card
import com.aminsoheyli.trelloclone.models.Task
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants

class CardDetailsActivity : BaseActivity() {
    private lateinit var boardDetails: Board
    private var taskListPosition: Int = -1
    private var cardPosition: Int = -1
    private var selectedColor: String = ""

    private lateinit var membersDetailList: ArrayList<User>

    private lateinit var binding: ActivityCardDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setupActionBar()
        binding.etNameCardDetails.setText(boardDetails.taskList[taskListPosition].cards[cardPosition].name)
        binding.etNameCardDetails.setSelection(binding.etNameCardDetails.text.toString().length)
        selectedColor = boardDetails.taskList[taskListPosition].cards[cardPosition].labelColor
        if (selectedColor.isNotEmpty())
            setColor()
        binding.tvSelectLabelColor.setOnClickListener { labelColorsListDialog() }
//        setupSelectedMembersList()
//        binding.tvSelectMembers.setOnClickListener { membersListDialog() }
        binding.btnUpdateCardDetails.setOnClickListener {
            if (binding.etNameCardDetails.text.toString().isNotEmpty())
                updateCardDetails()
            else
                Toast.makeText(this@CardDetailsActivity, "Enter card name.", Toast.LENGTH_SHORT)
                    .show()

        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_color_24dp)
            actionBar.title = boardDetails.taskList[taskListPosition].cards[cardPosition].name
        }
        binding.toolbarCardDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION))
            taskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION))
            cardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        if (intent.hasExtra(Constants.BOARD_DETAIL))
            boardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        if (intent.hasExtra(Constants.BOARD_MEMBERS_LIST))
            membersDetailList = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(boardDetails.taskList[taskListPosition].cards[cardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertDialogForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(
            resources.getString(
                R.string.confirmation_message_to_delete_card,
                cardName
            )
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCardDetails() {
        val card = Card(
            binding.etNameCardDetails.text.toString(),
            boardDetails.taskList[taskListPosition].cards[cardPosition].createdBy,
            boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo,
            selectedColor
        )
        val taskList: ArrayList<Task> = boardDetails.taskList
        taskList.removeAt(taskList.size - 1)
        boardDetails.taskList[taskListPosition].cards[cardPosition] = card
        showProgressDialog()
        Firestore().addUpdateTaskList(this, boardDetails)
    }


    private fun deleteCard() {
        val cardsList: ArrayList<Card> = boardDetails.taskList[taskListPosition].cards
        cardsList.removeAt(cardPosition)
        val taskList: ArrayList<Task> = boardDetails.taskList
        taskList.removeAt(taskList.size - 1)
        taskList[taskListPosition].cards = cardsList
        showProgressDialog()
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    private fun setColor() {
        binding.tvSelectLabelColor.text = ""
        binding.tvSelectLabelColor.setBackgroundColor(Color.parseColor(selectedColor))
    }

    private fun colorsList(): ArrayList<String> {
        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")
        return colorsList
    }

    private fun labelColorsListDialog() {
        val colorsList: ArrayList<String> = colorsList()
        val listDialog = object : LabelColorListDialog(
            this, colorsList, resources.getString(R.string.str_select_label_color), selectedColor
        ) {
            override fun onItemSelected(color: String) {
                selectedColor = color
                setColor()
            }
        }
        listDialog.show()
    }
//
//    private fun membersListDialog() {
//        val cardAssignedMembersList =
//            boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo
//        if (cardAssignedMembersList.size > 0)
//            for (i in membersDetailList.indices)
//                for (j in cardAssignedMembersList) {
//                    if (membersDetailList[i].id == j) {
//                        membersDetailList[i].selected = true
//                    }
//                }
//        else for (i in membersDetailList.indices)
//            membersDetailList[i].selected = false
//
//        val listDialog = object : MembersListDialog(
//            this, membersDetailList, resources.getString(R.string.str_select_member)
//        ) {
//            override fun onItemSelected(user: User, action: String) {
//                if (action == Constants.SELECT) {
//                    if (!boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo.contains(
//                            user.id
//                        )
//                    ) {
//                        boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo.add(
//                            user.id
//                        )
//                    }
//                } else {
//                    boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo.remove(
//                        user.id
//                    )
//
//                    for (i in membersDetailList.indices) {
//                        if (membersDetailList[i].id == user.id) {
//                            membersDetailList[i].selected = false
//                        }
//                    }
//                }
//                setupSelectedMembersList()
//            }
//        }
//        listDialog.show()
//    }
//
//    private fun setupSelectedMembersList() {
//        val cardAssignedMembersList =
//            boardDetails.taskList[taskListPosition].cards[cardPosition].assignedTo
//        val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()
//        for (i in membersDetailList.indices)
//            for (j in cardAssignedMembersList)
//                if (membersDetailList[i].id == j) {
//                    val selectedMember = SelectedMembers(
//                        membersDetailList[i].id,
//                        membersDetailList[i].image
//                    )
//                    selectedMembersList.add(selectedMember)
//                }
//        if (selectedMembersList.size > 0) {
//            selectedMembersList.add(SelectedMembers("", ""))
//            tv_select_members.visibility = View.GONE
//            rv_selected_members_list.visibility = View.VISIBLE
//            rv_selected_members_list.layoutManager = GridLayoutManager(this@CardDetailsActivity, 6)
//            val adapter = CardMemberListItemsAdapter(this@CardDetailsActivity, selectedMembersList)
//            rv_selected_members_list.adapter = adapter
//            adapter.setOnClickListener(object :
//                CardMemberListItemsAdapter.OnClickListener {
//                override fun onClick() {
//                    membersListDialog()
//                }
//            })
//        } else {
//            tv_select_members.visibility = View.VISIBLE
//            rv_selected_members_list.visibility = View.GONE
//        }
//    }
}