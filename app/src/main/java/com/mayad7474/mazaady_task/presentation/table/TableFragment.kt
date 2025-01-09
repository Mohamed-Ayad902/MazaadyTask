package com.mayad7474.mazaady_task.presentation.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mayad7474.mazaady_task.core.extensions.collect
import com.mayad7474.mazaady_task.core.extensions.logd
import com.mayad7474.mazaady_task.databinding.FragmentTableBinding
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import com.mayad7474.mazaady_task.presentation.category.CategoriesUIState
import com.mayad7474.mazaady_task.presentation.category.CategoriesVM
import dagger.hilt.android.AndroidEntryPoint
import ir.androidexception.datatable.model.DataTableHeader
import ir.androidexception.datatable.model.DataTableRow

@AndroidEntryPoint
class TableFragment : Fragment() {
    private lateinit var binding: FragmentTableBinding
    private val viewModel: CategoriesVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        viewModel.getAllData()
    }
    private fun observeData() {
        viewModel.state.collect(viewLifecycleOwner) { state ->
            when (state) {
                is CategoriesUIState.LoadedProperties -> {
                    setupDataTable()
                    "LoadedProperties: ${state.properties}".logd("TableFragment")
                }
                else -> Unit
            }
        }
    }

    private fun setupDataTable() {
        val rows = mutableListOf<DataTableRow>()

        // Add Main Category
        viewModel.selectedCat.value?.let { category ->
            rows.add(DataTableRow.Builder().value("Main Category").value(category.name).build())
        }

        // Add Sub Category
        viewModel.selectedSubCat.value?.let { subCategory ->
            rows.add(DataTableRow.Builder().value("Sub Category").value(subCategory.name).build())
        }

        // Add Properties
        viewModel.properties.value.forEach { property ->
            val selectedOption = property.options.firstOrNull { it.isSelected }?.name ?: "Not Selected"
            rows.add(DataTableRow.Builder().value(property.name).value(selectedOption).build())
        }

        binding.dataTable.apply {
            header = DataTableHeader.Builder()
                .item("Key", 1)
                .item("Value", 1)
                .build()

            removeAllViews()
            this.rows = rows as ArrayList<DataTableRow>
            inflate(requireContext())
        }
    }
}