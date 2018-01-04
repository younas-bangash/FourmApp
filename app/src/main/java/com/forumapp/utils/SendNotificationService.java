package com.forumapp.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * This job service will send the notification
 */

public class SendNotificationService extends IntentService {
    public static final String SENDER_ID = "senderID";
    public static final String MESSAGE = "notificationMessage";
    public static final String TITLE = "notificationTitle";

    public SendNotificationService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //noinspection ConstantConditions
        String sendingNotificationID = intent.getStringExtra(SENDER_ID);
        String notificationMessage = intent.getStringExtra(MESSAGE);
        String notificationTitle = intent.getStringExtra(TITLE);
        try {
            FCMNotification.pushFCMNotification(sendingNotificationID,
                    notificationMessage, notificationTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}