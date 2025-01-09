package com.mayad7474.mazaady_task.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayad7474.mazaady_task.core.utils.Resource
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory
import com.mayad7474.mazaady_task.doamin.model.options.Option
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import com.mayad7474.mazaady_task.doamin.useCase.GetCategoriesUC
import com.mayad7474.mazaady_task.doamin.useCase.GetOptionsForPropertyUC
import com.mayad7474.mazaady_task.doamin.useCase.GetPropertiesUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesVM @Inject constructor(
    private val getCategoriesUC: GetCategoriesUC,
    private val getPropertiesUC: GetPropertiesUC,
    private val getOptionsForPropertyUC: GetOptionsForPropertyUC,
) : ViewModel() {

    private val _state = MutableStateFlow<CategoriesUIState>(CategoriesUIState.Idle)
    val state = _state.asStateFlow()

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties = _properties.asStateFlow()

    private val _allCats = MutableStateFlow<List<Category>>(listOf())
    val allCats: StateFlow<List<Category>> = _allCats.asStateFlow()

    private val _selectedSubCat = MutableStateFlow<SubCategory?>(null)
    val selectedSubCat: StateFlow<SubCategory?> = _selectedSubCat.asStateFlow()

    init {
        getCategories()
    }

    fun getState(){
        _properties.value = _properties.value
        _allCats.value = _allCats.value
        _selectedSubCat.value = _selectedSubCat.value
    }

    fun selectCat(category: Category) {
        _allCats.value = _allCats.value.map {
            it.copy(isSelected = it.id == category.id)
        }
        _selectedSubCat.value = null
        _properties.value = emptyList()
        _state.value = CategoriesUIState.LoadedProperties(emptyList())
    }

    fun selectSubCat(subCategory: SubCategory) {
        if (subCategory != _selectedSubCat.value) {
            _selectedSubCat.value = subCategory
            _properties.value = emptyList()
            getProperties(subCategory.id)
        }
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUC.invoke().collectLatest {
                when (it) {
                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
                    is Resource.Success -> {
                        _allCats.value = it.data.mapIndexed { index, category ->
                            category.copy(isSelected = index == 0) // Mark the first category as selected
                        }
                        _state.emit(CategoriesUIState.LoadedCategories(_allCats.value))
                        _selectedSubCat.value = null
                    }
                }
            }
        }
    }

    private fun getProperties(subCatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPropertiesUC.invoke(subCatId).collect {
                when (it) {
                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
                    is Resource.Success -> {
                        _properties.value = it.data
                        _state.emit(CategoriesUIState.LoadedProperties(it.data))
                    }
                }
            }
        }
    }

    fun getOptionForProperty(option: Option, property: Property) {
        _properties.value.first { it.id == property.id }.options.forEach { it.isSelected = false }
        _properties.value.first { it.id == property.id }.options.first { it.id == option.id }.isSelected = true

        viewModelScope.launch(Dispatchers.IO) {
            getOptionsForPropertyUC.invoke(option.id).collect {
                when (it) {
                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
                    is Resource.Success -> {
                        val newProperties = it.data
                        _properties.value = _properties.value.toMutableList().apply {
                            addAll(indexOf(property) + 1, newProperties)
                        }
                        _state.emit(CategoriesUIState.LoadedProperties(_properties.value))
                    }
                }
            }
        }
    }

    fun getAllData() {
        viewModelScope.launch {
            _state.emit(CategoriesUIState.LoadedProperties(_properties.value))
        }
    }
}

