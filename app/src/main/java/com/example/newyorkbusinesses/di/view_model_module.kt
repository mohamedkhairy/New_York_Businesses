package com.example.newyorkbusinesses.di

import com.example.newyorkbusinesses.ui.businessDetails.DetailsViewModel
import com.example.newyorkbusinesses.ui.home.BusinessViewmodel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BusinessViewmodel(get()) }

    viewModel { DetailsViewModel(get()) }
}
