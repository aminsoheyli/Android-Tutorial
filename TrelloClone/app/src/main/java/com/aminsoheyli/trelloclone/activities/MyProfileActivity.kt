package com.aminsoheyli.trelloclone.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityMyProfileBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.User
import com.aminsoheyli.trelloclone.utils.Constants
import com.bumptech.glide.Glide
import java.io.IOException

class MyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var selectedImageFileUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        setupFirebase()
    }

    private fun setupFirebase() {
        Firestore().loadUserData(this)
    }

    private fun initUi() {
        setupActionBar()
        binding.ivProfileUserImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                Constants.showImageChooser(this@MyProfileActivity)
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.PERMISSION_CODE_READ_STORAGE
                )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.REQUEST_CODE_PICK_IMAGE
            && data?.data != null
        ) {
            selectedImageFileUri = data.data!!

            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(selectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding.ivProfileUserImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_CODE_READ_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Constants.showImageChooser(this@MyProfileActivity)
            else
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbarProfileActivity
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white_color_24dp)
            title = resources.getString(R.string.my_profile_title)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun setUserDataInUi(user: User) {
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding.ivProfileUserImage)

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if (user.mobile != 0L) {
            binding.etEmail.setText(user.mobile.toString())
        }
    }
}