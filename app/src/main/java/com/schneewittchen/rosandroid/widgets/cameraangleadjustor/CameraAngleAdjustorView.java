package com.schneewittchen.rosandroid.widgets.cameraangleadjustor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.DynamicLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

public class CameraAngleAdjustorView extends PublisherView {
    public static final String TAG = "ButtonView";

    Paint buttonPaint;
    Paint buttonPaintCHECK;
    TextPaint textPaint;
    public String status = "\n+1";
    public int counter = 0;

    Rect increaseAngleRectangle = new Rect();
    Rect currentValueRectangle = new Rect();
    Rect decreaseAngleRectangle = new Rect();

    public CameraAngleAdjustorView(Context context) {
        super(context);
        init();
    }

    public CameraAngleAdjustorView(Context context, @Nullable AttributeSet attrs) {
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

    private void changeState(float count) {
        this.publishViewData(new CameraAngleAdjustorData(count));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(counter);
                status = "\n+0"; //not pressed
                break;
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                if ((increaseAngleRectangle.contains (x,y)) && (counter > 0)){
                    changeState(counter); //pressed
                    status = "\n-1";
                    counter = counter - 5;
                }
                if ((decreaseAngleRectangle.contains (x,y)) && (counter < 110)){
                    changeState(counter); //pressed
                    status = "\n+1";
                    counter = counter + 5;
                }

                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();

        return true;
    }


    @Override
    public void onDraw(Canvas canvas1) {
        float width = getWidth();
        float height = getHeight();
        int topIncrease, leftIncrease, rightIncrease, bottomIncrease,
                leftCrntVal, topCrntVal, rightCrntVal, bottomCrntVal,
                leftDecrease, topDecrease, rightDecrease, bottomDecrease;

        CameraAngleAdjustorEntity entity = (CameraAngleAdjustorEntity) widgetEntity;


        topIncrease = topCrntVal = topDecrease = 0;
        bottomIncrease = bottomCrntVal = bottomDecrease = (int)height;

        leftIncrease = 0;
        rightIncrease = (int)width/3;

        leftCrntVal = (int)width/3;
        rightCrntVal = 2*(int)width/3;

        leftDecrease = 2*(int)width/3;
        rightDecrease = (int)width;

        if (entity.rotation == 180){
            leftIncrease = leftDecrease;
            leftDecrease = 0;
            rightIncrease = rightDecrease;
            rightDecrease = (int)width/3;
        }
        else if (entity.rotation == 90 || entity.rotation == 270) {
            leftIncrease = leftCrntVal = leftDecrease = 0;
            rightIncrease = rightCrntVal = rightDecrease = (int)width;

            topIncrease = 0;
            bottomIncrease = (int)height/3;

            topCrntVal = (int)height/3;
            bottomCrntVal = 2*(int)height/3;

            topDecrease = 2*(int)height/3;
            bottomDecrease = (int)height;

            if (entity.rotation == 270) {
                topIncrease = topDecrease;
                topDecrease = 0;
                bottomIncrease = bottomDecrease;
                bottomDecrease = (int)height/3;
            }
        }

        increaseAngleRectangle.set(leftIncrease, topIncrease, rightIncrease, bottomIncrease);
        currentValueRectangle.set(leftCrntVal, topCrntVal, rightCrntVal, bottomCrntVal);
        decreaseAngleRectangle.set(leftDecrease, topDecrease, rightDecrease, bottomDecrease);
        canvas1.drawRect(increaseAngleRectangle,buttonPaint);
        canvas1.drawRect(currentValueRectangle,buttonPaintCHECK);
        canvas1.drawRect(decreaseAngleRectangle,buttonPaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas1.drawText("-", increaseAngleRectangle.centerX(), increaseAngleRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas1.drawText(String.valueOf(counter), currentValueRectangle.centerX(), currentValueRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas1.drawText("+", decreaseAngleRectangle.centerX (), decreaseAngleRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
    }
}
