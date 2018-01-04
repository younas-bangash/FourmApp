package com.forumapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.forumapp.R;

/**
 * Created by YounasBangash on 1/3/2018.
 */

public class PrefManager {
    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;
    private final String NAME = "userName";
    private final String EMAIL = "userEmail";
    private final String USER_TOKEN = "userToken";

    public String getName() {
        return getPreferences(this.context).getString(NAME, "");
    }

    public void setName(String name) {
        editor = pref.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getEmail() {
        return getPreferences(this.context).getString(EMAIL, "");
    }

    public String getUserToken() {
        return getPreferences(this.context).getString(USER_TOKEN, "");
    }

    public void setUserToken(String token) {
        editor = pref.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public void setEmail(String email) {
        editor = pref.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public void clearPref(){
        editor = pref.edit();
        editor.clear().apply();
    }


    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

}
