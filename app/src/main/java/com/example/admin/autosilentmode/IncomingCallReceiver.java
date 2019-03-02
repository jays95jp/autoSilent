package com.example.admin.autosilentmode;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.admin.autosilentmode.model.TimeBean;
import com.example.admin.autosilentmode.utils.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class IncomingCallReceiver extends BroadcastReceiver {
    String incomingNumber = "";
    TelephonyManager telephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Call", "sd");
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.e("Call-" + TelephonyManager.EXTRA_STATE_RINGING, state);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            DBHelper mydb = new DBHelper(context);

            //            Utils.isMatchTime(bean)
            if (!TextUtils.isEmpty(incomingNumber)) {

                ArrayList<TimeBean> allData = mydb.getAllData();
                for (TimeBean timeBean : allData) {
                    if (Utils.isMatchTime(timeBean)) {
                        rejectCall();
                        sendMessage(context, incomingNumber, timeBean);
                    }
                }

            }
        }

    }

    private void sendMessage(Context context, String number, TimeBean timeBean) {
        // PackageManager pm=context.getPackageManager();
        try {
            String text = timeBean.getMsg();
            String scAddress = null;
            PendingIntent sentIntent = null, deliveryIntent = null;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, scAddress, text, sentIntent, deliveryIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void rejectCall() {
        try {
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            /* Disable Access Check */
            method.setAccessible(true);
            //Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(telephonyManager);
            // Get the end call method from ITelephony
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            //Invoke Endcall()
            methodEndCall.invoke(telephonyInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
