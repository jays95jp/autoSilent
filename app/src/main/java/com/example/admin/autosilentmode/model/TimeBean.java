package com.example.admin.autosilentmode.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jay.Patel on 22/01/19.
 */

public class TimeBean implements Parcelable {

    public TimeBean() {

    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("startHr")
    @Expose
    private int startHr;
    @SerializedName("startMin")
    @Expose
    private int startMin;
    @SerializedName("endHr")
    @Expose
    private int endHr;
    @SerializedName("endMin")
    @Expose
    private int endMin;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStartHr() {
        return startHr;
    }

    public void setStartHr(int startHr) {
        this.startHr = startHr;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndHr() {
        return endHr;
    }

    public void setEndHr(int endHr) {
        this.endHr = endHr;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }


    protected TimeBean(Parcel in) {
        name = in.readString();
        msg = in.readString();
        startHr = in.readInt();
        startMin = in.readInt();
        endHr = in.readInt();
        endMin = in.readInt();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(msg);
        dest.writeInt(startHr);
        dest.writeInt(startMin);
        dest.writeInt(endHr);
        dest.writeInt(endMin);
        dest.writeInt(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeBean> CREATOR = new Parcelable.Creator<TimeBean>() {
        @Override
        public TimeBean createFromParcel(Parcel in) {
            return new TimeBean(in);
        }

        @Override
        public TimeBean[] newArray(int size) {
            return new TimeBean[size];
        }
    };
}