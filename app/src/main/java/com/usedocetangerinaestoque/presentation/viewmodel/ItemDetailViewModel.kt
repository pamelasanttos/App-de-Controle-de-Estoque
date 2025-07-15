package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.entities.Item
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.usecases.item.DeleteItemUseCase
import com.usedocetangerinaestoque.usecases.item.GetItemFullByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val getItemDetailUseCase: GetItemFullByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    private val _item = MutableLiveData<ItemFull>()
    val item: LiveData<ItemFull> get() = _item

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = _deleted

    fun loadItem(itemId: Long) {
        viewModelScope.launch {
            try {
                val result = getItemDetailUseCase(itemId)
                _item.postValue(result)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            try {
                deleteItemUseCase(item)
                _deleted.postValue(true)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    private fun setError(message: String?) {
        _error.postValue(message ?: "Erro desconhecido.")
    }

    fun clearError() {
        _error.value = null
    }
}