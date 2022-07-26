package com.aminsoheyli.a7minutesworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {
    companion object {
        private enum class Unit { METRIC_UNITS_VIEW, US_UNITS_VIEW }
    }

    private lateinit var binding: ActivityBmiBinding
    private var currentUnit = Unit.METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setSupportActionBar(binding.toolbarBmiActivity)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "BMI"
        }
        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCalculateUnits.setOnClickListener {
            if (isMetricUnitsValid())
                displayBMIResult(calculateBMI())
            else
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()
        }
        binding.rgUnits.setOnCheckedChangeListener { group, checkedId ->
            currentUnit = when (checkedId) {
                R.id.rbMetricUnits -> Unit.METRIC_UNITS_VIEW
                else -> Unit.US_UNITS_VIEW
            }
            makeCurrentUnitVisible()
        }
    }

    private fun calculateBMI(): Float = when (currentUnit) {
        Unit.METRIC_UNITS_VIEW -> {
            val weightValue = binding.etWeight.text.toString().toFloat()
            val heightValue = binding.etMetricUnitHeight.text.toString().toFloat() / 100
            weightValue / (heightValue * heightValue)
        }
        Unit.US_UNITS_VIEW -> {
            val weightValue = binding.etWeight.text.toString().toFloat()
            val heightFeet = binding.etUsUnitHeightFeet.text.toString().toFloat()
            val heightInch = binding.etUsUnitHeightInch.text.toString().toFloat()
            val heightValue = heightFeet * 12 + heightInch
            703 * (weightValue / (heightValue * heightValue))
        }
    }

    private fun isMetricUnitsValid(): Boolean = when (currentUnit) {
        Unit.METRIC_UNITS_VIEW -> binding.etWeight.text.toString().isNotEmpty() &&
                binding.etMetricUnitHeight.text.toString().isNotEmpty()
        Unit.US_UNITS_VIEW -> binding.etWeight.text.toString().isNotEmpty() &&
                binding.etUsUnitHeightFeet.text.toString().isNotEmpty() &&
                binding.etUsUnitHeightInch.text.toString().isNotEmpty()
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding.apply {
            binding.llDiplayBMIResult.visibility = View.VISIBLE
            val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
            tvBMIValue.text = bmiValue
            tvBMIType.text = bmiLabel
            tvBMIDescription.text = bmiDescription
        }
    }

    private fun makeCurrentUnitVisible() = when (currentUnit) {
        Unit.METRIC_UNITS_VIEW -> {
            binding.llUsUnitsHeight.visibility = View.GONE
            binding.tilMetricUnitHeight.visibility = View.VISIBLE
            binding.tilMetricUnitWeight.setHint("WEIGHT (in kg)")
        }
        Unit.US_UNITS_VIEW -> {
            binding.llUsUnitsHeight.visibility = View.VISIBLE
            binding.tilMetricUnitHeight.visibility = View.GONE
            binding.tilMetricUnitWeight.setHint("WEIGHT (in pounds)")
        }
    }

}