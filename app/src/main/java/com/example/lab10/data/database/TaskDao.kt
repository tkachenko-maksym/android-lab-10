package com.example.lab10.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab10.data.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks  ORDER BY dateCreated DESC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE isCompleted=:status ORDER BY dateCreated DESC")
    suspend fun getAllTasksByStatus(status: Boolean): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?
}
