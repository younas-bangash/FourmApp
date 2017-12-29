package com.forumapp.views;

/**
 * Created by Developer on 12/28/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.forumapp.views.InfiniteFirebaseArray.ADDED;
import static com.forumapp.views.InfiniteFirebaseArray.CHANGED;
import static com.forumapp.views.InfiniteFirebaseArray.REMOVED;

/**
 * @param <T> The class type to use as a model for the data contained in the children of the given Firebase location
 * @author greg
 * @since 6/21/13
 * <p>
 * This class is a generic way of backing an Android ListView with a Firebase location.
 * It handles all of the child events at the given Firebase location. It marshals received data into the given
 * class type. Extend this class and provide an implementation of <code>populateView</code>, which will be given an
 * instance of your list item mLayout and an instance your class that holds your data. Simply populate the view however
 * you like and this class will handle updating the list as the data changes.
 */
public abstract class FirebaseListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = FirebaseListAdapter.class.getSimpleName();

    protected int mModelLayout;
    private Class<T> mModelClass;
    public Class<VH> mViewHolderClass;
    private InfiniteFirebaseArray mSnapshots;
    private Query mQuery;

    private FirebaseListAdapter(Class<T> modelClass, @LayoutRes int modelLayout,
                                Class<VH> viewHolderClass, InfiniteFirebaseArray snapshots) {
        mModelClass = modelClass;
        mModelLayout = modelLayout;
        mViewHolderClass = viewHolderClass;
        mSnapshots = snapshots;
        mSnapshots.setOnChangedListener(new InfiniteFirebaseArray.OnChangedListener() {

            @Override
            public void onChanged(@InfiniteFirebaseArray.EventType int type, int index, int oldIndex) {
                Log.d(TAG, "onChanged() called with: type = [" + type + "], index = " +
                        "[" + index + "], oldIndex = [" + oldIndex + "]");
                switch (type) {
                    case ADDED:
                        notifyItemInserted(index);
                        break;
                    case CHANGED:
                        notifyItemChanged(index);
                        break;
                    case REMOVED:
                        notifyItemRemoved(index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseListAdapter.this.onCancelled(databaseError);
            }
        });
    }

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseListAdapter(Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass,
                                           Query ref, int itemsPerPage ,Context mContext) {
        this(modelClass, modelLayout, viewHolderClass, new InfiniteFirebaseArray(ref, itemsPerPage, mContext));
        mQuery = ref;
        Context mContext1 = mContext;
    }

    public void cleanup() {
        mSnapshots.cleanup();
    }

    public void more() {
        if (mSnapshots != null) {
            mSnapshots.more(mQuery);
        }
    }

    @Override
    public int getItemCount() {
        return mSnapshots.getCount();
    }
    public T getItem(int position) {
        return parseSnapshot(mSnapshots.getItem(position));
    }

    /**
     * This method parses the DataSnapshot into the requested type. You can override it in subclasses
     * to do custom parsing.
     *
     * @param snapshot the DataSnapshot to extract the model from
     * @return the model extracted from the DataSnapshot
     */
    protected T parseSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(mModelClass);
    }

    public DatabaseReference getRef(int position) {
        return mSnapshots.getItem(position).getRef();
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mSnapshots.getItem(position).getKey().hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        try {
            Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException | InvocationTargetException |
                InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        T model = getItem(position);
        populateViewHolder(viewHolder, model, position);
    }


    @Override
    public int getItemViewType(int position) {
        return mModelLayout;
    }

    /**
     * This method will be triggered in the event that this listener either failed at the server,
     * or is removed as a result of the security and Firebase Database rules.
     *
     * @param error A description of the error that occurred
     */
    protected void onCancelled(DatabaseError error) {
        Log.w(TAG, error.toException());
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The first two arguments correspond to the mLayout and mModelClass given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param viewHolder The view to populate
     * @param model      The object containing the data used to populate the view
     * @param position   The position in the list of the view being populated
     */
    abstract protected void populateViewHolder(VH viewHolder, T model, int position);
}