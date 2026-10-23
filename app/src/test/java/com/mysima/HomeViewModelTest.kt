package com.mysima

import com.mysima.home.HomeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun initialState_hasDefaultMetricsAndActivities() {
        val state = viewModel.uiState.value
        assertEquals("Alex Rivera", state.userName)
        assertTrue(state.metrics.isNotEmpty())
        assertTrue(state.activities.isNotEmpty())
        assertFalse(state.showLogoutDialog)
    }

    @Test
    fun setUser_updatesNameAndEmail() {
        viewModel.setUser("Jordan Smith", "jordan@company.com")
        val state = viewModel.uiState.value
        assertEquals("Jordan Smith", state.userName)
        assertEquals("jordan@company.com", state.userEmail)
    }

    @Test
    fun toggleLogoutDialog_updatesState() {
        viewModel.toggleLogoutDialog(true)
        assertTrue(viewModel.uiState.value.showLogoutDialog)

        viewModel.toggleLogoutDialog(false)
        assertFalse(viewModel.uiState.value.showLogoutDialog)
    }
}
