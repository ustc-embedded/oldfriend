/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oldfriend.group;

import com.oldfriend.group.GroupBrowseListAdapter.GroupListItemViewCache;
import com.oldfriend.ui.R;
import com.oldfriend.widget.AutoScrollListView;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

/**
 * Fragment to display the list of groups.
 */
public class GroupBrowseListFragment extends Fragment
        implements OnFocusChangeListener, OnTouchListener {

    /**
     * Action callbacks that can be sent by a group list.
     */
    public interface OnGroupBrowserActionListener  {

        /**
         * Opens the specified group for viewing.
         *
         * @param groupUri for the group that the user wishes to view.
         */
        void onViewGroupAction(Uri groupUri);

    }

    private static final String TAG = "GroupBrowseListFragment";
    private static final boolean DEBUG = false;

    private static final int LOADER_GROUPS = 1;

    private Context mContext;
    private Cursor mGroupListCursor;

    private boolean mSelectionToScreenRequested;

    private static final String EXTRA_KEY_GROUP_URI = "groups.groupUri";

    private View mRootView;
    private AutoScrollListView mListView;
    private LinearLayout mEmptyView;
    private View mCreateGroupButton;

    private GroupBrowseListAdapter mAdapter;
    private boolean mSelectionVisible;
    private Uri mSelectedGroupUri;

    private OnGroupBrowserActionListener mListener;

    public GroupBrowseListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedGroupUri = savedInstanceState.getParcelable(EXTRA_KEY_GROUP_URI);
            if (mSelectedGroupUri != null) {
                // The selection may be out of screen, if rotated from portrait to landscape,
                // so ensure it's visible.
                mSelectionToScreenRequested = true;
            }
        }

        mRootView = inflater.inflate(R.layout.group_browse_list_fragment, null);
        mEmptyView = (LinearLayout)mRootView.findViewById(R.id.no_groups);

        mAdapter = new GroupBrowseListAdapter(mContext);
        mAdapter.setSelectionVisible(mSelectionVisible);
        mAdapter.setSelectedGroup(mSelectedGroupUri);

        mListView = (AutoScrollListView) mRootView.findViewById(R.id.list);
        mListView.setOnFocusChangeListener(this);
        mListView.setOnTouchListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListItemViewCache groupListItem = (GroupListItemViewCache) view.getTag();
                if (groupListItem != null) {
                    viewGroup(groupListItem.getUri());
                }
            }
        });

        mListView.setEmptyView(mEmptyView);
        mCreateGroupButton = mRootView.findViewById(R.id.create_group);
        mCreateGroupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	// TODO start an activity to create a Group
            }
        });

        return mRootView;
    }
/*
    public void setVerticalScrollbarPosition(int position) {
        if (mVerticalScrollbarPosition != position) {
            mVerticalScrollbarPosition = position;
            configureVerticalScrollbar();
        }
    }

    private void configureVerticalScrollbar() {
        mListView.setVerticalScrollbarPosition(mVerticalScrollbarPosition);
        mListView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        int leftPadding = 0;
        int rightPadding = 0;
        if (mVerticalScrollbarPosition == View.SCROLLBAR_POSITION_LEFT) {
            leftPadding = mContext.getResources().getDimensionPixelOffset(
                    R.dimen.list_visible_scrollbar_padding);
        } else {
            rightPadding = mContext.getResources().getDimensionPixelOffset(
                    R.dimen.list_visible_scrollbar_padding);
        }
        mListView.setPadding(leftPadding, mListView.getPaddingTop(),
                rightPadding, mListView.getPaddingBottom());
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        try{
        	mListener = (OnGroupBrowserActionListener) activity;
        } catch (ClassCastException e){
        	throw new ClassCastException(activity.toString()
        			+ "must implement GroupBrowseListFragment.OnGroupBrouserActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onStart() {
        getLoaderManager().initLoader(LOADER_GROUPS, null, mGroupLoaderListener);
        super.onStart();
    }

    /**
     * The listener for the group meta data loader for all groups.
     */
    private final LoaderManager.LoaderCallbacks<Cursor> mGroupLoaderListener =
            new LoaderCallbacks<Cursor>() {

        @Override
        public CursorLoader onCreateLoader(int id, Bundle args) {
            mEmptyView.setVisibility(View.GONE);
            return new GroupListLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mGroupListCursor = data;
            bindGroupList();
        }

        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    private void bindGroupList() {
 //       setAddAccountsVisibility(!ContactsUtils.areGroupWritableAccountsAvailable(mContext));
        if (mGroupListCursor == null) {
        	mEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        mAdapter.setCursor(mGroupListCursor);

        if (mSelectionToScreenRequested) {
            mSelectionToScreenRequested = false;
            requestSelectionToScreen();
        }

        mSelectedGroupUri = mAdapter.getSelectedGroup();
        if (mSelectionVisible && mSelectedGroupUri != null) {
            viewGroup(mSelectedGroupUri);
        }
    }

    public void setListener(OnGroupBrowserActionListener listener) {
        mListener = listener;
    }

    public void setSelectionVisible(boolean flag) {
        mSelectionVisible = flag;
        if (mAdapter != null) {
            mAdapter.setSelectionVisible(mSelectionVisible);
        }
    }

    private void setSelectedGroup(Uri groupUri) {
        mSelectedGroupUri = groupUri;
        mAdapter.setSelectedGroup(groupUri);
        mListView.invalidateViews();
    }

    private void viewGroup(Uri groupUri) {
        setSelectedGroup(groupUri);
        if (mListener != null) mListener.onViewGroupAction(groupUri);
    }

    public void setSelectedUri(Uri groupUri) {
        viewGroup(groupUri);
        mSelectionToScreenRequested = true;
    }

    protected void requestSelectionToScreen() {
        if (!mSelectionVisible) {
            return; // If selection isn't visible we don't care.
        }
        int selectedPosition = mAdapter.getSelectedGroupPosition();
        if (selectedPosition != -1) {
            mListView.requestPositionToScreen(selectedPosition,
                    true /* smooth scroll requested */);
        }
    }

    private void hideSoftKeyboard() {
        if (mContext == null) {
            return;
        }
        // Hide soft keyboard, if visible
        InputMethodManager inputMethodManager = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mListView.getWindowToken(), 0);
    }

    /**
     * Dismisses the soft keyboard when the list takes focus.
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view == mListView && hasFocus) {
            hideSoftKeyboard();
        }
    }

    /**
     * Dismisses the soft keyboard when the list is touched.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == mListView) {
            hideSoftKeyboard();
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_KEY_GROUP_URI, mSelectedGroupUri);
    }
}