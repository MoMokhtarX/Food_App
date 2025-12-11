package com.example.food_app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food_app.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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

        // Handle Back Navigation
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        binding.usernameText.text = user?.email ?: getString(R.string.unknown_user)

        updateThemeButtonText()

        // Updated ID from themeButton to cardTheme
        binding.cardTheme.setOnClickListener {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val sharedPreferences = requireContext().getSharedPreferences("food_app_prefs", Context.MODE_PRIVATE)
            
            sharedPreferences.edit {
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    // Switch to Light
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    putBoolean("dark_mode", false)
                } else {
                    // Switch to Dark
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    putBoolean("dark_mode", true)
                }
            }
        }

        // Updated ID from logoutButton to cardLogout
        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_settingsFragment_to_signUpFragment)
        }
    }

    private fun updateThemeButtonText() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        // Updated text view ID from themeButton to tvThemeAction
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            binding.tvThemeAction.text = getString(R.string.switch_to_light_mode)
        } else {
            binding.tvThemeAction.text = getString(R.string.switch_to_dark_mode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
