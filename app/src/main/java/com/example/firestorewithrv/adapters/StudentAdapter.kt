package com.example.firestorewithrv.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestorewithrv.databinding.RowLayoutBinding
import com.example.firestorewithrv.interfaces.ClickDetectorInterface
import com.example.firestorewithrv.models.Student


class StudentAdapter( val myItems:MutableList<Student>, val clickInterface: ClickDetectorInterface) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder (binding.root) {
        // Bind data to views
        fun bind(student: Student, position: Int) {
            binding.tvRow1.text = "Name: ${student.name}"
            binding.tvRow2.text = "GPA: ${student.gpa}"

            // Set click listeners for buttons
            binding.btnUpdate.setOnClickListener {
                clickInterface.updateRow(position)
            }
            binding.btnDelete.setOnClickListener {
                clickInterface.deleteRow(position)
            }
        }

    }

    // Update the data set
    fun updateData(newStudents: List<Student>) {
        myItems.clear()
        myItems.addAll(newStudents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myItems.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = myItems[position]
        holder.bind(student, position)
    }
}