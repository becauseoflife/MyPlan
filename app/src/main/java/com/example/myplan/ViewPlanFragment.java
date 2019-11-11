package com.example.myplan;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myplan.data.model.Plan;


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

        ImageView imageView = view.findViewById(R.id.view_item_image_view);     // 背景图
        TextView title = view.findViewById(R.id.view_item_title_text_view);     // 背景标题
        TextView date = view.findViewById(R.id.view_item_date_text_view);       // 背景日期
        TextView time = view.findViewById(R.id.view_item_count_down_text_view); // 背景倒计时

        imageView.setImageResource(plan.getBackgroundImg());
        title.setText(plan.getTitle());
        String dateStr = plan.getYear() + "年" + plan.getMonth() + "月" + plan.getDay() + "日";
        date.setText(dateStr);
        String CountDown = plan.getDay() + "天" + plan.getHour() + "小时" + plan.getMinute() + "分钟" + plan.getSecond() + "秒";
        time.setText(CountDown);

        return view;
    }

}
