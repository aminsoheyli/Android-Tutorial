package com.aminsoheyli.trelloclone.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {
    const val USERS = "users"

    const val PERMISSION_CODE_READ_STORAGE = 1
    const val REQUEST_CODE_PICK_IMAGE = 2

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE)
    }
}