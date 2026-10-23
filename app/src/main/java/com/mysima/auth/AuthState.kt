package com.mysima.auth

enum class AuthTab {
    LOGIN,
    REGISTER,
    RECOVERY
}

data class AuthUser(
    val name: String,
    val email: String
)

sealed interface ValidationResult {
    data object Success : ValidationResult
    data class Error(val message: String) : ValidationResult
}
