package com.kitcod.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kitcod.android.R;
import com.kitcod.android.interfaces.OnItemClickListener;
import com.kitcod.android.model.KcGroup;
import com.kitcod.android.utils.Util;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GroupListFeedAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<KcGroup> getGroupList;
    private OnItemClickListener<KcGroup> listener;
    OnItemClickListener createGroupListener;

    public GroupListFeedAdapter(Activity context, List<KcGroup> getGroupList) {
        this.context = context;
        this.getGroupList = getGroupList;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<KcGroup> listener) {
        this.listener = listener;
    }

    public void setOnCreateGroupListener(@Nullable OnItemClickListener createGroupListener) {
        this.createGroupListener = createGroupListener;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kc_grouplist_feed_preview, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewholder = (MyViewHolder) holder;
        if (Util.validateString(getGroupList.get(position).name)) {
            viewholder.tv_groupname.setText(getGroupList.get(position).name);
        }
        if (position == 0) {
            Picasso.get()
                    .load(R.drawable.plus_icon)
                    .into(viewholder.iv_community);
        } else {
            if (getGroupList.get(position).media.proImg != null) {
                if (getGroupList.get(position).media.proImg.resizedMediaList != null && getGroupList.get(position).media.proImg.resizedMediaList.size() > 0) {
                    for (int i = 0; i < getGroupList.get(position).media.proImg.resizedMediaList.size(); i++) {
                        if (getGroupList.get(position).media.proImg.resizedMediaList.get(i).type.equalsIgnoreCase("resized")) {
                            final String img = getGroupList.get(position).media.proImg.resizedMediaList.get(i).mediaUrl;
                            Picasso.get()
                                    .load(img)
                                    .into(viewholder.iv_community);

                        }
                    }
                }
            } else {
                //TODO - Mehdi Add default profile pic
            }
        }
        viewholder.cv_layout.setOnClickListener(view -> {
            if (position == 0) {
                createGroupListener.onItemClick(view, holder.getAdapterPosition(), getGroupList.get(position));
            } else {
                listener.onItemClick(view, holder.getAdapterPosition(), getGroupList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return getGroupList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_groupname;
        ImageView iv_community;
        LinearLayout cv_layout;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_groupname = itemView.findViewById(R.id.tv_groupname);
            iv_community = itemView.findViewById(R.id.iv_community);
            cv_layout = itemView.findViewById(R.id.cv_layout);
        }
    }

}
