package com.example.myplan;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myplan.data.GetPlanDateBetweenNow;
import com.example.myplan.data.model.Plan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPlanFragment extends Fragment {

    private static final int REQUEST_CODE_EDITOR_PLAN = 902;
    private int planPosition;
    private Plan plan;
    public ViewPlanFragment(Plan plan, int position) {
        // Required empty public constructor
        this.plan = plan;
        this.planPosition = position;
    }

    @SuppressLint("DefaultLocale")
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

        GetPlanDateBetweenNow getDate = new GetPlanDateBetweenNow(plan);

        // 显示日期时间星期
        dateTextView.setText(getDate.getDateStr());

        // 获得毫秒数
        long seconds = 0;
        try {
            seconds = getDate.CalculationMillisecondWhitNow();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 倒计时
        CountDownTimer timer = new CountDownTimer(seconds, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTick(long countDownTimeMs) {

                ArrayList<String> countDownTime = getDate.getCountDownDateArrayList(countDownTimeMs);

                StringBuilder countDownStr = new StringBuilder();
                for (int i=0; i<countDownTime.size(); i++)
                    countDownStr.append(countDownTime.get(i));

                countDownTextView.setText(countDownStr.toString());
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editorIntent = new Intent(getContext(), EditorActivity.class);
                editorIntent.putExtra("editor_plan", plan);
                editorIntent.putExtra("editor_plan_position", planPosition);
                Objects.requireNonNull(getActivity()).startActivityForResult(editorIntent, REQUEST_CODE_EDITOR_PLAN);
            }
        });

        return view;
    }

}
