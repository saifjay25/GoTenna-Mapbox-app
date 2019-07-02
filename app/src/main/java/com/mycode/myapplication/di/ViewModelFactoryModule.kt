package com.mycode.myapplication.di

import androidx.lifecycle.ViewModelProvider
import com.mycode.myapplication.viewModel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelsProvidersFactory: ViewModelProviderFactory) : ViewModelProvider.Factory
}