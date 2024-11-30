package com.example.firestorewithrv.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.firestorewithrv.models.Student

class StudentViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchStudents() {
        _loading.value = true
        db.collection("students")
            .get()
            .addOnSuccessListener { documents ->
                val studentList = documents.map { document ->
                    document.toObject(Student::class.java)
                }
                _students.value = studentList
                _error.value = null
            }
            .addOnFailureListener { exception ->
                _error.value = "Failed to load data: ${exception.message}"
            }
            .addOnCompleteListener {
                _loading.value = false
            }
    }
    fun searchByName(studentName:String) {
        db.collection("students").whereEqualTo("name",studentName)
            .get()
            .addOnSuccessListener { documents->
                    val studentList = documents.map { document ->
                        document.toObject(Student::class.java)
                    }
                    _students.value=studentList
                    _error.value = null
            }
            .addOnFailureListener{exception ->
                _error.value = "Failed to load data: ${exception.message}"
            }
           }

    fun deleteDocument(docId:String) {
        db.collection("students").document(docId)
            .delete()
            .addOnSuccessListener {
                fetchStudents()
            }
            .addOnFailureListener { exception ->
                _error.value = "Error deleting document: ${exception.message}"
            }
    }

    fun addMultipleStudents() {
        // Create a list of students you want to add
        val studentsToAdd = listOf(
            Student(name = "John Doe", gpa = 3.5, hasCar = true, imageUrl = "https://via.placeholder.com/250"),
            Student(name = "Jane Smith", gpa = 4.0, hasCar = false, imageUrl = "https://via.placeholder.com/250"),
            Student(name = "Alice Johnson", gpa = 3.8, hasCar = true, imageUrl = "https://via.placeholder.com/250"),

            )

        // Reference to the FireStore collection
        val studentsCollection = db.collection("students")

        // Create a batch to add all students atomically
        val batch = db.batch()

        // Loop through the list of students and add them to the batch
        for (student in studentsToAdd) {
            val studentRef = studentsCollection.document()  // FireStore generates a new ID for each student
            batch.set(studentRef, student)
        }
        // Commit the batch operation
        batch.commit()
            .addOnSuccessListener {
                fetchStudents()  // Reload data to show the new students
            }
            .addOnFailureListener { exception ->
                _error.value = "Error adding students: ${exception.message}"
            }
    }
}
