package com.example.myplan.data.model;

import com.example.myplan.R;

import java.io.Serializable;

public class ThemeColor implements Serializable {
    private int myColorPrimaryDark;

    public ThemeColor(){
        myColorPrimaryDark = -1;
    }

    public int getMyColorPrimaryDark() {
        return myColorPrimaryDark;
    }

    public void setMyColorPrimaryDark(int myColorPrimaryDark) {
        this.myColorPrimaryDark = myColorPrimaryDark;
    }
}
