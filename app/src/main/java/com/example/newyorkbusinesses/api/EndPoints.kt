package com.example.newyorkbusinesses.api

import com.example.newyorkbusinesses.model.BusinessDetails
import com.example.newyorkbusinesses.model.CityBusinessData
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndPoints {

    @GET("businesses/search")
    fun businessEndpoint(@Query("location") location: String,
                         @Query("limit") limit: Int,
                         @Query("offset") offset: Int,
                         @Query("term") term: String? = null): Deferred<CityBusinessData>


    @GET("businesses/{id}")
    fun businessDetailsEndpoint(@Path("id") id: String): Deferred<BusinessDetails>
}