package com.mycode.myapplication.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.mycode.myapplication.entity.PinData;

@Database(entities = {PinData.class}, version = 1)
public abstract class PinDatabase extends RoomDatabase {

    public abstract PinDAO pinDAO();
    private static PinDatabase instance = null;
    private static PinDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,
                    PinDatabase.class, "database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
