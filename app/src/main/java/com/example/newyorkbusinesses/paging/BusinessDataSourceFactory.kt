package com.example.newyorkbusinesses.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.newyorkbusinesses.model.Businesse
import com.example.newyorkbusinesses.repository.BusinessRepository
import kotlinx.coroutines.CoroutineScope

class BusinessDataSourceFactory(private val repository: BusinessRepository,
                                private val scope: CoroutineScope,
                                private var searchkey: String?): DataSource.Factory<Int, Businesse>() {

    val dataSource = MutableLiveData<BusinessDataSource>()

    override fun create(): DataSource<Int, Businesse> {
        val source = BusinessDataSource(repository, scope , searchkey)
        this.dataSource.postValue(source)
        return source
    }


    fun getsearchkey() = searchkey

    fun getSource() = dataSource.value

    fun updateSearch(key: String){
        this.searchkey = key
        getSource()?.refresh()
    }

    fun resetAndRefresh() {
        this.searchkey = null
        getSource()?.refresh()
    }
}