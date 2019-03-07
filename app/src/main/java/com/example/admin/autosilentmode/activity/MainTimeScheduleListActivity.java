package com.example.admin.autosilentmode.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.admin.autosilentmode.DBHelper;
import com.example.admin.autosilentmode.R;
import com.example.admin.autosilentmode.adaper.TimeScheduleListAdapter;
import com.example.admin.autosilentmode.databinding.ActivityTimeScheduleListBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainTimeScheduleListActivity extends AppCompatActivity {

    private ActivityTimeScheduleListBinding activityTimeScheduleListBinding;
    private DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTimeScheduleListBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_schedule_list);
        activityTimeScheduleListBinding.setOnClick(this);
        mydb = new DBHelper(this);
        // And From your main() method or any other method
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent1 = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent1);
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                    }
                });
            }
        }, 0, 1000);

    }

    private void setAdapter() {
        TimeScheduleListAdapter timeScheduleListAdapter = new TimeScheduleListAdapter(this, mydb.getAllData());
        activityTimeScheduleListBinding.recyclerView.setAdapter(timeScheduleListAdapter);

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab:
                TimeSelectActivity.newInstance(this, false, null);
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

}

