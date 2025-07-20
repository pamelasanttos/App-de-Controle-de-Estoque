package com.usedocetangerinaestoque.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.usedocetangerinaestoque.R
import com.usedocetangerinaestoque.data.entities.Size
import com.usedocetangerinaestoque.presentation.viewmodel.SizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SizeActivity : BaseDrawerActivity() {

    private val viewModel: SizeViewModel by viewModels()
    private lateinit var containerItems: LinearLayout
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filters)

        initViews()
        setupSearchView()
        setupAddButton()
        observeViewModel()
        observeErrors()
        observeDeletion()

        supportActionBar?.title = "Tamanhos"
    }

    private fun initViews() {
        containerItems = findViewById(R.id.containerItems)
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

    private fun setupAddButton() {
        findViewById<ImageButton>(R.id.btnAddItem).setOnClickListener {
            showInputDialog("Novo tamanho:") { newSize ->
                viewModel.addSize(newSize)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.filteredSizes.observe(this) { list ->
            containerItems.removeAllViews()

            if (list.isEmpty()) {
                showToast("Nenhum tamanho encontrado para a busca.")
                return@observe
            }

            list.forEach { size ->
                val card = createSizeCard(size)
                containerItems.addView(card)
            }
        }
    }

    private fun observeErrors() {
        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                showToast(it)
                viewModel.clearError()
            }
        }
    }

    private fun observeDeletion() {
        viewModel.deleted.observe(this) { wasDeleted ->
            if (wasDeleted) {
                showToast("Tamanho excluído com sucesso.")
                viewModel.clearDeleted()
            }
        }
    }

    private fun createSizeCard(size: Size): View {
        val card = layoutInflater.inflate(R.layout.filter_manage_card, containerItems, false)

        val name = card.findViewById<TextView>(R.id.textName)
        val quantity = card.findViewById<TextView>(R.id.textQuantity)
        val btnEdit = card.findViewById<ImageButton>(R.id.btnEdit)
        val btnDelete = card.findViewById<ImageButton>(R.id.btnDelete)

        name.text = size.name
        // quantity.text = "Itens: ${size.totalItems}"

        btnEdit.setOnClickListener {
            showInputDialog("Editar tamanho:") { newName ->
                viewModel.updateSize(Size(id = size.id, name = newName))
            }
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmation(size)
        }

        return card
    }

    private fun showDeleteConfirmation(size: Size) {
        AlertDialog.Builder(this)
            .setTitle("Excluir \"${size.name}\"?")
            .setMessage("Essa ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteSize(size)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showInputDialog(title: String, onConfirm: (String) -> Unit) {
        val input = EditText(this).apply {
            hint = title
            setPadding(32, 16, 32, 16)
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) onConfirm(name)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}