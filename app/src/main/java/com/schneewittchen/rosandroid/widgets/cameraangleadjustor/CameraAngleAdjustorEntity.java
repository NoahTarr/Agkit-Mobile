package com.schneewittchen.rosandroid.widgets.cameraangleadjustor;

import com.schneewittchen.rosandroid.model.entities.PublisherEntity;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import std_msgs.Bool;
import std_msgs.Float32;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class CameraAngleAdjustorEntity extends PublisherEntity {

    public String text;
    public int rotation;

    public CameraAngleAdjustorEntity(){
        this.width=3;
        this.height=1;
        this.topic = new Topic("angle_press", Float32._TYPE);
        this.immediatePublish = true;
        this.publishRate = 20f;
        this.text = "Button";
        this.rotation = 0;
    }

}