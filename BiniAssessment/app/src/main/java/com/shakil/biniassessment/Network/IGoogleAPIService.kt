package com.shakil.biniassessment.Network

import com.shakil.biniassessment.Model.MyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyPlace(@Url url: String):Call<MyPlaces>
}