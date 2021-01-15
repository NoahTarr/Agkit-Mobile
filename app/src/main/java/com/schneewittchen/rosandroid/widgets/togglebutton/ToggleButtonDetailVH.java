package com.schneewittchen.rosandroid.widgets.togglebutton;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */


import androidx.annotation.NonNull;

public class ToggleButtonDetailVH extends BaseDetailViewHolder<ToggleButtonEntity> {
    private EditText topicNameText;
    private EditText topicTypeEditText;

    private EditText textText;
    private Spinner rotationSpinner;

    private ArrayAdapter<CharSequence> rotationAdapter;

    public ToggleButtonDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void initView(View view) {
        topicNameText = view.findViewById(R.id.topicNameEditText);
        topicTypeEditText = view.findViewById(R.id.topicTypeEditText);

        textText = view.findViewById(R.id.btnTextTypeText);
        rotationSpinner = view.findViewById(R.id.btnTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        rotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bindEntity(ToggleButtonEntity entity) {
        topicNameText.setText(entity.topic.name);
        topicTypeEditText.setText(entity.topic.type);

        textText.setText(entity.text);
        rotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }

    @Override
    public void updateEntity() {
        entity.topic.type = std_msgs.Bool._TYPE;
        entity.topic.name = topicNameText.getText().toString();

        entity.text = textText.getText().toString();
        entity.rotation = Utils.degreesToNumber(rotationSpinner.getSelectedItem().toString());
    }
}
