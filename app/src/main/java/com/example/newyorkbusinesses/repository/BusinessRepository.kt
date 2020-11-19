package com.example.newyorkbusinesses.repository

import arrow.core.Either
import com.example.newyorkbusinesses.api.EndPoints
import com.example.newyorkbusinesses.model.CityBusinessData
import com.example.newyorkbusinesses.utils.Result
import com.example.newyorkbusinesses.utils.callAPI

class BusinessRepository(private val endPoints: EndPoints) {

    suspend fun callBusinessService(location: String , limit: Int, offset: Int ,searchkey: String?): Either<Result.Error, Result.Success<CityBusinessData>>
            = callAPI {  endPoints.businessEndpoint(location ,limit ,offset,searchkey) }

}