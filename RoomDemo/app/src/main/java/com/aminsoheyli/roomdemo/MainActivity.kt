package com.aminsoheyli.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aminsoheyli.roomdemo.database.EmployeeDao
import com.aminsoheyli.roomdemo.database.EmployeeEntity
import com.aminsoheyli.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var employeeDao: EmployeeDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDatabase()
        initUi()
    }

    private fun initDatabase() {
        employeeDao = (application as EmployeeApp).db.employeeDao()
    }

    private fun initUi() {
        binding.btnAdd.setOnClickListener {
            addRecord(employeeDao)
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding.etName.text.toString()
        val email = binding.etEmailId.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty())
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name, email))
                runOnUiThread {
                    Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT).show()
                    binding.etName.text.clear()
                    binding.etEmailId.text.clear()
                }
            }
        else
            Toast.makeText(applicationContext, "Name/Email can't be blank", Toast.LENGTH_SHORT)
                .show()
    }
}