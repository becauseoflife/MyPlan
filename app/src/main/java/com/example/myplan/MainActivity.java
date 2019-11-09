package com.example.myplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myplan.data.Plan;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager homeViewPager;
    private ListView homeListView;
    private ArrayList<Plan> myPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        // 显示倒计时轮播图

        homeViewPager = findViewById(R.id.home_view_pager);


        // 显示倒计时列表菜单
        PlansArrayAdapter theAdapter = new PlansArrayAdapter(this, R.layout.list_item_plan, myPlan);
        homeListView = findViewById(R.id.home_list_view);
        homeListView.setAdapter(theAdapter);

        // 点击新建按钮


    }
    // 初始化数据
    private void initData() {
        myPlan = new ArrayList<>();
        myPlan.add(new Plan("标题1","备注1",R.drawable.test4,1,1,1998,11,20,9,50,15));
        myPlan.add(new Plan("标题2","备注2",R.drawable.test2,1,1,2000,11,20,9,50,15));
        myPlan.add(new Plan("标题3","备注3",R.drawable.test3,1,1,2002,11,20,9,50,15));
    }
    // 主页显示item适配器
    private class PlansArrayAdapter extends ArrayAdapter<Plan> {
        private int resourceId;

        public PlansArrayAdapter(@NonNull Context context, int resource, @NonNull List<Plan> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
            @SuppressLint("ViewHolder") View item = layoutInflater.inflate(resourceId, null);

            RelativeLayout relativeLayout = item.findViewById(R.id.list_item_layout);
            ImageView bgImg = item.findViewById(R.id.list_item_image_view);          // 背景图
            TextView tip = item.findViewById(R.id.list_item_tip_text_view);          // 显示剩余或者过去
            TextView time = item.findViewById(R.id.list_item_time_text_view);        // 剩余或者过去的天数
            TextView title = item.findViewById(R.id.list_item_title_text_view);      // 标题
            TextView date = item.findViewById(R.id.list_item_date_text_view);        // 日期
            TextView remarks = item.findViewById(R.id.list_item_remarks_text_view);  // 备注

            Plan plan_item = this.getItem(position);    // 获得一个plan

            if (plan_item != null) {
                //relativeLayout.setBackgroundResource(plan_item.getBackgroundImg());
                bgImg.setImageResource(plan_item.getBackgroundImg());
                tip.setText("只剩");
                time.setText("100天");

                title.setText(plan_item.getTitle());
                String dateStr = plan_item.getYear() + "年" + plan_item.getMonth() + "月" + plan_item.getDay() + "日";
                date.setText(dateStr);
                remarks.setText(plan_item.getRemarks());

            }

            return item;
        }


    }
}
