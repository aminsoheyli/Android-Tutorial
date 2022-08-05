package com.aminsoheyli.trelloclone.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aminsoheyli.trelloclone.R
import com.aminsoheyli.trelloclone.databinding.ActivityCreateBoardBinding
import com.aminsoheyli.trelloclone.firebase.Firestore
import com.aminsoheyli.trelloclone.models.Board
import com.aminsoheyli.trelloclone.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateBoardBinding
    private lateinit var username: String
    private var selectedImageFileUri: Uri? = null
    private var boardImageUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setupActionBar()
        if (intent.hasExtra(Constants.User.NAME))
            username = intent.getStringExtra(Constants.User.NAME).toString()
        binding.ivBoardImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                Constants.showImageChooser(this)
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.PERMISSION_CODE_READ_STORAGE
                )
        }
        binding.btnCreate.setOnClickListener {
            if (selectedImageFileUri != null)
                uploadBoardImage()
            else {
                showProgressDialog()
                createBoard()
            }
        }
    }

    private fun createBoard() {
        val assignedUserArrayList = ArrayList<String>()
        assignedUserArrayList.add(getCurrentUserID())
        val board = Board(
            binding.etBoardName.text.toString(),
            boardImageUrl,
            username,
            assignedUserArrayList
        )
        Firestore().createBoard(board, this)
    }

    private fun uploadBoardImage() {
        showProgressDialog()
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "BOARD_IMAGE" + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(this, selectedImageFileUri!!)
        )
        sRef.putFile(selectedImageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Board Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        boardImageUrl = uri.toString()
                        createBoard()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                hideProgressDialog()
            }
    }

    fun onBoardCreatedSuccessfully() {
        hideProgressDialog()
        finish()
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbarCreateBoardActivity
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white_color_24dp)
            title = resources.getString(R.string.create_board_title)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
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
                    .with(this)
                    .load(selectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(binding.ivBoardImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}