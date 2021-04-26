package com.schneewittchen.rosandroid.widgets.cameraangleadjustor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.PublisherView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * TODO: Description
 *
 * @author Neil Katahira
 * @version 1.0.1
 * @created on 02.11.2021
 * @updated on 04.11.2021
 * @modified by Noah Tarr
 */

public class CameraAngleAdjustorView extends PublisherView {
    public static final String TAG = "ButtonView";

    Paint increaseAngleRectanglePaint;
    Paint currentAngleRectanglePaint;
    Paint decreaseAngleRectanglePaint;
    TextPaint textPaint;

    private final int minAngle = -55;
    private final int maxAngle = 55;
    public int counter = 0;
    public String status = "\n+1";

    Rect increaseAngleRectangle = new Rect();
    Rect currentAngleRectangle = new Rect();
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
        increaseAngleRectanglePaint = new Paint();
        increaseAngleRectanglePaint.setColor(getResources().getColor(R.color.colorPrimary));
        increaseAngleRectanglePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        currentAngleRectanglePaint = new Paint();
        currentAngleRectanglePaint.setColor(getResources().getColor(R.color.colorAccent));
        currentAngleRectanglePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        decreaseAngleRectanglePaint = new Paint();
        decreaseAngleRectanglePaint.setColor(getResources().getColor(R.color.colorPrimary));
        decreaseAngleRectanglePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
    }

    private void changeState(float count) {
        this.publishViewData(new CameraAngleAdjustorData(count + (int)(maxAngle / 2)));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                increaseAngleRectanglePaint.setColor(getResources().getColor(R.color.colorPrimary));
                decreaseAngleRectanglePaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(counter);
                status = "\n+0"; //not pressed
                break;
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                if ((increaseAngleRectangle.contains (x,y)) && (counter > minAngle)){
                    increaseAngleRectanglePaint.setColor(getResources().getColor(R.color.color_attention));
                    changeState(counter); //pressed
                    status = "\n-1";
                    counter = counter - 5;
                }
                if ((decreaseAngleRectangle.contains (x,y)) && (counter < maxAngle)){
                    decreaseAngleRectanglePaint.setColor(getResources().getColor(R.color.color_attention));
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
    public void onDraw(Canvas canvas) {
        setDefaultRectangleCoords();
        int widgetRotation = ((CameraAngleAdjustorEntity) widgetEntity).rotation;
        if (widgetRotation != 0) rotateWidget(widgetRotation);

        canvas.drawRect(increaseAngleRectangle, increaseAngleRectanglePaint);
        canvas.drawRect(currentAngleRectangle, currentAngleRectanglePaint);
        canvas.drawRect(decreaseAngleRectangle, decreaseAngleRectanglePaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("-", increaseAngleRectangle.centerX(), increaseAngleRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas.drawText(String.valueOf(counter), currentAngleRectangle.centerX(), currentAngleRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas.drawText("+", decreaseAngleRectangle.centerX (), decreaseAngleRectangle.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
    }

    private enum widgetButton {
        INCREMENT,
        CURRENT_VALUE,
        DECREMENT
    }

    private void setDefaultRectangleCoords() {
        setCoordsFor(widgetButton.INCREMENT);
        setCoordsFor(widgetButton.CURRENT_VALUE);
        setCoordsFor(widgetButton.DECREMENT);
    }

    private void setCoordsFor(@NonNull widgetButton widgetButton) {
        int widgetWidth = getWidth();
        int top, left, right, bottom;
        top = 0;
        bottom = getHeight();

        switch (widgetButton) {
            case INCREMENT:
                left = 0;
                right = (int)widgetWidth/3;
                increaseAngleRectangle.set(left, top, right, bottom);
                break;
            case CURRENT_VALUE:
                left = (int)widgetWidth/3;
                right = 2*(int)widgetWidth/3;
                currentAngleRectangle.set(left, top, right, bottom);
                break;
            case DECREMENT:
                left = 2*(int)widgetWidth/3;
                right = (int)widgetWidth;
                decreaseAngleRectangle.set(left, top, right, bottom);
        }
    }

    private void rotateWidget(int rotation) {
        if (rotation == 180) {
            decreaseAngleRectangle.left = 0;
            decreaseAngleRectangle.right = getWidth()/3;
            increaseAngleRectangle.left = 2 * getWidth()/3;
            increaseAngleRectangle.right = getWidth()/3;
        }
        else {
            increaseAngleRectangle.left = currentAngleRectangle.left = decreaseAngleRectangle.left = 0;
            increaseAngleRectangle.right = currentAngleRectangle.right = decreaseAngleRectangle.right = getWidth();
            currentAngleRectangle.top = getHeight()/3;
            currentAngleRectangle.bottom = 2 * getHeight()/3;
            switch (rotation) {
                case 90:
                    increaseAngleRectangle.top = 0;
                    increaseAngleRectangle.bottom = getHeight()/3;
                    decreaseAngleRectangle.top = 2 * getHeight()/3;
                    decreaseAngleRectangle.bottom = getHeight();
                    break;
                case 270:
                    decreaseAngleRectangle.top = 0;
                    decreaseAngleRectangle.bottom = getHeight()/3;
                    increaseAngleRectangle.top = 2 * getHeight()/3;
                    increaseAngleRectangle.bottom = getHeight();
            }
        }
    }
}
