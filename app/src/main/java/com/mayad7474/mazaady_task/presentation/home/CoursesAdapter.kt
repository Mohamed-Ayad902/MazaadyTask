package com.mayad7474.mazaady_task.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.intuit.sdp.R.dimen._10sdp
import com.intuit.sdp.R.dimen._4sdp
import com.intuit.sdp.R.dimen._8sdp
import com.mayad7474.mazaady_task.R
import com.mayad7474.mazaady_task.databinding.ItemSliderBinding
import com.mayad7474.mazaady_task.doamin.model.main.course.Course
import javax.inject.Inject

class CoursesAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<CoursesAdapter.CoursesVH>() {

    var items: List<Course>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val differCallBack = object : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Course, newItem: Course) =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CoursesVH(
            ItemSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CoursesVH, position: Int) {
        val course = differ.currentList[position]
        holder.binding.apply {
            courseImage.setImageResource(course.image)
            courseName.text = course.name
            courseTime.text = course.time
            upperTag.isVisible = course.upperTag != null
            course.upperTag?.let { upperTag.text = it }
            userImage.setImageResource(course.userImage)
            username.text = course.username
            userTitle.text = course.userTitle

            courseTags.removeAllViews()

            course.tags.forEach { tag ->
                val tagView = TextView(holder.itemView.context).apply {
                    text = tag.name
                    setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                    setPadding(
                        context.resources.getDimensionPixelSize(_8sdp),
                        context.resources.getDimensionPixelSize(_4sdp),
                        context.resources.getDimensionPixelSize(_8sdp),
                        context.resources.getDimensionPixelSize(_4sdp)
                    )
                    background =
                        ContextCompat.getDrawable(context, R.drawable.rounded_corners_rectangle)
                    backgroundTintList = ContextCompat.getColorStateList(context, tag.color)

                }
                // Set layout parameters with margin end
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = holder.itemView.context.resources.getDimensionPixelSize(_10sdp)
                }
                tagView.layoutParams = layoutParams
                courseTags.addView(tagView)
            }

        }

    }

    override fun getItemCount(): Int = differ.currentList.size
    class CoursesVH(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root)
}