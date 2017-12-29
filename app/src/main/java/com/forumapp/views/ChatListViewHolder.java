package com.forumapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forumapp.R;
import com.forumapp.models.PostModel;
import com.forumapp.models.TopicModel;

/**
 * Created by YounasBangash on 12/29/2017.
 */

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    public TextView postTitle;
    public TextView postBy;;
    public TextView dateTime;
    public PostModel mItem;
    public final View mView;

    public ChatListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        postTitle = itemView.findViewById(R.id.postDescp);
        postBy = itemView.findViewById(R.id.chatby);
        dateTime = itemView.findViewById(R.id.dateTime);
    }
}
