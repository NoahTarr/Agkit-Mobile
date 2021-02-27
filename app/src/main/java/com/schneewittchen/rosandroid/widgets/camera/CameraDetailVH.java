package com.schneewittchen.rosandroid.widgets.camera;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.Arrays;
import java.util.List;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 13.05.20
 * @updated on 07.09.20
 * @modified by Nico Studt
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class CameraDetailVH extends BaseDetailSubscriberVH<CameraEntity> {

    public static final String TAG = CameraDetailVH.class.getSimpleName();

    private Spinner rotationSpinner;
    private ArrayAdapter<CharSequence> rotationAdapter;

    public CameraDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    protected void initView(View parentView) {
        rotationSpinner = parentView.findViewById(R.id.btnTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        rotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bindEntity(CameraEntity entity) {
        rotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }

    @Override
    protected void updateEntity() {
        entity.rotation = Utils.degreesToNumber(rotationSpinner.getSelectedItem().toString());
    }

    @Override
    public List<String> getTopicTypes() {
        return Arrays.asList(Image._TYPE, CompressedImage._TYPE);
    }

}
