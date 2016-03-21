package com.ivan.chart.simplechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

/**
 * 修复不同版本的api过期/更新问题
 * Created by Ivan on 16/2/2.
 */
public class Compat {

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(id,null);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Canvas canvas, Paint paint) {
        if (Build.VERSION.SDK_INT >= 23) {
            canvas.drawRoundRect(left,top,right,bottom,rx,ry,paint);
        } else {
            RectF rect;
            rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, 10, 10, paint);
        }
    }

}
