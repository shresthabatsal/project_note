package com.example.note.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.note.databinding.ActivityForgotPasswordBinding
import com.example.note.repository.UserRepositoryImpl
import com.example.note.viewmodel.UserViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize view model
        userViewModel = UserViewModel()

        // Set up click listeners
        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (validateInput(email)) {
                userViewModel.sendPasswordResetEmail(email) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after sending the email
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInput(email: String): Boolean {
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            return false
        }
        return true
    }
}