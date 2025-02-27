package com.example.note.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.note.R
import com.example.note.databinding.FragmentProfileBinding
import com.example.note.repository.UserRepositoryImpl
import com.example.note.ui.activity.LoginActivity
import com.example.note.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        // Fetch and display user data
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            userViewModel.fetchUserData(currentUser.uid)
        }

        // Observe user data
        userViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding.textViewName.text = user.fullName
                binding.textViewEmail.text = user.email
            } else {
                binding.textViewName.text = "User not found"
                binding.textViewEmail.text = ""
            }
        })

        // Logout button
        binding.buttonLogout.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}