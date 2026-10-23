package com.mysima

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mysima.auth.AuthTab
import com.mysima.auth.AuthViewModel
import com.mysima.auth.ValidationResult
import com.mysima.databinding.ActivityMainBinding
import com.mysima.home.HomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        setupTabClicks()
        setupLoginActions()
        setupRegisterActions()
        setupRecoveryActions()
        observeState()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top + 16,
                v.paddingRight,
                systemBars.bottom + 24
            )
            insets
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentTab.collect { tab ->
                    updateTabUi(tab)
                }
            }
        }
    }

    private fun setupTabClicks() {
        binding.tabBtnLogin.setOnClickListener { viewModel.selectTab(AuthTab.LOGIN) }
        binding.tabBtnRegister.setOnClickListener { viewModel.selectTab(AuthTab.REGISTER) }
        binding.tabBtnRecovery.setOnClickListener { viewModel.selectTab(AuthTab.RECOVERY) }
    }

    private fun updateTabUi(tab: AuthTab) {
        setTabStyle(binding.tabBtnLogin, tab == AuthTab.LOGIN)
        setTabStyle(binding.tabBtnRegister, tab == AuthTab.REGISTER)
        setTabStyle(binding.tabBtnRecovery, tab == AuthTab.RECOVERY)

        binding.authLogin.root.visibility = if (tab == AuthTab.LOGIN) View.VISIBLE else View.GONE
        binding.authRegister.root.visibility = if (tab == AuthTab.REGISTER) View.VISIBLE else View.GONE
        binding.authForgotPassword.root.visibility = if (tab == AuthTab.RECOVERY) View.VISIBLE else View.GONE
    }

    private fun setTabStyle(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setBackgroundResource(R.drawable.bg_tab_indicator)
            textView.setTextColor(ContextCompat.getColor(this, R.color.tab_text_selected))
            textView.setTypeface(null, Typeface.BOLD)
        } else {
            textView.background = null
            textView.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselected))
            textView.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun setupLoginActions() {
        val loginView = binding.authLogin

        loginView.btnLoginSubmit.setOnClickListener {
            val email = loginView.etLoginEmail.text.toString().trim()
            val password = loginView.etLoginPassword.text.toString()

            when (val result = viewModel.validateLogin(email, password)) {
                is ValidationResult.Success -> {
                    loginView.tvLoginError.visibility = View.GONE
                    val displayName = email.substringBefore("@").replace(".", " ").capitalizeWords()
                    HomeActivity.start(this, name = displayName, email = email)
                }
                is ValidationResult.Error -> {
                    loginView.tvLoginError.text = result.message
                    loginView.tvLoginError.visibility = View.VISIBLE
                }
            }
        }

        loginView.btnQuickDemo.setOnClickListener {
            HomeActivity.start(this, name = "Alex Rivera", email = "alex.rivera@example.com")
        }

        loginView.tvForgotPassword.setOnClickListener {
            viewModel.selectTab(AuthTab.RECOVERY)
        }

        loginView.tvGoToRegister.setOnClickListener {
            viewModel.selectTab(AuthTab.REGISTER)
        }
    }

    private fun setupRegisterActions() {
        val registerView = binding.authRegister

        registerView.btnRegisterSubmit.setOnClickListener {
            val name = registerView.etRegisterName.text.toString().trim()
            val email = registerView.etRegisterEmail.text.toString().trim()
            val pass = registerView.etRegisterPassword.text.toString()
            val confirmPass = registerView.etRegisterConfirmPassword.text.toString()
            val agreed = registerView.cbAgreeTerms.isChecked

            when (val result = viewModel.validateRegister(name, email, pass, confirmPass, agreed)) {
                is ValidationResult.Success -> {
                    registerView.tvRegisterError.visibility = View.GONE
                    HomeActivity.start(this, name = name, email = email)
                }
                is ValidationResult.Error -> {
                    registerView.tvRegisterError.text = result.message
                    registerView.tvRegisterError.visibility = View.VISIBLE
                }
            }
        }

        registerView.tvGoToLoginFromRegister.setOnClickListener {
            viewModel.selectTab(AuthTab.LOGIN)
        }
    }

    private fun setupRecoveryActions() {
        val forgotView = binding.authForgotPassword

        forgotView.btnForgotSubmit.setOnClickListener {
            val email = forgotView.etForgotEmail.text.toString().trim()

            when (val result = viewModel.validateRecovery(email)) {
                is ValidationResult.Success -> {
                    forgotView.tvForgotError.visibility = View.GONE
                    forgotView.llForgotSuccess.visibility = View.VISIBLE
                }
                is ValidationResult.Error -> {
                    forgotView.llForgotSuccess.visibility = View.GONE
                    forgotView.tvForgotError.text = result.message
                    forgotView.tvForgotError.visibility = View.VISIBLE
                }
            }
        }

        forgotView.btnBackToSignIn.setOnClickListener {
            viewModel.selectTab(AuthTab.LOGIN)
        }
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
