package com.schneewittchen.rosandroid.widgets.button;

import com.schneewittchen.rosandroid.model.entities.PublisherEntity;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import std_msgs.Bool;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class ButtonEntity extends PublisherEntity {

    public String text;
    public int rotation;

    public ButtonEntity(){
        this.width=3;
        this.height=3;
        this.topic = new Topic("btn_press", Bool._TYPE);
        this.immediatePublish = true;
        this.publishRate = 20f;
        this.text = "Button";
        this.rotation = 0;
    }

}
