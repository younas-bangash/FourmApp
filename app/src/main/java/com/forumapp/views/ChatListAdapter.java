package com.forumapp.views;

/**
 * Created by Developer on 12/28/2017.
 */

import android.app.Activity;
import android.view.View;

import com.forumapp.models.TopicModel;
import com.google.firebase.database.Query;


/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<TopicModel> {
    public ChatListAdapter(Query ref, Activity activity, int layout) {
        super(ref, TopicModel.class, layout, activity);

    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param topicModel An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, TopicModel topicModel) {
        // Map a Chat object to an entry in our listview

       /* String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());*/
    }
}