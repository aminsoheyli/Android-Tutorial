package com.aminsoheyli.trelloclone.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.adapters.TaskItemsAdapter
import com.aminsoheyli.trelloclone.databinding.ActivityTaskListBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.Task
import com.aminsoheyli.trelloclone.utils.Constants

class TaskListActivity : BaseActivity() {
    private lateinit var boardDetails: Board
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
    }

    private fun setupFirebase() {
        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID))
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
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
        val addTaskList = Task(resources.getString(R.string.add_list))
        boardDetails.taskList.add(addTaskList)
        binding.rvTaskList.layoutManager =
            LinearLayoutManager(this@TaskListActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaskList.setHasFixedSize(true)
        val adapter = TaskItemsAdapter(this@TaskListActivity, boardDetails.taskList)
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
        Firestore().addUpdateTaskList(this@TaskListActivity, boardDetails)
    }
}