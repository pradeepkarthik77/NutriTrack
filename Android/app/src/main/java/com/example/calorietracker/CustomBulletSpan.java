package com.example.calorietracker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.BulletSpan;

public class CustomBulletSpan extends BulletSpan {
    private int color;
    private static final int BULLET_RADIUS = 15;
    private static final int PADDING = BULLET_RADIUS;

    public CustomBulletSpan(int color) {
        this.color = color;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int oldColor = p.getColor();

        p.setColor(color);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle(x + dir * BULLET_RADIUS, (top + bottom) / 2.0f, BULLET_RADIUS, p);

        p.setColor(oldColor);
        p.setStyle(style);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return BULLET_RADIUS * 2 + PADDING;
    }
}