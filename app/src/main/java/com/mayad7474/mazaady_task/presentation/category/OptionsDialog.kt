package com.mayad7474.mazaady_task.presentation.category

import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mayad7474.mazaady_task.databinding.DialogSelectOptionsBinding
import com.mayad7474.mazaady_task.doamin.model.options.Option

object OptionsDialog {

    fun showDialog(
        adapter: OptionsAdapter,
        items: List<Option>,
        canBeEmpty: Boolean = false,
        context: Context,
        operation: String,
        onItemSelected: (Option) -> Unit
    ) {
        val binding = DialogSelectOptionsBinding.inflate(LayoutInflater.from(context))
        val originalItems = if (canBeEmpty) items + fixedOptions else items

        // Reorder items: "Other" first, "Unspecified" second, then the rest
        val reorderedItems = reorderItems(originalItems)

        // Create and show the BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()

        // Initialize the adapter with the reordered list
        adapter.items = reorderedItems
        binding.dialogTitle.text = operation
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Set up item click listener
        adapter.onClickListener { selectedItem ->
            onItemSelected(selectedItem)
            // Dismiss the dialog after selection
            bottomSheetDialog.dismiss()
        }

        // Handle search functionality
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString()
            if (query.isEmpty()) {
                adapter.items = reorderedItems // Restore original list
            } else {
                filterList(query, adapter, reorderedItems)
            }
        }

        // Set up close button
        binding.closeButton.setOnClickListener { bottomSheetDialog.dismiss() }
    }

    private fun reorderItems(items: List<Option>): List<Option> {
        val otherOption = items.find { it.name == "Other" }
        val unspecifiedOption = items.find { it.name == "Unspecified" }
        val remainingItems = items.filter { it.name != "Other" && it.name != "Unspecified" }

        val reorderedList = mutableListOf<Option>()
        otherOption?.let { reorderedList.add(it) }
        unspecifiedOption?.let { reorderedList.add(it) }
        reorderedList.addAll(remainingItems)

        return reorderedList
    }

    private fun filterList(query: String, adapter: OptionsAdapter, originalItems: List<Option>) {
        val filteredList = originalItems.filter {
            it.name.contains(query, ignoreCase = true)
        }
        adapter.items = filteredList
    }

    private val fixedOptions =
        listOf(Option(-1, null, "Other", false), Option(-2, null, "Unspecified"))
}
