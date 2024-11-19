package com.example.lab10.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val dateCreated: Long,
    var isCompleted: Boolean = false
){
    fun prettyDateFormat(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.format(Date(dateCreated))
        return date
    }
}
