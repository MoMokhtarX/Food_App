package com.example.food_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val usernameText = view.findViewById<TextView>(R.id.username_text)
        val themeButton = view.findViewById<Button>(R.id.theme_button)

        val user = FirebaseAuth.getInstance().currentUser
        usernameText.text = "Logged in as: ${user?.email ?: "Unknown"}"

        themeButton.setOnClickListener {
            val current = AppCompatDelegate.getDefaultNightMode()

            if (current == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        return view
    }
}
