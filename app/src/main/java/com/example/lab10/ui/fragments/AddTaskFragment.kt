package com.example.lab10.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.lab10.data.Task
import com.example.lab10.data.database.TaskDao
import com.example.lab10.data.database.TaskDatabase
import com.example.lab10.databinding.FragmentAddTaskBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskDao: TaskDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDao = Room.databaseBuilder(requireContext(), TaskDatabase::class.java, "task_database")
            .build().taskDao()


        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.format(Date(System.currentTimeMillis()))
        binding.taskDateInput.text = date
        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskNameInput.text.toString()
            val taskDescription = binding.taskDescriptionInput.text.toString()

            if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {
                val newTask = Task(
                    name = taskName,
                    description = taskDescription,
                    dateCreated = System.currentTimeMillis(),
                    isCompleted = false
                )
                Snackbar.make(view, "Task added", Snackbar.LENGTH_SHORT).show()
                GlobalScope.launch(Dispatchers.IO) {
                    taskDao.insert(newTask)
                    withContext(Dispatchers.Main) {
                        findNavController().navigateUp()
                    }
                }
            } else {
                Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}