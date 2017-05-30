package com.twowater.qqmsgnotify.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

public class CommonUtil {

    /**
     * 获取像素比
     **/
    public static float getDisplayDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(float dp, Context context) {
        return (int) (dp * getDisplayDensity(context) + 0.5f);
    }

    public static float px2dp(int px, Context context) {
        return px / getDisplayDensity(context);
    }

    public static int getStatusBarHeight(View v) {
        if (v == null) {
            return 0;
        }
        Rect frame = new Rect();
        v.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}
