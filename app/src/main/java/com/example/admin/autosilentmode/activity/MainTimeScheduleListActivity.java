package com.example.admin.autosilentmode.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                Log.e("Call->","Runnable");
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
                TimeSelectActivity.newInstance(this, false,null);
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

}

