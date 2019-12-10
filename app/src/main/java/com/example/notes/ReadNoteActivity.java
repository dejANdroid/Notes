package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;

public class ReadNoteActivity extends AppCompatActivity {

    EditText noteT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        Toolbar toolbar = findViewById(R.id.toolBar_readNote);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageButton checkedBtn=findViewById(R.id.checkBtn);
        ImageButton shareBtn=findViewById(R.id.shareBtn);
        noteT = findViewById(R.id.read_noteTv);
        TextView dateTv = findViewById(R.id.read_dateTv);

        Intent intent = getIntent();
        final String text = intent.getStringExtra("Note item text");
        final String date = intent.getStringExtra("Note item date");
        final int id=intent.getIntExtra("Note id",1);

        String textFromNotification=intent.getStringExtra("NOTE");
        String dateFromNotification=intent.getStringExtra("DATE");

        if(getIntent().hasExtra("Note id")){

            noteT.setText(text);
            dateTv.setText(date);

        }else {

            noteT.setText(textFromNotification);
            dateTv.setText(dateFromNotification);

//            Toast.makeText(ReadNoteActivity.this, "Try not to forget!", Toast.LENGTH_SHORT).show();
        }

        noteT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteT.setCursorVisible(true);
            }
        });

        checkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note note=new Note();
                note.setId(id);
                note.setDate(date);
                note.setNoteDescripiton(noteT.getText().toString());

                DataBase.getNoteDbInstance(ReadNoteActivity.this).myDao().updateNote(note);

                Toast toast=Toast.makeText(ReadNoteActivity.this, "Note is saved", Toast.LENGTH_SHORT);
                View toastView=toast.getView();

                TextView toastMessage = toastView.findViewById(android.R.id.message);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setBackgroundColor(Color.GRAY);
                toastView.setBackgroundColor(Color.GRAY);
                toast.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent=new Intent(ReadNoteActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                },1800);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mimeType = "text/plain";
                String title = "learning how to share";
                String textToShare = noteT.getText().toString();

                ShareCompat.IntentBuilder.from(ReadNoteActivity.this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(textToShare)
                        .startChooser();
            }
        });
    }
}
