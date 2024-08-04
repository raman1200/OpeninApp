package com.raman.project.openinapp.di

import com.raman.project.openinapp.repositories.LinkRepository
import com.raman.project.openinapp.viewmodels.LinkViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent


@InstallIn(FragmentComponent::class)
@Module
object BasicModule {

    @Provides
    fun provideLinkRepository(): LinkRepository {
        return LinkRepository()
    }


    @Provides
    fun provideLinkViewModel(repository: LinkRepository): LinkViewModel {
        return LinkViewModel(repository)
    }

}