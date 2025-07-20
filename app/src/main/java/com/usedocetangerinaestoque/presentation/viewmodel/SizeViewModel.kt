package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.entities.Size
import com.usedocetangerinaestoque.usecases.size.AddSizeUseCase
import com.usedocetangerinaestoque.usecases.size.DeleteSizeUseCase
import com.usedocetangerinaestoque.usecases.size.GetAllSizesUseCase
import com.usedocetangerinaestoque.usecases.size.UpdateSizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SizeViewModel @Inject constructor(
    private val addSizeUseCase: AddSizeUseCase,
    private val getAllSizesUseCase: GetAllSizesUseCase,
    private val deleteSizeUseCase: DeleteSizeUseCase,
    private val updateSizeUseCase: UpdateSizeUseCase
) : ViewModel() {

    private val sizes = getAllSizesUseCase().asLiveData()

    private val _searchQuery = MutableLiveData<String>()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = _deleted

    val filteredSizes = MediatorLiveData<List<Size>>().apply {
        addSource(sizes) { filterSizes() }
        addSource(_searchQuery) { filterSizes() }
    }

    private fun filterSizes() {
        val all = sizes.value.orEmpty()
        val query = _searchQuery.value.orEmpty().trim().lowercase()

        filteredSizes.value = all.filter { size ->
            size.name.lowercase().contains(query) || query.isBlank()
        }
    }

    fun addSize(name: String) = viewModelScope.launch {
        if (name.isBlank()) {
            setError("Nome do tamanho não pode ficar em branco.")
            return@launch
        }

        try {
            addSizeUseCase(Size(name = name))
        } catch (e: Exception) {
            setError(e.message ?: "Erro ao adicionar tamanho.")
        }
    }

    fun updateSize(size: Size) = viewModelScope.launch {
        try {
            updateSizeUseCase(size)
        } catch (ex: Exception) {
            setError(ex.message)
        }
    }

    fun deleteSize(size: Size) = viewModelScope.launch {
        try {
            deleteSizeUseCase(size)
            _deleted.postValue(true)
        } catch (ex: Exception) {
            setError(ex.message)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.postValue(query)
    }

    private fun setError(message: String?) {
        _error.postValue(message)
    }

    fun clearError() {
        _error.value = null
    }

    fun clearDeleted() {
        _deleted.value = false
    }
}
