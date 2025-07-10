package com.usedocetangerinaestoque.presentation.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.data.dao.UserDao
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.usecases.user.RegisterUserUseCase
import com.usedocetangerinaestoque.usecases.user.RegisterUserValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.valiktor.ConstraintViolationException
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var listView: ListView
    @Inject lateinit var userDao: UserDao
    @Inject lateinit var useCase: RegisterUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste)

        nameField = findViewById(R.id.name)
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        saveButton = findViewById(R.id.save_data)
        loadButton = findViewById(R.id.load_data)
        listView = findViewById(R.id.listView)

        saveButton.setOnClickListener {
            val user = User(
                name = nameField.text.toString(),
                email = emailField.text.toString(),
                password = passwordField.text.toString()
            )

            lifecycleScope.launch {
                try {
                    useCase(user)
                    Toast.makeText(this@TestActivity, "Usuário salvo!", Toast.LENGTH_SHORT).show()
                } catch (ex: ConstraintViolationException) {
                    val message = RegisterUserValidator.errorsTranslateToPtBr(ex)
                    val messages = message.joinToString("\n")

                    Toast.makeText(this@TestActivity, messages, Toast.LENGTH_LONG).show()
                } catch (ex: Exception) {
                    Toast.makeText(this@TestActivity, "${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }


        loadButton.setOnClickListener {
            lifecycleScope.launch {
                val users = userDao.getAll()
                val names = users.map { it.showSummary() }
                val adapter = ArrayAdapter(this@TestActivity, android.R.layout.simple_list_item_1, names)
                listView.adapter = adapter
            }
        }
    }

    private fun User.showSummary(): String {
        return """
        ID: $id
        Nome: $name
        Email: $email
        Senha: $password
    """.trimIndent()
    }
}
