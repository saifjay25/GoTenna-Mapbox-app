package com.mycode.myapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mycode.myapplication.InstantExecutorExtends
import com.mycode.myapplication.LiveDataTestUtil
import com.mycode.myapplication.entity.PinData
import com.mycode.myapplication.network.GoTennaAPI
import com.mycode.myapplication.persistence.Repository
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtends::class)
class MainViewModelTest {
    private var mainViewModel: MainViewModel? = null

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var goTennaAPI: GoTennaAPI

    companion object {
        private val pinData: PinData = PinData(UtilTest.pinData)
    }

    private fun <T> any(type: Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(goTennaAPI, repository)
    }

    @Test
    @Throws(Exception::class)
    fun insertMeal_returnRow() {
        // Arrange
        val insertRow = 1
        val returnedData = Flowable.just(Resource.success(insertRow, "success"))
        Mockito.`when`(repository.insertPinData(any(PinData::class.java))).thenReturn(returnedData)

        val valueReturn = mainViewModel?.addPinData(pinData)?.blockingFirst()

        Mockito.verify(repository).insertPinData(any(PinData::class.java))
        Mockito.verifyNoMoreInteractions(repository)

        assertEquals(Resource.success(insertRow, "success"), valueReturn)
    }

    @Test
    @Throws(Exception::class)
    fun deleteMeal_returnRow() {
        // Arrange
        val insertRow = 1
        val returnedData = Flowable.just(Resource.success(insertRow, "success"))
        Mockito.`when`(repository.deleteAllPinData()).thenReturn(returnedData)

        val valueReturn = mainViewModel?.deleteAllPinData()?.blockingFirst()

        Mockito.verify(repository).deleteAllPinData()
        Mockito.verifyNoMoreInteractions(repository)

        assertEquals(Resource.success(insertRow, "success"), valueReturn)
    }
}

