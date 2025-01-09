package com.mayad7474.mazaady_task.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mayad7474.mazaady_task.R
import com.mayad7474.mazaady_task.core.constants.Strings
import com.mayad7474.mazaady_task.core.exceptions.CustomException
import com.mayad7474.mazaady_task.core.extensions.collect
import com.mayad7474.mazaady_task.core.extensions.loge
import com.mayad7474.mazaady_task.core.extensions.logw
import com.mayad7474.mazaady_task.databinding.FragmentCategoriesBinding
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory
import com.mayad7474.mazaady_task.doamin.model.categories.toOption
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        viewModel.getState()
        observeState()
        setActions()
    }

    private fun observeState() {
        viewModel.state.collect(viewLifecycleOwner) { state ->
            when (state) {
                is CategoriesUIState.Failure -> handleFailure(state.exception)
                is CategoriesUIState.LoadedCategories -> {
//                    handleLoadedCategories(state)
                }
                is CategoriesUIState.Loading -> state.isLoading.logw(TAG)
                CategoriesUIState.Idle -> Unit
                is CategoriesUIState.LoadedProperties -> {
//                    propertiesAdapter.items = state.properties
                }
            }
        }

        viewModel.allCats.collect(viewLifecycleOwner) { categories ->
            val selectedCat = categories.firstOrNull { it.isSelected }
            binding.mainCategoryET.editText?.setText(selectedCat?.name.orEmpty())
            selectedCat?.subCategories?.let { setupSubCategorySelection(it) }
            setupMainCategorySelection(categories)
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
                operation = getString(Strings.main_category),
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
                operation = getString(Strings.sub_category),
                onItemSelected = { option ->
                    val selectedSubCategory = subCategories.first { it.id == option.id }
                    viewModel.selectSubCat(selectedSubCategory)
                }
            )
        }
    }

    private fun handleFailure(exception: CustomException) {
        exception.loge(TAG)
        Snackbar.make(
            binding.root,
            requireContext().getString(exception.msg),
            Snackbar.LENGTH_SHORT
        ).show()
    }


    private fun setActions() = binding.apply {
        btnNaviateToTable.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_tableFragment)
        }
        btnNaviateToHome.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_homeFragment)
        }
        propertiesAdapter.setOnOptionSelectedListener { property, option ->
            if ((option.id == -1 || option.id == -2).not())
                viewModel.getOptionForProperty(option, property)
        }
    }

    companion object {
        private const val TAG = "CategoriesFragment"
    }
}

