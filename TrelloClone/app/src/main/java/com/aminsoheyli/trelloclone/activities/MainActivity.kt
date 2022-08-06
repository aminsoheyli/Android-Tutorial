package com.aminsoheyli.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityMainBinding
import com.aminsoheyli.trelloclone.databinding.NavHeaderMainBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.projemanag.adapters.BoardItemsAdapter

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val REQUEST_CODE_MY_PROFILE = 1
        const val REQUEST_CODE_CREATE_BOARD = 2
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFirebase()
        initUi()
    }

    private fun setupFirebase() {
        Firestore().loadUserData(this, true)
    }

    private fun initUi() {
        setupActionBar()
        binding.navView.setNavigationItemSelectedListener(this)
        navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        binding.content.fabCreateBoard.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.User.NAME, username)
            startActivityForResult(intent, REQUEST_CODE_CREATE_BOARD)
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>) {
        hideProgressDialog()
        with(binding.content.mainContent) {
            if (boardsList.size > 0) {
                tvNoBoardsAvailable.visibility = View.GONE
                val boardItemsAdapter = BoardItemsAdapter(this@MainActivity, boardsList)
                boardItemsAdapter.setOnClickListener(object : BoardItemsAdapter.OnClickListener {
                    override fun onClick(position: Int, model: Board) {
                        startActivity(Intent(this@MainActivity, TaskListActivity::class.java))
                    }
                })
                rvBoardsList.apply {
                    visibility = View.VISIBLE
                    setHasFixedSize(true)
                    layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = boardItemsAdapter
                }
            } else {
                rvBoardsList.visibility = View.GONE
                tvNoBoardsAvailable.visibility = View.VISIBLE
            }
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.content.toolbarMainActivity
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar.setNavigationOnClickListener { toggleDrawer() }
    }

    private fun toggleDrawer() = if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    else
        binding.drawerLayout.openDrawer(GravityCompat.START)

    override fun onBackPressed() = if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    else
        doubleBackToExit()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> startActivityForResult(
                Intent(
                    this,
                    MyProfileActivity::class.java
                ), REQUEST_CODE_MY_PROFILE
            )
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        username = user.name
        navHeaderBinding.tvUsername.text = username
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navHeaderBinding.ivUserImage)
        if (readBoardsList) {
            showProgressDialog()
            Firestore().getBoardsList(this, onGetBoardsListSuccess)
        }
    }

    private val onGetBoardsListSuccess = { document: QuerySnapshot ->
        val boardsList: ArrayList<Board> = ArrayList()
        for (item in document.documents) {
            val board = item.toObject(Board::class.java)!!
            board.documentId = item.id
            boardsList.add(board)
        }
        populateBoardsListToUI(boardsList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUEST_CODE_MY_PROFILE -> Firestore().loadUserData(this@MainActivity)
                REQUEST_CODE_CREATE_BOARD -> Firestore().getBoardsList(this, onGetBoardsListSuccess)
            }
    }
}