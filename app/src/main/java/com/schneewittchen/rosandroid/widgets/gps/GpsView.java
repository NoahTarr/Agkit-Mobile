package com.schneewittchen.rosandroid.widgets.gps;

import android.content.res.AssetManager;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.ros.internal.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import sensor_msgs.NavSatFix;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

// TODO: Add maybe a button for getting back to gps position
public class GpsView extends SubscriberView {
    
    public static final String TAG = "GpsView";

    // Open Street Map (OSM)
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private GeoPoint locationGeoPoint = new GeoPoint(38.531702, -121.753551);
    private GeoPoint centerGeoPoint = new GeoPoint(38.531702, -121.753551);
    IMapController mapController = null;

    // Rectangle Surrounding
    Paint paint;
    float cornerWidth;

    // Grid Map Information
    GpsData data;

    // Zoom Parameters, TODO: Add this into details
    private double minZoom = 2;         // min. and max. zoom
    private double maxZoom = 15;
    private float zoomScale = 1;
    private float scaleFactor = 15;
    private double dragSensitivity = 0.05;
    private ScaleGestureDetector detector;

    private static int NONE = 0;                // mode
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;

    private float startX = 0f;                  // finger position tracker
    private float startY = 0f;

    private float translateX = 0f;              // Amount of translation
    private float translateY = 0f;
    private double moveLat = 0;
    private double moveLon = 0;

    private double accLat = 0;
    private double accLon = 0;

    private boolean hadLongPressed = false;

    // Polyline
    private Polyline polyline;

    public GpsView(Context context) {
        super(context);
        init();
    }

    public GpsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    

    private void init() {
        this.cornerWidth = Utils.dpToPx(this.getContext(), 8);

        requestPermissionsIfNecessary(new String[]{
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        });

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        // OSM (initialize the map)
        Context ctx = this.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        String loc = ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();

        // Move assets to external storage
        AssetManager assetManager = ctx.getAssets();
        String fileName = "NCalBig.gemf";
        File f = new File(loc);
        File mapLoc = new File(loc + "/" + fileName);
        if (!mapLoc.exists()) {
            if (!f.mkdirs()) {
                f.mkdirs();
            }
            InputStream mapFile = null;
            OutputStream out = null;
            try {
                mapFile = assetManager.open(fileName);
                out = new FileOutputStream(loc + "/" + fileName);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = mapFile.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                mapFile.close();
                mapFile = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }

        // Initialize MapView
        Configuration.getInstance().setOsmdroidBasePath(new File(loc)); // Change dir to external memory
        map = new MapView(this.getContext(), null, null);
        map.setUseDataConnection(false); // Set to offline maps only
        //map.setTileSource(TileSourceFactory.MAPNIK); // Online map source
        ITileSource tileSource = new XYTileSource("4uMaps", 2, 15, 256, ".png", new String[] {""}); // Offline 4uMaps
        map.setTileSource(tileSource);
        
        //map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS); // Turn off useless controls on bottom
        map.setMultiTouchControls(true);
        minZoom = map.getMinZoomLevel();
        maxZoom = map.getMaxZoomLevel();
        
        // Map controller
        mapController = map.getController();
        mapController.setCenter(centerGeoPoint);
        mapController.setZoom(scaleFactor);
        
        // Touch
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());

        // Polyline initialize
        polyline = new Polyline();
        polyline.getOutlinePaint().setColor(Color.parseColor("#EB7734"));
        polyline.getOutlinePaint().setStrokeWidth(10f);
        polyline.getOutlinePaint().setAlpha(80);
    }

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            moveLat = 0;
            moveLon = 0;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean dragged = false;
      
        gestureDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                startX = event.getX();
                startY = event.getY();
                break;
                
            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
                double distance = Math.sqrt(Math.pow(event.getX() - startX, 2) +
                        Math.pow(event.getY() - startY, 2)
                );
                if (distance > 0) {
                    dragged = true;
                }
                break;
                
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
                
            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;
                break;
                
            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;
                break;
        }
        // Activate Zoom
        detector.onTouchEvent(event);
        
        // Redraw the canvas
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            this.invalidate();
        }
        
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
//        Log.i(TAG, "On Draw");
        super.onDraw(canvas);
        canvas.save();

        // Get vizualization size
        float left = 0F;
        float right = 0F;
        float width = getWidth();
        float height = getHeight();

        // Set overlay item
        OverlayItem overlayItem = new OverlayItem("Position", "Robot", locationGeoPoint);
        ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<>();
        overlayItemArrayList.add(overlayItem);
        ItemizedOverlay<OverlayItem> locationOverlay = new ItemizedIconOverlay<>(this.getContext(), overlayItemArrayList, null);

        // Move the map to specific location
        zoomScale = (float) Math.pow(2, scaleFactor);

        // Just separating acceleration component
        accLat = (translateY/zoomScale) * dragSensitivity;
        accLon = (translateX/zoomScale) * dragSensitivity;

        moveLat = moveLat + accLat;
        moveLon = moveLon - accLon;

        // Resets dynamics, otherwise every time GPS publishes it keeps scrolling the map
        translateY = 0;
        translateX = 0;

        centerGeoPoint.setLatitude(locationGeoPoint.getLatitude() + moveLat);
        centerGeoPoint.setLongitude(locationGeoPoint.getLongitude() + moveLon);
        mapController.setCenter(centerGeoPoint);
        mapController.setZoom(scaleFactor);
        map.requestLayout();


        // Draw the OMS
        map.layout((int) left, (int) right, (int) width, (int) height);
        map.getOverlays().add(locationOverlay);
        map.draw(canvas);

        // Apply the changes
        canvas.restore();
        // Put a rectangle around
        canvas.drawRoundRect(left, right, width, height, cornerWidth, cornerWidth, paint);
    }
    
    @Override
    public void onNewMessage(Message message) {
        this.data = new GpsData((NavSatFix) message);
        GeoPoint newGeo = new GeoPoint(this.data.getLat(), this.data.getLon());
        locationGeoPoint.setLatitude(this.data.getLat());
        locationGeoPoint.setLongitude(this.data.getLon());
        polyline.addPoint(locationGeoPoint); // Add new point for path drawing
        map.getOverlays().add(polyline); // TODO: Check if this line is needed
        this.invalidate();
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max((float) minZoom, Math.min(scaleFactor, (float) maxZoom));
            return true;
        }
    }
}
