package com.mayad7474.mazaady_task.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mayad7474.mazaady_task.R
import com.mayad7474.mazaady_task.core.exceptions.CustomException
import com.mayad7474.mazaady_task.core.extensions.collect
import com.mayad7474.mazaady_task.core.extensions.logd
import com.mayad7474.mazaady_task.core.extensions.loge
import com.mayad7474.mazaady_task.core.extensions.logw
import com.mayad7474.mazaady_task.databinding.FragmentCategoriesBinding
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory
import com.mayad7474.mazaady_task.doamin.model.categories.toOption
import com.mayad7474.mazaady_task.doamin.model.options.Option
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private val viewModel: CategoriesVM by viewModels()

    @Inject
    lateinit var optionsAdapter: OptionsAdapter

    @Inject
    lateinit var propertiesAdapter: PropertiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(context))
        binding.propertiesRV.adapter = propertiesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories()
        observeState()
        setActions()
    }

    private fun observeState() {
        viewModel.state.collect(viewLifecycleOwner) { state ->
            when (state) {
                is CategoriesUIState.Failure -> handleFailure(state.exception)
                is CategoriesUIState.LoadedCategories -> handleLoadedCategories(state)
                is CategoriesUIState.Loading -> state.isLoading.logw(TAG)
                CategoriesUIState.Idle -> Unit
                is CategoriesUIState.LoadedProperties -> handleLoadedProperties(state.properties)
            }
        }
    }

    private fun handleLoadedProperties(properties: List<Property>) {
        "LoadedProperties: $properties".logd(TAG)
        propertiesAdapter.items = properties
    }

    private fun handleLoadedCategories(state: CategoriesUIState.LoadedCategories) {
        val selectedMainCategory = state.categories.firstOrNull { it.isSelected }
        binding.mainCategoryET.editText?.setText(selectedMainCategory?.name.orEmpty())

        setupMainCategorySelection(state.categories)
        selectedMainCategory?.subCategories?.let { setupSubCategorySelection(it) }
    }

    private fun setupMainCategorySelection(categories: List<Category>) {
        binding.btnSelectCategory.setOnClickListener {
            OptionsDialog.showDialog(
                adapter = optionsAdapter,
                items = categories.map { it.toOption() },
                canBeEmpty = false,
                context = requireContext(),
                operation = "Main Category",
                onItemSelected = { option ->
                    binding.mainCategoryET.editText?.setText(option.name)

                    // Reset the subcategory field when a new main category is selected
                    binding.subCategoryET.editText?.setText("")
                    binding.btnSelectSubs.setOnClickListener(null) // Clear previous subcategory selection listener

                    // Update subcategories based on the selected main category
                    val subCategories = categories.first { it.id == option.id }.subCategories
                    setupSubCategorySelection(subCategories)
                }
            )
        }
    }


    private fun handleFailure(exception: CustomException) {
        exception.loge(TAG)
        Snackbar.make(
            */
/* context = *//*
 requireContext(),
            */
/* view = *//*
 binding.root,
            */
/* text = *//*
 requireContext().getString(exception.msg),
            */
/* duration = *//*
 Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setupSubCategorySelection(subCategories: List<SubCategory>) {
        binding.btnSelectSubs.setOnClickListener {
            OptionsDialog.showDialog(
                adapter = optionsAdapter,
                items = subCategories.map { it.toOption() },
                canBeEmpty = false,
                context = requireContext(),
                operation = "Sub Category",
                onItemSelected = { option ->
                    binding.subCategoryET.editText?.setText(option.name)
                    viewModel.getProperties(option.id)
                }
            )
        }
    }

    private fun setActions() = binding.apply {
        btnNaviateToHome.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_homeFragment)
        }
    }

    companion object {
        private const val TAG = "CategoriesFragment"
    }
}*/


@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private val viewModel: CategoriesVM by activityViewModels ()

    @Inject
    lateinit var optionsAdapter: OptionsAdapter

    @Inject
    lateinit var propertiesAdapter: PropertiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        binding.propertiesRV.adapter = propertiesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getCategories()
        viewModel.getState()
        observeState()
        setActions()

        // Set the listener for property item interactions
        propertiesAdapter.setOnOptionSelectedListener { property, option ->
            handlePropertyOptionSelected(property, option)
        }
    }

    private fun observeState() {
        viewModel.state.collect(viewLifecycleOwner) { state ->
            when (state) {
                is CategoriesUIState.Failure -> handleFailure(state.exception)
                is CategoriesUIState.LoadedCategories -> handleLoadedCategories(state)
                is CategoriesUIState.Loading -> state.isLoading.logw(TAG)
                CategoriesUIState.Idle -> Unit
                is CategoriesUIState.LoadedProperties -> propertiesAdapter.items = state.properties
            }
        }

        viewModel.selectedCat.collect(viewLifecycleOwner) { category ->
            binding.mainCategoryET.editText?.setText(category?.name.orEmpty())
            category?.subCategories?.let { setupSubCategorySelection(it) }
        }

        viewModel.selectedSubCat.collect(viewLifecycleOwner) { subCategory ->
            binding.subCategoryET.editText?.setText(subCategory?.name.orEmpty())
        }

        viewModel.properties.collect(viewLifecycleOwner) { properties ->
            propertiesAdapter.items = properties
        }
    }

    private fun setupMainCategorySelection(categories: List<Category>) {
        binding.btnSelectCategory.setOnClickListener {
            OptionsDialog.showDialog(
                adapter = optionsAdapter,
                items = categories.map { it.toOption() },
                canBeEmpty = false,
                context = requireContext(),
                operation = "Main Category",
                onItemSelected = { option ->
                    val selectedCategory = categories.first { it.id == option.id }
                    viewModel.selectCat(selectedCategory)
                }
            )
        }
    }

    private fun setupSubCategorySelection(subCategories: List<SubCategory>) {
        binding.btnSelectSubs.setOnClickListener {
            OptionsDialog.showDialog(
                adapter = optionsAdapter,
                items = subCategories.map { it.toOption() },
                canBeEmpty = false,
                context = requireContext(),
                operation = "Sub Category",
                onItemSelected = { option ->
                    val selectedSubCategory = subCategories.first { it.id == option.id }
                    viewModel.selectSubCat(selectedSubCategory)
                }
            )
        }
    }

    private fun handleLoadedCategories(state: CategoriesUIState.LoadedCategories) {
        val selectedMainCategory = state.categories.firstOrNull { it.isSelected }
        binding.mainCategoryET.editText?.setText(selectedMainCategory?.name.orEmpty())

        setupMainCategorySelection(state.categories)
        selectedMainCategory?.subCategories?.let { setupSubCategorySelection(it) }
    }

    private fun handleFailure(exception: CustomException) {
        exception.loge(TAG)
        Snackbar.make(
            binding.root,
            requireContext().getString(exception.msg),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handlePropertyOptionSelected(property: Property, option: Option) {
        if ((option.id == -1 || option.id == -2).not())
            viewModel.getOptionForProperty(option, property)
    }

    private fun setActions() = binding.apply {
        btnNaviateToHome.setOnClickListener {
//            findNavController().navigate(R.id.action_categoriesFragment_to_homeFragment)
            findNavController().navigate(R.id.action_categoriesFragment_to_tableFragment)
        }
    }

    companion object {
        private const val TAG = "CategoriesFragment"
    }
}

