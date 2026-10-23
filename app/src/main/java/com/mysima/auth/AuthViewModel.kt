package com.mysima.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _currentTab = MutableStateFlow(AuthTab.LOGIN)
    val currentTab: StateFlow<AuthTab> = _currentTab.asStateFlow()

    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }

    fun selectTab(tab: AuthTab) {
        _currentTab.value = tab
    }

    fun validateLogin(email: String, pass: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Error("Please enter your email address")
        }
        if (!isValidEmail(email)) {
            return ValidationResult.Error("Please enter a valid email address")
        }
        if (pass.length < 6) {
            return ValidationResult.Error("Password must be at least 6 characters")
        }
        return ValidationResult.Success
    }

    fun validateRegister(
        name: String,
        email: String,
        pass: String,
        confirmPass: String,
        agreedTerms: Boolean
    ): ValidationResult {
        if (name.trim().length < 2) {
            return ValidationResult.Error("Please enter your full name")
        }
        if (!isValidEmail(email)) {
            return ValidationResult.Error("Please enter a valid email address")
        }
        if (pass.length < 6) {
            return ValidationResult.Error("Password must be at least 6 characters")
        }
        if (pass != confirmPass) {
            return ValidationResult.Error("Passwords do not match")
        }
        if (!agreedTerms) {
            return ValidationResult.Error("You must agree to the Terms of Service")
        }
        return ValidationResult.Success
    }

    fun validateRecovery(email: String): ValidationResult {
        if (!isValidEmail(email)) {
            return ValidationResult.Error("Please enter a valid email address")
        }
        return ValidationResult.Success
    }
}
