package com.mycode.myapplication.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mycode.myapplication.InstantExecutorExtends
import com.mycode.myapplication.LiveDataTestUtil
import com.mycode.myapplication.entity.PinData
import com.mycode.myapplication.ui.Resource
import io.reactivex.Single
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtends::class)
class RepositoryTest{

    companion object{
        private val pinData = PinData(UtilTest.pinData)
        private val pinList = UtilTest.pinList
    }

    private lateinit var repository : Repository
    @Mock
    private lateinit var pinDAO : PinDAO

    @BeforeEach
    fun init(){
        MockitoAnnotations.initMocks(this)
        repository = Repository(pinDAO)
    }

    private fun <T> any(type : Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    //add meal then verify correct method, then confirm new row is inserted
    @Test
    @Throws(Exception::class)
    fun insertMeal_returnRow() {
        val insertRow = 1L
        val returnData = Single.just(insertRow)
        Mockito.`when`(pinDAO.addPinData(any(PinData::class.java))).thenReturn(returnData)
        val valueReturn = repository.insertPinData(pinData).blockingFirst()
        Mockito.verify(pinDAO).addPinData(any(PinData::class.java))
        verifyNoMoreInteractions(pinDAO)
        Assertions.assertEquals(Resource.success(1, "success"), valueReturn)
    }

    @Test
    @Throws(Exception::class)
    fun insertMealReturnFailure(){
        val insertFail = -1L
        val returnData = Single.just(insertFail)
        Mockito.`when`(pinDAO.addPinData(any(PinData::class.java))).thenReturn(returnData)
        val valueReturn = repository.insertPinData(pinData).blockingFirst()
        Mockito.verify(pinDAO).addPinData(any(PinData::class.java))
        verifyNoMoreInteractions(pinDAO)
        Assertions.assertEquals(Resource.error(null, "failure"), valueReturn)
    }

    @Test
    @Throws(Exception::class)
    fun deleteMeal_returnRow() {
        val deleted = 1
        Mockito.`when`(pinDAO.removeAllPinData()).thenReturn(Single.just(deleted))
        val returnVal = repository.deleteAllPinData().blockingFirst()
        Mockito.verify(pinDAO).removeAllPinData()
        verifyNoMoreInteractions(pinDAO)
        Assertions.assertEquals(Resource.success(1, "success"), returnVal)
    }

    @Test
    @Throws(Exception::class)
    fun deleteMealReturnFailure(){
        val insertFail = -1
        val returnData = Single.just(insertFail)
        Mockito.`when`(pinDAO.removeAllPinData()).thenReturn(returnData)
        val valueReturn = repository.deleteAllPinData().blockingFirst()
        Mockito.verify(pinDAO).removeAllPinData()
        verifyNoMoreInteractions(pinDAO)
        Assertions.assertEquals(Resource.error(null, "failure"), valueReturn)
    }

}