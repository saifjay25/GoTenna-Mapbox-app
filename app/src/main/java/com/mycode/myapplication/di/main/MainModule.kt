package com.mycode.myapplication.di.main

import com.mycode.myapplication.network.GoTennaAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object MainModule {

    @Provides
    @JvmStatic
    fun provideMainAPI (retrofit : Retrofit): GoTennaAPI
    {
        return retrofit.create(GoTennaAPI::class.java)
    }
}