package com.example.note.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.note.R
import com.example.note.databinding.FragmentProfileBinding
import com.example.note.model.User
import com.example.note.repository.UserRepositoryImpl
import com.example.note.ui.activity.LoginActivity
import com.example.note.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = UserViewModel()

        // Fetch and display user data
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            userViewModel.fetchUserData(currentUser.uid)
        }

        // Observe user data
        userViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding.editTextName.setText(user.fullName)
                binding.editTextEmail.setText(user.email)
            } else {
                binding.editTextName.setText("User not found")
                binding.editTextEmail.setText("")
            }
        })

        // Edit Button
        binding.buttonEdit.setOnClickListener {
            toggleEditMode(true)
        }

        // Save Button
        binding.buttonSave.setOnClickListener {
            saveUserDetails()
        }

        // Logout Button
        binding.buttonLogout.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun toggleEditMode(enable: Boolean) {
        isEditMode = enable
        binding.editTextName.isEnabled = enable
        binding.editTextEmail.isEnabled = enable
        binding.buttonSave.visibility = if (enable) View.VISIBLE else View.GONE
        binding.buttonEdit.visibility = if (enable) View.GONE else View.VISIBLE
    }

    private fun saveUserDetails() {
        val fullName = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()

        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            val updatedUser = User(fullName, email)
            userViewModel.saveUserData(currentUser.uid, updatedUser) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    toggleEditMode(false)
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}