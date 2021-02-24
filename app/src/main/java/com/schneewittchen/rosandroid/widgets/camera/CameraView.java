package com.schneewittchen.rosandroid.widgets.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.internal.message.Message;

import rapid.decoder.BitmapDecoder;
import sensor_msgs.CompressedImage;
import sensor_msgs.Image;


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
public class CameraView extends SubscriberView {

    public static final String TAG = CameraView.class.getSimpleName();

    private Paint borderPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private CameraData data;
    private final RectF imageRect = new RectF();
    private final Rect vizBounds = new Rect();
    private final Rect vizRotatedBounds = new Rect();


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

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int rotation = ((CameraEntity) widgetEntity).rotation;

        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        float width = widthViz;
        float height = heightViz;
        float left = leftViz;
        float top = topViz;
        canvas.drawPaint(paintBackground);

        if (data != null) {
            float mapRatio = (float) data.map.getHeight() / data.map.getWidth();
            float vizRatio = heightViz / widthViz;

            if (vizRatio != 1 && (rotation == 90 || rotation == 270)) {
                float mapRatioRotated = (float) data.map.getWidth() / data.map.getHeight();
                if (mapRatioRotated >= vizRatio) {
                    width = heightViz;
                    height = (vizRatio / mapRatioRotated) * widthViz;
                    top = 0.5F * (widthViz - height);
                } else if (vizRatio > mapRatioRotated) {
                    height = widthViz;
                    width = (mapRatioRotated / vizRatio) * heightViz;
                    left = 0.5F * (heightViz - width);
                }
            } else {
                if (mapRatio >= vizRatio) {
                    height = heightViz;
                    width = (vizRatio / mapRatio) * widthViz;
                    left = 0.5F * (widthViz - width);

                } else if (vizRatio > mapRatio) {
                    width = widthViz;
                    height = (mapRatio / vizRatio) * heightViz;
                    top = 0.5F * (heightViz - height);
                }
            }

            canvas.getClipBounds(vizBounds); //Left, top, right, bottom
            canvas.save();
            canvas.rotate(rotation, (float) getWidth() / 2, (float) getHeight() / 2);
            canvas.getClipBounds(vizRotatedBounds); //Left, top, right, bottom
            left += vizRotatedBounds.left + vizBounds.left;
            top += vizRotatedBounds.top + vizBounds.top;
            imageRect.set(left, top, left + width, top + height);
            canvas.drawBitmap(data.map, null, imageRect, borderPaint);
            canvas.restore();
        }

        // Draw Border
        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, borderPaint);
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

//    public boolean onTouchEvent(View v, MotionEvent e) {
//        super.onTouchEvent(e);
//        super.draw(super.canvas);
//        this.draw
//        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, borderPaint);
//        return false;
//    }
}