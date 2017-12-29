package com.forumapp.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.forumapp.R;
import com.forumapp.databinding.ActivityTopicBinding;
import com.forumapp.viewModels.SignUpViewModel;
import com.forumapp.viewModels.TopicViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class TopicsActivity extends AppCompatActivity {
    ActivityTopicBinding activityTopicBinding = null;
    TopicViewModel topicViewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.ic_logout){
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
        }
        return true;
    }

    private void initDataBinding() {
        activityTopicBinding = DataBindingUtil.setContentView(this,R.layout.activity_topic);
        topicViewModel = new TopicViewModel(TopicsActivity.this,activityTopicBinding.topicsList);
        activityTopicBinding.setTopicViewModel(topicViewModel);
    }
}
