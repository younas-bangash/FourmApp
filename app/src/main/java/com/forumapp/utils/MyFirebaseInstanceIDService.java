package com.forumapp.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private DatabaseReference databaseReferenceUsers = null;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    @SuppressLint("LongLogTag")
    private void sendRegistrationToServer(final String token) {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuth != null) {
            databaseReferenceUsers = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(mAuth.getUid());


            databaseReferenceUsers.child(mAuth.getUid()).child("userTokenID")
                    .setValue(token).addOnCompleteListener(task -> {
                Log.d("sendRegistrationToServer() : ", "Token Updated Successfully");

            }).addOnFailureListener(e -> {

                Log.d("sendRegistrationToServer() : ", "Error in Updating Token");

            });
        }

    }
}