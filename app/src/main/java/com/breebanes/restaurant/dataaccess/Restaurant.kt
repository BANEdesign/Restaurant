package com.breebanes.restaurant.dataaccess

import com.squareup.moshi.Json

class Restaurant(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "description")
    val description: String = "",
    @Json(name = "cover_img_url")
    val coverImgUrl: String = "",
    @Json(name = "status")
    val status: String = "",
    @Json(name = "delivery_fee")
    val deliveryFee: Double? = null
)
