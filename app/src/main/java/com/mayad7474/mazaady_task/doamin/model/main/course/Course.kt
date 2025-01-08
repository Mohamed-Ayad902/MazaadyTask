package com.mayad7474.mazaady_task.doamin.model.main.course

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.mayad7474.mazaady_task.core.constants.Colors
import com.mayad7474.mazaady_task.core.constants.Drawables

data class Course(
    val id: Int,
    @DrawableRes val image: Int,
    val name: String,
    val time: String,
    val upperTag: String? = null,
    @DrawableRes val userImage: Int,
    val username: String,
    val userTitle: String,
    val tags: List<CourseTag>
)

data class CourseTag(
    val name: String,
    @ColorRes val color: Int
)

val courses = listOf(
    Course(
        id = 1,
        image = Drawables.first_page,
        name = "Step design sprint for beginner",
        time = "5h 21m",
        upperTag = "Free e-book",
        userImage = Drawables.first_page_user,
        username = "Laurel Seilha",
        userTitle = "Product Designer",
        tags = listOf(
            CourseTag(name = "6 Lessons", color = Colors.cyan),
            CourseTag(name = "UI/UX", color = Colors.blue),
            CourseTag(name = "Free", color = Colors.purple)
        )
    ),
    Course(
        id = 2,
        image = Drawables.second_page,
        name = "Basic skill for sketch illustratio",
        time = "3h 21m",
        userImage = Drawables.first_page_user,
        username = "Laurel Seilha",
        userTitle = "Product Designer",
        tags = listOf(
            CourseTag("2 Lessongs", Colors.cyan),
            CourseTag("Design", Colors.blue),
        ),
    )
)
