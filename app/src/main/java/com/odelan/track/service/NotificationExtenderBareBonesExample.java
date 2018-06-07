package com.odelan.track.service;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by Administrator on 8/16/2016.
 */
public class NotificationExtenderBareBonesExample extends NotificationExtenderService {

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        return true;
    }
}