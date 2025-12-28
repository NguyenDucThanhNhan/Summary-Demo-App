package com.example.medicalsum

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsum.data.AppDatabase
import com.example.medicalsum.repository.SummaryRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class SummaryActivity : ComponentActivity() {
    private lateinit var repository: SummaryRepository
    private val adapter = SummaryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summary)

        val db = AppDatabase.getDatabase(this)
        repository = SummaryRepository(db.summaryDao())

        val recyclerView = findViewById<RecyclerView>(R.id.rvSummaries)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val deleteBtn = findViewById<ImageView>(R.id.delete_all_button)
        deleteBtn.setOnClickListener {
            lifecycleScope.launch {
                repository.deleteAll()
                loadSummaries()
            }
        }

        loadSummaries()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_library
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_library -> true
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadSummaries() {
        lifecycleScope.launch {
            val list = repository.getAll()
            adapter.submitList(list)
        }
    }
}