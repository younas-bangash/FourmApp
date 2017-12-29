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
    private String stallID;
    private String senderID;
    private String eventID;
    private String notificationType;
    private String stallPrice;
    private String notifReceiverUserID;

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
//        eventID = intent.getStringExtra(SINGLE_EVENT_ID);
//        senderID = intent.getStringExtra(SENDER_USER_ID);
//        notificationType = intent.getStringExtra(NOTIF_TYPE);
//        stallID = intent.getStringExtra(SINGLE_STALL_ID);
//        stallPrice = intent.getStringExtra(SINGLE_STALL_PRICE);
//        notifReceiverUserID = intent.getStringExtra(NOTIF_RECEIVER_USER_ID);
        try {
            FCMNotification.pushFCMNotification(sendingNotificationID, notificationMessage,
                    notificationTitle,eventID,stallID,senderID,stallPrice,notifReceiverUserID,notificationType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}