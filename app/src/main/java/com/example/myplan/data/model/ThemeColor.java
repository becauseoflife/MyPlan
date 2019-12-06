package com.example.myplan.data.model;

import java.io.Serializable;

public class ThemeColor implements Serializable {
    private int myColorPrimaryDark;

    public ThemeColor(){ }

    public int getMyColorPrimaryDark() {
        return myColorPrimaryDark;
    }

    public void setMyColorPrimaryDark(int myColorPrimaryDark) {
        this.myColorPrimaryDark = myColorPrimaryDark;
    }
}
