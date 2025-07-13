package com.usedocetangerinaestoque.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.presentation.viewmodel.StockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockActivity : BaseDrawerActivity() {

    private val viewModel: StockViewModel by viewModels()
    private lateinit var containerItems: LinearLayout
    private lateinit var searchView: SearchView
    private var initialCategoryFilterId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock)

        initViews()
        setupSearchView()
        setupSpinners()
        setupAddButton()
        observeFilters()
        observeItems()

        supportActionBar?.title = "Estoque"
    }

    private fun initViews() {
        containerItems = findViewById(R.id.containerItems)
        initialCategoryFilterId = intent
            .getLongExtra("filterCategoryId", -1L)
            .takeIf { it != -1L }

        val layoutSearch = findViewById<View>(R.id.layoutSearch)
        searchView = layoutSearch.findViewById(R.id.searchView)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun observeItems() {
        viewModel.filteredItems.observe(this) { list ->
            containerItems.removeAllViews()

            if (list.isEmpty()) {
                showToast("Nenhum item encontrado para o filtro selecionado.")
                return@observe
            }

            list.forEach { item -> addItemCard(item) }
        }
    }

    private fun addItemCard(item: ItemFull) {
        try {
            val card = layoutInflater.inflate(R.layout.item, containerItems, false)

            card.findViewById<TextView>(R.id.textNome).text =
                "Nome: ${item.item.name}"
            card.findViewById<TextView>(R.id.textValor).text =
                "Valor: R$ ${item.item.value}"
            card.findViewById<TextView>(R.id.textQuantidade).text =
                "Quantidade: ${item.item.quantity}"
            card.findViewById<TextView>(R.id.textTamanho).text =
                "Tamanho: ${item.size?.name ?: "-"}"

            val imageView = card.findViewById<ImageView>(R.id.imageItem)
            val pathFirstImage = item.images.firstOrNull()?.path

            Glide.with(this)
                .load(pathFirstImage ?: R.drawable.produto_exemplo)
                .centerCrop()
                .placeholder(R.drawable.produto_exemplo)
                .into(imageView)

            card.setOnClickListener {
                val intent = Intent(this, ItemDetailActivity::class.java)
                intent.putExtra("itemId", item.item.id)
                startActivity(intent)
            }

            containerItems.addView(card)
        } catch (ex: Exception) {
            showToast("Erro ao carregar lista de itens")
        }
    }

    private fun observeFilters() {
        viewModel.categories.observe(this) { list ->
            initialCategoryFilterId?.let { catId ->
                if (list.any { it.id == catId }) {
                    viewModel.setCategoryFilter(catId)
                }
                initialCategoryFilterId = null
            }
        }
    }

    private fun setupSpinners() {
        setupSizeSpinner()
        setupCategorySpinner()
    }

    private fun setupSizeSpinner() {
        viewModel.sizes.observe(this) { list ->
            val names = mutableListOf("Todos") + list
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            val spinnerSize = findViewById<Spinner>(R.id.spinnerSizeFilter)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSize.adapter = adapter

            spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val sizeId = if (position == 0) null else list[position - 1].id
                    viewModel.setSizeFilter(sizeId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    viewModel.setSizeFilter(null)
                }
            }
        }
    }

    private fun setupCategorySpinner() {
        viewModel.categories.observe(this) { list ->
            val names = mutableListOf("Todos") + list
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategoryFilter)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter

            initialCategoryFilterId?.let { catId ->
                val index = list.indexOfFirst { it.id == catId }
                if (index >= 0) {
                    spinnerCategory.setSelection(index + 1)
                    viewModel.setCategoryFilter(catId)
                }
                initialCategoryFilterId = null
            }

            spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val categoryId = if (position == 0) null else list[position - 1].id
                    viewModel.setCategoryFilter(categoryId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    viewModel.setCategoryFilter(null)
                }
            }
        }
    }

    private fun setupAddButton() {
        findViewById<ImageButton>(R.id.btnAddItem).setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
