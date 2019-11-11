package com.example.myplan.data;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myplan.ViewPlanFragment;
import com.example.myplan.data.model.Plan;

import java.util.ArrayList;

public class PlanFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Plan> planArrayList;

    public PlanFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setPlanArrayList(ArrayList<Plan> planArrayList) {
        this.planArrayList = planArrayList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Plan plan = planArrayList.get(position);
        return new ViewPlanFragment(plan);
    }

    @Override
    public int getCount() {
        return planArrayList == null ? 0 : planArrayList.size();
    }


}
