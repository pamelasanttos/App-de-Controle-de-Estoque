package com.usedocetangerinaestoque.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.data.entities.User
import com.usedocetangerinaestoque.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import com.bumptech.glide.Glide

@AndroidEntryPoint
class UserEditActivity : BaseDrawerActivity() {

    private val viewModel: UserViewModel by viewModels()
    private var imageUri: Uri? = null
    private lateinit var currentUser: User
    private lateinit var pickImagesLauncher: ActivityResultLauncher<Array<String>>
    private var selectedImagePaths = mutableListOf<String>()

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var imageView: ImageView
    private lateinit var btnChangeImage: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_edit)
        supportActionBar?.title = "Editar Perfil"

        initViews()
        setupImagePicker()
        observeUserData()
        setupListeners()

        viewModel.loadUser()
    }

    private fun initViews() {
        nameField = findViewById(R.id.editUserName)
        emailField = findViewById(R.id.editUserEmail)
        passwordField = findViewById(R.id.editUserPassword)
        imageView = findViewById(R.id.userImageProfile)
        btnChangeImage = findViewById(R.id.btnChangeImage)
        btnSave = findViewById(R.id.btnSaveUser)
    }

    private fun setupImagePicker() {
        pickImagesLauncher = registerForActivityResult(
            ActivityResultContracts.OpenMultipleDocuments()
        ) { uris ->
            val paths = uris.mapNotNull { uri -> saveImageToInternalStorage(uri) }
            selectedImagePaths.addAll(paths)
            if (uris.isNotEmpty()) {
                imageUri = uris.first()
                Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(imageView)
            }
        }
    }

    private fun observeUserData() {
        viewModel.user.observe(this) { user ->
            user?.let {
                currentUser = it
                nameField.setText(it.name)
                emailField.setText(it.email)
                passwordField.setText("")

                if (it.image.isNotEmpty()) {
                    val file = File(it.image)
                    if (file.exists()) {
                        Glide.with(this)
                            .load(file)
                            .circleCrop()
                            .into(imageView)
                    } else {
                        imageView.setImageDrawable(generateInitialDrawable(it.name))
                    }
                } else {
                    imageView.setImageDrawable(generateInitialDrawable(it.name))
                }
            }
        }
    }

    private fun setupListeners() {
        btnChangeImage.setOnClickListener {
            pickImagesLauncher.launch(arrayOf("image/*"))
        }

        btnSave.setOnClickListener {
            val updatedUser = currentUser.copy(
                name = nameField.text.toString(),
                email = emailField.text.toString(),
                password = passwordField.text.toString(),
                image = imageUri?.let { saveImageToInternalStorage(it) } ?: currentUser.image
            )

            viewModel.updateUser(
                updatedUser,
                onSuccess = {
                    showToast("Usuário atualizado")
                    finish()
                },
                onError = {
                    showToast(it)
                }
            )
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val input = contentResolver.openInputStream(uri) ?: return null
            val dir = File(filesDir, "images").apply { mkdirs() }
            val file = File(dir, "${System.currentTimeMillis()}.jpg")
            input.use { inp -> file.outputStream().use { out -> inp.copyTo(out) } }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}