package com.mycode.myapplication.persistence
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

abstract class PinDatabaseTest {

    private lateinit var pinDatabase: PinDatabase

    fun getPintDAO() :PinDAO{
        return pinDatabase.pinDAO()
    }
    @Before
    fun init() {
        pinDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
            pinDatabase::class.java
        ).build()
    }

    @After
    fun finish() {
        pinDatabase.close()
    }
}