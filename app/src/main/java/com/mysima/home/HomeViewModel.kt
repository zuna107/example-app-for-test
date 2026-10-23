package com.mysima.home

import androidx.lifecycle.ViewModel
import com.mysima.home.model.ActivityItem
import com.mysima.home.model.ActivityStatus
import com.mysima.home.model.MetricItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val userName: String = "Alex Rivera",
    val userEmail: String = "alex.rivera@example.com",
    val metrics: List<MetricItem> = emptyList(),
    val activities: List<ActivityItem> = emptyList(),
    val selectedTab: Int = 0,
    val showLogoutDialog: Boolean = false,
    val isLoading: Boolean = false
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            metrics = defaultMetrics,
            activities = defaultActivities
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setUser(name: String, email: String) {
        _uiState.update { current ->
            current.copy(
                userName = name.ifBlank { "Alex Rivera" },
                userEmail = email.ifBlank { "alex.rivera@example.com" }
            )
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun toggleLogoutDialog(show: Boolean) {
        _uiState.update { it.copy(showLogoutDialog = show) }
    }

    companion object {
        private val defaultMetrics = listOf(
            MetricItem(
                id = "1",
                title = "Total Revenue",
                value = "$45,231.89",
                change = "+20.1% vs last month",
                isPositive = true
            ),
            MetricItem(
                id = "2",
                title = "Active Subscriptions",
                value = "+2,350",
                change = "+180.1% vs last month",
                isPositive = true
            ),
            MetricItem(
                id = "3",
                title = "Deployments",
                value = "12",
                change = "+19% vs last week",
                isPositive = true
            ),
            MetricItem(
                id = "4",
                title = "Uptime SLA",
                value = "99.98%",
                change = "+0.2% all systems normal",
                isPositive = true
            )
        )

        private val defaultActivities = listOf(
            ActivityItem(
                id = "a1",
                title = "Subscription payment received",
                user = "Olivia Martin",
                timestamp = "2 minutes ago",
                amountOrDetail = "+$1,999.00",
                status = ActivityStatus.COMPLETED
            ),
            ActivityItem(
                id = "a2",
                title = "Production cluster deploy",
                user = "Jackson Lee",
                timestamp = "1 hour ago",
                amountOrDetail = "v2.4.0",
                status = ActivityStatus.COMPLETED
            ),
            ActivityItem(
                id = "a3",
                title = "Security key rotation",
                user = "Isabella Nguyen",
                timestamp = "3 hours ago",
                amountOrDetail = "Pending review",
                status = ActivityStatus.IN_PROGRESS
            ),
            ActivityItem(
                id = "a4",
                title = "Database index rebuild",
                user = "System Automated",
                timestamp = "Yesterday",
                amountOrDetail = "Scheduled",
                status = ActivityStatus.PENDING
            )
        )
    }
}
