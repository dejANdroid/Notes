package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;

import java.util.List;

public class FragmentAllNotes extends Fragment implements CustomListenerGetNote {

    private View view;
    private RecyclerAdapter adapter;
    private List<Note> allNotes;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmetn_all_notes, container, false);

        Toolbar toolbar = view.findViewById(R.id.all_note_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        allNotes = DataBase.getNoteDbInstance(getContext()).myDao().getAllNotes();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_allNotes);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter = new RecyclerAdapter(allNotes, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnCustomListener(this);

        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void getNoteForPosition(int position) {

        Intent intent = new Intent(getContext(), ReadNoteActivity.class);
        intent.putExtra("Note item text", allNotes.get(position).getNoteDescripiton());
        intent.putExtra("Note item date", allNotes.get(position).getDate());
        intent.putExtra("Note id", allNotes.get(position).getId());
        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_layout, menu);

        MenuItem itemSearch = menu.findItem(R.id.searchBtn);
        SearchView searchView = (SearchView) itemSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

            MenuItem itemDelete = menu.findItem(R.id.delete_allBtn).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                            .setTitle("Delete All Notes")
                            .setMessage("Do you want to delete all notes?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DataBase.getNoteDbInstance(getContext()).myDao().deleteAllNotes();
                                    allNotes.clear();
                                    adapter.getListSearch.clear();
                                    adapter.notifyDataSetChanged();

                                    Toast toast = Toast.makeText(getContext(), "All notes are deleted", Toast.LENGTH_SHORT);

                                    View toastView = toast.getView();

                                    TextView toastMessage = toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextColor(Color.WHITE);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setBackgroundColor(Color.GRAY);
                                    toastView.setBackgroundColor(Color.GRAY);
                                    toast.show();
                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });
                    alert.show();
                    return false;
                }
            });

            public void setItemDelete(MenuItem itemDelete) {

                this.itemDelete = itemDelete;
            }
        });
    }

    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
            (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int id = viewHolder.getAdapterPosition();
            Note note = adapter.getNoteAtPosition(id);

            allNotes.remove(viewHolder.getAdapterPosition());
            adapter.getListSearch.remove(viewHolder.getAdapterPosition());
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            DataBase.getNoteDbInstance(getContext()).myDao().deleteNote(note);
        }
    });


}
