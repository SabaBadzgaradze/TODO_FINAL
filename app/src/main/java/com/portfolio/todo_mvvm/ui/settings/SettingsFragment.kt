package com.portfolio.todo_mvvm.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.portfolio.todo_mvvm.databinding.FragmentSettingsBinding
import com.portfolio.todo_mvvm.main.MainActivity

class SettingsFragment: Fragment() {
    // MARK: Variables
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedPrefsKey = "theme_prefs"
    private lateinit var themePrefs: SharedPreferences

    // MARK: Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerListeners()
    }

    // MARK: - Private methods
    private fun init() {
        val parentActivity = (activity as? MainActivity)?: return
        parentActivity.setAppBarTitle("Settings")
        parentActivity.menuButtonVisibility(View.GONE)
        parentActivity.propertiesButtonVisibility(View.GONE)

        themePrefs = requireContext().getSharedPreferences(sharedPrefsKey, Context.MODE_PRIVATE)
    }

    private fun registerListeners() {
        binding.switcherDarkTheme.isChecked = themePrefs.getBoolean("dark_mode", false)

        binding.switcherDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            // Save the theme state to shared preferences
            themePrefs.edit().putBoolean("dark_mode", isChecked).apply()
            // Set the theme based on the switch state
            setAppTheme(isChecked)
            // Recreate the activity to apply the new theme
            requireActivity().recreate()
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}