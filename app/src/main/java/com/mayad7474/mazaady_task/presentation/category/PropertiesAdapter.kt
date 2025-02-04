package com.mayad7474.mazaady_task.presentation.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mayad7474.mazaady_task.databinding.ItemPropertyBinding
import com.mayad7474.mazaady_task.doamin.model.options.Option
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import javax.inject.Inject

class PropertiesAdapter @Inject constructor(private val context: Context) :
    RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder>() {

    var items: List<Property>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val differCallBack = object : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Property, newItem: Property) = oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, differCallBack)

    private var onOptionSelected: ((Property, Option) -> Unit)? = null
    fun setOnOptionSelectedListener(listener: (Property, Option) -> Unit) {
        onOptionSelected = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder {
        val binding =
            ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) {
        val property = items[position]
        holder.binding.apply {
            mainCategoryET.hint = property.name
            val selectedOption = property.options.firstOrNull { it.isSelected }
            otherOptionET.visibility = if (property.otherOption != null) View.VISIBLE else View.GONE
            val text = if (selectedOption?.name != null) selectedOption.name
            else if (property.otherOption != null) "Other"
            else ""

            mainCategoryET.editText?.setText(text)
            otherOptionET.editText?.setText(property.otherOption ?: "")

            btnSelectOption.setOnClickListener {
                OptionsDialog.showDialog(
                    adapter = OptionsAdapter(context),
                    items = property.options,
                    canBeEmpty = true,
                    context = holder.itemView.context,
                    operation = property.name,
                    onItemSelected = { option ->
                        mainCategoryET.editText?.setText(option.name)
                        if (option.name == "Other") {
                            otherOptionET.visibility = View.VISIBLE
                            otherOptionET.editText?.doOnTextChanged { text, _, _, _ ->
                                property.otherOption = text.toString()
                            }
                        } else {
                            property.otherOption = null
                            otherOptionET.editText?.setText("")
                            otherOptionET.visibility = View.GONE
                        }
                        onOptionSelected?.invoke(property, option)
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class PropertiesViewHolder(val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root)
}