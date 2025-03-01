package com.example.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.model.User
import com.example.note.repository.UserRepository
import com.example.note.repository.UserRepositoryImpl

class UserViewModel() : ViewModel() {
    private val repo: UserRepository = UserRepositoryImpl();

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> get() = _userData

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

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.login(email, password, callback)
    }

    fun getCurrentUser() = repo.getCurrentUser()

    fun logout() {
        repo.logout()
    }

    fun fetchUserData(userId: String) {
        repo.getUserData(userId) { user, success, message ->
            if (success) {
                _userData.value = user
            } else {
                _userData.value = null
            }
        }
    }

    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        repo.sendPasswordResetEmail(email, callback)
    }
}