package com.usedocetangerinaestoque.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.presentation.viewmodel.LoginRegisterViewModel
import com.usedocetangerinaestoque.services.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var loginForm: View
    private lateinit var registerForm: View

    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText

    private lateinit var nameRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel: LoginRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        initViews()
        setupFormSwitching()
        setupFormActions()
        observeViewModel()
    }

    private fun initViews() {
        loginForm = findViewById(R.id.loginForm)
        registerForm = findViewById(R.id.registerForm)

        emailLogin = loginForm.findViewById(R.id.editEmail)
        passwordLogin = loginForm.findViewById(R.id.editPassword)

        nameRegister = registerForm.findViewById(R.id.editName)
        emailRegister = registerForm.findViewById(R.id.editEmail)
        passwordRegister = registerForm.findViewById(R.id.editPassword)
    }

    private fun setupFormSwitching() {
        registerForm.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            showLoginForm()
        }

        loginForm.findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            showRegisterForm()
        }
    }

    private fun showLoginForm() {
        loginForm.visibility = View.VISIBLE
        registerForm.visibility = View.GONE
    }

    private fun showRegisterForm() {
        loginForm.visibility = View.GONE
        registerForm.visibility = View.VISIBLE
    }

    private fun setupFormActions() {
        findViewById<Button>(R.id.btnLoginFinal).setOnClickListener {
            val email = emailLogin.text.toString()
            val password = passwordLogin.text.toString()
            viewModel.login(email, password)
        }

        findViewById<Button>(R.id.btnRegisterFinal).setOnClickListener {
            val name = nameRegister.text.toString()
            val email = emailRegister.text.toString()
            val password = passwordRegister.text.toString()
            viewModel.register(name, email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { user ->
                sessionManager.setLogged(true)
                sessionManager.setUserId(user?.id ?: -1)
                navigateToHome()
            }
            result.onFailure {
                showToast("Falha no login: ${it.message}")
            }
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                showToast("Cadastro realizado com sucesso")
                showLoginForm()
            }
            result.onFailure {
                showToast("Erro no cadastro: ${it.message}")
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
