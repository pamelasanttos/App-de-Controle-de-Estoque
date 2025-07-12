package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.entities.Category
import com.usedocetangerinaestoque.data.entities.Image
import com.usedocetangerinaestoque.data.entities.Item
import com.usedocetangerinaestoque.data.entities.Size
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.exceptions.ItemAlreadyExistsException
import com.usedocetangerinaestoque.usecases.category.AddCategoryUseCase
import com.usedocetangerinaestoque.usecases.category.GetAllCategoriesUseCase
import com.usedocetangerinaestoque.usecases.item.AddNewItemUseCase
import com.usedocetangerinaestoque.usecases.item.GetItemFullByIdUseCase
import com.usedocetangerinaestoque.usecases.item.UpdateItemUseCase
import com.usedocetangerinaestoque.usecases.size.AddSizeUseCase
import com.usedocetangerinaestoque.usecases.size.GetAllSizesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addNewItemUseCase: AddNewItemUseCase,
    private val getAllSizesUseCase: GetAllSizesUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addSizeUseCase: AddSizeUseCase,
    private val getItemFullByIdUseCase: GetItemFullByIdUseCase,
    private val updateItemUseCase: UpdateItemUseCase
) : ViewModel() {

    val sizes: LiveData<List<Size>> = getAllSizesUseCase().asLiveData()
    val categories: LiveData<List<Category>> = getAllCategoriesUseCase().asLiveData()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _saveStatus = MutableLiveData<Boolean>()
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _editItem = MutableLiveData<ItemFull?>()
    val editItem: LiveData<ItemFull?> get() = _editItem

    fun clearError() {
        _error.value = null
    }

    private fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    private fun setError(message: String?) {
        _error.value = message
    }

    private fun setSaveStatus(success: Boolean) {
        _saveStatus.value = success
    }

    fun loadItemForEdit(itemId: Long) {
        viewModelScope.launch {
            try {
                val item = getItemFullByIdUseCase(itemId)
                _editItem.postValue(item)
            } catch (e: Exception) {
                setError("Erro ao carregar item para edição.")
            }
        }
    }

    fun saveItem(
        name: String,
        description: String,
        value: Double,
        quantity: Int,
        sizeId: Long,
        categoryId: Long,
        imagePaths: List<String>
    ) {
        if (name.isBlank() || description.isBlank()) {
            setError("Nome e descrição não podem ficar em branco.")
            return
        }

        viewModelScope.launch {
            setLoading(true)
            setError(null)

            try {
                val itemFull = buildItemFull(
                    name = name,
                    description = description,
                    value = value,
                    quantity = quantity,
                    sizeId = sizeId,
                    categoryId = categoryId,
                    imagePaths = imagePaths
                )

                addNewItemUseCase(itemFull)
                setSaveStatus(true)
            } catch (e: Exception) {
                setError(e.message ?: "Erro ao salvar item.")
                setSaveStatus(false)
            } finally {
                setLoading(false)
            }
        }
    }

    fun updateItem(
        id: Long,
        name: String,
        description: String,
        value: Double,
        quantity: Int,
        sizeId: Long,
        categoryId: Long,
        imagePaths: List<String>
    ) {
        viewModelScope.launch {
            try {
                val itemFull = buildItemFull(
                    id = id,
                    name = name,
                    description = description,
                    value = value,
                    quantity = quantity,
                    sizeId = sizeId,
                    categoryId = categoryId,
                    imagePaths = imagePaths
                )

                updateItemUseCase(itemFull)
                _saveStatus.postValue(true)
            } catch (e: ItemAlreadyExistsException) {
                _error.postValue(e.message)
            } catch (e: Exception) {
                _error.postValue("Erro ao atualizar item.")
            }
        }
    }

    private fun buildItemFull(
        id: Long? = null,
        name: String,
        description: String,
        value: Double,
        quantity: Int,
        sizeId: Long,
        categoryId: Long,
        imagePaths: List<String>
    ): ItemFull {
        val item = Item(
            id = id ?: 0L,
            name = name,
            description = description,
            value = value,
            quantity = quantity,
            sizeId = sizeId,
            categoryId = categoryId
        )

        val images = imagePaths.map { Image(path = it) }

        return ItemFull(item = item, images = images)
    }

    fun addSize(name: String) {
        if (name.isBlank()) {
            setError("Nome do tamanho não pode ficar em branco.")
            return
        }

        viewModelScope.launch {
            try {
                addSizeUseCase(Size(name = name))
            } catch (e: Exception) {
                setError(e.message ?: "Erro ao adicionar tamanho.")
            }
        }
    }

    fun addCategory(name: String) {
        if (name.isBlank()) {
            setError("Nome da categoria não pode ficar em branco.")
            return
        }

        viewModelScope.launch {
            try {
                addCategoryUseCase(Category(name = name))
            } catch (e: Exception) {
                setError(e.message ?: "Erro ao adicionar categoria.")
            }
        }
    }
}