package com.aminsoheyli.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aminsoheyli.roomdemo.database.EmployeeDao
import com.aminsoheyli.roomdemo.database.EmployeeEntity
import com.aminsoheyli.roomdemo.databinding.ActivityMainBinding
import com.aminsoheyli.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
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
        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list)
            }
        }
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
                employeeDao.insert(EmployeeEntity(name = name, email = email))
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

    private fun setupListOfDataIntoRecyclerView(employeeList: ArrayList<EmployeeEntity>) {
        if (employeeList.isNotEmpty()) {
            val employeeAdapter = EmployeeAdapter(employeeList, { updateId ->
                updateRecordDialog(updateId)
            }, { deleteId ->
                deleteRecordAlertDialog(deleteId)
            })
            binding.rvItemsList.adapter = employeeAdapter
            binding.tvNoRecordsAvailable.visibility = View.GONE
            binding.rvItemsList.visibility = View.VISIBLE
        } else {
            binding.rvItemsList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialog(id: Int) {
        val updateDialog = Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog)
        updateDialog.setCancelable(false)
        val dialogBinding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                dialogBinding.etUpdateName.setText(it.name)
                dialogBinding.etUpdateEmailId.setText(it.email)
            }
        }

        dialogBinding.tvUpdate.setOnClickListener {
            val name = dialogBinding.etUpdateName.text.toString()
            val email = dialogBinding.etUpdateEmailId.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty()) lifecycleScope.launch {
                employeeDao.update(EmployeeEntity(id, name, email))
                updateDialog.dismiss()
            }
            else Toast.makeText(this, "Name/Email can't be blank", Toast.LENGTH_SHORT).show()
        }
        dialogBinding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun deleteRecordAlertDialog(id: Int) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Delete Record")
        alertBuilder.setIcon(android.R.drawable.ic_dialog_alert)
        alertBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                dialogInterface.dismiss()
            }
        }
        alertBuilder.setPositiveButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = alertBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}