package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.aminsoheyli.androidtutorial.R

class PopTime : DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var timePicker: TimePicker
    private lateinit var buttonDone: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogView = inflater.inflate(R.layout.pop_time, container, false)
        timePicker = dialogView.findViewById(R.id.timepicker)
        buttonDone = dialogView.findViewById(R.id.button_pop_time_done)
        buttonDone.setOnClickListener {
            dismiss()
            val timeOn = timePicker.hour.toString() + ":" + timePicker.minute
            (activity as StorageActivity).setTime(timeOn)
        }
        return dialogView
    }
}