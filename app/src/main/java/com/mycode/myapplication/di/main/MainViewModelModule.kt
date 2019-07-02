package com.mycode.myapplication.di.main

import androidx.lifecycle.ViewModel
import com.mycode.myapplication.di.ViewModelKey
import com.mycode.myapplication.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel) : ViewModel
}