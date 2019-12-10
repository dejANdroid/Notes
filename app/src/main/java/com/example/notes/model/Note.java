package com.example.notes.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

//    private boolean isSelected=false;


    @PrimaryKey(autoGenerate = true)
    @NonNull

    private Integer id;

    @ColumnInfo(name = "note")
    private String noteDescripiton;

    private String date;

    @Ignore
    public Note(){

    }

    public Note(String noteDescripiton, String date) {
        this.noteDescripiton = noteDescripiton;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoteDescripiton() {
        return noteDescripiton;
    }

    public void setNoteDescripiton(String noteDescripiton) {
        this.noteDescripiton = noteDescripiton;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public void setSelected(boolean selected){
//        this.isSelected=selected;
//    }
//
//    public boolean isSelected(){
//        return isSelected;
//    }
}
