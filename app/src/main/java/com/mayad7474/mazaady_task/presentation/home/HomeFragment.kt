package com.mayad7474.mazaady_task.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.intuit.sdp.R.dimen._20sdp
import com.mayad7474.mazaady_task.core.constants.Colors
import com.mayad7474.mazaady_task.core.utils.ViewPagerIndicatorUtils
import com.mayad7474.mazaady_task.databinding.FragmentHomeBinding
import com.mayad7474.mazaady_task.doamin.model.main.course.courses
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewPagerAdapter: CoursesAdapter

    @Inject
    lateinit var indicatorUtils: ViewPagerIndicatorUtils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        binding.liveSection.isHorizontalScrollBarEnabled = false

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                if (radioButton.id == checkedId)
                    radioButton.setTextColor(getColor(requireContext(), Colors.white))
                else
                    radioButton.setTextColor(getColor(requireContext(), Colors.text_inactive))
            }
        }


        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        viewPagerAdapter.items = courses

        binding.viewPager.setPageTransformer(ScalePageTransformer())
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.viewPager.setPadding(
                        0,
                        0,
                        resources.getDimensionPixelSize(_20sdp),
                        0
                    )
                } else {
                    binding.viewPager.setPadding(
                        resources.getDimensionPixelSize(_20sdp),
                        0,
                        resources.getDimensionPixelSize(_20sdp),
                        0
                    )
                }
            }
        })

        indicatorUtils.setupIndicators(
            viewPager = binding.viewPager,
            linearLayout = binding.indicatorContainer,
            itemCount = viewPagerAdapter.itemCount,
            adapter = viewPagerAdapter
        )

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        indicatorUtils.destroy()
    }

}

class ScalePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scale = if (position < -1 || position > 1) {
            0.6f // Scale down for off-screen pages
        } else {
            1 - (0.15f * Math.abs(position)) // Gradually scale down
        }

        val alpha = if (position < -1 || position > 1) {
            0.5f // Set lower alpha for off-screen pages
        } else {
            1 - (0.5f * Math.abs(position)) // Gradually reduce alpha
        }

        page.scaleY = scale
        page.alpha = alpha
    }
}
