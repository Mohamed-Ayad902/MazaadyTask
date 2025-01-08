package com.mayad7474.mazaady_task.core.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mayad7474.mazaady_task.core.constants.Drawables
import javax.inject.Inject

class ViewPagerIndicatorUtils @Inject constructor(private val context: Context) :
    ViewPager2.OnPageChangeCallback() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorView: LinearLayout
    private var itemCount: Int = 0

    override fun onPageSelected(position: Int) {
        setCurrentIndicator(position)
    }

    fun setupIndicators(
        viewPager: ViewPager2,
        linearLayout: LinearLayout,
        itemCount: Int,
        adapter: RecyclerView.Adapter<*>? = null
    ) {
        this.viewPager = viewPager
        this.indicatorView = linearLayout
        this.itemCount = itemCount
        adapter?.let { this.viewPager.adapter = it }
        viewPager.registerOnPageChangeCallback(this)
        initializeIndicators(itemCount)
        setCurrentIndicator(0)
    }

    private fun initializeIndicators(itemCount: Int) {
        indicatorView.removeAllViews()
        val indicators = arrayOfNulls<ImageView>(itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        for (i in indicators.indices) {
            indicators[i] = ImageView(context).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(context, Drawables.indicator_inactive)
                )
                this.layoutParams = layoutParams
            }
            indicatorView.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorView.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorView[i] as ImageView
            val drawableRes = if (i == index) {
                Drawables.indicator_active
            } else {
                Drawables.indicator_inactive
            }
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        }
    }

    fun getCurrentPosition(): Int = viewPager.currentItem

    fun destroy() {
        if (::viewPager.isInitialized) {
            viewPager.unregisterOnPageChangeCallback(this)
        }
    }
}
