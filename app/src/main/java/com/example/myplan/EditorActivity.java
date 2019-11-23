package com.example.myplan;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplan.data.GetPlanDateBetweenNow;
import com.example.myplan.data.model.Plan;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EDITOR_PLAN = 905;

    private Toolbar editorToolBar;
    private ListView listViewSettingMenu;
    private TextView editorTitle;
    private TextView editorDate;
    private TextView editorCountDown;
    private FrameLayout bkgFrameLayout;
    private List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
    private Plan plan;
    private int planPosition;
    private CountDownTimer timer;   // 倒计时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        bkgFrameLayout = findViewById(R.id.editor_bkg_frame);
        editorTitle = findViewById(R.id.editor_bkg_title_textView);
        editorDate = findViewById(R.id.editor_bkg_date_textView);
        editorCountDown = findViewById(R.id.editor_count_down_textView);

        // toolBar工具栏
        editorToolBar = findViewById(R.id.editor_menu_toolBar);
        editorToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 将修改的数据传回主界面
                Intent editorIntent = new Intent(EditorActivity.this, MainActivity.class);
                editorIntent.putExtra("editor_plan", plan);
                editorIntent.putExtra("editor_plan_position", planPosition);
                setResult(RESULT_OK, editorIntent);
                EditorActivity.this.finish();
            }
        });
        editorToolBar.setOnMenuItemClickListener(new MyMenuClickListener());

        // 菜单选项和页面
        listViewSettingMenu = findViewById(R.id.editor_set_menu_listView);
        menuList = InitMenuList();
        SimpleAdapter menuAdapter = new SimpleAdapter(
                this,
                menuList,
                R.layout.editor_list_item_menu,
                new String[] {"menu_image", "menu_title", "menu_select_icon"},
                new int[] {R.id.editor_menu_icon, R.id.editor_menu_title, R.id.editor_menu_select_icon}
        );
        listViewSettingMenu.setAdapter(menuAdapter);


        // 获取数据
        plan = (Plan) getIntent().getSerializableExtra("editor_plan");
        planPosition = getIntent().getIntExtra("editor_plan_position",-1);

        // 界面显示
        editorPlanView();

        editorCountDown.setOnClickListener(new ChangeFormatOnClickListener());

    }

    // 编辑界面plan的显示
    private void editorPlanView() {
        // 设置显示的数据
        bkgFrameLayout.setBackgroundResource(plan.getBackgroundImg());
        editorTitle.setText(plan.getTitle());
        // 时间格式
        GetPlanDateBetweenNow getDate = new GetPlanDateBetweenNow(plan);
        editorDate.setText(getDate.getDateStr());

        // 获取秒数
        long ms = 0;
        try {
            ms = getDate.CalculationMillisecondWhitNow();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 倒计时
        timer = new CountDownTimer(ms, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTick(long countDownTimeMs) {
                ArrayList<String> countDownTime = getDate.getCountDownDateArrayList(countDownTimeMs);

                StringBuilder countDownStr = new StringBuilder();
                for (int i=0; i<countDownTime.size(); i++)
                    countDownStr.append(countDownTime.get(i));

                editorCountDown.setText(countDownStr);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    // 处理修改返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_EDITOR_PLAN:{
                if (resultCode == RESULT_OK)
                {
                    if (data != null) {
                        this.plan = (Plan)data.getSerializableExtra("newPlan");
                    }
                    timer.cancel();     // 取消旧的倒计时
                    editorPlanView();   // 刷新界面
                }
                break;
            }
        }
    }

    // 左滑菜单里面的内容
    private List<Map<String, Object>> InitMenuList() {

        // 日期
        Map<String, Object> menuItem = new HashMap<String, Object>();
        menuItem.put("menu_image", R.drawable.editor_menu_notibar);
        menuItem.put("menu_title", "通知栏");
        menuItem.put("menu_select_icon", R.drawable.editor_menu_select);
        menuList.add(menuItem);

        // 设置重复
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.editor_menu_calendar);
        menuItem.put("menu_title", "在日历中显示");
        menuItem.put("menu_select_icon", R.drawable.editor_menu_select);
        menuList.add(menuItem);

        // 图片
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.editor_menu_fast_icon);
        menuItem.put("menu_title", "快捷图标");
        menuItem.put("menu_select_icon", R.drawable.editor_menu_select);
        menuList.add(menuItem);

        // 添加标签
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.editor_menu_floating);
        menuItem.put("menu_title", "悬浮窗口");
        menuItem.put("menu_select_icon", R.drawable.editor_menu_select);
        menuList.add(menuItem);

        return menuList;
    }


    // toolbar菜单栏点击响应事件
    private class MyMenuClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int menuItemId = item.getItemId();
            switch (menuItemId)
            {
                case R.id.full_screen_icon:{
                    Toast.makeText(EditorActivity.this, "点击全屏", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.delete_icon:{
                    Intent deleteIntent = new Intent(EditorActivity.this, MainActivity.class);
                    deleteIntent.putExtra("delete_plan_position", planPosition);
                    setResult(RESULT_OK, deleteIntent);
                    EditorActivity.this.finish();
                    Toast.makeText(EditorActivity.this, "点击删除", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.share_icon:{
                    Toast.makeText(EditorActivity.this, "点击分享", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.editor_icon:{
                    Intent editorIntent = new Intent(EditorActivity.this, AddPlanActivity.class);
                    editorIntent.putExtra("editor_plan", plan);
                    startActivityForResult(editorIntent, REQUEST_CODE_EDITOR_PLAN);
                    Toast.makeText(EditorActivity.this, "点击修改", Toast.LENGTH_SHORT).show();
                    break;
                }
            }


            return false;
        }
    }

    // 设置倒计时点击响应事件
    private class ChangeFormatOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }
}
