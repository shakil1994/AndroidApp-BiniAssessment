package com.shakil.biniassessment.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Result {
    var business_status: String? = null
    val geometry: Geometry? = null
    var icon: String? = null
    var icon_background_color: String? = null
    var icon_mask_base_uri: String? = null
    var name: String? = null
    var opening_hours: OpeningHours? = null
    var photos: Array<Photos>? = null
    var place_id: String? = null
    var plus_code: PlusCode? = null
    var rating: Double = 0.0
    var reference: String? = null
    var scope: String? = null
    var types: Array<String>? = null
    var user_ratings_total: Int = 0
    var vicinity: String? = null
    var price_level: Int = 0


}