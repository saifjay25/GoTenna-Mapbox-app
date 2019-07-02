package com.mycode.myapplication.network

import com.mycode.myapplication.entity.PinData
import io.reactivex.Flowable
import retrofit2.http.GET

interface GoTennaAPI {
    @GET("/development/scripts/get_map_pins.php")
    fun getPinData() : Flowable<List<PinData>>
}
