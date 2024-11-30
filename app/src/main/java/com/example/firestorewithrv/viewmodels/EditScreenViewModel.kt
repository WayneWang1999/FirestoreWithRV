package com.example.firestorewithrv.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firestorewithrv.models.Student
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EditScreenViewModel:ViewModel() {
    private val db = Firebase.firestore

    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> get() = _student

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun updateStudent(docId: String, name: String, gpa: Double, hasCar: Boolean) {
        val updatedStudent = mapOf(
            "name" to name,
            "gpa" to gpa,
            "hasCar" to hasCar
        )

        db.collection("students").document(docId)
            .update(updatedStudent)
            .addOnSuccessListener {
                _updateStatus.value = true
                _error.value = null
            }
            .addOnFailureListener { e ->
                _updateStatus.value = false
                _error.value = "Error updating student: ${e.message}"
                Log.e("FireStoreError", "Error updating student", e)
            }
    }
    fun searchById(studentId: String) {
        db.collection("students").document(studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedStudent = document.toObject(Student::class.java)?.apply {
                        id = document.id // Make sure ID is set manually
                    }
                    _student.value = fetchedStudent
                } else {
                    _student.value = null
                    _error.value = "Student not found"
                }
            }
            .addOnFailureListener { exception ->
                _student.value = null
                _error.value = "Error fetching student: ${exception.message}"
            }
    }

}