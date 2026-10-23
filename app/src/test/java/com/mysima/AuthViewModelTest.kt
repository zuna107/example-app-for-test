package com.mysima

import com.mysima.auth.AuthTab
import com.mysima.auth.AuthViewModel
import com.mysima.auth.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        viewModel = AuthViewModel()
    }

    @Test
    fun defaultTab_isLogin() {
        assertEquals(AuthTab.LOGIN, viewModel.currentTab.value)
    }

    @Test
    fun selectTab_updatesCurrentTab() {
        viewModel.selectTab(AuthTab.REGISTER)
        assertEquals(AuthTab.REGISTER, viewModel.currentTab.value)

        viewModel.selectTab(AuthTab.RECOVERY)
        assertEquals(AuthTab.RECOVERY, viewModel.currentTab.value)
    }

    @Test
    fun validateLogin_withValidCredentials_returnsSuccess() {
        val result = viewModel.validateLogin("user@example.com", "secret123")
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun validateLogin_withInvalidEmail_returnsError() {
        val result = viewModel.validateLogin("invalid-email", "secret123")
        assertTrue(result is ValidationResult.Error)
        assertEquals("Please enter a valid email address", (result as ValidationResult.Error).message)
    }

    @Test
    fun validateLogin_withShortPassword_returnsError() {
        val result = viewModel.validateLogin("user@example.com", "123")
        assertTrue(result is ValidationResult.Error)
        assertEquals("Password must be at least 6 characters", (result as ValidationResult.Error).message)
    }

    @Test
    fun validateRegister_withMismatchedPasswords_returnsError() {
        val result = viewModel.validateRegister(
            name = "Alex Rivera",
            email = "alex@example.com",
            pass = "password123",
            confirmPass = "different123",
            agreedTerms = true
        )
        assertTrue(result is ValidationResult.Error)
        assertEquals("Passwords do not match", (result as ValidationResult.Error).message)
    }

    @Test
    fun validateRegister_withoutTerms_returnsError() {
        val result = viewModel.validateRegister(
            name = "Alex Rivera",
            email = "alex@example.com",
            pass = "password123",
            confirmPass = "password123",
            agreedTerms = false
        )
        assertTrue(result is ValidationResult.Error)
        assertEquals("You must agree to the Terms of Service", (result as ValidationResult.Error).message)
    }

    @Test
    fun validateRecovery_withValidEmail_returnsSuccess() {
        val result = viewModel.validateRecovery("alex@example.com")
        assertTrue(result is ValidationResult.Success)
    }
}
