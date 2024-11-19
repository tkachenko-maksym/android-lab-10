package com.example.lab10.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.lab10.R
import com.example.lab10.data.Task
import com.example.lab10.data.database.TaskDatabase
import com.example.lab10.databinding.FragmentHomeBinding
import com.example.lab10.ui.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private var status: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        status = arguments?.let {
            HomeFragmentArgs.fromBundle(it).status
        }
        Log.d("status", status.toString())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        taskAdapter = TaskAdapter(emptyList(), { task ->
            updateTask(task)

        }) { task ->
            val action = HomeFragmentDirections.actionHomeFragmentToTaskDetailsFragment(task.id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = taskAdapter

        loadTasks()

    }

    private fun loadTasks() {
        GlobalScope.launch(Dispatchers.Main) {
            val taskDao =
                TaskDatabase.getInstance(requireContext())?.taskDao()

            val tasks = withContext(Dispatchers.IO) {
                status?.let {
                    taskDao?.getAllTasksByStatus(it) ?: emptyList()
                } ?: taskDao?.getAllTasks() ?: emptyList()
            }

            taskAdapter.updateTasks(tasks)
        }
    }

    private fun updateTask(task: Task) {
        GlobalScope.launch(Dispatchers.Main) {
            val taskDao =
                TaskDatabase.getInstance(requireContext())?.taskDao()
            taskDao?.update(task)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}