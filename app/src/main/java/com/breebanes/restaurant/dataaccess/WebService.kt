package com.breebanes.restaurant.dataaccess

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    @GET("v2/restaurant/")
    suspend fun getRestaurantList(
        @Query("lat") lat: String,
        @Query("lng") long: String
    ) : Response<List<Restaurant>>

    @GET("/v2/restaurant/{restaurant_id}")
    suspend fun getRestaurantDetail(
        @Path("restaurant_id") id: String
    ) : Response<Restaurant>
}