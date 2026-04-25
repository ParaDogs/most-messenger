package com.most.messenger.data.repository.firebase

import com.google.firebase.auth.FirebaseAuthException

fun mapFirebaseAuthError(throwable: Throwable): String {
    val authCode = (throwable as? FirebaseAuthException)?.errorCode
    return when (authCode) {
        "ERROR_INVALID_EMAIL" -> "Invalid email format"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Email is already registered"
        "ERROR_WEAK_PASSWORD" -> "Password is too weak (minimum 6 chars)"
        "ERROR_USER_NOT_FOUND", "ERROR_INVALID_CREDENTIAL", "ERROR_WRONG_PASSWORD" ->
            "Invalid email or password"
        "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
        "ERROR_OPERATION_NOT_ALLOWED" ->
            "Email/Password auth is disabled in Firebase Console"
        "CONFIGURATION_NOT_FOUND" ->
            "Firebase Auth configuration is incomplete. Check Sign-in method and app config"
        else -> throwable.message ?: "Authentication failed"
    }
}
