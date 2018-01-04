package com.forumapp.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class SingUpModel {
    public String userName;
    public String userEmail;
    public String userTokenID;
    public String userID;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserTokenID(String userTokenID) {
        this.userTokenID = userTokenID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("userTokenID",userTokenID);
        result.put("userID",userID);
        return result;
    }
}
