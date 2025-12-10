package com.example.food_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food_app.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(context, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
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

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        return true
    }

    private fun loginUser(email: String, password: String) {
        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.login_failed) + ": ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        getString(R.string.reset_email_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.reset_email_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}