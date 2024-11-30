package com.example.firestorewithrv

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.firestorewithrv.databinding.ActivityEditScreenBinding
import com.example.firestorewithrv.models.Student
import com.example.firestorewithrv.viewmodels.EditScreenViewModel

class EditScreen : AppCompatActivity() {

    private lateinit var binding: ActivityEditScreenBinding
    private val viewModel: EditScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Retrieve the add boolean
        val isAddStudent = intent.getBooleanExtra("addStudent", false)
        if (isAddStudent) {
            binding.tvTitle.text = "Add Student"
            binding.etDocId.isVisible = false
            binding.btnSubmit.setText("Add Student")
        } else {

            // Retrieve the studentId from the intent
            val studentId = intent.getStringExtra("studentId")
            if (studentId == null) {
                Toast.makeText(this, "Student ID is missing", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            // Observe the student data
            observeStudentData()

            // Trigger fetching the student by ID
            viewModel.searchById(studentId)

            // Observe ViewModel LiveData
            observeViewModel()

            // Handle click on submit button
            binding.btnSubmit.setOnClickListener {
                val nameFromUI = binding.etName.text.toString()
                val gpaFromUI = binding.etGpa.text.toString().toDoubleOrNull()
                val hasCarFromUI = binding.swHasCar.isChecked

                if (gpaFromUI == null) {
                    Toast.makeText(this, "Invalid GPA", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.updateStudent(studentId, nameFromUI, gpaFromUI, hasCarFromUI)
            }
        }
    }

    private fun observeStudentData() {
        viewModel.student.observe(this) { student ->
            student?.let {
                binding.etName.setText(it.name)
                binding.etGpa.setText(it.gpa.toString())
                binding.swHasCar.isChecked = it.hasCar
                binding.etDocId.setText(it.id)
            } ?: run {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.updateStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
