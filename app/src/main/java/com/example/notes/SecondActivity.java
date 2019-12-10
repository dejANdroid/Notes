package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, new FragmentAllNotes())
                .commit();

        Intent intent = getIntent();
        String add = intent.getStringExtra("ADD");

        if (add != null) {
            if (add.contains("add")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, new FragmentAddNote())
                        .commit();
            }
        }
    }
}
