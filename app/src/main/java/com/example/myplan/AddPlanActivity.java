package com.example.myplan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.myplan.data.model.Plan;
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

    /* 初始化用户除标题外，其他需要的数据 */
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private int cycleTime;
    private int img;
    private ArrayList<String> label;

    private Toolbar toolbar;
    private ListView menuListView;
    private EditText titleExitText;
    private EditText remarksExitText;
    private SimpleAdapter menuAdapter;
    private List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        toolbar = findViewById(R.id.add_plan_toolBar);
        titleExitText = findViewById(R.id.add_plan_title_editText);
        remarksExitText = findViewById(R.id.add_plan_remarks_editText);
        menuListView = findViewById(R.id.add_plan_menu_listView);
        // 初始化用户数据
        InitDate();

        // 工具栏的实现
        toolbar.inflateMenu(R.menu.add_plan_toolbar_menu);

        // 工具栏返回图标的监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlanActivity.this.finish();
            }
        });
        // 点击成功监听事件
        toolbar.setOnMenuItemClickListener(new MyMenuClickListener());

        // 添加页面的选项菜单项
        // listView 适配器
        menuList = getMenuData();
        menuAdapter = new SimpleAdapter(
                AddPlanActivity.this,
                menuList,
                R.layout.add_plan_list_item_menu,
                new String[] {"menu_image", "menu_title", "menu_tip"},
                new int[] {R.id.menu_imageView, R.id.menu_title_textView, R.id.menu_tip_textView});

        menuListView.setAdapter(menuAdapter);

        // listView菜单点击事件
        menuListView.setOnItemClickListener(new MenuItemClick());

    }

    // 点击确认按钮，生成新的Plan
    private class MyMenuClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (titleExitText.getText().toString().trim().isEmpty())
            {
                Toast.makeText(AddPlanActivity.this, R.string.tipInputText, Toast.LENGTH_SHORT).show();
                return true;
            }
            String title = titleExitText.getText().toString();
            String remarks = remarksExitText.getText().toString();
            Plan newPlan = new Plan();
            newPlan.setTitle(title);
            newPlan.setRemarks(remarks);
            newPlan.setBackgroundImg(img);
            newPlan.setCycleTime(cycleTime);
            newPlan.setLabel(label);

            newPlan.setYear(mYear);
            newPlan.setMonth(mMonth);
            newPlan.setDay(mDay);
            newPlan.setHour(mHour);
            newPlan.setMinute(mMinute);
            newPlan.setSecond(mSecond);

            // 数据传回主界面
            Intent intent = new Intent(AddPlanActivity.this, MainActivity.class);
            intent.putExtra("newPlan", newPlan);
            setResult(RESULT_OK, intent);

            // 销毁界面
            AddPlanActivity.this.finish();
            return true;
        }
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
        mSecond = calendar.get(Calendar.SECOND);

        cycleTime = 0;
        img = R.drawable.test3;
        label = new ArrayList<>();
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

        /* 创建用户选择时间界面 */
        showDialogForSelectTime();

        /* 设置选择后的日期，并在菜单下方显示选择的日期 */
        mYear = selectYear;
        mMonth = selectMonthOfYear;
        mDay = selectDayOfMonth;
        Map<String, Object> newDateMap = menuList.get(MENU_SELECT_TIME);
        String dateStr = mYear + "年" + mMonth + "月" + mDay + "日";
        newDateMap.put("menu_tip", dateStr);

        /* 更新菜单，显示用户选择的日期 */
        menuAdapter.notifyDataSetChanged();
    }

    // 获得用户选择的时间的函数
    @Override
    public void onTimeSet(TimePickerDialog view, int selectHourOfDay, int selectMinute, int selectSecond) {
        /* 设置选择后的时间 */
        mHour = selectHourOfDay;
        mMinute = selectMinute;
        mSecond = selectSecond;
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
                    // Toast.makeText(AddPlanActivity.this, "选择时间", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 设置重复
                case MENU_SELECT_REPEAT:{
                    showDialogForSelectRepeat();
                    //Toast.makeText(AddPlanActivity.this, "选择重复", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 选择背景图片
                case MENU_SELECT_IMG:{
                    Toast.makeText(AddPlanActivity.this, "选择图片", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 选择标签
                case MENU_SELECT_LABEL:{
                    showDialogSelectLabel();
                    //Toast.makeText(AddPlanActivity.this, "选择标签", Toast.LENGTH_SHORT).show();
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
         datePickerDialog.setOkText("确定");
         datePickerDialog.setCancelText("取消");
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
        timePickerDialog.setOkText("确定");
        timePickerDialog.setCancelText("取消");
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    // 选择重复时间
    private void showDialogForSelectRepeat() {
        final int EVERY_WEEK = 0;
        final int EVERY_MONTH = 1;
        final int EVERY_YEAR = 2;
        final int CUSTOMIZE = 3;
        final int NOT = 4;

        String[] items;
        // 如果有自义定重复设置，则添加 “无” 菜单选项
        if (cycleTime == 0){
            items = new String[]{"每周", "每月", "每年", "自定义"};
        }else{
            items = new String[]{"每周", "每月", "每年", "自定义", "无"};
        }
        AlertDialog.Builder listDialog = new AlertDialog.Builder(AddPlanActivity.this);
        listDialog.setTitle("周期");
        listDialog.setItems(items, (dialogInterface, position) -> {
            // Toast.makeText(AddPlanActivity.this, "你点击了" + items[position],Toast.LENGTH_SHORT).show();
            // 提示栏显示设置
            Map<String, Object> newRepeatMap = menuList.get(MENU_SELECT_REPEAT);
            newRepeatMap.put("menu_tip", items[position]);
            menuAdapter.notifyDataSetChanged();

            switch (position)
            {
                case EVERY_WEEK:{
                    cycleTime = 7;
                    break;
                }
                case EVERY_MONTH:{
                    cycleTime = 30;
                    break;
                }
                case EVERY_YEAR:{
                    cycleTime = 365;
                    break;
                }
                case NOT:{
                    cycleTime = 0;
                    break;
                }
                case CUSTOMIZE:{
                    // 创建layout对象
                    LinearLayout labelLinearLayout = new LinearLayout(AddPlanActivity.this);
                    LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    labelLinearLayout.setLayoutParams(labelParams);
                    // 创建EditText对象
                    EditText editText = new EditText(AddPlanActivity.this);
                    editText.setHint("输入周期(天)");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    // 为EditText建立布局样式
                    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    editTextParams.leftMargin = 80;
                    editTextParams.rightMargin = 80;
                    // 在父布局中添加他，及其布局样式
                    labelLinearLayout.addView(editText, editTextParams);

                    AlertDialog.Builder inputDialog = new AlertDialog.Builder(AddPlanActivity.this);
                    inputDialog.setTitle("周期").setView(labelLinearLayout);

                    inputDialog.setNegativeButton("取消", (dialogInterface1, i) -> dialogInterface1.dismiss());

                    inputDialog.setPositiveButton("确定", (dialogInterface12, i) -> {
                        cycleTime = Integer.valueOf(editText.getText().toString());
                        // 提示栏显示设置
                        if (cycleTime != 0) {
                            newRepeatMap.put("menu_tip", cycleTime + "天");
                            menuAdapter.notifyDataSetChanged();
                        }else{
                            newRepeatMap.put("menu_tip","无");
                            menuAdapter.notifyDataSetChanged();
                        }
                        dialogInterface12.dismiss();
                    });
                    inputDialog.show();
                    break;
                }
            }
        });
        listDialog.show();

    }

    // 显示选择标签界面
    private void showDialogSelectLabel() {
/*        ArrayList<String> labelList = new ArrayList<>();
        labelList.add("生日");
        labelList.add("纪念日");
        labelList.add("工作");
        labelList.add("考试");
        ArrayList<Boolean> initChoiceItems = new ArrayList<>();
        for (int i=0; i<labelList.size(); i++)
            initChoiceItems.add(false);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxParams.topMargin = 80;
        checkBoxParams.leftMargin = 80;
        CheckBox checkBox = new CheckBox(AddPlanActivity.this);

        checkBox.setPadding(20,20,20,20);
        checkBox.setBackground(getResources().getDrawable(R.drawable.add_plan_label_background));*/

        ArrayList<String> checkLabel = new ArrayList<>();
        String[] items = {"生日", "纪念日", "工作", "考试"};
        boolean[] initChoiceItems = {false, false, false, false};
        // 当再次点击时，还原上次设置 （先用着比较愚蠢的办法（猝死...））
        for (int i=0; i<label.size(); i++)
        {
            for (int j=0; j<items.length; j++)
                if (label.get(i).equals(items[j]))
                {
                    initChoiceItems[j] = true;
                    checkLabel.add(items[j]);
                }
        }

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(AddPlanActivity.this);
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceItems, (DialogInterface.OnMultiChoiceClickListener) (dialogInterface, i, isCheck) -> {
            if (isCheck){
                checkLabel.add(items[i]);
            }else{
                checkLabel.remove(items[i]);
            }
        });

        multiChoiceDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        multiChoiceDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            // 更新菜单显示
            Map<String, Object> newRepeatMap = menuList.get(MENU_SELECT_LABEL);
            String labelStr = "";
            for (int index=0; index<checkLabel.size(); index++){
                labelStr += checkLabel.get(index) + " ";
            }
            newRepeatMap.put("menu_tip", labelStr);
            menuAdapter.notifyDataSetChanged();

            label = checkLabel;
        });

        // 添加新标签按钮
        multiChoiceDialog.setNeutralButton("添加新标签", (dialogInterface, i) -> {
            // 创建layout对象
            LinearLayout addLabelLinearLayout = new LinearLayout(AddPlanActivity.this);
            LinearLayout.LayoutParams addLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            addLabelLinearLayout.setLayoutParams(addLabelParams);
            // 创建EditText对象
            EditText editText = new EditText(AddPlanActivity.this);
            editText.setHint("10个字符以内");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            editTextParams.leftMargin = 80;
            editTextParams.rightMargin = 80;
            // 在父布局中添加他，及其布局样式
            addLabelLinearLayout.addView(editText, editTextParams);

            AlertDialog.Builder inputLabelDialog = new AlertDialog.Builder(AddPlanActivity.this);
            inputLabelDialog.setTitle("添加标签").setView(addLabelLinearLayout);
            inputLabelDialog.setNegativeButton("取消", (labelDialogInterface, position) -> {
               labelDialogInterface.dismiss();
            });
            inputLabelDialog.setPositiveButton("确定", (labelDialogInterface, position) -> {

            });
            inputLabelDialog.show();
        });

        multiChoiceDialog.show();
    }

}
