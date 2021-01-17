package com.schneewittchen.rosandroid.widgets.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.DynamicLayout;
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

public class ButtonView extends PublisherView {
    public static final String TAG = "ButtonView";

    Paint buttonPaint;
    Paint buttonPaintCHECK;
    TextPaint textPaint;
    DynamicLayout dynamicLayout;
    public String status = "\n+1";
    public int counter = 0;

    public ButtonView(Context context) {
        super(context);
        init();
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        buttonPaintCHECK = new Paint();
        buttonPaintCHECK.setColor(getResources().getColor(R.color.color_attention));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
    }

    private void changeState(boolean pressed) {
        this.publishViewData(new ButtonData(pressed));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(false);
                status = "\n+1"; //not pressed
                break;
            case MotionEvent.ACTION_DOWN:
                buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                changeState(true); //pressed
                status = "\n+1";
                counter=counter +1;

                break;
            default:
                return false;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas1) {
        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;

        ButtonEntity entity = (ButtonEntity) widgetEntity;

        if (entity.rotation == 90 || entity.rotation == 270) {
            textLayoutWidth = height;
        }
        System.out.println("Width:" + width); //print to console
        System.out.println("Height" + height);
        canvas1.drawRect(new Rect(0,3*(int)height/10,(int)width/3,7*(int)height/10),buttonPaint);
        canvas1.drawRect(new Rect((int)width/3,3*(int)height/10,2*(int)width/3,7*(int)height/10),buttonPaintCHECK);
        canvas1.drawRect(new Rect((int)width,3*(int)height/10,2*(int)width/3,7*(int)height/10),buttonPaint);

        dynamicLayout = new DynamicLayout( "" + counter,
                textPaint,
                (int) textLayoutWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0,
                false);
        canvas1.save();
        canvas1.rotate(entity.rotation,width / 2,height / 2);
        canvas1.translate( ((width / 2)-dynamicLayout.getWidth()/2), height / 2 - dynamicLayout.getHeight() / 2);
        dynamicLayout.draw(canvas1);
        canvas1.restore();
    }
}
