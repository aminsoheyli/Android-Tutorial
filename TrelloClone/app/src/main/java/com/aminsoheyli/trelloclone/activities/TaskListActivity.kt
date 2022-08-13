package com.aminsoheyli.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.adapters.TaskItemsAdapter
import com.aminsoheyli.trelloclone.databinding.ActivityTaskListBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.Card
import com.aminsoheyli.trelloclone.models.Task
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants

class TaskListActivity : BaseActivity() {
    companion object {
        const val MEMBERS_REQUEST_CODE: Int = 1
        const val CARD_DETAILS_REQUEST_CODE: Int = 2
    }

    private lateinit var boardDetails: Board
    private lateinit var boardDocumentId: String
    lateinit var assignedMembersDetailList: ArrayList<User>
    private lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        setupFirebase()
    }

    private fun initUi() {
        showProgressDialog()
        if (intent.hasExtra(Constants.DOCUMENT_ID))
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
    }

    private fun setupFirebase() {
        Firestore().getBoardDetails(this, boardDocumentId)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_color_24dp)
            actionBar.title = boardDetails.name
        }
        binding.toolbarTaskListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun showBoardDetails(board: Board) {
        boardDetails = board
        hideProgressDialog()
        setupActionBar()

        showProgressDialog()
        Firestore().getAssignedMembersListDetails(this, boardDetails.assignedTo)
    }

    fun boardMembersDetailList(list: ArrayList<User>) {
        assignedMembersDetailList = list
        hideProgressDialog()
        val addTaskList = Task(resources.getString(R.string.add_list))
        boardDetails.taskList.add(addTaskList)
        binding.rvTaskList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaskList.setHasFixedSize(true)
        val adapter = TaskItemsAdapter(this, boardDetails.taskList)
        binding.rvTaskList.adapter = adapter
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        showProgressDialog()
        Firestore().getBoardDetails(this, boardDetails.documentId)
    }

    fun createTaskList(taskListName: String) {
        Log.e("Task List Name", taskListName)
        val task = Task(taskListName, Firestore().getCurrentUserID())
        boardDetails.taskList.add(0, task)
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    fun updateTaskList(position: Int, listName: String, model: Task) {
        val task = Task(listName, model.createdBy)
        boardDetails.taskList[position] = task
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    fun deleteTaskList(position: Int) {
        boardDetails.taskList.removeAt(position)
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    fun addCardToTaskList(cardName: String, position: Int) {
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(Firestore().getCurrentUserID())
        val card = Card(cardName, Firestore().getCurrentUserID(), cardAssignedUsersList)
        val cardsList = boardDetails.taskList[position].cards
        cardsList.add(card)
        val task = Task(
            boardDetails.taskList[position].title,
            boardDetails.taskList[position].createdBy,
            cardsList
        )
        boardDetails.taskList[position] = task
        showProgressDialog(resources.getString(R.string.please_wait))
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        val intent = Intent(this, CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, boardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
        intent.putExtra(Constants.BOARD_MEMBERS_LIST, assignedMembersDetailList)
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)
    }

    fun updateCardsInTaskList(taskListPosition: Int, cards: ArrayList<Card>) {
        boardDetails.taskList.removeAt(boardDetails.taskList.size - 1)
        boardDetails.taskList[taskListPosition].cards = cards
        showProgressDialog(resources.getString(R.string.please_wait))
        Firestore().addUpdateTaskList(this, boardDetails)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, boardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && (requestCode == MEMBERS_REQUEST_CODE || requestCode == CARD_DETAILS_REQUEST_CODE)
        ) {
            showProgressDialog(resources.getString(R.string.please_wait))
            Firestore().getBoardDetails(this@TaskListActivity, boardDocumentId)
        } else
            Log.e("Cancelled", "Cancelled")
    }
}