package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentAddNote extends Fragment {

    private View view;
    private EditText editText;
    private String currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.framnet_add_note,container,false);

        editText=view.findViewById(R.id.noteEdt);
        editText.setCursorVisible(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setCursorVisible(true);
                editText.setText("");
            }
        });

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = new Date();
        currentDate = timeStampFormat.format(myDate);
        

        Toolbar toolbar=view.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_note,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.addNoteIcon:

                MainActivity.hideKeyboard(getActivity());

                AlertDialog.Builder alert=new AlertDialog.Builder(getContext())
                        .setTitle("Note Reminder")
                        .setMessage("Do you want to set reminder about this note?")
                        .setPositiveButton("set reminder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DateFrament dateFrament=new DateFrament();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_frame,dateFrament)
                                        .addToBackStack("back")
                                        .commit();

                                Bundle bundle=new Bundle();
                                bundle.putString("NOTE",editText.getText().toString());
                                bundle.putString("DATE",currentDate);
                                dateFrament.setArguments(bundle);
                            }
                        })
                        .setNegativeButton("save note", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String noteText=editText.getText().toString();

                                DataBase.getNoteDbInstance(getContext()).myDao()
                                        .addNote(new Note(noteText, currentDate));

                                Toast toast=Toast.makeText(getContext(), "Saved to 'My Notes'", Toast.LENGTH_SHORT);
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

                                        Intent intent=new Intent(getActivity(),MainActivity.class);
                                        startActivity(intent);
                                    }
                                },1800);
                            }
                        });
                alert.show();
                break;
                default:
        }
        return super.onOptionsItemSelected(item);

    }


}
