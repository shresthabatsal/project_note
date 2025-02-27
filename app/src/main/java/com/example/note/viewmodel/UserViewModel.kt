package com.example.note.viewmodel

import androidx.lifecycle.ViewModel
import com.example.note.model.User
import com.example.note.repository.UserRepository

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun signUp(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        repo.signUp(email, password, callback)
    }

    fun saveUserData(
        userId: String,
        user: User,
        callback: (Boolean, String) -> Unit
    ) {
        repo.saveUserData(userId, user, callback)
    }

    fun getCurrentUser() = repo.getCurrentUser()
}