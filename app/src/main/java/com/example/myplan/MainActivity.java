package com.example.myplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myplan.data.model.Plan;
import com.example.myplan.data.PlanFragmentPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

public class MainActivity extends AppCompatActivity {
    private ViewPager homeViewPager;        // 主页轮播图控件
    private LinearLayout homeViewPoints;    // 主页导航小圆点控件
    private ListView homeListView;          // 主页Plan列表控件
    private FloatingActionButton homeFABtn; // 主页悬浮按钮控件
    private ArrayList<Plan> myPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        // 显示倒计时轮播图
        PlanFragmentPagerAdapter myPagerAdapter = new PlanFragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT);
        myPagerAdapter.setPlanArrayList(myPlan);

        homeViewPager = findViewById(R.id.home_viewPager);
        homeViewPager.setAdapter(myPagerAdapter);

        // 轮播图的小圆点
        homeViewPoints = findViewById(R.id.home_select_points);
        setPoints();
        // 页面改变时改变导航小圆点的监听事件
        homeViewPager.addOnPageChangeListener(new myOnPageChangeListener());

        // 显示倒计时列表菜单
        PlansArrayAdapter theAdapter = new PlansArrayAdapter(this, R.layout.list_item_plan, myPlan);
        homeListView = findViewById(R.id.home_listView);
        homeListView.setAdapter(theAdapter);

        // 点击新建的悬浮按钮
        homeFABtn = findViewById(R.id.home_add_btn);
        homeFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPlanActivity.class);
                startActivity(intent);
            }
        });

    }

    // 创建滑动界面的小圆点数
    private void setPoints() {
        for (int i = 0; i < myPlan.size(); i++)
        {
            View view = new View(MainActivity.this);
            view.setBackgroundResource(R.drawable.point_selector);
            if (i == 0)
                view.setEnabled(true);      // 第一个默认选中
            else
                view.setEnabled(false);

            // 设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            // 设置间隔
            if (i != 0)
                layoutParams.leftMargin = 15;

            // 添加到LinearLayout中
            homeViewPoints.addView(view, layoutParams);
        }
    }

    // 初始化数据
    private void initData() {
        myPlan = new ArrayList<>();
        myPlan.add(new Plan("标题1","备注1",R.drawable.test1,1,1,1998,11,20,9,50,15));
        myPlan.add(new Plan("标题2","备注2",R.drawable.test2,1,1,2000,11,20,9,50,15));
        myPlan.add(new Plan("标题3","备注3",R.drawable.test3,1,1,2002,11,20,9,50,15));
        myPlan.add(new Plan("标题4","备注4",R.drawable.test4,1,1,2002,11,20,9,50,15));
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
            ImageView bgImg = item.findViewById(R.id.list_item_imageView);          // 背景图
            TextView tip = item.findViewById(R.id.list_item_tip_textView);          // 显示剩余或者过去
            TextView time = item.findViewById(R.id.list_item_time_textView);        // 剩余或者过去的天数
            TextView title = item.findViewById(R.id.list_item_title_textView);      // 标题
            TextView date = item.findViewById(R.id.list_item_date_textView);        // 日期
            TextView remarks = item.findViewById(R.id.list_item_remarks_textView);  // 备注

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

    // 监听滑动
    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        private int lastPosition = 0;
        @Override
        public void onPageSelected(int position) {
            homeViewPoints.getChildAt(lastPosition).setEnabled(false);
            homeViewPoints.getChildAt(position).setEnabled(true);
            lastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
