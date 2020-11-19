package com.example.newyorkbusinesses.repository

import arrow.core.Either
import com.example.newyorkbusinesses.api.EndPoints
import com.example.newyorkbusinesses.model.BusinessDetails
import com.example.newyorkbusinesses.utils.Result
import com.example.newyorkbusinesses.utils.callAPI

class DetailsRepository(private val endPoints: EndPoints) {

    suspend fun callDetailsService(buisnessId: String): Either<Result.Error, Result.Success<BusinessDetails?>>
            = callAPI {  endPoints.businessDetailsEndpoint(buisnessId) }
}