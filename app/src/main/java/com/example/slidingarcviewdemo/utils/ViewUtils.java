package com.example.slidingarcviewdemo.utils;

import android.view.MotionEvent;
import android.view.View;

public class ViewUtils {
    //判断当前View 是否处于touch中
    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y
                + view.getHeight())) {
            return false;
        }
        return true;
    }
}
