package com.mycode.myapplication.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.mycode.myapplication.entity.PinData;
import io.reactivex.Single;

import java.util.List;
@Dao
public interface PinDAO {

    @Query("SELECT * FROM pinDataTable")
    LiveData<List<PinData>> getAllPinData();

    @Insert
    Single<Long> addPinData(PinData pinData);

    @Query("DELETE FROM pinDataTable")
    Single<Integer> removeAllPinData();
}
