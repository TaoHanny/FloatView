package com.shutuo.floatview.windowmanager;

public class Location {
    int viewX = 0;
    int ViewY = 0;

    public Location(int viewX, int viewY) {
        this.viewX = viewX;
        ViewY = viewY;
    }

    public int getViewX() {
        return viewX;
    }

    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    public int getViewY() {
        return ViewY;
    }

    public void setViewY(int viewY) {
        ViewY = viewY;
    }
}
