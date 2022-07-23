package com.aminsoheyli.a7minutesworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*

class ExerciseActivity : AppCompatActivity() {
    companion object {
        // Exercise
        const val EXERCISE_DURATION_TIME = 30000L
        const val EXERCISE_MAX_PROGRESS = 30

        // Rest
        const val REST_DURATION_TIME = 5000L
        const val REST_MAX_PROGRESS = 5
    }

    private lateinit var binding: ActivityExerciseBinding
    private lateinit var restTimer: CountDownTimer
    private var progressValue = 0
    private val exerciseList = Constants.defaultExerciseList()
    private var currentExerciseIndex = -1

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setSupportActionBar(binding.toolbarExerciseActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            onBackPressed()
        }
        initTextToSpeech()
        setupRest()
    }

    private fun initTextToSpeech() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    Toast.makeText(this, "The language is not supported!", Toast.LENGTH_SHORT)
                        .show()
            } else
                Toast.makeText(this, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun textToSpeech(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }

    private fun setupExercise() {
        val bottomPaddingInDP =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        binding.root.setPaddingRelative(0, 0, 0, bottomPaddingInDP)
        binding.imageViewExerciseImage.visibility = View.VISIBLE
        val exercise = exerciseList[currentExerciseIndex]
        binding.textViewExerciseName.text = exercise.name
        binding.imageViewExerciseImage.setImageResource(exercise.image)
        binding.textView.visibility = View.GONE
        binding.textViewUpcommingExerciseName.visibility = View.GONE
        textToSpeech(exercise.name)
        if (currentExerciseIndex < exerciseList.size - 1)
            setProgressBar(EXERCISE_DURATION_TIME, EXERCISE_MAX_PROGRESS) { setupRest() }
        else
            Toast.makeText(this@ExerciseActivity, "Finished", Toast.LENGTH_LONG).show()
    }

    private fun setupRest() {
        binding.root.setPaddingRelative(0, 0, 0, 0)
        binding.imageViewExerciseImage.visibility = View.GONE
        binding.textViewExerciseName.text = getString(R.string.rest_title)
        binding.textView.visibility = View.VISIBLE
        binding.textViewUpcommingExerciseName.visibility = View.VISIBLE
        binding.textViewUpcommingExerciseName.text = exerciseList[currentExerciseIndex + 1].name
        textToSpeech("Rest for $REST_MAX_PROGRESS seconds")
        setProgressBar(REST_DURATION_TIME, REST_MAX_PROGRESS) {
            currentExerciseIndex++
            setupExercise()
        }
    }

    private fun setProgressBar(
        duration: Long,
        maxProgressValue: Int,
        onFinishFunction: () -> Any
    ) {
        binding.progressBar.max = maxProgressValue
        binding.progressBar.progress = progressValue
        restTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = maxProgressValue - progressValue++
                binding.progressBar.progress = progress
                binding.textViewTimer.text = progress.toString()
            }

            override fun onFinish() {
                restTimer.cancel()
                progressValue = 0
                onFinishFunction()
            }
        }.start()
    }
}