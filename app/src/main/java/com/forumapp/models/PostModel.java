package com.forumapp.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YounasBangash on 12/29/2017.
 */

public class PostModel {
    public String postDescription;
    public String postUserName;
    public String postDateTime;
    public String postUserID;
    public String postID;

    public PostModel(){
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }

    public String getPostUserID() {
        return postUserID;
    }

    public void setPostUserID(String postUserID) {
        this.postUserID = postUserID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postDescription", postDescription);
        result.put("postUserName", postUserName);
        result.put("postDateTime",postDateTime);
        result.put("postUserID",postUserID);
        result.put("postID",postID);
        return result;
    }
}
