package com.schneewittchen.rosandroid.widgets.led;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.text.DynamicLayout;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.PublisherView;

import androidx.annotation.Nullable;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LEDView extends PublisherView {
    public static final String TAG = "LEDView";

    Paint LEDPaint;
    TextPaint textPaint;
    //StaticLayout staticLayout;
    DynamicLayout dynamicLayout;
    boolean press = false;

    public LEDView(Context context) {
        super(context);
        init();
    }

    public LEDView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LEDPaint = new Paint();
        LEDPaint.setColor(getResources().getColor(R.color.colorPrimary));
        LEDPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
    }

    private void changeState(boolean pressed) {
        this.publishViewData(new LEDData(pressed));
        invalidate();
    }

    /* Old button code
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LEDEntity entity = (LEDEntity) widgetEntity;
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                LEDPaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(false);
                entity.text = "LED Toggle\n OFF";

                break;
            case MotionEvent.ACTION_DOWN:
                LEDPaint.setColor(getResources().getColor(R.color.color_attention));
                changeState(true);
                entity.text = "LED Toggle\n ON";
                break;
            default:
                return false;
        }

        return true;
    } */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LEDEntity entity = (LEDEntity) widgetEntity;
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (!press) { // Currently LED off, turn on
                    LEDPaint.setColor(getResources().getColor(R.color.color_attention));
                    changeState(true);
                    entity.text = "LED Toggle\n ON";
                    press = !press;
                    break;
                } else if (press) { // Currently LED on, turn off
                    LEDPaint.setColor(getResources().getColor(R.color.colorPrimary));
                    changeState(false);
                    entity.text = "LED Toggle\n OFF";
                    press = !press;
                    break;
                }
            default:
                return false;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;

        LEDEntity entity = (LEDEntity) widgetEntity;

        if (entity.rotation == 90 || entity.rotation == 270) {
            textLayoutWidth = height;
        }

        canvas.drawRect(new Rect(0,0,(int)width,(int)height),LEDPaint);

        /*
        staticLayout = new StaticLayout(entity.text,
                textPaint,
                (int) textLayoutWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0,
                false);
                */
        dynamicLayout = new DynamicLayout(entity.text,
                textPaint,
                (int) textLayoutWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0,
                false);
        canvas.save();
        canvas.rotate(entity.rotation,width / 2,height / 2);
        /*
        canvas.translate( ((width / 2)-staticLayout.getWidth()/2), height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
         */
        canvas.translate( ((width / 2)-dynamicLayout.getWidth()/2), height / 2 - dynamicLayout.getHeight() / 2);
        dynamicLayout.draw(canvas);
        canvas.restore();
    }
}
