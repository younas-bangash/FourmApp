package com.forumapp.views;

/**
 * Created by Developer on 12/28/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.forumapp.R;
import com.forumapp.models.PostModel;
import com.forumapp.models.TopicModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * @author greg
 * @since 6/21/13
 * <p>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter implements ValueEventListener {
    private List<PostModel> topicList = new ArrayList<>();
    private FirebaseListAdapter<PostModel, ChatListViewHolder> mAdapter;
    private Activity callingActivity = null;
    private Query eventsQuery = null;
    private RecyclerView list = null;

    public ChatListAdapter(Activity callingActivity, Query eventsQuery, RecyclerView list) {
        this.callingActivity = callingActivity;
        this.eventsQuery = eventsQuery;
        this.list = list;
    }

    public void setRecylerView() {
        eventsQuery.addValueEventListener(this);
        mAdapter = new FirebaseListAdapter<PostModel, ChatListViewHolder>(
                PostModel.class,
                R.layout.post_view,
                ChatListViewHolder.class,
                eventsQuery,
                10,
                callingActivity.getApplicationContext()) {

            @Override
            protected void populateViewHolder(final ChatListViewHolder viewHolder,
                                              final PostModel model, int position) {
                if (model != null) {
                    viewHolder.mItem = model;
                    topicList.add(model);
                    viewHolder.postTitle.setText(model.getPostDescription());
                    viewHolder.postBy.setText(model.getPostUserName());
                    viewHolder.dateTime.setText(model.getPostDateTime());
                }
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(callingActivity);
        list.setLayoutManager(linearLayoutManager);
        RecyclerViewScrollListener recylerViewScrolllistner
                = new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mAdapter.more();
            }
        };
        list.setAdapter(mAdapter);
        list.addOnScrollListener(recylerViewScrolllistner);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}