package com.example.admin.autosilentmode.utils;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;

import com.example.admin.autosilentmode.DBHelper;
import com.example.admin.autosilentmode.activity.TimeSelectActivity;
import com.example.admin.autosilentmode.model.TimeBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jay.Patel on 22/01/19.
 */

public class Utils {

    public static void openTimePickerDialog(Context context, final TimeSelectActivity.TimeSelectInterface timeSelectInterface) {
        TimePickerDialog timePickerDialog;
        Calendar calender = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minutes) {
                timeSelectInterface.setTime(hourOfDay, minutes);
            }
        }, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public static boolean isScheduleTime(Context context) {
        DBHelper myDb = new DBHelper(context);
        ArrayList<TimeBean> beanArrayList = new ArrayList<>();
        beanArrayList.addAll(myDb.getAllData());

        for (TimeBean bean : beanArrayList) {
            if (isMatchTime(bean))
                return true;
        }
        return false;
    }

    public static boolean isMatchTime(TimeBean timeBean) {

        int currentHr, currentMin;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String formattedDate = dateFormat.format(date);

        String[] strings = formattedDate.split(":");

        currentHr = Integer.valueOf(strings[0]);
        currentMin = Integer.valueOf(strings[1]);


        return   ((currentHr>timeBean.getStartHr() && currentHr<timeBean.getEndHr()))
                || (currentHr==timeBean.getStartHr() && currentMin>=timeBean.getStartMin())
                || (currentHr==timeBean.getEndHr() && currentMin<=timeBean.getEndMin());

    }
}
