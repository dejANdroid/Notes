package com.example.notes.RoomDb;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.model.Note;

import java.util.List;

@androidx.room.Dao
public interface Dao {


    @Insert
    long addNote(Note note);

    @Insert
    long retriveId(Note note);

    @Query("select * from note_table" )
    List<Note> getAllNotes();

    @Delete
    void deleteNote(Note note);

    @Query("delete from note_table")
    void deleteAllNotes();

    @Update
    void updateNote(Note note);

    @Query("select * from note_table where id=:id")
    List<Note> getNoteId(int id);
}
