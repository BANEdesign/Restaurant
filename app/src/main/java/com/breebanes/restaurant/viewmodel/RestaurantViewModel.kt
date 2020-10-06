package com.breebanes.restaurant.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.breebanes.restaurant.dataaccess.Restaurant
import com.breebanes.restaurant.dataaccess.RetrofitClient
import com.breebanes.restaurant.dataaccess.WebService
import com.breebanes.restaurant.utils.CoroutinesDispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _restaurantList = MutableLiveData<List<Restaurant>>()
    val restaurantList: LiveData<List<Restaurant>>
        get() = _restaurantList
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant>
        get() = _restaurant

    val retrofit = RetrofitClient.getClient()
    val apiService = retrofit.create(WebService::class.java)

    fun fetchRestaurants(lat: String, long: String): Job = viewModelScope.launch(dispatcher.io) {
        try {
            val response = apiService.getRestaurantList(lat, long)
            when {
                response.isSuccessful -> _restaurantList.postValue(response.body()?.map { it })
            }
        } catch (exception : Exception){
            Log.e("RestaurantViewModel", "Failed to fetch data!")
        }
    }

    fun fetchRestaurantById(id: String): Job = viewModelScope.launch(dispatcher.io) {
        try {
            val response = apiService.getRestaurantDetail(id)
            when {
                response.isSuccessful -> _restaurant.postValue(response.body())
            }
        } catch (e: Exception) {
            Log.e("RestaurantViewModel", "Failed to fetch data!")
        }
    }
}