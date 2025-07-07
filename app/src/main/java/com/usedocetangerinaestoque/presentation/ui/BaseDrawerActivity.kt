package com.usedocetangerinaestoque.presentation.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.presentation.viewmodel.BaseDrawerViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.bumptech.glide.Glide
import java.io.File

@AndroidEntryPoint
abstract class BaseDrawerActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var profilePicture: ImageView

    protected val drawerViewModel: BaseDrawerViewModel by viewModels()

    override fun setContentView(layoutResID: Int) {
        val fullLayout = layoutInflater.inflate(R.layout.activity_drawer_base, null)
        val content = fullLayout.findViewById<FrameLayout>(R.id.fragmentContainer)
        layoutInflater.inflate(layoutResID, content, true)
        super.setContentView(fullLayout)

        initViews()
        setupToolbar()
        setupDrawerToggle()
        setupNavigationMenu()
        observeUserData()

        drawerViewModel.loadUser()
    }

    override fun onResume() {
        super.onResume()
        drawerViewModel.loadUser()
    }


    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.navigationView)
        profilePicture = toolbar.findViewById(R.id.userImage)

        profilePicture.setOnClickListener {
            startActivity(Intent(this, UserViewActivity::class.java))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupDrawerToggle() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigationMenu() {
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home     -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.nav_stock    -> startActivity(Intent(this, StockActivity::class.java))
                R.id.nav_category -> startActivity(Intent(this, CategoryActivity::class.java))
                R.id.nav_size     -> startActivity(Intent(this, SizeActivity::class.java))
                R.id.nav_logout   -> logoutUser()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun logoutUser() {
        drawerViewModel.sessionManager.setLogged(false)
        drawerViewModel.sessionManager.clearSession()
        finishAffinity()
        startActivity(Intent(this, LoginRegisterActivity::class.java))
    }

    private fun observeUserData() {
        drawerViewModel.user.observe(this) { user ->
            user?.let {
                val imagePath = it.image
                val initial = it.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

                val file = File(imagePath)
                if (imagePath.isNotEmpty() && file.exists()) {
                    Glide.with(this)
                        .load(file)
                        .circleCrop()
                        .into(profilePicture)
                } else {
                    profilePicture.setImageDrawable(generateInitialDrawable(initial))
                }
            }
        }
    }

    protected fun generateInitialDrawable(initial: String): Drawable {
        val size = 100
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint().apply {
            color = ContextCompat.getColor(this@BaseDrawerActivity, R.color.my_light_secondary)
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, backgroundPaint)

        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val xPos = canvas.width / 2f
        val yPos = (canvas.height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f)

        canvas.drawText(initial, xPos, yPos, textPaint)

        return bitmap.toDrawable(resources)
    }
}