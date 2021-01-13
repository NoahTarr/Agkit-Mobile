package com.schneewittchen.rosandroid.widgets.led;

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

public class LEDEntity extends PublisherEntity {

    public String text;
    public int rotation;

    public LEDEntity(){
        this.width=3;
        this.height=4;
        this.topic = new Topic("led_press", Bool._TYPE);
        this.immediatePublish = true;
        this.publishRate = 20f;
        this.text = "LED Toggle\n OFF";
        this.rotation = 0;
    }

}
