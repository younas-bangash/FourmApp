package com.forumapp.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.forumapp.R;
import com.forumapp.databinding.ActivityPostsBinding;
import com.forumapp.models.TopicModel;
import com.forumapp.viewModels.PostsViewModel;

public class PostsActivity extends AppCompatActivity {
    private ActivityPostsBinding activityPostsBinding = null;
    private PostsViewModel postsViewModel = null;
    private TopicModel topicModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if(data != null){
            topicModel = data.getParcelable("topicModel");
        }

        initDataBinding();
    }

    private void initDataBinding() {
        activityPostsBinding = DataBindingUtil.setContentView(this,R.layout.activity_posts);
        postsViewModel = new PostsViewModel(topicModel,this,activityPostsBinding.topicPosList);
        activityPostsBinding.setPostsViewModel(postsViewModel);
        activityPostsBinding.setTopicModel(topicModel);
    }
}
