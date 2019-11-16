package com.example.myplan;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myplan.data.GetPlanDateBetweenNow;
import com.example.myplan.data.model.Plan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.login.LoginException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPlanFragment extends Fragment {

    private Plan plan;
    public ViewPlanFragment(Plan plan) {
        // Required empty public constructor
        this.plan = plan;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_plan, container, false);

        ImageView imageView = view.findViewById(R.id.view_item_imageView);     // 背景图
        TextView title = view.findViewById(R.id.view_item_title_textView);     // 背景标题
        TextView dateTextView = view.findViewById(R.id.view_item_date_textView);       // 背景日期
        TextView countDownTextView = view.findViewById(R.id.view_item_count_down_textView); // 背景倒计时

        imageView.setImageResource(plan.getBackgroundImg());
        title.setText(plan.getTitle());

        String dateStr = plan.getYear() + "年" + plan.getMonth() + "月" + plan.getDay() + "日";
        // 如果设置了时间则显示时间
        if (plan.getHour() != 0)
            dateStr += " " + plan.getHour() + ":" + plan.getMinute();
        // 显示星期
        dateStr += " " + plan.getWeek();
        // 显示日期时间星期
        dateTextView.setText(dateStr);

        // 获得毫秒数
        String planPattern = plan.getYear() + "-" + plan.getMonth() + "-" + plan.getDay() + " " + plan.getHour() + ":" + plan.getMinute();
        long seconds = 0;
        GetPlanDateBetweenNow getDateSeconds = new GetPlanDateBetweenNow(planPattern, "yyyy-MM-dd HH:mm");
        try {
            seconds = getDateSeconds.CalculationMillisecond();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 倒计时
        CountDownTimer timer = new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long l) {
                if (l < 0)
                    return;
                int cMonth = 0;
                int cYear = 0;
                int cDay = 0;
                int cHour = 0;
                int cMinute = 0;
                int cSecond = (int) (l/1000);
                String countDownStr = cSecond + "秒";
                if (cSecond > 60) {
                    cMinute = cSecond / 60;   // 取整
                    cSecond = cSecond % 60;   // 取余
                    countDownStr = cMinute + "分钟" + cSecond + "秒";
                }
                if (cMinute > 60){
                    cHour = cMinute / 60;
                    cMinute = cMinute % 60;
                    countDownStr = cHour + "小时" + cMinute + "分钟" + cSecond + "秒";
                }
                if (cHour > 24){
                    cDay = cHour / 24;
                    cHour = cHour % 24;
                    countDownStr = cDay + "天" + cHour + "小时" + cMinute + "分钟" + cSecond + "秒";
                }
                Calendar nowCalendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date planDate = null;
                try {
                    planDate = simpleDateFormat.parse(planPattern);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar planCalendar = Calendar.getInstance();
                planCalendar.setTime(planDate);

                cYear = planCalendar.get(Calendar.YEAR) - nowCalendar.get(Calendar.YEAR);
                cMonth = planCalendar.get(Calendar.MONTH) - nowCalendar.get(Calendar.MONTH);
                cDay = planCalendar.get(Calendar.DAY_OF_MONTH) - nowCalendar.get(Calendar.DAY_OF_MONTH);

                if (cDay < 0){
                    cMonth -= 1;
                    nowCalendar.add(Calendar.MONTH, -1);
                    cDay = cDay + nowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获取日
                }
                if (cMonth < 0){
                    cMonth = (cMonth + 12) % 12;
                    cYear -= 1;
                }
                if (cMonth > 0) {
                    countDownStr = cMonth + "月" + cDay + "天" + cHour + "小时" + cMinute + "分钟" + cSecond + "秒";
                    if (cYear > 0)
                        countDownStr = cYear + "年" + cMonth + "月" + cDay + "天" + cHour + "小时" + cMinute + "分钟" + cSecond + "秒";
                }
                if (cMonth == 0 && cYear > 0)
                {
                    countDownStr = cYear + "年" + cDay + "天" + cHour + "小时" + cMinute + "分钟" + cSecond + "秒";
                }

                countDownTextView.setText(countDownStr);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();



        return view;
    }

}
