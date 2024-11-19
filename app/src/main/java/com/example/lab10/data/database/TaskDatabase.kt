package com.example.lab10.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.lab10.data.Task
import kotlin.concurrent.Volatile


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao


    companion object {
        @Volatile
        var instance: TaskDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TaskDatabase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context): TaskDatabase {
            return databaseBuilder<TaskDatabase>(
                context,
                TaskDatabase::class.java,
                "task_database"
            ).build()
        }
    }


}
