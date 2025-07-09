package com.usedocetangerinaestoque.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.bumptech.glide.Glide
import java.io.File


@AndroidEntryPoint
class UserViewActivity : BaseDrawerActivity() {

    private val viewModel: UserViewModel by viewModels()

    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_view)
        supportActionBar?.title = "Perfil do Usuário"

        initViews()
        observeUserData()
        setupListeners()

        viewModel.loadUser()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUser()
    }

    private fun initViews() {
        nameText = findViewById(R.id.userName)
        emailText = findViewById(R.id.userEmail)
        imageView = findViewById(R.id.userImageProfile)
        btnEdit = findViewById(R.id.btnEditUser)
    }

    private fun observeUserData() {
        viewModel.user.observe(this) { user ->
            user?.let {
                nameText.text = it.name
                emailText.text = it.email

                val imagePath = it.image
                val initial = it.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

                val file = File(imagePath)
                if (imagePath.isNotEmpty() && file.exists()) {
                    Glide.with(this)
                        .load(file)
                        .circleCrop()
                        .into(imageView)
                } else {
                    imageView.setImageDrawable(generateInitialDrawable(initial))
                }
            }
        }
    }

    private fun setupListeners() {
        btnEdit.setOnClickListener {
            startActivity(Intent(this, UserEditActivity::class.java))
        }
    }
}