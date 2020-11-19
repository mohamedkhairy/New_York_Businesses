package com.example.newyorkbusinesses.di

import com.example.newyorkbusinesses.repository.BusinessRepository
import com.example.newyorkbusinesses.repository.DetailsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { BusinessRepository(get()) }

    factory { DetailsRepository(get()) }
}