package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.entities.Category
import com.usedocetangerinaestoque.usecases.category.AddCategoryUseCase
import com.usedocetangerinaestoque.usecases.category.DeleteCategoryUseCase
import com.usedocetangerinaestoque.usecases.category.GetAllCategoriesUseCase
import com.usedocetangerinaestoque.usecases.category.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    private val categories = getAllCategoriesUseCase().asLiveData()
    private val _searchQuery = MutableLiveData<String>()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = _deleted

    val filteredCategories = MediatorLiveData<List<Category>>().apply {
        addSource(categories)     { filterCategories() }
        addSource(_searchQuery)   { filterCategories() }
    }

    private fun filterCategories() {
        val all = categories.value.orEmpty()
        val query = _searchQuery.value.orEmpty().trim().lowercase()

        filteredCategories.value = all.filter { category ->
            category.name.lowercase().contains(query)
        }
    }

    fun addCategory(name: String) = viewModelScope.launch {
        if (name.isBlank()) {
            setError("Nome da categoria não pode ficar em branco.")
            return@launch
        }

        try {
            addCategoryUseCase(Category(name = name))
        } catch (e: Exception) {
            setError(e.message ?: "Erro ao adicionar categoria.")
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        try {
            deleteCategoryUseCase(category)
            _deleted.postValue(true)
        } catch (ex: Exception) {
            setError(ex.message)
        }
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        try {
            updateCategoryUseCase(category)
        } catch (ex: Exception) {
            setError(ex.message)
        }
    }

    private fun setError(message: String?) {
        _error.postValue(message)
    }

    fun clearError() {
        _error.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.postValue(query)
    }

    fun clearDeleted() {
        _deleted.value = false
    }
}
