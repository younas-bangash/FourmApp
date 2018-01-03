package com.forumapp.viewModels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.databinding.AddTopicDialogBinding;
import com.forumapp.models.TopicModel;
import com.forumapp.utils.PrefManager;
import com.forumapp.views.TopicListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class TopicViewModel extends BaseObservable {
    public String topic = null;
    private Dialog dialog = null;
    private TopicListAdapter mChatListAdapter = null;
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

    private Dialog showAnimatedProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(callingActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
    
    public void getTopicList(){
//        showAnimatedProgressDialog().show();
        mChatListAdapter = new TopicListAdapter(callingActivity,databaseReferenceTopics,topicsList);
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
        PrefManager prefManager = new PrefManager(callingActivity);
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
        topicModel.setTopicUserName(prefManager.getName());

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
