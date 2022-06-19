package com.aminsoheyli.androidtutorial.utilities

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class Utility {
    companion object {
        fun showSnackBar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setTextColor(Color.YELLOW)
                .show()
        }

        fun showToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}