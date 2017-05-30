package com.twowater.qqmsgnotify.util;

import android.graphics.PointF;

/**
 * 几何图形工具类
 */
public class GeometryUtil {

    /**
     * 获得两点之间的距离.
     **/
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        double distance = Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
        return (float) distance;
    }

    /**
     * 获得两点连线的中点.
     **/
    public static PointF getMiddlePoint(PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
    }

    /**
     * 根据百分比获取两点之间的某个点坐标. percent范围为0 -> 1
     **/
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        float newX = evaluateValue(percent, p1.x, p2.x);
        float newY = evaluateValue(percent, p1.y, p2.y);
        return new PointF(newX, newY);
    }

    /**
     * 根据分度值,计算从start到end中fraction位置的值.fraction范围为0 -> 1
     **/
    public static float evaluateValue(float fraction, float start, float end) {
        return start + (end - start) * fraction;
    }

    /**
     * 获取通过指定圆心、斜率为lineK的直线与圆的交点.
     **/
    public static PointF[] getIntersectionPoints(PointF middle, float radius, Double lineK) {
        PointF[] points = new PointF[2];
        float radian, xOffset = 0, yOffset = 0;
        if (lineK != null) {
            radian = (float) Math.atan(lineK);
            xOffset = (float) (Math.sin(radian) * radius);
            yOffset = (float) (Math.cos(radian) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(middle.x + xOffset, middle.y - yOffset);
        points[1] = new PointF(middle.x - xOffset, middle.y + yOffset);
        return points;
    }
}