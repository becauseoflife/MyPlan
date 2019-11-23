package com.example.myplan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myplan.data.model.Plan;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private Plan plan;

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

        // 初始化Plan数据
        InitPlan();

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

    // 初始化Plan数据
    private void InitPlan() {
        plan = (Plan) getIntent().getSerializableExtra("editor_plan");
        if (plan != null)
        {
            titleExitText.setText(plan.getTitle());
            remarksExitText.setText(plan.getRemarks());
        } else {
            // 新建plan的初始化
            plan = new Plan();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            /* 年月日默认为添加的时间 */
            plan.setYear(calendar.get(Calendar.YEAR));
            plan.setMonth(calendar.get(Calendar.MONTH)+1);
            plan.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            /* 时分秒默认为0 */
            plan.setHour(-1);
            plan.setMinute(-1);
            plan.setSecond(0);
            /* 星期 */
            plan.setWeek(simpleDateFormat.format(date));

            /* 其他的属性默认值 */
            plan.setCycleTime("");
            plan.setBackgroundImg(R.drawable.test3);
            plan.setLabel(new ArrayList<>());
        }
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
            // 设置标题和备注
            plan.setTitle(titleExitText.getText().toString());
            plan.setRemarks(remarksExitText.getText().toString());

            // 数据传回主界面
            Intent intent = new Intent(AddPlanActivity.this, MainActivity.class);
            intent.putExtra("newPlan", plan);
            setResult(RESULT_OK, intent);

            // 销毁界面
            AddPlanActivity.this.finish();
            return true;
        }
    }

    // 设置菜单列表每一列的内容
    @SuppressLint("DefaultLocale")
    private List<Map<String, Object>> getMenuData() {
        // 判断是否为新建plan
        boolean createNewPlan = getIntent().getBooleanExtra("create_new_plan",false);

        String menuDateTip = "长按使用日期计算器";
        String menuRepeatTip = "无";
        StringBuilder menuLabelTip = new StringBuilder();
        // 如果是编辑界面设置菜单列表内容
        if (!createNewPlan) {
            menuDateTip = plan.getYear() + "年" + plan.getMonth() + "月" + plan.getDay() + "日";
            if (plan.getHour() >= 0)
                menuDateTip += " " + String.format("%02d",plan.getHour()) + ":" + String.format("%02d",plan.getMinute());
            if (!plan.getCycleTime().equals(""))
                menuRepeatTip = plan.getCycleTime();
            for (int i=0; i <plan.getLabel().size(); i++){
                menuLabelTip.append(plan.getLabel().get(i)).append(" ");
            }
        }

        // 日期
        Map<String, Object> menuItem = new HashMap<String, Object>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_date);
        menuItem.put("menu_title", "时间");
        menuItem.put("menu_tip",menuDateTip);
        menuList.add(menuItem);

        // 设置重复
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_repeat);
        menuItem.put("menu_title", "重复");
        menuItem.put("menu_tip", menuRepeatTip);
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
        menuItem.put("menu_tip", menuLabelTip);
        menuList.add(menuItem);

        return menuList;
    }

    // 获得用户选择的日期的函数
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onDateSet(DatePickerDialog view, int selectYear, int selectMonthOfYear, int selectDayOfMonth) {
        /* 创建用户选择时间界面 */
        showDialogForSelectTime();

        /* 设置选择后的日期，并在菜单下方显示选择的日期 */
        plan.setYear(selectYear);
        plan.setMonth(selectMonthOfYear + 1);
        plan.setDay(selectDayOfMonth);
        Map<String, Object> newDateMap = menuList.get(MENU_SELECT_TIME);
        String dateStr = selectYear + "年" + (selectMonthOfYear+1) + "月" + selectDayOfMonth + "日";
        newDateMap.put("menu_tip", dateStr);

        /* 更新菜单，显示用户选择的日期 */
        menuAdapter.notifyDataSetChanged();

        /* 获得星期 */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date weekDate = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat("E");
            plan.setWeek(simpleDateFormat.format(weekDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // 获得用户选择的时间的函数
    @Override
    public void onTimeSet(TimePickerDialog view, int selectHourOfDay, int selectMinute, int selectSecond) {
        /* 设置选择后的时间 */
        plan.setHour(selectHourOfDay);
        plan.setMinute(selectMinute);
        // 更新菜单标题下的提示文字
        Map<String, Object> newDateMap = menuList.get(MENU_SELECT_TIME);
        @SuppressLint("DefaultLocale") String dateStr =
                plan.getYear() + "年" + plan.getMonth() + "月" + plan.getDay() + "日 " +
                String.format("%02d", plan.getHour()) + ":" + String.format("%02d", plan.getMinute());
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
         DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddPlanActivity.this, plan.getYear(), plan.getMonth()-1, plan.getDay());
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
        int hour = 0;
        int minute = 0;
        if (plan.getHour() >= 0) {
            hour = plan.getHour();
            minute = plan.getMinute();
        }
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddPlanActivity.this, hour, minute, true);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        timePickerDialog.setOkText("确定");
        timePickerDialog.setCancelText("取消");
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    // 选择重复时间
    private void showDialogForSelectRepeat() {

        String[] items;
        // 如果有自义定重复设置，则添加 “无” 菜单选项
        if (plan.getCycleTime().equals("")){
            items = new String[]{"每周", "每月", "每年", "自定义"};
        }else{
            items = new String[]{"每周", "每月", "每年", "自定义", "无"};
        }
        AlertDialog.Builder listDialog = new AlertDialog.Builder(AddPlanActivity.this);
        listDialog.setTitle("周期");
        listDialog.setItems(items, (dialogInterface, position) -> {
            // Toast.makeText(AddPlanActivity.this, "你点击了" + items[position],Toast.LENGTH_SHORT).show();
            // 设置循环天数
            if (!items[position].equals("自定义")) {
                // 提示栏显示设置
                Map<String, Object> newRepeatMap = menuList.get(MENU_SELECT_REPEAT);
                newRepeatMap.put("menu_tip", items[position]);
                menuAdapter.notifyDataSetChanged();

                plan.setCycleTime(items[position]);
            }
            // 自义定循环天数
            if (items[position].equals("自定义")) {
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
                // 取消输入响应事件
                inputDialog.setNegativeButton("取消", (dialogInterface1, i) -> dialogInterface1.dismiss());
                // 确定输入响应事件
                inputDialog.setPositiveButton("确定", (dialogInterface12, i) -> {
                    Map<String, Object> newRepeatMap = menuList.get(MENU_SELECT_REPEAT);
                    // 提示栏显示设置
                    if (!editText.getText().toString().equals("")) {
                        plan.setCycleTime(editText.getText().toString());
                        newRepeatMap.put("menu_tip", editText.getText() + "天");
                        menuAdapter.notifyDataSetChanged();
                    } else {
                        newRepeatMap.put("menu_tip", "无");
                        menuAdapter.notifyDataSetChanged();
                    }
                    dialogInterface12.dismiss();
                });
                inputDialog.show();
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
        for (int i=0; i<plan.getLabel().size(); i++)
        {
            for (int j=0; j<items.length; j++)
                if (plan.getLabel().get(i).equals(items[j]))
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

            plan.setLabel(checkLabel);
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
