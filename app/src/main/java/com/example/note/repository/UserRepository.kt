package com.example.note.repository

import com.example.note.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun signUp(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun saveUserData(userId: String, user: User, callback: (Boolean, String) -> Unit)
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun logout()
    fun getUserData(userId: String, callback: (User?, Boolean, String) -> Unit)
    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) // New method
}