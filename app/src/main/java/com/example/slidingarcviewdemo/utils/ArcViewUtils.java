package com.example.slidingarcviewdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class ArcViewUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static void initScreen(Context mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)mActivity).getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }

    public static float getScreenDensity() {
        return screenDensity;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    /**
     * 某个点围绕另外一个点旋转角度后的坐标
     *
     * @param p       原点
     * @param pCenter 中线点
     * @param angle   角度
     * @return 最终坐标
     */
    public static Point calcNewPoint(Point p, Point pCenter, float angle) {
        // calc arc
        float l = (float) ((angle * Math.PI) / 180);

        //sin/cos value
        float cosv = (float) cos(l);
        float sinv = (float) sin(l);

        // calc new point
        float newX = (float) ((p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x);
        float newY = (float) ((p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y);
        return new Point((int) newX, (int) newY);
    }

    /**
     * 根据角度计算圆的坐标点
     *
     * @param radius 圆的半径
     * @param angle  角度
     * @param center 圆心
     * @return
     */
    public static Point getPointByAngle(int radius, int angle, Point center) {
        int x = (int) (center.x + radius * Math.cos(angle * 3.14 / 180));
        int y = (int) (center.y + radius * Math.sin(angle * 3.14 / 180));
        return new Point(x, y);
    }

    /**
     * 根据坐标计算角度
     *
     * @param center 圆心
     * @return 角度
     */
    public static double getAngleByPoint(Point center, Point point) {
        return Math.atan2(point.y - center.y, point.x - center.x) * (180 / Math.PI);
    }

    /**
     * 算出该点与水平的角度的值，用移动点角度减去起始点角度就是旋转角度。
     */
    private double getAngle(double xTouch, double yTouch, Point center) {
        double x = xTouch - center.x;
        double y = yTouch - center.y;
        return (double) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }
}
