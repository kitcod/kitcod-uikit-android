package com.kitcod.android.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.kitcod.android.model.Post;

import java.util.List;

public class FeedDiffCallback extends DiffUtil.Callback {
    List<Post> oldPostList;
    List<Post> newPostList;

    FeedDiffCallback(@NonNull List<Post> oldPostList, @NonNull List<Post> newPostList) {
        this.oldPostList = oldPostList;
        this.newPostList = newPostList;
    }

    @Override
    public int getOldListSize() {
        return oldPostList.size();
    }

    @Override
    public int getNewListSize() {
        return newPostList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPostList.get(oldItemPosition).id.
                equalsIgnoreCase(newPostList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldPostList.get(oldItemPosition).id != newPostList.get(newItemPosition).id) {
            return false;
        } else if (oldPostList.get(oldItemPosition).title != newPostList.get(newItemPosition).title) {
            return false;
        } else if (oldPostList.get(oldItemPosition).contentHTML != newPostList.get(newItemPosition).contentHTML) {
            return false;
        } else if (oldPostList.get(oldItemPosition).media.auds != null && newPostList.get(newItemPosition).media.auds != null) {
            if (oldPostList.get(oldItemPosition).media.auds.get(0).mediaUrl != newPostList.get(newItemPosition).media.auds.get(0).mediaUrl) {
                return false;
            } else if (oldPostList.get(oldItemPosition).isPlaying != newPostList.get(newItemPosition).isPlaying) {
                return false;
            }
        } else {
            return true;
        }  /*else if (oldPostList.get(oldItemPosition).interactionSummary != newPostList.get(newItemPosition).interactionSummary) {
            return false;
        } else if (oldPostList.get(oldItemPosition).interactionSummary.interactionCounts != newPostList.get(newItemPosition).interactionSummary.interactionCounts) {
            return false;
        }*/
        return true;
    }
}
