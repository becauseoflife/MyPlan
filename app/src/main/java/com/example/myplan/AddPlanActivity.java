package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddPlanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    /* 菜单选项所在的位置 */
    private static final int MENU_SELECT_TIME = 0;      // 设置时间
    private static final int MENU_SELECT_REPEAT = 1;    // 设置重复
    private static final int MENU_SELECT_IMG = 2;       // 设置背景图
    private static final int MENU_SELECT_LABEL = 3;     // 设置标签

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private Toolbar toolbar;
    private ListView menuListView;
    private SimpleAdapter menuAdapter;
    private List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        // 初始化时间
        InitDate();

        // 工具栏的实现
        toolbar = findViewById(R.id.add_plan_toolBar);
        toolbar.inflateMenu(R.menu.add_plan_toolbar_menu);

        // 工具栏返回图标的监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlanActivity.this.finish();
            }
        });

        // 添加页面的选项菜单项
        menuListView = findViewById(R.id.add_plan_menu_listView);

        // listView 适配器
        menuList = getMenuData();
        menuAdapter = new SimpleAdapter(
                AddPlanActivity.this,
                menuList,
                R.layout.add_plan_list_item_menu,
                new String[] {"menu_image", "menu_title", "menu_tip"},
                new int[] {R.id.menu_imageView, R.id.menu_title_textView, R.id.menu_tip_textView});

        menuListView.setAdapter(menuAdapter);

        // listView点击事件
        menuListView.setOnItemClickListener(new MenuItemClick());


    }

    // 初始化时间，设置时间为当前时间
    private void InitDate() {
        Date date = new Date();

        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

    }

    // 设置菜单列表每一列的内容
    private List<Map<String, Object>> getMenuData() {

        // 日期
        Map<String, Object> menuItem = new HashMap<String, Object>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_date);
        menuItem.put("menu_title", "时间");
        menuItem.put("menu_tip", "长按使用日期计算器");
        menuList.add(menuItem);

        // 设置重复
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_repeat);
        menuItem.put("menu_title", "重复");
        menuItem.put("menu_tip", "无");
        menuList.add(menuItem);

        // 图片
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_img);
        menuItem.put("menu_title", "图片");
        menuItem.put("menu_tip", "选取背景图片");
        menuList.add(menuItem);

        // 添加标签
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_label);
        menuItem.put("menu_title", "添加标签");
        menuItem.put("menu_tip", "");
        menuList.add(menuItem);

        return menuList;
    }

    // 获得用户选择的日期的函数
    @Override
    public void onDateSet(DatePickerDialog view, int selectYear, int selectMonthOfYear, int selectDayOfMonth) {
        /* 设置选择后的日期，并在菜单下方显示选择的日期 */
        mYear = selectYear;
        mMonth = selectMonthOfYear;
        mDay = selectDayOfMonth;
        Map<String, Object> newDateMap = menuList.get(MENU_SELECT_TIME);
        String dateStr = mYear + "年" + mMonth + "月" + mDay + "日";
        newDateMap.put("menu_tip", dateStr);

        /* 更新菜单，显示用户选择的日期 */
        menuAdapter.notifyDataSetChanged();
        /* 创建用户选择时间界面 */
        showDialogForSelectTime();
    }

    // 获得用户选择的时间的函数
    @Override
    public void onTimeSet(TimePickerDialog view, int selectHourOfDay, int selectMinute, int selectSecond) {
        /* 设置选择后的时间 */
        mHour = selectHourOfDay;
        mMinute = selectMinute;
        Map<String, Object> newDateMap = menuList.get(MENU_SELECT_TIME);
        String dateStr = mYear + "年" + mMonth + "月" + mDay + "日" + " " + mHour + ":" + mMinute;
        newDateMap.put("menu_tip", dateStr);

        /* 更新菜单，显示用户选择的日期和时间 */
        menuAdapter.notifyDataSetChanged();
    }

    // menu列表菜单点击事件
    private class MenuItemClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            switch (position)
            {
                // 选择时间
                case MENU_SELECT_TIME: {
                    showDialogForSelectDate();  // 点击出现选择日期界面
                    Toast.makeText(AddPlanActivity.this, "选择时间", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 设置重复
                case MENU_SELECT_REPEAT:{
                    Toast.makeText(AddPlanActivity.this, "选择重复", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 选择背景图片
                case MENU_SELECT_IMG:{
                    Toast.makeText(AddPlanActivity.this, "选择图片", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 选择标签
                case MENU_SELECT_LABEL:{
                    Toast.makeText(AddPlanActivity.this, "选择标签", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    //  显示日期选择界面
     private void showDialogForSelectDate()
     {
         /*                                                                                   *
         * 时间日期选择器                                                                      *
         * https://github.com/wdullaer/MaterialDateTimePicker#using-material-datetime-pickers *
         **/
         DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddPlanActivity.this, mYear, mMonth, mDay);
         datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
         datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
         datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
     }

    // 选择时间选择界面
    private void showDialogForSelectTime()
    {
        /*                                                                                    *
         * 时间日期选择器                                                                      *
         * https://github.com/wdullaer/MaterialDateTimePicker#using-material-datetime-pickers *
         **/
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddPlanActivity.this, mHour, mMinute, true);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }
}
