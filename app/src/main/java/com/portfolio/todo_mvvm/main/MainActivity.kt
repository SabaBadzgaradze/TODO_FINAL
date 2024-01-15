package com.portfolio.todo_mvvm.main

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.portfolio.todo_mvvm.R
import com.portfolio.todo_mvvm.databinding.ActivityMainBinding
import com.portfolio.todo_mvvm.firebase.FirebaseRepository
import com.portfolio.todo_mvvm.firebase.Retrofit
import com.portfolio.todo_mvvm.firebase.TaskModel
import com.portfolio.todo_mvvm.ui.tasks.AddTaskBottomSheetFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val titleLiveData = MutableLiveData<String>()

    var activeGroupId: Long = 0L

    private val sharedPrefsKey = "theme_prefs"
    private val themePrefs: SharedPreferences by lazy {
        getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setAppTheme(themePrefs.getBoolean("dark_mode", false))
        setContentView(binding.root)
        setup()
        registerListeners()
        test()
    }

    // MARK: - Test
    private val repository = FirebaseRepository()

    private fun test() {
        lifecycleScope.launch {
            val task = TaskModel(234L, "Title", "Note", 2342L, false)
            Log.d("Saba", "task variable created")
            val response = repository.createTask(task)
//            Log.d("Body", response.body()!!.toString())
            Retrofit.api.createTask(task)
//            Log.d("Saba", response.message())
//            if (response.isSuccessful) {
//                // Task creation was successful
//                val createdTask = response.body()
//                Log.d("Saba", createdTask?.title ?: "")
//                // Handle the created task, if needed
//            } else {
//                // Handle the error
//                val errorBody = response.errorBody()
//                Log.e("Saba", errorBody.toString())
//                // Handle the error response, if needed
//            }
        }
    }

    // MARK: - Private methods
    private fun setup() {
        navController = findNavController(R.id.host_fragment)
        binding.bottomNavView.setupWithNavController(navController)

        titleLiveData.observe(this) { title ->
            binding.toolbarTitle.text = title
        }
    }

    private fun registerListeners() {
        binding.buttonShowLists.setOnClickListener {
            navController.navigateUp()
        }

        binding.buttonProperties.setOnClickListener {
            val bottomSheetFragment = AddTaskBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // MARK: - Other methods
    fun setAppBarTitle(title: String) {
        titleLiveData.value = title
    }

    fun menuButtonVisibility(visibility: Int) {
        try {
            binding.buttonShowLists.visibility = visibility
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("Saba", it) }
        }
    }

    fun propertiesButtonVisibility(visibility: Int) {
        try {
            binding.buttonProperties.visibility = visibility
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("Saba", it) }
        }
    }
}
