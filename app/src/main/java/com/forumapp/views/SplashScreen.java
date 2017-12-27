package com.forumapp.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.forumapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = null;
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    mainIntent = new Intent(SplashScreen.this, TopicsActivity.class);
                }else{
                    mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
}
