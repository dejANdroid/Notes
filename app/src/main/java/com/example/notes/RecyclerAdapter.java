package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> implements Filterable {

    List<Note> mList;
    List<Note> getListSearch;
    CustomListenerGetNote mListener;
    Context context;

    public void setOnCustomListener(CustomListenerGetNote mListener) {
        this.mListener = mListener;
    }

    public RecyclerAdapter(List<Note> mList, Context context) {

        this.mList = mList;
        this.getListSearch = new ArrayList<>(mList);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_layout, parent, false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.MyHolder holder, final int position) {

        final Note note = mList.get(position);

        holder.noteText.setText(note.getNoteDescripiton());
        holder.dateText.setText(note.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete All Notes")
                        .setMessage("Do you want to delete all notes?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DataBase.getNoteDbInstance(context).myDao().deleteAllNotes();
                                mList.clear();
                                getListSearch.clear();
                                notifyDataSetChanged();

                                Toast toast=Toast.makeText(context, "All notes are deleted", Toast.LENGTH_SHORT);

                                View toastView=toast.getView();

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

                return true;

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView noteText, dateText;
        View view;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            view = itemView;
            noteText = itemView.findViewById(R.id.noteText);
            dateText = itemView.findViewById(R.id.dateText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.getNoteForPosition(position);
                        }
                    }
                }
            });
        }
    }

    public Note getNoteAtPosition(int position) {
        return mList.get(position);
    }


    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(getListSearch);
            } else {

                String filterName = constraint.toString().toLowerCase().trim();

                for (Note note : getListSearch) {

                    if (note.getNoteDescripiton().toLowerCase().contains(filterName)) {
                        filterList.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mList.clear();
            mList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
