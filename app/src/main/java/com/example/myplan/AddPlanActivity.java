package com.example.myplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddPlanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView menuListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        // 工具栏的实现
        toolbar = findViewById(R.id.add_plan_toolbar);
        toolbar.inflateMenu(R.menu.add_plan_toolbar_menu);
        // 工具栏返回图标的监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPlanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 添加页面的选项菜单项
        menuListView = findViewById(R.id.add_plan_menu_list_view);

        MenuSimpleAdapter menuAdapter = new MenuSimpleAdapter(
                AddPlanActivity.this,
                getMenuData(),
                R.layout.add_plan_list_item_menu,
                new String[] {"menu_image", "menu_title", "menu_tip"},
                new int[] {R.id.menu_image_view, R.id.menu_title_text_view, R.id.menu_tip_text_view});

        menuListView.setAdapter(menuAdapter);

    }

    // 设置菜单列表每一列的内容
    private List<Map<String, Object>> getMenuData() {
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();

        // 日期
        Map<String, Object> menuItem = new HashMap<String, Object>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_date);
        menuItem.put("menu_title", "时间");
        menuItem.put("menu_tip", "长按使用日期计算器");
        menuList.add(menuItem);

        // 设置重复
        menuItem = new HashMap<>();
        menuItem.put("menu_image", R.drawable.add_plan_menu_repeat);
        menuItem.put("menu_title", "设置重复");
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

    private class MenuSimpleAdapter extends SimpleAdapter {

        MenuSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }
    }
}
