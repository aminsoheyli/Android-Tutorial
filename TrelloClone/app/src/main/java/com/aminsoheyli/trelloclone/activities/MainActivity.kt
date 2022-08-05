package com.aminsoheyli.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityMainBinding
import com.aminsoheyli.trelloclone.databinding.NavHeaderMainBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.User
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val REQUEST_CODE_MY_PROFILE = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        setupFirebase()
    }

    private fun setupFirebase() {
        Firestore().loadUserData(this)
    }

    private fun initUi() {
        setupActionBar()
        binding.navView.setNavigationItemSelectedListener(this)
        navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        binding.content.fabCreateBoard.setOnClickListener {
            startActivity(Intent(this, CreateBoardActivity::class.java))
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

    fun updateNavigationUserDetails(user: User) {
        navHeaderBinding.tvUsername.text = user.name
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navHeaderBinding.ivUserImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MY_PROFILE)
            Firestore().loadUserData(this@MainActivity)
        else
            Log.e(this.localClassName + "/Canceled", "Cancelled")
    }
}