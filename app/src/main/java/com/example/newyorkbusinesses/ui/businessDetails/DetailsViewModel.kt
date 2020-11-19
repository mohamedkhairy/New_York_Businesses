package com.example.newyorkbusinesses.ui.businessDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.newyorkbusinesses.model.BusinessDetails
import com.example.newyorkbusinesses.repository.DetailsRepository
import com.example.newyorkbusinesses.utils.Result
import kotlinx.coroutines.launch

class DetailsViewModel(val repository: DetailsRepository): ViewModel() {


    private val liveDetails = MutableLiveData<Either<Result.Error, Result.Success<BusinessDetails?>>>()

    fun getLiveDetails() = liveDetails

    fun getDetails(buisnessId: String){
        viewModelScope.launch {
            val result = repository.callDetailsService(buisnessId)
            liveDetails.postValue(result)
        }
    }
}