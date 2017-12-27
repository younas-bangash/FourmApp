package com.forumapp.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class TopicModel {
    private String topicTitle;
    private String topicDescription;
    private String topicUserName;
    private String topicDateTime;
    private String topicUserID;
    private String topicID;

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public void setTopicUserName(String topicUserName) {
        this.topicUserName = topicUserName;
    }

    public void setTopicDateTime(String topicDateTime) {
        this.topicDateTime = topicDateTime;
    }

    public void setTopicUserID(String topicUserID) {
        this.topicUserID = topicUserID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("topicTitle", topicTitle);
        result.put("topicDescription", topicDescription);
        result.put("topicUserName",topicUserName);
        result.put("topicDateTime",topicDateTime);
        result.put("topicUserID",topicUserID);
        result.put("topicID",topicID);
        return result;
    }
}
