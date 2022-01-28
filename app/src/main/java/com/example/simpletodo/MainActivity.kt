package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.onLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove item from list
                listOfTasks.removeAt(position)

                // Notify adapter
                adapter.notifyDataSetChanged()

                // save update on file
                saveItems()
            }

        }

        // Load existing data file for the user
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputText = findViewById<EditText>(R.id.addTaskField)
        // Detecting click on Add button
        findViewById<Button>(R.id.button).setOnClickListener {

            // Select input text @id/addTasKField
            val userInputtedTask = inputText.text.toString()

            // Add string to listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify the adapter about data update
            adapter.notifyItemInserted(listOfTasks.size-1)

            // Reset input field
            inputText.text.clear()

            // save to file
            saveItems()
        }
    }

    // Save the input data on file
    // Saving by reading/writing on file

    // Get the file we need
    fun getDataFile(): File {

        // Every line represents previously added tasks
        return File(filesDir,"appdata.txt")
    }

    // Create a method to get the file

    // Load the items by reading every line in data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Write to file/ save data onto data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }
}