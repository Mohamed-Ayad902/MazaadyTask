package com.mayad7474.mazaady_task.presentation.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mayad7474.mazaady_task.databinding.ItemOptionBinding
import com.mayad7474.mazaady_task.doamin.model.options.Option
import javax.inject.Inject

class OptionsAdapter@Inject constructor(context: Context) : RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    private var onClick: ((Option) -> Unit)? = null
    fun onClickListener(onItemClick: (Option) -> Unit) {
        onClick = onItemClick
    }

    var items: List<Option>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val differCallBack = object : DiffUtil.ItemCallback<Option>() {
        override fun areItemsTheSame(oldItem: Option, newItem: Option) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Option, newItem: Option) =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OptionsViewHolder(
            ItemOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val option = differ.currentList[position]
        holder.binding.apply {
            optionText.text = option.name
            holder.itemView.setOnClickListener { onClick?.invoke(option) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class OptionsViewHolder(val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root)
}