package com.example.newyorkbusinesses.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import arrow.core.Either
import com.example.newyorkbusinesses.model.Businesse
import com.example.newyorkbusinesses.repository.BusinessRepository
import com.example.newyorkbusinesses.utils.RequestState
import kotlinx.coroutines.*

class BusinessDataSource(private val repository: BusinessRepository,
                         private val scope: CoroutineScope,
                         private var searchkey: String?): PageKeyedDataSource<Int, Businesse>() {


    companion object {
        const val LOACTION = "NYC"
        const val SIZE = 30
    }
    private var supervisorJob = SupervisorJob()
    private var total = 0
    private val requestState = MutableLiveData<RequestState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Businesse>) {
        getBusiness(0 ,searchkey) {
            callback.onResult(it, null, getNextPage(0))
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Businesse>) {
        val page = params.key
        getBusiness(page ,searchkey) {
            callback.onResult(it, getNextPage(page))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Businesse>) { }

    private fun getBusiness(page: Int, searchkey: String? = null, callback:(List<Businesse>) -> Unit) {
        requestState.postValue(RequestState.RUNNING)
        scope.launch(getJobErrorHandler() + supervisorJob) {
            var searchResult = repository.callBusinessService(LOACTION, SIZE, page, searchkey)

            when(searchResult){
                is Either.Right ->{
                    val data = searchResult.b.t
                    if (data.businesses.isNullOrEmpty()){
                        requestState.postValue(RequestState.FAILED)
                    }else {
                        requestState.postValue(RequestState.SUCCESS)
                        total = data.total
                        callback(data.businesses)
                    }
                }
                is Either.Left ->{
                    requestState.postValue(RequestState.FAILED)
                }
            }

        }
    }

    private fun getNextPage(current: Int): Int{
        val page = current+ SIZE
        if(total > 1000){
            val maxOffset = 1000 - SIZE
            if (page > maxOffset) return maxOffset
            else return page
        }else{
            if (current > total) return total - SIZE
            else return page
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(BusinessDataSource::class.java.simpleName, "An error happened: $e")
        requestState.postValue(RequestState.FAILED)
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()
    }


    fun getRequestState(): LiveData<RequestState> =
        requestState

    fun refresh() =
        this.invalidate()



}