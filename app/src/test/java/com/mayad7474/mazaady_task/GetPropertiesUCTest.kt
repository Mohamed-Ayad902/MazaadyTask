package com.mayad7474.mazaady_task

import com.mayad7474.mazaady_task.core.utils.Resource
import com.mayad7474.mazaady_task.doamin.model.options.Option
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import com.mayad7474.mazaady_task.doamin.useCase.GetPropertiesUC
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
class GetPropertiesUCTest {

    private lateinit var useCase: GetPropertiesUC
    private val repository: ICategoriesRepo = mockk()

    @Before
    fun setUp() {
        useCase = GetPropertiesUC(repository)
    }

    @Test
    fun `invoke should return properties successfully`() = runTest {
        val subCatId = 2
        val properties = listOf(
            Property(1, "Property1", listOf(Option(1, 1, "Option1", false))),
            Property(2, "Property2", listOf(Option(2, 2, "Option2", true)))
        )
        coEvery { repository.getProperties(subCatId) } returns properties // Mock raw data

        val resultFlow = useCase.invoke(subCatId)
        val results = resultFlow.toList()

        assertTrue(results.first() is Resource.Loading) // First emission should be Loading
        assertTrue(results[1] is Resource.Success) // Second emission should be Success
        assertEquals(properties, (results[1] as Resource.Success).data) // Verify data
        assertTrue(results.last() is Resource.Loading) // Last emission should be Loading(false)
        assertFalse((results.last() as Resource.Loading).loading) // Verify loading is false

        coVerify { repository.getProperties(subCatId) }
    }
}
