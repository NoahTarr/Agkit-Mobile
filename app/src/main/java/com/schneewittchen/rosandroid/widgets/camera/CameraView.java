package com.schneewittchen.rosandroid.widgets.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.ros.internal.message.Message;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import static android.media.MediaRecorder.MetricsConstants.HEIGHT;
import static java.awt.font.TextAttribute.WIDTH;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.1
 * @created on 27.04.19
 * @updated on 20.10.2020
 * @modified by Nico Studt
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class CameraView extends SubscriberView  {

    public static final String TAG = CameraView.class.getSimpleName();

    private Paint borderPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private CameraData data;
    private RectF imageRect = new RectF();
    RectF rect;
    boolean pressed = false;
    int heightScreen;
    int widthScreen;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    float scale = displayMetrics.densityDpi;


    //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics );

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init() {
        this.cornerWidth = Utils.dpToPx(getContext(), 8);

        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(R.color.whiteHigh));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);

        // Background color
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(100, 0, 0, 0));
        paintBackground.setStyle(Paint.Style.FILL);
    }

   // @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //Button b = new Button(this);
//        Button buttonZoomIn = findViewById(R.id.zoomInButton);
//        //Button buttonZoomOut = findViewById(R.id.zoomOutButton);
//
//        //set listener to button
//        buttonZoomIn.setOnClickListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
         heightScreen = metrics.heightPixels;
         widthScreen = metrics.widthPixels;
//
//
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                float width = getWidth();
                float height = getHeight();

                if (rect.contains (x,y)){
                    System.out.println("PRESSED");
                    //pressed = true;
                    this.invalidate();
                }
                pressed = !pressed;

                break;
            default:
                return false;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(paintBackground);

        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        float width = widthViz;
        float height = heightViz;
        float left = leftViz;
        float top = topViz;

        if (data != null) {
            float mapRatio = (float) data.map.getHeight() / data.map.getWidth();
            float vizRatio = heightViz / widthViz;

            if (mapRatio >= vizRatio) {
                height = heightViz;
                width = (vizRatio / mapRatio) * widthViz;
                left = 0.5F * (widthViz - width);

            } else if (vizRatio > mapRatio) {
                width = widthViz;
                height = (mapRatio / vizRatio) * heightViz;
                top = 0.5F * (heightViz - height);
            }

            imageRect.set(left, top, left + width, top + height);
            canvas.drawBitmap(data.map, null, imageRect, borderPaint);
        }

        // Draw Border
        if (pressed == false) {
            rect = new RectF(leftViz, topViz, widthViz, heightViz);
            canvas.drawRoundRect(rect, cornerWidth, cornerWidth, borderPaint);

        }
        else
        {
            System.out.println ("Scale");
            canvas.drawRoundRect(leftViz+10, topViz+10, widthViz+10, heightViz+10, cornerWidth, cornerWidth, borderPaint);
            //canvas.scale(heightScreen, widthScreen);
        }
    }

    @Override
    public void onNewMessage(Message message) {
        this.data = null;

        if(message instanceof CompressedImage) {
            this.data = new CameraData((CompressedImage) message);
        } else if (message instanceof Image) {
            this.data = new CameraData((Image) message);
        } else {
            return;
        }
        
        this.invalidate();
    }

//    @Override
//    public void onClick(View view) {
//        {
//            @SuppressLint("WrongViewCast") ImageView img = findViewById(R.id.zoomInButton);
//            rect.left = img.getLeft();
//            rect.top = img.getTop();
//            rect.bottom = img.getBottom();
//            rect.right = img.getRight();
//
//            Animation animZoomIn = AnimationUtils.loadAnimation(getContext(),
//                    R.anim.zoomin);
//            img.startAnimation(animZoomIn);
//        }
    }
