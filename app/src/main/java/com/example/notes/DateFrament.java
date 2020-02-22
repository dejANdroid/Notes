package com.example.notes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;


import java.util.Calendar;

public class DateFrament extends Fragment {

    private int get_hour;
    private int get_minutes;
    private int get_year;
    private int get_month;
    private int get_day;
    private String noteText;
    private String dateText;
    private long id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_alarm, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolBar_alarm);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        TimePicker time = view.findViewById(R.id.time_picker);
        DatePicker date = view.findViewById(R.id.date_picker);
        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                get_hour = hourOfDay;
                get_minutes = minute;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    get_year = year;
                    get_month = monthOfYear;
                    get_day = dayOfMonth;
                }
            });
        }


        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_alarm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.checkAlarmIcon: //TODO

                if(getArguments()!=null){

                        noteText = getArguments().getString("NOTE");
                        dateText = getArguments().getString("DATE");

                        id = DataBase.getNoteDbInstance(getContext()).myDao().addNote(new Note(noteText, dateText));
                        setAlarmNotification();
                }
                Toast toast = Toast.makeText(getContext(), "Notification Alarm is set", Toast.LENGTH_SHORT);
                View toastView = toast.getView();

                TextView toastMessage = toastView.findViewById(android.R.id.message);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setBackgroundColor(Color.GRAY);
                toastView.setBackgroundColor(Color.GRAY);
                toast.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }, 1800);
        }
        return super.onOptionsItemSelected(item);
    }
    public void setAlarmNotification() {

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationReciever.class);
        intent.putExtra("NOTE_TEXT", noteText);
        intent.putExtra("DATE_TEXT", dateText);
        intent.putExtra("ID", id);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(getContext(), (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {

            if (get_month == 0 || get_day == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.get(Calendar.YEAR);
                calendar.get(Calendar.MONTH);
                calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.HOUR_OF_DAY, get_hour);
                calendar.set(Calendar.MINUTE, get_minutes);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.YEAR, get_year);
                calendar.set(Calendar.MONTH, get_month);
                calendar.set(Calendar.DAY_OF_MONTH, get_day);
                calendar.set(Calendar.HOUR_OF_DAY, get_hour);
                calendar.set(Calendar.MINUTE, get_minutes);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
