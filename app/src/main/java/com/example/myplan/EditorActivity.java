package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplan.data.model.Plan;

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
        if (plan != null)
        {
            bkgFrameLayout.setBackgroundResource(plan.getBackgroundImg());
            editorTitle.setText(plan.getTitle());
            // 时间格式
            String dateStr = plan.getYear() + "年" + plan.getMonth() + "月" + plan.getDay() + "日";
            if (plan.getHour() > 0)
                dateStr += plan.getHour() + ":" + plan.getMinute();
            dateStr += plan.getWeek();

            editorDate.setText(dateStr);
            editorCountDown.setText("测试成功");
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
}
