package com.nerdstone.neatformcore.datasource.local;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nerdstone.neatformcore.utils.Constants;

//@Database(entities = NFormViewData.class, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    private static volatile LocalDatabase INSTANCE;

    public static LocalDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, Constants.Database.NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract FormDataDao formDataDao();

}
