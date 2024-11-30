package com.example.firestorewithrv.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    @DocumentId
    var id: String = "",
    var name: String = "",
    var gpa: Double = 0.0,
    var hasCar: Boolean = false,
    var imageUrl: String = "https://via.placeholder.com/250",
): Parcelable
