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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplan.data.GetPlanDateBetweenNow;
import com.example.myplan.data.model.Plan;
import com.example.myplan.data.PlanFragmentPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_NEW_PLAN = 901;
    public static final int REQUEST_CODE_EDITOR_PLAN = 902;

    private ViewPager homePlanViewPager;        // 主页轮播图控件
    private LinearLayout homeViewPoints;    // 主页导航小圆点控件
    private ListView homePlanListView;          // 主页Plan列表控件
    private FloatingActionButton homeFABtn; // 主页悬浮按钮控件
    private ArrayList<Plan> myPlan;
    private PlansArrayAdapter thePlansListAdapter;
    private PlanFragmentPagerAdapter thePlansPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        // 显示倒计时轮播图
        thePlansPagerAdapter = new PlanFragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT);
        thePlansPagerAdapter.setPlanArrayList(myPlan);

        homePlanViewPager = findViewById(R.id.home_viewPager);
        homePlanViewPager.setAdapter(thePlansPagerAdapter);

        // 轮播图的小圆点
        homeViewPoints = findViewById(R.id.home_select_points);
        setPoints();
        // 页面改变时改变导航小圆点的监听事件
       homePlanViewPager.addOnPageChangeListener(new myOnPageChangeListener());

        // 显示倒计时列表菜单
        thePlansListAdapter = new PlansArrayAdapter(this, R.layout.list_item_plan, myPlan);
        homePlanListView = findViewById(R.id.home_listView);
        homePlanListView.setAdapter(thePlansListAdapter);
        homePlanListView.setOnItemClickListener(new PlanItemClickListener());

        // 点击新建的悬浮按钮
        homeFABtn = findViewById(R.id.home_add_btn);
        homeFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewPlanIntent = new Intent(MainActivity.this, AddPlanActivity.class);
                NewPlanIntent.putExtra("create_new_plan",true);
                startActivityForResult(NewPlanIntent, REQUEST_CODE_ADD_NEW_PLAN);
            }
        });

    }

    // 获得传回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_ADD_NEW_PLAN:
                if (resultCode == RESULT_OK)
                {
                    Plan newPlan = (Plan) data.getSerializableExtra("newPlan");
                    // 更新列表
                    myPlan.add(newPlan);
                    thePlansListAdapter.notifyDataSetChanged();
                    // 更新轮播列表
                    thePlansPagerAdapter.setPlanArrayList(myPlan);
                    thePlansPagerAdapter.notifyDataSetChanged();
                    // 添加一个导航小圆点
                    addPoint();

                    Toast.makeText(MainActivity.this, "新建成功", Toast.LENGTH_SHORT).show();
                }
            break;
        }
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

    private void addPoint()
    {
        View view = new View(MainActivity.this);
        view.setBackgroundResource(R.drawable.point_selector);
        view.setEnabled(false);
        // 设置宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        // 设置间隔
        layoutParams.leftMargin = 15;
        // 添加到LinearLayout中
        homeViewPoints.addView(view, layoutParams);
    }

    // 初始化数据
    private void initData() {
        myPlan = new ArrayList<>();
        ArrayList<String> label = new ArrayList<>();
        myPlan.add(new Plan("标题1","备注1",R.drawable.test1,label,"",1998,11,20,9,50,15, "周四"));
        myPlan.add(new Plan("标题2","备注2",R.drawable.test4,label,"无",2019,11,15,9,50,15,"周五"));
        myPlan.add(new Plan("标题3","备注3",R.drawable.test3,label,"每周",2019,12,25,0,00,15,"周六"));
        myPlan.add(new Plan("标题4","",R.drawable.test2,label,"每年",2021,11,20,9,50,15,"周日"));
    }

    // 主页显示item适配器
    private class PlansArrayAdapter extends ArrayAdapter<Plan> {
        private int resourceId;

        public PlansArrayAdapter(@NonNull Context context, int resource, @NonNull List<Plan> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
            @SuppressLint("ViewHolder") View item = layoutInflater.inflate(resourceId, null);

            ImageView bgImg = item.findViewById(R.id.list_item_imageView);          // 背景图
            TextView tip = item.findViewById(R.id.list_item_tip_textView);          // 显示剩余或者过去
            TextView time = item.findViewById(R.id.list_item_time_textView);        // 剩余或者过去的天数
            TextView title = item.findViewById(R.id.list_item_title_textView);      // 标题
            TextView date = item.findViewById(R.id.list_item_date_textView);        // 日期
            TextView remarks = item.findViewById(R.id.list_item_remarks_textView);  // 备注

            Plan plan_item = this.getItem(position);    // 获得一个plan

            if (plan_item != null) {
                String tipStr="";
                long tipDay = 0;
                String planDateStr = plan_item.getYear() + "-" + plan_item.getMonth()+ "-" + plan_item.getDay();

                GetPlanDateBetweenNow getDays = new GetPlanDateBetweenNow(planDateStr, "yyyy-MM-dd");
                try {
                    tipDay = getDays.CalculationMillisecond(); // 获得相隔秒数
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 比较大小
                if (tipDay < 0){
                    tipStr = "已经";
                    tipDay = Math.abs(tipDay);
                }
                else if (tipDay > 0){
                    if ((tipDay / 24*60*60*1000) < 30)
                        tipStr = "只剩";
                    else
                        tipStr = "还有";
                }
                else {
                    tipStr = "今天";
                }
                // 控件上显示的信息
                tip.setText(tipStr);
                if (tipDay != 0){
                    tipDay /= 24*60*60*1000;
                    int day = (int) (tipDay-1);
                    time.setText(day + "天");
                }
                else
                    time.setVisibility(View.GONE);

                bgImg.setImageResource(plan_item.getBackgroundImg());

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

    // Plan设置监听事件
    private class PlanItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            // 向编辑界面传输数据
            Intent editorIntent = new Intent(MainActivity.this, EditorActivity.class);
            editorIntent.putExtra("editor_plan", myPlan.get(position));
            startActivityForResult(editorIntent, REQUEST_CODE_EDITOR_PLAN);
        }
    }

}
