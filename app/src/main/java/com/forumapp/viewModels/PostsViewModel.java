package com.forumapp.viewModels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.models.PostModel;
import com.forumapp.models.TopicModel;
import com.forumapp.utils.SendNotificationService;
import com.forumapp.views.ChatListAdapter;
import com.forumapp.views.PostsActivity;
import com.forumapp.views.TopicListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by YounasBangash on 12/29/2017.
 */

public class PostsViewModel extends BaseObservable {
    private DatabaseReference databaseReferencePosts = null;
    private DatabaseReference databaseReferenceTopics = null;
    private TopicModel topicModel = null;
    private PostsActivity postsActivity = null;
    public String postMessage = null;
    private ChatListAdapter mChatListAdapter = null;
    private RecyclerView postsList = null;


    public PostsViewModel(TopicModel topicModel, PostsActivity postsActivity,RecyclerView postsList) {
        this.topicModel = topicModel;
        this.postsActivity = postsActivity;
        this.postsList  = postsList;
        databaseReferencePosts = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(topicModel.getTopicID());
        databaseReferenceTopics = FirebaseDatabase.getInstance().getReference()
                .child("topics").child(topicModel.getTopicID());

        getPostsList();
    }

    public void onFabBtnClick() {
        if (TextUtils.isEmpty(postMessage) || postMessage == null) {
            Toast.makeText(postsActivity,
                    postsActivity.getString(R.string.invalid_post_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            addPost();
        }
    }

    public void getPostsList(){
        mChatListAdapter = new ChatListAdapter(postsActivity,databaseReferencePosts,postsList);
        mChatListAdapter.setRecylerView();
    }

    private void addPost() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate3 = df3.format(calendar.getTime());
        String postID = databaseReferencePosts.child(topicModel.getTopicID()).push().getKey();
        PostModel postModel = new PostModel();
        postModel.setPostDateTime(formattedDate3);
        postModel.setPostDescription(getPostMessage());
        postModel.setPostID(postID);
        postModel.setPostUserID(FirebaseAuth.getInstance().getUid());
        postModel.setPostUserName("Muhammad Younas");

        databaseReferencePosts.child(postID).setValue(postModel.toMap())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && task.isComplete()) {
                    incrementComments();
                }
            }
        });
    }

    private void incrementComments() {
        int totalComments = Integer.parseInt(topicModel.getTotalComments()) + 1;
        databaseReferenceTopics.child("totalComments").setValue("" + totalComments)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete() && task.isSuccessful()){
                            sendNotificationToUser();
                        }
                    }
                });
    }

    private void sendNotificationToUser() {

//        Intent intent = new Intent(postsActivity, SendNotificationService.class);
//        intent.putExtra(SendNotificationService.MESSAGE, getPostMessage());
//        intent.putExtra(SendNotificationService.TITLE, postsActivity.getString(
//                R.string.new_post_title));
//        intent.putExtra(SendNotificationService.SENDER_ID, topicModel.get);
//        postsActivity.startService(intent);
    }

    @Bindable
    private String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }
}
