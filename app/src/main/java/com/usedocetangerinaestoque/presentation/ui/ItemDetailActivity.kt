package com.usedocetangerinaestoque.presentation.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.presentation.viewmodel.ItemDetailViewModel
import com.usedocetangerinaestoque.util.ImageSliderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailActivity : BaseDrawerActivity() {

    private val viewModel: ItemDetailViewModel by viewModels()

    private lateinit var textName: TextView
    private lateinit var textDescription: TextView
    private lateinit var textValue: TextView
    private lateinit var textQuantity: TextView
    private lateinit var textSize: TextView
    private lateinit var textCategory: TextView
    private lateinit var galleryContainer: LinearLayout
    private lateinit var btnEditItem: Button
    private lateinit var btnDeleteItem: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)

        val itemId = intent.getLongExtra("itemId", -1L)
        if (itemId == -1L) {
            showToast("ID inválido.")
            finish()
            return
        }

        initViews()
        setupListeners()
        observeViewModel()

        viewModel.loadItem(itemId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.item.value?.item?.id?.let { viewModel.loadItem(it) }
    }

    private fun initViews() {
        textName = findViewById(R.id.textName)
        textDescription = findViewById(R.id.textDescription)
        textValue = findViewById(R.id.textValue)
        textQuantity = findViewById(R.id.textQuantity)
        textSize = findViewById(R.id.textSize)
        textCategory = findViewById(R.id.textCategory)
        galleryContainer = findViewById(R.id.galleryContainer)
        btnEditItem = findViewById(R.id.btnEditItem)
        btnDeleteItem = findViewById(R.id.btnDeleteItem)
    }

    private fun setupListeners() {
        textCategory.setOnClickListener {
            viewModel.item.value?.category?.let { category ->
                val intent = Intent(this, StockActivity::class.java)
                intent.putExtra("filterCategoryId", category.id)
                startActivity(intent)
            }
        }

        btnEditItem.setOnClickListener {
            viewModel.item.value?.item?.id?.let { itemId ->
                val intent = Intent(this, AddItemActivity::class.java)
                intent.putExtra("editItemId", itemId)
                startActivity(intent)
            }
        }

        btnDeleteItem.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun observeViewModel() {
        viewModel.item.observe(this) { item ->
            updateUI(item)
        }

        viewModel.error.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearError()
            }
        }

        viewModel.deleted.observe(this) { wasDeleted ->
            if (wasDeleted) {
                showToast("Item excluído com sucesso.")
                finish()
            }
        }
    }

    private fun updateUI(item: com.usedocetangerinaestoque.data.relations.ItemFull) {
        supportActionBar?.title = item.item.name

        textName.text = item.item.name
        textDescription.text = item.item.description
        textValue.text = "Valor: R$ ${item.item.value}"
        textQuantity.text = "Quantidade: ${item.item.quantity}"
        textSize.text = "Tamanho: ${item.size?.name ?: "-"}"
        textCategory.text = "Categoria: ${item.category?.name ?: "-"}"

        renderGallery(item.images)
    }

    private fun renderGallery(images: List<com.usedocetangerinaestoque.data.entities.Image>) {
        galleryContainer.removeAllViews()

        images.forEachIndexed { index, image ->
            val img = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                    setMargins(8, 0, 8, 0)
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
                adjustViewBounds = true
                setOnClickListener { showImageSlider(index) }
            }

            Glide.with(this)
                .load(image.path)
                .placeholder(R.drawable.produto_exemplo)
                .into(img)

            galleryContainer.addView(img)
        }
    }

    private fun showImageSlider(startIndex: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_image_slider, null)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.viewPagerImages)
        val tabDots = dialogView.findViewById<TabLayout>(R.id.tabDots)

        val imagePaths = viewModel.item.value?.images?.map { it.path } ?: return
        val adapter = ImageSliderAdapter(this, imagePaths)

        viewPager.adapter = adapter
        viewPager.setCurrentItem(startIndex, false)

        TabLayoutMediator(tabDots, viewPager) { tab, _ ->
            tab.setIcon(R.drawable.dot_indicator)
        }.attach()

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .show()
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Excluir item")
            .setMessage("Tem certeza que deseja excluir este item?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.item.value?.item?.let { viewModel.deleteItem(it) }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}