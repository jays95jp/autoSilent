package com.example.admin.autosilentmode.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.autosilentmode.DBHelper;
import com.example.admin.autosilentmode.R;
import com.example.admin.autosilentmode.databinding.ActivityTimeSelectBinding;
import com.example.admin.autosilentmode.fireBaseJob.MyReceiver;
import com.example.admin.autosilentmode.model.TimeBean;
import com.example.admin.autosilentmode.utils.Utils;
import com.google.gson.Gson;

import java.util.Calendar;

public class TimeSelectActivity extends AppCompatActivity {

    private ActivityTimeSelectBinding activityTimeSelectBinding;
    private int startHr, startMin, endHr, endMin;
    private DBHelper myDb;
    private boolean isEdit = false;
    private TimeBean timeBean;

    public static void newInstance(Context context, boolean isEdit, TimeBean bean) {
        Intent intent = new Intent(context, TimeSelectActivity.class);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("bean", bean);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTimeSelectBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_select);
        activityTimeSelectBinding.setOnClick(this);
        myDb = new DBHelper(this);

        isEdit = getIntent().getExtras().getBoolean("isEdit");

        if (isEdit) {
            setEditData();
        }
    }

    private void setEditData() {
        timeBean = (TimeBean) getIntent().getExtras().get("bean");
        activityTimeSelectBinding.txtStartTime.setText(String.format("%02d:%02d", timeBean.getStartHr(), timeBean.getStartMin()));
        activityTimeSelectBinding.txtEndTime.setText(String.format("%02d:%02d", timeBean.getEndHr(), timeBean.getEndMin()));
        activityTimeSelectBinding.edtScheduleTitle.setText(timeBean.getName());
        activityTimeSelectBinding.edtMsg.setText(timeBean.getMsg());
        startHr = timeBean.getStartHr();
        startMin = timeBean.getStartMin();

        endHr = timeBean.getEndHr();
        endMin = timeBean.getEndMin();

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_start_time:
                Utils.openTimePickerDialog(TimeSelectActivity.this, new TimeSelectInterface() {
                    @Override
                    public void setTime(int hourOfDay, int minutes) {
                        Log.e("Hr->" + hourOfDay, "Min->" + minutes);
                        startHr = hourOfDay;
                        startMin = minutes;

                        activityTimeSelectBinding.txtStartTime.setText(String.format("%02d:%02d", startHr, startMin));
                    }
                });
                break;

            case R.id.txt_end_time:
                Utils.openTimePickerDialog(TimeSelectActivity.this, new TimeSelectInterface() {
                    @Override
                    public void setTime(int hourOfDay, int minutes) {
                        Log.e("Hr->" + hourOfDay, "Min->" + minutes);
                        endHr = hourOfDay;
                        endMin = minutes;

                        activityTimeSelectBinding.txtEndTime.setText(String.format("%02d:%02d", endHr, endMin));
                    }
                });
                break;

            case R.id.btn_save:
                if (isValid() && !isEdit) {
                    TimeBean timeBean = new TimeBean();
                    timeBean.setName(activityTimeSelectBinding.edtScheduleTitle.getText().toString());
                    timeBean.setStartHr(startHr);
                    timeBean.setStartMin(startMin);
                    timeBean.setEndHr(endHr);
                    timeBean.setEndMin(endMin);
                    timeBean.setMsg(activityTimeSelectBinding.edtMsg.getText().toString());

                    Gson gson = new Gson();
                    String json = gson.toJson(timeBean);

                    myDb.insertContact(json);
                    setJob(startHr, startMin + 1);
                    setJob(endHr, endMin + 1);
                    finish();

                } else if (isValid() && isEdit) {
                    TimeBean timeBean = new TimeBean();
                    timeBean.setName(activityTimeSelectBinding.edtScheduleTitle.getText().toString());
                    timeBean.setStartHr(startHr);
                    timeBean.setStartMin(startMin);
                    timeBean.setEndHr(endHr);
                    timeBean.setEndMin(endMin);
                    timeBean.setMsg(activityTimeSelectBinding.edtMsg.getText().toString());

                    myDb.updateData(timeBean, this.timeBean.getId());
                    finish();
                } else {
                    Toast.makeText(this, "Please provide valid information", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void setJob(int hr, int min) {
        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        Intent myIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntentam = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManageram = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManageram.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntentam);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntentam);

    }

    private boolean isValid() {
        return ((startHr != endHr &&
                startHr < endHr) || (startHr == endHr && startMin < endMin)) &&
                !TextUtils.isEmpty(activityTimeSelectBinding.edtMsg.getText().toString()) &&
                !TextUtils.isEmpty(activityTimeSelectBinding.edtScheduleTitle.getText().toString());
    }

    public interface TimeSelectInterface {
        void setTime(int hourOfDay, int minutes);
    }
}
