package com.yondevstudio.colortapfun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bakwan on 23/12/2017.
 */

public class BorderedView extends View {
    private int color = Color.BLACK;
    private int border = Color.WHITE;
    public BorderedView(Context context) {
        super(context);
    }

    public BorderedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw( Canvas canvas )
    {
        Paint paint = new Paint();
        paint.setColor(this.color);

        paint.setStyle( Paint.Style.STROKE);
        canvas.drawRect( 0, 0, getWidth(), getHeight(), paint );
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
