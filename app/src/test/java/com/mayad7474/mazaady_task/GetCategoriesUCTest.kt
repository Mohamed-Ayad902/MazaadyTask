package com.mayad7474.mazaady_task

import com.mayad7474.mazaady_task.core.utils.Resource
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import com.mayad7474.mazaady_task.doamin.useCase.GetCategoriesUC
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCategoriesUCTest {

    private lateinit var useCase: GetCategoriesUC
    private val repository: ICategoriesRepo = mockk()

    @Before
    fun setUp() {
        useCase = GetCategoriesUC(repository)
    }

    @Test
    fun `invoke should return categories successfully`() = runTest {
        // Arrange
        val categories = listOf(
            Category(1, "Cat1", true, listOf(SubCategory(1, "sub1", false))),
            Category(2, "Cat2", false, listOf(SubCategory(3, "sub1OfCat2", false)))
        )
        coEvery { repository.getCategories() } returns categories // Mock raw data

        // Act
        val resultFlow = useCase.invoke() // Get the flow from the use case
        val results = resultFlow.toList() // Collect all emissions from the flow

        // Assert
        assertTrue(results.first() is Resource.Loading) // First emission should be Loading
        assertTrue(results[1] is Resource.Success) // Second emission should be Success
        assertEquals(categories, (results[1] as Resource.Success).data) // Verify data
        assertTrue(results.last() is Resource.Loading) // Last emission should be Loading(false)
        assertFalse((results.last() as Resource.Loading).loading) // Verify loading is false

        coVerify { repository.getCategories() }
    }
}
