package com.forumapp.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Developer on 5/2/2017.
 */

public class FCMNotification {

    // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "AIzaSyAkbhywOjup8bEniOtucnNSYb9ExX2f_4M";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void pushFCMNotification(String sendingDeviceIdKey, String notificationMessage,
                                           String notificationTitle, String eventID, String stallID,
                                           String senderID,
                                           String stallPrice,
                                           String notifReceiverUserID,
                                           String notificationType) throws Exception {
        URL url = new URL(AUTH_KEY_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");
        JSONObject data = new JSONObject();
        data.put("to", sendingDeviceIdKey.trim());
        JSONObject info = new JSONObject();
        info.put("title", notificationTitle); // Notification title
        info.put("body", notificationMessage); // Notification body

//        info.put(SINGLE_EVENT_ID,eventID);
//        info.put(SENDER_USER_ID, senderID);
//        info.put(SINGLE_STALL_ID, stallID);
//        info.put(SINGLE_STALL_PRICE, stallPrice);
//        info.put(NOTIF_RECEIVER_USER_ID,notifReceiverUserID);
//        info.put(NOTIF_TYPE,notificationType);

        data.put("data", info);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data.toString());
        wr.flush();
        wr.close();
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }
}