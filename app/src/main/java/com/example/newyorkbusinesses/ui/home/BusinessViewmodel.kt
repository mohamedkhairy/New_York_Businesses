package com.example.newyorkbusinesses.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.newyorkbusinesses.model.Businesse
import com.example.newyorkbusinesses.paging.BusinessDataSource.Companion.SIZE
import com.example.newyorkbusinesses.paging.BusinessDataSourceFactory
import com.example.newyorkbusinesses.repository.BusinessRepository

class BusinessViewmodel(val repository: BusinessRepository): ViewModel() {

    private val ioScope = viewModelScope
    private val source = BusinessDataSourceFactory(repository = repository, scope = ioScope, searchkey = null)
     private val businesselist = LivePagedListBuilder(source , pagedListConfig()).build()
    private val requestState = source.dataSource.switchMap { it.getRequestState() }

    fun getLiveBusinessList(): LiveData<PagedList<Businesse>?>? = businesselist

    fun getLiveRequestState() = requestState

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(SIZE)
        .build()



    fun searchForBusiness(searchKey: String?) {
        searchKey?.let {
            val search = it.trim()
            if (search.length <= 0) return
            if (source.getsearchkey() == search) return
            source.updateSearch(search)
        }
    }


    fun refreshAllList() =
        source.resetAndRefresh()

}