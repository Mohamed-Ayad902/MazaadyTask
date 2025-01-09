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

//@HiltViewModel
//class CategoriesVM @Inject constructor(
//    private val getCategoriesUC: GetCategoriesUC,
//    private val getPropertiesUC: GetPropertiesUC,
//    private val getOptionsForPropertyUC: GetOptionsForPropertyUC,
//) : ViewModel() {
//
//    private val _state = MutableStateFlow<CategoriesUIState>(CategoriesUIState.Idle)
//    val state = _state.asStateFlow()
//
//    init {
//        getCategories()
//    }
//
//    private val _selectedCat = MutableStateFlow<Category?>(null)
//    val selectedCat: StateFlow<Category?> = _selectedCat.asStateFlow()
//
//    private val _selectedSubCat = MutableStateFlow<SubCategory?>(null)
//    val selectedSubCat: StateFlow<SubCategory?> = _selectedSubCat.asStateFlow()
//
//
//    fun selectCat(category: Category) {
//        _selectedCat.value = category
//        _selectedSubCat.value = null
//        _state.value = CategoriesUIState.LoadedProperties(emptyList())
//    }
//
//    fun selectSubCat(subCategory: SubCategory) {
//        _state.value = CategoriesUIState.LoadedProperties(emptyList())
//        _selectedSubCat.value = subCategory
//        getProperties(subCategory.id)
//    }
//
//    fun getCategories() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getCategoriesUC.invoke().collectLatest {
//                when (it) {
//                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
//                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
//                    is Resource.Success -> {
//                        _state.emit(CategoriesUIState.LoadedCategories(it.data))
//                        _selectedCat.value = it.data.first()
//                        _selectedSubCat.value = null
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getProperties(subCatId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            getPropertiesUC.invoke(subCatId).collect {
//                when (it) {
//                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
//                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
//                    is Resource.Success -> _state.emit(CategoriesUIState.LoadedProperties(it.data))
//                }
//            }
//        }
//    }
//
//    fun getOptionForProperty(propertyId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            getOptionsForPropertyUC.invoke(propertyId).collect {
//                when (it) {
//                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
//                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
//                    is Resource.Success -> _state.emit(CategoriesUIState.LoadedOptionsForProperty(it.data))
//                }
//            }
//        }
//    }
//}


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

    private val _selectedCat = MutableStateFlow<Category?>(null)
    val selectedCat: StateFlow<Category?> = _selectedCat.asStateFlow()

    private val _selectedSubCat = MutableStateFlow<SubCategory?>(null)
    val selectedSubCat: StateFlow<SubCategory?> = _selectedSubCat.asStateFlow()

    init {
        getCategories()
    }

    fun selectCat(category: Category) {
        if (category != _selectedCat.value) {
        _selectedCat.value = category
        _selectedSubCat.value = null
        _properties.value = emptyList()
        _state.value = CategoriesUIState.LoadedProperties(emptyList())
        }
    }

    fun selectSubCat(subCategory: SubCategory) {
        if (subCategory != _selectedSubCat.value) {
        _selectedSubCat.value = subCategory
        _properties.value = emptyList()
        getProperties(subCategory.id)
        }
    }

    init {
        getCategories()
    }

    fun getState(){
        _properties.value = _properties.value
        _selectedCat.value = _selectedCat.value
        _selectedSubCat.value = _selectedSubCat.value
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesUC.invoke().collectLatest {
                when (it) {
                    is Resource.Error -> _state.emit(CategoriesUIState.Failure(it.exception))
                    is Resource.Loading -> _state.emit(CategoriesUIState.Loading(it.loading))
                    is Resource.Success -> {
                        _state.emit(CategoriesUIState.LoadedCategories(it.data))
                        _selectedCat.value = it.data.first()
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
                        val newProperties = it.data // Assuming this is a List<Property>
                        _properties.value = _properties.value.toMutableList().apply {
                            addAll(
                                indexOf(property) + 1,
                                newProperties
                            ) // Insert the new properties right after the specified index
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

