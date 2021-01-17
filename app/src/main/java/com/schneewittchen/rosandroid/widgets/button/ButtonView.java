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

import std_msgs.Char;

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

    float width = getWidth();
    float height = getHeight();
    Rect r1;
    Rect r2;
    Rect r3;

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
                status = "\n+0"; //not pressed
                break;
            case MotionEvent.ACTION_DOWN:
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    float width = getWidth();
                    float height = getHeight();

                    System.out.println("Width:" + width); //print to console
                    System.out.println("Height" + height);

                    System.out.println("x: " + x + "  y: " + y); //print to console
                    //System.out.println("x check: " + (int)width/2 + "  y check: " + (3*(int)height/10) + " to " + (7*(int)height/10));

                    //if (((x>=0)&&(x<=(int)width/2))&&((y>=3*(int)height/10)&&(y<=7*(int)height/10))) {
                    if (r1.contains (x,y)){
                        //buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                        changeState(true); //pressed
                        status = "\n-1";
                        counter = counter - 1;
                        System.out.println("INCREMENT");
                    }
                    //if (((x>=(int)width/2)&&(x<=(int)width))&&((y>=3*(int)height/10)&&(y<=7*(int)height/10))) {
                    if (r3.contains (x,y)){
                        //buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                        changeState(true); //pressed
                        status = "\n+1";
                        counter = counter + 1;
                        System.out.println("DECREMENT");
                    }

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

         r1 = new Rect(0,2*(int)height/3,(int)width/3,(int)height);
        canvas1.drawRect(r1,buttonPaint);
         r2 = new Rect((int)width/3,2*(int)height/3,2*(int)width/3,(int)height);
        canvas1.drawRect(r2,buttonPaintCHECK);
         r3 = new Rect(2*(int)width/3,2*(int)height/3,(int)width,(int)height);
        canvas1.drawRect(r3,buttonPaint);

//        dynamicLayout = new DynamicLayout( ("+"),
//                textPaint,
//                r1.width()/2,
//                Layout.Alignment.ALIGN_NORMAL,
//                0,
//                0,
//                false);
        canvas1.save();
        canvas1.rotate(entity.rotation,width / 2,height / 2);
//        canvas1.translate( ((width / 2)-dynamicLayout.getWidth()/2), height / 2 - dynamicLayout.getHeight() / 2);
//        dynamicLayout.draw(canvas1);

        textPaint.setTextAlign(Paint.Align.CENTER);
        String stringCounter = String.valueOf(counter);
        //int xText = (canvas1.getWidth() / 2); x position
        //int yText = (int) ((canvas1.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas1.drawText("-", r1.centerX(), r1.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas1.drawText(stringCounter, r2.centerX(), r2.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        canvas1.drawText("+", r3.centerX (), r3.centerY()-((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas1.restore();
    }
}
