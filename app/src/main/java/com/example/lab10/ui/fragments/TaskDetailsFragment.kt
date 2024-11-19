package com.example.lab10.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.lab10.R
import com.example.lab10.data.Task
import com.example.lab10.data.database.TaskDao
import com.example.lab10.data.database.TaskDatabase
import com.example.lab10.databinding.FragmentTaskDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskDao: TaskDao
    private var taskId: Int? = null
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        taskId = arguments?.let {
            TaskDetailsFragmentArgs.fromBundle(it).taskId
        }
        taskDao = Room.databaseBuilder(requireContext(), TaskDatabase::class.java, "task_database")
            .build().taskDao()

        GlobalScope.launch(Dispatchers.Main) {
            task = withContext(Dispatchers.IO) { taskDao.getTaskById(taskId!!)!! }
            Log.d("TaskDetailsFragment", task.toString())
            binding.taskNameDetail.text = task.name
            binding.taskDescriptionDetail.text = task.description
            binding.taskDateDetail.text = "Created: ${task.prettyDateFormat()}"
            binding.taskStatusDetail.text =
                if (task.isCompleted == true) "Status: Completed" else "Status: Not Completed"


        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TaskDetailsFragment", taskId.toString())
        binding.deleteTaskButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                taskDao.delete(task)
                withContext(Dispatchers.Main) {
                    Snackbar.make(view, "Task deleted", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}