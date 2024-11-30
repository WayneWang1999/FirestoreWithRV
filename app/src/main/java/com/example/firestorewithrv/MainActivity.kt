package com.example.firestorewithrv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestorewithrv.adapters.StudentAdapter
import com.example.firestorewithrv.databinding.ActivityMainBinding
import com.example.firestorewithrv.interfaces.ClickDetectorInterface
import com.example.firestorewithrv.viewmodels.StudentViewModel


class MainActivity : AppCompatActivity(), ClickDetectorInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StudentAdapter
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ------------------------------
        // RecyclerView Adapter
        // ------------------------------
        // initialize the adapter
        adapter = StudentAdapter(mutableListOf(), this)

        // ------------------------------
        // recyclerview configuration
        // ------------------------------
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // Observe ViewModel data
        studentViewModel.students.observe(this) { studentList ->
            adapter.updateData(studentList)
        }

        studentViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        // ------------------------------
        // click handlers
        // ------------------------------

        binding.btnGetAll.setOnClickListener {
            studentViewModel.fetchStudents()
        }

        binding.btnSearch.setOnClickListener {
            // get search keyword from textbox
            val keywordFromUI: String = binding.etSearchText.text.toString()
            studentViewModel.searchByName(keywordFromUI)

        }
        binding.btnAddStudents.setOnClickListener {
//            val intent = Intent(this@MainActivity, EditScreen::class.java).apply {
//                putExtra("studentId", "defaultValue")
//                putExtra("addStudent",true)
//
//            }
//            startActivity(intent)

          studentViewModel.addMultipleStudents()
        }
    }

    override fun onResume() {
        super.onResume()
        studentViewModel.fetchStudents() // Reload data when returning from EditScreen
    }
    // ---------------------------------------
    // click handlers for recyclerview rows
    // ---------------------------------------

    // click handler for UPDATE button in recycler row
    override fun updateRow(position: Int) {
        // Get the current item from the list of data
        val studentId=studentViewModel.students.value?.get(position)?.id
       // val student = studentViewModel.students.value?.get(position)
        studentId.let {
            val intent = Intent(this@MainActivity, EditScreen::class.java).apply {
                putExtra("studentId", it)
                putExtra("addStudent",false)
            }
            startActivity(intent)
        }

    }

    // click handler for DELETE button in recycler row
    override fun deleteRow(position: Int) {
        val student = studentViewModel.students.value?.get(position)
        student?.let {
            studentViewModel.deleteDocument(it.id)
        }
    }
    // ---------------------------------------
    // Helper functions to perform FireStore operations
    // ---------------------------------------


}