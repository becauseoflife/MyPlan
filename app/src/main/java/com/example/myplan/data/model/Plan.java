package com.example.myplan.data.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {
    private static final long serialVersionUID=1L;
    private String title;                // 标题
    private String remarks;              // 备注信息
    private int backgroundImg;           // 背景图
    private ArrayList<String> label;     // 标签
    private int cycleTime;               // 重复设置

    private int year;       // 年
    private int month;      // 月
    private int day;        // 日
    private int hour;       // 时
    private int minute;     // 分
    private int second;     // 秒

    public Plan() {}

    public Plan(String title, String remarks, int backgroundImg, ArrayList<String> label, int cycleTime, int year, int month, int day, int hour, int minute, int second) {
        this.title = title;
        this.remarks = remarks;
        this.backgroundImg = backgroundImg;
        this.label = label;
        this.cycleTime = cycleTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(int backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public ArrayList<String> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<String> label) {
        this.label = label;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

}
