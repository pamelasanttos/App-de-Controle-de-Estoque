package com.usedocetangerinaestoque.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usedocetangerinaestoque.data.relations.CategoryWithFirstItem
import com.usedocetangerinaestoque.data.relations.ItemFull
import com.usedocetangerinaestoque.usecases.category.GetAllCategoriesUseCase
import com.usedocetangerinaestoque.usecases.item.GetAllItensUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllItemsUseCase: GetAllItensUseCase
) : ViewModel() {

    private val _categoriesWithImages = MutableLiveData<List<CategoryWithFirstItem>>()
    val categoriesWithImages: LiveData<List<CategoryWithFirstItem>> get() = _categoriesWithImages

    private val _items = MutableLiveData<List<ItemFull>>()
    val items: LiveData<List<ItemFull>> get() = _items

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            combine(
                getAllCategoriesUseCase(),
                getAllItemsUseCase()
            ) { categories, items ->
                Pair(categories, items)
            }.collect { (categories, items) ->
                processHomeData(categories, items)
            }
        }
    }

    private fun processHomeData(
        categories: List<com.usedocetangerinaestoque.data.entities.Category>,
        items: List<ItemFull>
    ) {
        val groupedItems = items.groupBy { it.category?.id }

        val categoriesWithImages = categories.map { category ->
            val firstImagePath = groupedItems[category.id]
                ?.firstOrNull()
                ?.images
                ?.firstOrNull()
                ?.path

            CategoryWithFirstItem(category, firstImagePath)
        }

        _categoriesWithImages.postValue(categoriesWithImages)
        _items.postValue(items)
    }
}