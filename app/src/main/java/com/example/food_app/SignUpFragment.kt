package com.example.food_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food_app.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSignUp.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(username, email, password, confirmPassword)) {
                signUpUser(username, email, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty()) {
            binding.tilUsername.error = getString(R.string.username_required)
            return false
        }

        if (username.length < 3) {
            binding.tilUsername.error = getString(R.string.username_too_short)
            return false
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.email_required)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.invalid_email)
            return false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.password_required)
            return false
        }

        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.password_too_short)
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = getString(R.string.confirm_password_required)
            return false
        }

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = getString(R.string.passwords_not_match)
            return false
        }

        // Clear all errors
        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        return true
    }

    private fun signUpUser(username: String, email: String, password: String) {
        binding.btnSignUp.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update user profile with username
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            binding.btnSignUp.isEnabled = true

                            if (profileTask.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.signup_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                            }
                        }
                } else {
                    binding.btnSignUp.isEnabled = true
                    Toast.makeText(
                        context,
                        getString(R.string.signup_failed) + ": ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
