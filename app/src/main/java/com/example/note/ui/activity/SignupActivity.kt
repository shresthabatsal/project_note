package com.example.note.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.note.MainActivity
import com.example.note.databinding.ActivitySignupBinding
import com.example.note.model.User
import com.example.note.repository.UserRepositoryImpl
import com.example.note.viewmodel.UserViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize view model
        userViewModel = UserViewModel()

        binding.signupButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (validateInputs(fullName, email, password, confirmPassword)) {
                userViewModel.signUp(email, password) { success, message, userId ->
                    if (success) {
                        val user = User(fullName, email)
                        userViewModel.saveUserData(userId, user) { saveSuccess, saveMessage ->
                            if (saveSuccess) {
                                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty()) {
            binding.fullNameEditText.error = "Full Name is required"
            return false
        }
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            return false
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            return false
        }
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordEditText.error = "Confirm Password is required"
            return false
        }
        if (password != confirmPassword) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            return false
        }
        return true
    }
}