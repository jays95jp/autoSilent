package com.example.admin.autosilentmode;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.admin.autosilentmode.model.TimeBean;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TIMER_TABLE_NAME = "TimeSchedule";
    public static final String TIMER_COLUMN_ID = "id";
    public static final String TIMER_COLUMN_NAME = "bean";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table TimeSchedule " +
                        "(id integer primary key, bean text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS TimeSchedule");
        onCreate(db);
    }

    public void insertContact(String bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMER_COLUMN_NAME, bean);
        db.insert(TIMER_TABLE_NAME, null, contentValues);
        close();
    }

    public void updateData(TimeBean bean,int id){

        Gson gson = new Gson();
        String json = gson.toJson(bean);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bean",json);
        db.update(TIMER_TABLE_NAME, contentValues, "id ="+id,null);
        close();
    }
    public ArrayList<TimeBean> getAllData() {
        ArrayList<TimeBean> beanArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("select * from TimeSchedule", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Gson gson = new Gson();
            TimeBean timeBean;
            String json1 = res.getString(res.getColumnIndex(TIMER_COLUMN_NAME));
            int id = res.getInt(res.getColumnIndex(TIMER_COLUMN_ID));
            if (TextUtils.isEmpty(json1))
                timeBean = new TimeBean();
            else
                timeBean = gson.fromJson(json1, TimeBean.class);

            timeBean.setId(id);
            beanArrayList.add(timeBean);
            res.moveToNext();
        }
        close();
        return beanArrayList;
    }
}