package com.aminsoheyli.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setupActionBar()
        binding.navView.setNavigationItemSelectedListener(this)
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
            R.id.nav_my_profile -> Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
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
}