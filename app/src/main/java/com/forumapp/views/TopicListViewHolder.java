package com.forumapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forumapp.R;
import com.forumapp.models.TopicModel;

/**
 * Created by YounasBangash on 12/29/2017.
 */

public class TopicListViewHolder extends RecyclerView.ViewHolder {
    public TextView chatTitle;
    public TextView chatBy;
    public TextView totalComments;
    public TextView chatDescp;
    public TextView dateTime;
    public TopicModel mItem;
    public final View mView;

    public TopicListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        chatTitle = itemView.findViewById(R.id.chatTitle);
        chatDescp = itemView.findViewById(R.id.chatDescp);
        chatBy = itemView.findViewById(R.id.chatby);
        totalComments = itemView.findViewById(R.id.totalComment);
        dateTime = itemView.findViewById(R.id.dateTime);
    }
}
