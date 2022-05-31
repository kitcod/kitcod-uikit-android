package com.kitcod.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kitcod.android.R;
import com.kitcod.android.model.CommentModel;
import com.kitcod.android.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class CommentsAdapter extends RecyclerView.Adapter {
    Context context;
    List<CommentModel> commentLists;
    View.OnClickListener onReplyClickListener;
    View.OnLongClickListener onLongClickListener;

    public CommentsAdapter(Context context, List<CommentModel> commentLists, View.OnClickListener onReplyClickListener, View.OnLongClickListener onLongClickListener) {
        this.context = context;
        this.commentLists = commentLists;
        this.onReplyClickListener = onReplyClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    public CommentsAdapter(Context context, List<CommentModel> commentLists) {
        this.context = context;
        this.commentLists = commentLists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kc_comments_preview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewholder = (MyViewHolder) holder;
        viewholder.layout_edit.setTag(null);
        viewholder.layout_edit.setTag(R.id.mainCommentpostion, null);
        viewholder.layout_edit.setTag(R.id.childCommentposition, null);
     /*   GetUserBulkRealm getUserBulkRealm = Realm.getDefaultInstance().where(GetUserBulkRealm.class).equalTo("id", commentLists.get(position).userId).findFirst();
      if(getUserBulkRealm!=null) {
          viewholder.tv_name.setText(getUserBulkRealm.getBody().getFirstName());
          if (getUserBulkRealm.getBody().getMedia() != null && getUserBulkRealm.getBody().getMedia().getProfilePic() != null && getUserBulkRealm.getBody().getMedia().getProfilePic().getResizedMediaList() != null && getUserBulkRealm.getBody().getMedia().getProfilePic().getResizedMediaList().size() > 0) {
              for (int j = 0; j < getUserBulkRealm.getBody().getMedia().getProfilePic().getResizedMediaList().size(); j++) {
                  if (getUserBulkRealm.getBody().getMedia().getProfilePic().getResizedMediaList().get(j).getType().equalsIgnoreCase("thumbnail")) {
                      Picasso.get()
                              .load(getUserBulkRealm.getBody().getMedia().getProfilePic().getResizedMediaList().get(j).getMediaUrl())
                              .into(viewholder.iv_userpro);
                  }
              }
          } else {
              if (getUserBulkRealm.getBody().getGender() == 1) {
                  Picasso.get()
                          .load(R.mipmap.male_default)
                          .into(viewholder.iv_userpro);

              } else {
                  Picasso.get()
                          .load(R.mipmap.female_default)
                          .into(viewholder.iv_userpro);

              }
          }
      }*/
        viewholder.tv_comment.setText(commentLists.get(position).html);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            if (Util.validateString(commentLists.get(position).createdTS)) {

                Date createDate = simpleDateFormat.parse(commentLists.get(position).createdTS.replace("+0000", "Z"));
                Date date = new Date();
                String stringDate = simpleDateFormat.format(date);
                Date currentDate = simpleDateFormat.parse(stringDate);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat localDateFormat;
                if (!android.text.format.DateFormat.is24HourFormat(context)) {
                    localDateFormat = new SimpleDateFormat("hh:mm a");
                } else {
                    localDateFormat = new SimpleDateFormat("HH:mm");
                }

//                Date date = simpleDateFormat.parse(deenPosts.get(position).publishedTS);
                viewholder.tv_timepost.setVisibility(View.VISIBLE);
                long diff = currentDate.getTime() - createDate.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);
                int diffInDays = (int) ((currentDate.getTime() - createDate.getTime()) / (1000 * 60 * 60 * 24));
//                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
//                SimpleDateFormat localDateFormat;
//                if (!android.text.format.DateFormat.is24HourFormat(context)) {
//                    localDateFormat = new SimpleDateFormat("hh:mm a");
//                } else {
//                    localDateFormat = new SimpleDateFormat("HH:mm");
//                }
                if (diffInDays > 1) {

//                System.err.println("Difference in number of days (2) : " + diffInDays);
                    viewholder.tv_timepost.setText(simpleDateFormat1.format(createDate) + " at " + localDateFormat.format(createDate));


                } else if (diffHours > 23 && diffHours < 48) {
                    viewholder.tv_timepost.setText("Yesterday at " + localDateFormat.format(createDate));
//                System.err.println(">24");
//                return false;
                } else if ((diffHours < 1)) {
                    if (diffMinutes < 2) {
                        viewholder.tv_timepost.setText("A moment ago");
                    } else {
                        viewholder.tv_timepost.setText(diffMinutes + " minutes " + "ago");
                    }
//                System.err.println("minutes");
                } else if ((diffHours < 24)) {
                    if (diffHours == 1) {
                        viewholder.tv_timepost.setText(diffHours + " hour " + "ago");
                    } else {
                        viewholder.tv_timepost.setText(diffHours + " hours " + "ago");
                    }
//                System.err.println("minutes");
                }
//                viewholder.time_post.setText(simpleDateFormat1.format(localDate) + " at " + localDateFormat.format(localDate));
            } else {
                viewholder.tv_timepost.setVisibility(View.GONE);
            }

            if (commentLists.get(position).id != null) {
                viewholder.tv_reply.setText("Reply");
            } else {
                viewholder.tv_reply.setText("Posting..");
            }
/*            if (commentLists.get(position).childCommentLists != null && commentLists.get(position).childCommentLists.size() > 0) {
                viewholder.tv_view_all_reply.setVisibility(View.VISIBLE);
                if (commentLists.get(position).isVisible)
                    viewholder.rv_child_comment.setVisibility(View.VISIBLE);
                else
                    viewholder.rv_child_comment.setVisibility(View.GONE);
//                viewholder.rv_child_comment.setVisibility(View.VISIBLE);
                ChildCommentAdapter childCommentAdapter = new ChildCommentAdapter(context, commentLists.get(position).childCommentLists, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewholder.tv_reply.setTag(position);
                        viewholder.tv_reply.performClick();
                        viewholder.rv_child_comment.setVisibility(View.VISIBLE);

                    }
                }, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String childPosition = (String) v.getTag();
                        viewholder.layout_edit.setTag(R.id.mainCommentpostion, position);
                        viewholder.layout_edit.setTag(R.id.childCommentposition, childPosition);
                        viewholder.layout_edit.performLongClick();

                        return false;
                    }
                });
                viewholder.rv_child_comment.setAdapter(childCommentAdapter);
            } else {
                viewholder.tv_view_all_reply.setVisibility(View.GONE);
                viewholder.rv_child_comment.setVisibility(View.GONE);
            }*/

            viewholder.tv_view_all_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewholder.rv_child_comment.getVisibility() == View.VISIBLE) {
                        viewholder.rv_child_comment.setVisibility(View.GONE);
                        viewholder.tv_view_all_reply.setText("View replies");
                    } else {
                        viewholder.rv_child_comment.setVisibility(View.VISIBLE);
                        viewholder.tv_view_all_reply.setText("Hide replies");
                    }
                }
            });

            viewholder.tv_reply.setOnClickListener(onReplyClickListener);
            viewholder.tv_reply.setTag(position);

            viewholder.layout_edit.setOnLongClickListener(onLongClickListener);
            viewholder.layout_edit.setTag(position);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv_name, tv_comment, tv_timepost, tv_reply, tv_view_all_reply;
        ImageView iv_userpro;
        RecyclerView rv_child_comment;
        RelativeLayout layout_edit;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_view_all_reply = itemView.findViewById(R.id.tv_view_all_reply);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_timepost = (TextView) itemView.findViewById(R.id.tv_timepost);
            tv_reply = itemView.findViewById(R.id.tv_reply);
            layout_edit = itemView.findViewById(R.id.layout_edit);

            iv_userpro = itemView.findViewById(R.id.iv_userpro);
            rv_child_comment = itemView.findViewById(R.id.rv_child_comment);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rv_child_comment.setLayoutManager(linearLayoutManager);
//            checkBox1 = itemView.findViewById(R.id.checkBox1);
//            checkBox1.setVisibility(View.GONE);
//            facility_icon = itemView.findViewById(R.id.facility_icon);
//            facility_icon.setVisibility(View.VISIBLE);
//            name = (TextView) itemView.findViewById(R.id.name);
//            name.setTypeface(CollabDeenApplication.mRegularFont);
//            llLayout = (LinearLayout) itemView.findViewById(R.id.llLayout);

        }

    }

}
