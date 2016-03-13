package app.expense.org.expensemanagerapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Calendar;

public class ReminderCreateActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create_actvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();

                final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                final Intent intentAlarm = new Intent(ReminderCreateActvity.this, ReminderPendingActivity.class);
                Calendar calendar =  Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 45);
                calendar.set(Calendar.HOUR, 11);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DAY_OF_MONTH, 13);
                calendar.set(Calendar.YEAR, 2016);
                long when = calendar.getTimeInMillis();
                alarmManager.set(AlarmManager.RTC_WAKEUP, when, PendingIntent.getBroadcast(ReminderCreateActvity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ReminderCreateActvity.this, DashboardActivity.class));
        finish();
    }
}
