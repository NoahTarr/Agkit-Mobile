package com.schneewittchen.rosandroid.widgets.cameraangleadjustor;

import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

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

public class CameraAngleAdjustorData extends BaseData {

    public float counter;

    public CameraAngleAdjustorData(float counter) {
        this.counter = counter;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        std_msgs.Float32 message = (Float32) publisher.newMessage();
        message.setData(counter);
        return message;
    }
}
