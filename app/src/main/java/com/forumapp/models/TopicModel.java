package com.forumapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class TopicModel implements Parcelable {
    public String totalComments = "0";
    public String topicDescription;
    public String topicUserName;
    public String topicDateTime;
    public String topicUserID;
    public String topicID;
    public String topicTitle;

    public TopicModel() {
    }

    protected TopicModel(Parcel in) {
        topicTitle = in.readString();
        totalComments = in.readString();
        topicDescription = in.readString();
        topicUserName = in.readString();
        topicDateTime = in.readString();
        topicUserID = in.readString();
        topicID = in.readString();
    }

    public static final Creator<TopicModel> CREATOR = new Creator<TopicModel>() {
        @Override
        public TopicModel createFromParcel(Parcel in) {
            return new TopicModel(in);
        }

        @Override
        public TopicModel[] newArray(int size) {
            return new TopicModel[size];
        }
    };

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public String getTopicUserName() {
        return topicUserName;
    }

    public String getTopicDateTime() {
        return topicDateTime;
    }

    public String getTopicUserID() {
        return topicUserID;
    }

    public String getTopicID() {
        return topicID;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

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
        result.put("totalComments",totalComments);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(topicTitle);
        parcel.writeString(totalComments);
        parcel.writeString(topicDescription);
        parcel.writeString(topicUserName);
        parcel.writeString(topicDateTime);
        parcel.writeString(topicUserID);
        parcel.writeString(topicID);
    }
}
