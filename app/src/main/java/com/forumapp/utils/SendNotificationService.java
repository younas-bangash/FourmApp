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
    private String sendingNotificationID;
    private String notificationTitle;
    private String notificationMessage;

    public SendNotificationService(String name) {
        super(name);
    }

    public SendNotificationService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //noinspection ConstantConditions
        sendingNotificationID = intent.getStringExtra(SENDER_ID);
        notificationMessage = intent.getStringExtra(MESSAGE);
        notificationTitle = intent.getStringExtra(TITLE);
        try {
            FCMNotification.pushFCMNotification(sendingNotificationID,
                    notificationMessage,notificationTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}