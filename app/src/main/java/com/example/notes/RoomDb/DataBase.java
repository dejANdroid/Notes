package com.example.notes.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.model.Note;

@Database(entities = {Note.class}, version = 1)

public abstract class DataBase extends RoomDatabase {

    public abstract Dao myDao();

    private static DataBase INSTANCE;

    public static DataBase getNoteDbInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (DataBase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context, DataBase.class, "db_note")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;

    }


}

