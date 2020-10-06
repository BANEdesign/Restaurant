package com.breebanes.restaurant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.breebanes.restaurant.dataaccess.Restaurant
import com.breebanes.restaurant.dataaccess.WebService
import com.breebanes.restaurant.utils.CoroutinesDispatcherProvider
import com.breebanes.restaurant.viewmodel.RestaurantViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.*
import retrofit2.Response

const val LAT = "37.422740"
const val LONG = "-122.139956"

@ExperimentalCoroutinesApi
class RestaurantViewModelTest {
    @get:Rule
    var coroutinesRule: MainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val api: WebService = mock(WebService::class.java)
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcher = CoroutinesDispatcherProvider(
        testCoroutineDispatcher
    )

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchRestaurants when api returns Success`() = coroutinesRule.runBlocking {
        val mockShop = mockShop()
        `when`(api.getRestaurantList(LAT, LONG))
            .thenReturn(Response.success(mockShop))

        val viewModel = RestaurantViewModel(CoroutinesDispatcherProvider())

        viewModel.fetchRestaurants(LAT, LONG)

        assertEquals(mockShop, viewModel.restaurantList.value)
    }

    private fun mockShop(): List<Restaurant> = listOf(
        Restaurant(
            "123",
            "Mock Restaurant",
            "tacos, burritos",
            "img_url",
            "closed",
            0.0
        )
    )
}

class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    fun runBlocking(block: suspend () -> Unit) = this.testDispatcher.runBlockingTest {
        block()
    }
}