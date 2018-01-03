package com.forumapp.viewModels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.models.PostModel;
import com.forumapp.models.TopicModel;
import com.forumapp.utils.SendNotificationService;
import com.forumapp.views.ChatListAdapter;
import com.forumapp.views.PostsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by YounasBangash on 12/29/2017.
 */

public class PostsViewModel extends BaseObservable {
    private DatabaseReference databaseReferencePosts = null;
    private DatabaseReference databaseReferenceTopics = null;
    private DatabaseReference databaseReferenceUsers = null;
    private TopicModel topicModel = null;
    private PostsActivity postsActivity = null;
    public String postMessage = null;
    private Dialog dialog = null;
    private int totalComments = 0;
    private RecyclerView postsList = null;


    public PostsViewModel(TopicModel topicModel, PostsActivity postsActivity,RecyclerView postsList) {
        this.topicModel = topicModel;
        this.postsActivity = postsActivity;
        this.postsList  = postsList;
        this.totalComments = Integer.parseInt(topicModel.getTotalComments());
        databaseReferencePosts = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(topicModel.getTopicID());
        databaseReferenceTopics = FirebaseDatabase.getInstance().getReference()
                .child("topics").child(topicModel.getTopicID());
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference()
                .child("users").child(topicModel.getTopicUserID());

        getPostsList();
    }

    public void onFabBtnClick() {
        if (TextUtils.isEmpty(postMessage) || postMessage == null) {
            Toast.makeText(postsActivity,
                    postsActivity.getString(R.string.invalid_post_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            hideSoftKeyboard();
            showAnimatedProgressDialog().show();
            addPost();
        }
    }

    private Dialog showAnimatedProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(postsActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.progress_window);
            //noinspection ConstantConditions
            dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_window_color);

        }
        dialog.setOnDismissListener(dialog -> {
        });
        dialog.setOnShowListener(dialog -> {
        });
        return dialog;
    }

    private  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) postsActivity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            //noinspection ConstantConditions
            inputMethodManager.hideSoftInputFromWindow(
                    postsActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void getPostsList(){
        ChatListAdapter mChatListAdapter = new ChatListAdapter(postsActivity, databaseReferencePosts, postsList);
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
                .addOnFailureListener(e -> {

                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete()) {
                        incrementComments();
                    }
                });
    }

    private void incrementComments() {
        totalComments = totalComments + 1;
        databaseReferenceTopics.child("totalComments").setValue("" + totalComments)
                .addOnCompleteListener(task -> {
                    if(task.isComplete() && task.isSuccessful()){
                        sendNotificationToUser();
                    }
                });
    }

    private void sendNotificationToUser() {
        // First get the user token id of the post

        databaseReferenceUsers.child("userTokenID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "onDataChange() called with: dataSnapshot = [" +
                        dataSnapshot.getValue() + "]");
                showAnimatedProgressDialog().dismiss();
                Intent intent = new Intent(postsActivity, SendNotificationService.class);
                intent.putExtra(SendNotificationService.MESSAGE, getPostMessage());
                intent.putExtra(SendNotificationService.TITLE, postsActivity.getString(
                        R.string.new_post_title));
                intent.putExtra(SendNotificationService.SENDER_ID,
                        dataSnapshot.getValue().toString());
                postsActivity.startService(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showAnimatedProgressDialog().dismiss();
            }
        });
    }

    @Bindable
    private String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }
}
