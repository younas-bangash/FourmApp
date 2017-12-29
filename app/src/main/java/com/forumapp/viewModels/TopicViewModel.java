package com.forumapp.viewModels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.DataSetObserver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.databinding.AddTopicDialogBinding;
import com.forumapp.models.TopicModel;
import com.forumapp.views.ChatListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class TopicViewModel extends BaseObservable {
    public String topic = null;
    private ChatListAdapter mChatListAdapter = null;
    public String topicDescription = null;
    private Activity callingActivity = null;
    private AlertDialog.Builder addTopicDialog = null;
    private AlertDialog addTopicDialogShow = null;
    private TopicModel topicModel = null;
    private DatabaseReference databaseReferenceTopics = null;
    private FirebaseAuth firebaseAuth = null;
    private RecyclerView topicsList = null;



    public TopicViewModel(Activity context, RecyclerView topicsList){
        this.callingActivity = context;
        topicModel = new TopicModel();
        firebaseAuth = FirebaseAuth.getInstance();
        this.topicsList = topicsList;
        databaseReferenceTopics = FirebaseDatabase.getInstance().getReference()
                .child("topics");

        getTopicList();
    }
    
    public void getTopicList(){
        mChatListAdapter = new ChatListAdapter(callingActivity,databaseReferenceTopics,topicsList);
        mChatListAdapter.setRecylerView();
    }

    public void addTopicBtnClick(){
        if(!validateTopicTitle())
            return;
        if(!validateTopicDescpiption())
            return;
        addTopic();
    }

    public void btnCancel(){
        addTopicDialogShow.dismiss();
    }

    public void onFabBtnClick(){
        AddTopicDialogBinding addTopicDialogBinding =
                DataBindingUtil.inflate(LayoutInflater.from(callingActivity),
                        R.layout.add_topic_dialog, null, false);
        addTopicDialogBinding.setTopicViewModel(this);
        addTopicDialog = new AlertDialog.Builder(callingActivity);
        addTopicDialog.setView(addTopicDialogBinding.getRoot());
        addTopicDialog.setCancelable(false);
        addTopicDialogShow = addTopicDialog.show();
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

    public void setTopicDescription(String description){
        this.topicDescription = description;
    }

    private void addTopic() {
        String topicID = databaseReferenceTopics.push().getKey();
        topicModel.setTopicID(topicID);
        topicModel.setTopicTitle(getTopic());
        topicModel.setTopicDescription(getTopicDescription());
        //noinspection ConstantConditions
        topicModel.setTopicUserID(firebaseAuth.getCurrentUser().getUid());
        topicModel.setTotalComments("0");
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate3 = df3.format(calendar.getTime());
        topicModel.setTopicDateTime(formattedDate3);
        topicModel.setTopicUserName("Muhammad Younas");

        databaseReferenceTopics.child(topicID).setValue(topicModel.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete() && task.isSuccessful()){
//                            FirebaseMessaging.getInstance().subscribeToTopic(getTopic());
                            Toast.makeText(callingActivity, callingActivity.getString(R.string.topic_add_success),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(callingActivity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateTopicTitle(){
        if(getTopic().isEmpty() || getTopic() == null){
            Toast.makeText(callingActivity, callingActivity.getString(R.string.req_topic_title),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateTopicDescpiption(){
        if(getTopicDescription().isEmpty() || getTopicDescription() == null){
            Toast.makeText(callingActivity, callingActivity.getString(R.string.req_topic_descp),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Bindable
    public String getTopic(){
        return topic;
    }
    @Bindable
    public String getTopicDescription(){
        return topicDescription;
    }
}
