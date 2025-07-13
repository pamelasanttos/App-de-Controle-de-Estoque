package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.usecases.category.GetAllCategoriesUseCase
import com.usedocetangerinaestoque.usecases.item.GetAllItensUseCase
import com.usedocetangerinaestoque.usecases.size.GetAllSizesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    getAllItensUseCase: GetAllItensUseCase,
    getAllSizesUseCase: GetAllSizesUseCase,
    getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val items = getAllItensUseCase().asLiveData()
    val sizes = getAllSizesUseCase().asLiveData()
    val categories = getAllCategoriesUseCase().asLiveData()

    private val _selectedSizeId = MutableLiveData<Long?>()
    private val _selectedCategoryId = MutableLiveData<Long?>()
    private val _searchQuery = MutableLiveData<String>()

    val filteredItems = MediatorLiveData<List<ItemFull>>().apply {
        addSource(items)              { applyFilters() }
        addSource(_selectedSizeId)    { applyFilters() }
        addSource(_selectedCategoryId){ applyFilters() }
        addSource(_searchQuery)       { applyFilters() }
    }

    private fun applyFilters() {
        val allItems = items.value.orEmpty()
        val sizeId = _selectedSizeId.value
        val categoryId = _selectedCategoryId.value
        val query = _searchQuery.value.orEmpty().trim().lowercase()

        filteredItems.value = allItems.filter { item ->
            matchesSize(item, sizeId) &&
                    matchesCategory(item, categoryId) &&
                    matchesSearch(item, query)
        }
    }

    private fun matchesSize(item: ItemFull, sizeId: Long?): Boolean {
        return sizeId == null || item.item.sizeId == sizeId
    }

    private fun matchesCategory(item: ItemFull, categoryId: Long?): Boolean {
        return categoryId == null || item.item.categoryId == categoryId
    }

    private fun matchesSearch(item: ItemFull, query: String): Boolean {
        return query.isBlank() || item.item.name.lowercase().contains(query)
    }

    fun setSizeFilter(id: Long?)         = _selectedSizeId.postValue(id)
    fun setCategoryFilter(id: Long?)     = _selectedCategoryId.postValue(id)
    fun setSearchQuery(query: String)    = _searchQuery.postValue(query)
}