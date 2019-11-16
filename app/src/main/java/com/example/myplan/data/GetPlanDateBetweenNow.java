package com.example.myplan.data;

/*
* 计算日期相隔天数
* */

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetPlanDateBetweenNow {
    private static final String RETURN_SECOND = "秒";
    private static final String RETURN_MINUTE = "分";
    private static final String RETURN_HOUR = "时";
    private static final String RETURN_DAY = "日";
    private static final String RETURN_MONTH = "月";
    private static final String RETURN_YEAR = "年";

    private String DateStr;
    private String pattern;

    public GetPlanDateBetweenNow(String otherDateStr, String pattern) {
        this.DateStr = otherDateStr;
        this.pattern = pattern;
    }

    // 与当前时间比较算出天数
    public long CalculationMillisecond() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String nowDateStr = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        Date nowDate = simpleDateFormat.parse(nowDateStr);
        Date otherDate = simpleDateFormat.parse(DateStr);

        //Log.i("returnTime","" + (int) ((otherDate.getTime() - nowDate.getTime()) / 1000));
        return (otherDate.getTime() - nowDate.getTime());
    }

    //
    public int getBetweenDate(String returnDatePattern) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String nowDateStr = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        Date nowDate = simpleDateFormat.parse(nowDateStr);      // 系统时间
        Date thatDate = simpleDateFormat.parse(DateStr);       //  查询时间

        Calendar calendarNow = Calendar.getInstance();        // 系统时间转Calendar类型
        Calendar calendarThat = Calendar.getInstance();       // 查询时间转Calendar类型

        calendarNow.setTime(nowDate);           // 设置系统时间
        calendarThat.setTime(thatDate);         // 设置当前时间

        int seconds = (int) (Math.abs(thatDate.getTime() - nowDate.getTime()) / 1000); // 获得绝对值
        switch (returnDatePattern)
        {
            // 获取秒数
            case RETURN_SECOND: {
                return seconds;
            }
            // 获取分数
            case RETURN_MINUTE:{
                if ( seconds > 60){
                    return (seconds / 60);
                }
                return 0;
            }
            // 获取小时数
            case RETURN_HOUR:{
                if ((seconds / 60) > 60){
                    return seconds / (60 * 60);
                }
                return 0;
            }
            // 获取天数
            case RETURN_DAY:{
                if ((seconds / (60 * 60)) > 24 ){
                    return seconds / (60 * 60 * 24);
                }
                return 0;
            }
            // 获取月数
            case RETURN_MONTH:{
                if (seconds / (60 * 60 * 24) > 30){
                    int year = calendarNow.get(Calendar.YEAR) - calendarThat.get(Calendar.YEAR);
                    Log.i("years", year + " thatTime:"+ DateStr + " nowTime:" + nowDateStr);

                    int month = calendarNow.get(Calendar.MONTH) - calendarThat.get(Calendar.MONTH);
                    int day = calendarNow.get(Calendar.DAY_OF_MONTH) - calendarThat.get(Calendar.DAY_OF_MONTH);
                    if (day < 0) {
                        month -= 1;
                        calendarNow.add(Calendar.MONTH, -1);
                    }
                    if (month < 0){
                        month = (month + 12) % 12;      // 获取月
                        year--;
                    }
                    //Log.i("month", month + " thatTime "+ DateStr + " nowTime" + nowDateStr);
                    return Math.abs(month);
                }

                return 0;
            }
            // 获取年数
            case RETURN_YEAR:{
                int year = calendarNow.get(Calendar.YEAR) - calendarThat.get(Calendar.YEAR);
                int month = calendarNow.get(Calendar.MONTH) - calendarThat.get(Calendar.MONTH);
                int day = calendarNow.get(Calendar.DAY_OF_MONTH) - calendarThat.get(Calendar.DAY_OF_MONTH);
                if (day < 0) {
                    month -= 1;
                    calendarNow.add(Calendar.MONTH, -1);
                }
                if (month < 0){
                    month = (month + 12) % 12;      // 获取月
                    year--;
                }
                //Log.i("year", year+ " thatTime "+ DateStr + " nowTime" + nowDateStr);
                return Math.abs(year);
            }
        }

        return 0;
    }

    public String getCountDownStr() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String nowDateStr = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        Date nowDate = simpleDateFormat.parse(nowDateStr);      // 系统时间
        Date thatDate = simpleDateFormat.parse(DateStr);       //  查询时间

        Calendar calendarNow = Calendar.getInstance();        // 系统时间转Calendar类型
        Calendar calendarThat = Calendar.getInstance();       // 查询时间转Calendar类型

        calendarNow.setTime(nowDate);           // 设置系统时间
        calendarThat.setTime(thatDate);         // 设置当前时间

        int year = calendarNow.get(Calendar.YEAR) - calendarThat.get(Calendar.YEAR);
        int month = calendarNow.get(Calendar.MONTH) - calendarThat.get(Calendar.MONTH);
        int day = calendarNow.get(Calendar.DAY_OF_MONTH) - calendarThat.get(Calendar.DAY_OF_MONTH);
        if (day < 0) {
            month -= 1;
            calendarNow.add(Calendar.MONTH, -1);
            day = day + calendarNow.getActualMaximum(Calendar.DAY_OF_MONTH);//获取日
        }
        if (month < 0){
            month = (month + 12) % 12;      // 获取月
            year--;
        }

        year = Math.abs(year);

        int seconds = (int) (Math.abs(thatDate.getTime() - nowDate.getTime()) / 1000);
        int minute = 0;
        int hour = 0;

        String countDownStr = seconds + "秒";

        if (seconds > 60) {
            minute = seconds / 60;   // 取整
            seconds %= 60;           // 取余
            countDownStr = minute + "分钟" + seconds + "秒";
        }
        if (minute > 60){
            hour = minute / 60;
            minute %= 60;
            countDownStr = hour + "小时" + minute + "分钟" + seconds + "秒";
        }
        if (hour > 24){
            day = day / 24;
            hour %= 24;
            countDownStr = day + "天" + hour + "小时" + minute + "分钟" + seconds + "秒";
        }
        if (month != 0)
            countDownStr = month + "月" + day + "天" + hour + "小时" + minute + "分钟" + seconds + "秒";
        if (year != 0)
            countDownStr = year +"年" + month + "月" + day + "天" + hour + "小时" + minute + "分钟" + seconds + "秒";

        return countDownStr;
    }

}
