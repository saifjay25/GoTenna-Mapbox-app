package com.mycode.myapplication.di

import com.mycode.myapplication.di.main.MainModule
import com.mycode.myapplication.di.main.MainViewModelModule
import com.mycode.myapplication.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [MainViewModelModule::class, MainModule::class])
    abstract fun contributesMainActivity(): MainActivity

}