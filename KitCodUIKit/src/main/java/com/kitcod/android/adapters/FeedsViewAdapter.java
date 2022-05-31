package com.kitcod.android.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcFeedsPreviewBinding;
import com.kitcod.android.interfaces.OnItemClickListener;
import com.kitcod.android.model.KcGroup;
import com.kitcod.android.model.Post;
import com.kitcod.android.utils.Util;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FeedsViewAdapter extends RecyclerView.Adapter {

    private KcFeedsPreviewBinding binding;
    private Activity context;
    private List<Post> getGroupPosts;
    private FeedListener feedListener;
    Handler handler;
    View.OnClickListener contentClickListener;
    private OnItemClickListener<Post> listener;
    private OnItemClickListener<KcGroup> feedGroupClickListener;
    private OnItemClickListener<String> userClickListener;

    public FeedsViewAdapter(Activity context, List<Post> getGroupPosts, FeedListener feedListener) {
        this.context = context;
        this.getGroupPosts = getGroupPosts;
        this.feedListener = feedListener;
    }

    public FeedsViewAdapter(Activity context, List<Post> getGroupPosts, FeedListener feedListener, View.OnClickListener contentClickListener) {
        this.context = context;
        this.getGroupPosts = getGroupPosts;
        this.feedListener = feedListener;
        this.contentClickListener = contentClickListener;
    }

    public void setOnUserClickListener(@Nullable OnItemClickListener<String> userClickListener) {
        this.userClickListener = userClickListener;
    }


    public void setOnItemClickListener(@Nullable OnItemClickListener<Post> listener) {
        this.listener = listener;
    }

    public void setOnFeedGroupClickListener(@Nullable OnItemClickListener<KcGroup> feedGroupClickListener) {
        this.feedGroupClickListener = feedGroupClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kc_feeds_preview, parent, false);
        binding = DataBindingUtil.bind(v);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewholder = (MyViewHolder) holder;
        if (Util.validateString(getGroupPosts.get(position).title)) {
            viewholder.tvTitle.setText(getGroupPosts.get(position).title);
        }
        if (Util.validateString(getGroupPosts.get(position).contentMarkdown)) {
            viewholder.tvContent.setText(getGroupPosts.get(position).contentMarkdown);
        } else {
            viewholder.tvContent.setText("");
        }

        if (getGroupPosts.get(position).group != null) {
            viewholder.tv_group.setText("#" + getGroupPosts.get(position).group.name);
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            if (Util.validateString(getGroupPosts.get(position).publishedTS)) {

                Date createDate = simpleDateFormat.parse(getGroupPosts.get(position).publishedTS.replace("+0000", "Z"));
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
                viewholder.tvTime.setVisibility(View.VISIBLE);
                long diff = currentDate.getTime() - createDate.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);
                int diffInDays = (int) ((currentDate.getTime() - createDate.getTime()) / (1000 * 60 * 60 * 24));
                if (diffInDays > 1) {
                    viewholder.tvTime.setText(simpleDateFormat1.format(createDate) + " at " + localDateFormat.format(createDate));
                } else if (diffHours > 24 && diffHours < 48) {
                    viewholder.tvTime.setText("Yesterday at " + localDateFormat.format(createDate));
                } else if ((diffHours < 1)) {
                    if (diffMinutes < 2) {
                        viewholder.tvTime.setText("A moment ago");
                    } else {
                        viewholder.tvTime.setText(diffMinutes + " minutes " + "ago");
                    }
                } else if ((diffHours < 24)) {
                    if (diffHours == 1) {
                        viewholder.tvTime.setText(diffHours + " hour " + "ago");
                    } else {
                        viewholder.tvTime.setText(diffHours + " hours " + "ago");
                    }
                }
            } else {
                viewholder.tvTime.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getGroupPosts.get(position).media.imgs != null && getGroupPosts.get(position).media.imgs.size() > 0) {
            viewholder.postImage.setVisibility(View.VISIBLE);
            if (getGroupPosts.get(position).media.imgs.get(0).resizedMediaList != null && getGroupPosts.get(position).media.imgs.get(0).resizedMediaList.size() > 0) {
                for (int i = 0; i < getGroupPosts.get(position).media.imgs.get(0).resizedMediaList.size(); i++) {
                    if (getGroupPosts.get(position).media.imgs.get(0).resizedMediaList.get(i).type.equalsIgnoreCase("resized")) {
                        final String img = getGroupPosts.get(position).media.imgs.get(0).resizedMediaList.get(i).mediaUrl;
                        Picasso.get()
                                .load(img)
                                .into(viewholder.postImage);

                    }
                }

            }

        } else {
            viewholder.postImage.setVisibility(View.GONE);
        }

        if (getGroupPosts.get(position).media != null && getGroupPosts.get(position).media.docs != null
                && getGroupPosts.get(position).media.docs.size() > 0) {
            for (int i = 0; i < getGroupPosts.get(position).media.docs.size(); i++) {
                if (getGroupPosts.get(position).media.docs.get(i).mediaUrl != null) {
                    String extension = getGroupPosts.get(position).media.docs.get(i).mediaUrl.substring(getGroupPosts.get(position).media.docs.get(i).mediaUrl.lastIndexOf("."));
                    if (extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".docx")) {
                        Picasso.get().load(R.drawable.doc_icon).into(viewholder.iv_img);
                    } else if (extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".xlsx")) {
                        Picasso.get().load(R.drawable.xls_icon).into(viewholder.iv_img);
                    } else if (extension.equalsIgnoreCase(".pdf")) {
                        Picasso.get().load(R.drawable.pdf_icon).into(viewholder.iv_img);
                    } else {
                        Picasso.get().load(R.drawable.otherfile_icon).into(viewholder.iv_img);
                    }
                    viewholder.layout_doc.setVisibility(View.VISIBLE);
                    viewholder.layout_doc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            feedListener.onDocClick(getGroupPosts.get(position).media.docs.get(0).mediaUrl);
                        }
                    });
                    viewholder.layout_doc.setTag(getGroupPosts.get(position).media.docs.get(i).mediaUrl);

                    viewholder.tv_name_doc.setText(URLUtil.guessFileName(getGroupPosts.get(position).media.docs.get(i).mediaUrl, null, null));
                    int finalI = i;
                    new FileSize(viewholder.tv_size_doc, getGroupPosts.get(position).media.docs.get(finalI).mediaUrl).execute();

                } else {
                    viewholder.layout_doc.setVisibility(View.GONE);
                }
            }
        } else {
            viewholder.layout_doc.setVisibility(View.GONE);
        }

        if (getGroupPosts.get(position).interactionSummary != null && getGroupPosts.get(position).interactionSummary.userInteractions != null && getGroupPosts.get(position).interactionSummary.userInteractions.size() > 0) {
            for (int i = 0; i < getGroupPosts.get(position).interactionSummary.userInteractions.size(); i++) {
                if (getGroupPosts.get(position).interactionSummary.userInteractions.get(i).interactionType.equalsIgnoreCase("LIKEOPTION1")) {
                    viewholder.iv_like.setImageResource(R.drawable.peace_icon);
                } else {
                    viewholder.iv_like.setImageResource(R.drawable.ic_like_icon);
                }
            }
        } else {
            viewholder.iv_like.setImageResource(R.drawable.ic_like_icon);
        }

        if (getGroupPosts.get(position).interactionSummary != null && getGroupPosts.get(position).interactionSummary.interactionCounts != null) {
            int likecount = getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption1 +
                    getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption2 +
                    getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption3 +
                    getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption4 +
                    getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption5 +
                    getGroupPosts.get(position).interactionSummary.interactionCounts.likeoption6;
            viewholder.tv_like.setText("" + likecount);
        }

        if (getGroupPosts.get(position).postType.equalsIgnoreCase("POLL")) {
            if (getGroupPosts.get(position).interactionSummary != null) {
                int voteCount = getGroupPosts.get(position).interactionSummary.interactionCounts.polloption1 +
                        getGroupPosts.get(position).interactionSummary.interactionCounts.polloption2 +
                        getGroupPosts.get(position).interactionSummary.interactionCounts.polloption3 +
                        getGroupPosts.get(position).interactionSummary.interactionCounts.polloption4 +
                        getGroupPosts.get(position).interactionSummary.interactionCounts.polloption5;
                viewholder.tv_total_votes.setVisibility(View.VISIBLE);
                if (voteCount == 1) {
                    viewholder.tv_total_votes.setText(voteCount + " Vote");
                } else {
                    viewholder.tv_total_votes.setText(voteCount + " Votes");
                }
            } else {
                viewholder.tv_total_votes.setVisibility(View.GONE);
                viewholder.tv_total_votes.setText(0 + " Votes");
            }
            boolean isPollEnded = true;
            if (Util.validateString(getGroupPosts.get(position).poll.pollEndsDateTime)) {

                try {
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date pollEndDate = simpleDateFormat2.parse(getGroupPosts.get(position).poll.pollEndsDateTime.replace("+0000", "Z"));
                    Date date = new Date();
                    String stringDate = simpleDateFormat2.format(date);
                    Date currentDate = simpleDateFormat2.parse(stringDate);
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                    SimpleDateFormat localDateFormat;
                    if (!android.text.format.DateFormat.is24HourFormat(context)) {
                        localDateFormat = new SimpleDateFormat("hh:mm a");
                    } else {
                        localDateFormat = new SimpleDateFormat("HH:mm");
                    }
                    long diff = pollEndDate.getTime() - currentDate.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    int diffInDays = (int) ((currentDate.getTime() - pollEndDate.getTime()) / (1000 * 60 * 60 * 24));

                    if (diffInDays > 0 && diffHours > 0) {
                        isPollEnded = false;
                        viewholder.tv_pollleft.setText("Ends in " + diffInDays + " days " + diffHours + " hours");
                    } else if (diffInDays <= 0 && diffHours >= 1) {
                        isPollEnded = false;
                        if (diffMinutes > 0) {
                            viewholder.tv_pollleft.setText("Ends in " + diffHours + " hours " + diffMinutes + " minutes");
                        } else {
                            viewholder.tv_pollleft.setText("Ends in " + diffHours + " hours");
                        }
                    } else if (diffInDays <= 0 && diffHours <= 0 && diffMinutes > 0) {
                        isPollEnded = false;
                        viewholder.tv_pollleft.setText("Ends in " + diffMinutes + " minutes");
                    } else {
                        isPollEnded = true;
                        viewholder.tv_pollleft.setText("Poll ended");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                viewholder.tv_pollleft.setText("");
                isPollEnded = false;
            }
            viewholder.layout_polls.setVisibility(View.VISIBLE);
            viewholder.tvTitle.setVisibility(View.VISIBLE);
//                    viewholder.tvContent.setTypeface(CollabDeenApplication.mRegularFont);
            viewholder.rv_poll_options.setVisibility(View.VISIBLE);
            PollOptionsAdapter pollOptionsAdapter = new PollOptionsAdapter(context, getGroupPosts.get(position).poll.options,
                    true, getGroupPosts.get(position).interactionSummary, getGroupPosts.get(position), position, isPollEnded);
            viewholder.rv_poll_options.setAdapter(pollOptionsAdapter);
        } else {
            viewholder.layout_polls.setVisibility(View.GONE);
            viewholder.rv_poll_options.setVisibility(View.GONE);
        }

        if (getGroupPosts.get(position).media != null && getGroupPosts.get(position).media.auds != null
                && getGroupPosts.get(position).media.auds.size() > 0) {
            viewholder.audio_layout.setVisibility(View.VISIBLE);
            viewholder.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onAudPlayClick(position);
                }
            });

            if (getGroupPosts.get(position).exoplayerModel != null) {
                int timer = (int) getGroupPosts.get(position).exoplayerModel.exoPlayer.getDuration();
//                    viewholder.tv_audio_timer.setText(Utils.stringForTime((int) getCommunitiesPosts.get(position).media.auds.get(0).exoplayerModel.exoPlayer.getDuration()));

                viewholder.tv_audio_timer.setText(Util.stringForTime(timer));
                viewholder.progress_audio.setProgress(0);
                viewholder.progress_audio.setMax(timer);
                if (handler == null) handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getGroupPosts.get(position).exoplayerModel != null && getGroupPosts.get(position).exoplayerModel.getExoPlayer() != null && getGroupPosts.get(position).isPlaying) {
                            viewholder.progress_audio.setMax((int) getGroupPosts.get(position).getExoplayerModel().getExoPlayer().getDuration() / 1000);
                            int mCurrentPosition = (int) getGroupPosts.get(position).getExoplayerModel().getExoPlayer().getCurrentPosition() / 1000;
                            viewholder.progress_audio.setProgress(mCurrentPosition);
                            int remainingtime = (int) getGroupPosts.get(position).exoplayerModel.exoPlayer.getDuration() - (int) getGroupPosts.get(position).exoplayerModel.exoPlayer.getContentPosition();
                            viewholder.tv_audio_start.setText(Util.stringForTime((int) getGroupPosts.get(position).exoplayerModel.exoPlayer.getCurrentPosition()));
//                    txtEndTime.setText(stringForTime((int) exoPlayer.getDuration()));
                            viewholder.tv_move_back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getGroupPosts.get(position).exoplayerModel.exoPlayer.seekTo(((mCurrentPosition - 15) * 1000));
                                }
                            });
                            viewholder.tv_move_forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getGroupPosts.get(position).exoplayerModel.exoPlayer.seekTo(((mCurrentPosition + 15) * 1000));
                                }
                            });
                            handler.postDelayed(this, 1000);
                        }
                    }
                });

            }

            if (getGroupPosts.get(position).isPlaying) {
                Picasso.get().load(R.drawable.pause_audio_btn).into(viewholder.btnPlay);
            } else {
                Picasso.get().load(R.drawable.play_audio_btn).into(viewholder.btnPlay);
            }
        }
        if (listener == null) {
            viewholder.cv_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLayoutListener(getGroupPosts.get(position));
                }
            });
        } else {
            viewholder.cv_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, holder.getAdapterPosition(), getGroupPosts.get(position));
                }
            });
        }

        viewholder.tv_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedGroupClickListener.onItemClick(view, holder.getAdapterPosition(), getGroupPosts.get(position).group);
            }
        });

        viewholder.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - Add userId who posted this post
                userClickListener.onItemClick(view, holder.getAdapterPosition(), getGroupPosts.get(position).communityId);
            }
        });


    }

    class FileSize extends AsyncTask<Integer, Integer, String> {
        TextView tv_size_doc;
        String URL;

        FileSize(TextView textView, String URL) {
            this.tv_size_doc = textView;
            this.URL = URL;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            java.net.URL url = null;
            try {
                url = new URL(URL);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                long file_size = urlConnection.getContentLength();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_size_doc.setText(Util.getFileSize(file_size));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvContent, tv_name_doc, tv_size_doc, tv_like, tv_pollleft, tv_total_votes, tv_audio_start,
                tv_audio_timer, tv_move_back, tv_move_forward, tv_group;
        ImageView postImage, iv_img, iv_like, btnPlay, iv_profile;
        RelativeLayout layout_doc;
        LinearLayout layout_polls;
        RecyclerView rv_poll_options;
        CardView audio_layout;
        ProgressBar progress_audio;
        ConstraintLayout cv_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_group = itemView.findViewById(R.id.tv_group);
            cv_layout = itemView.findViewById(R.id.cv_layout);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            postImage = itemView.findViewById(R.id.post_image);
            layout_doc = itemView.findViewById(R.id.layout_doc);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_size_doc = itemView.findViewById(R.id.tv_size_doc);
            tv_name_doc = itemView.findViewById(R.id.tv_name_doc);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like = itemView.findViewById(R.id.tv_like);

            tv_total_votes = itemView.findViewById(R.id.tv_total_votes);
            tv_pollleft = itemView.findViewById(R.id.tv_pollleft);
            layout_polls = itemView.findViewById(R.id.layout_polls);
            rv_poll_options = itemView.findViewById(R.id.rv_poll_options);

            tv_audio_start = itemView.findViewById(R.id.tv_audio_start);
            audio_layout = itemView.findViewById(R.id.audio_layout);
            progress_audio = itemView.findViewById(R.id.progress_audio);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            tv_audio_timer = itemView.findViewById(R.id.tv_audio_timer);
            tv_move_back = itemView.findViewById(R.id.tv_move_back);
            tv_move_forward = itemView.findViewById(R.id.tv_move_forward);
        }
    }

    @Override
    public int getItemCount() {
        return getGroupPosts.size();
    }

    public interface FeedListener {
        void onDocClick(String url);

        void onAudPlayClick(int position);

        void onLayoutListener(Post post);
    }

    public void setData(List<Post> newPostList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FeedDiffCallback(getGroupPosts, newPostList));
        getGroupPosts = newPostList;
        diffResult.dispatchUpdatesTo(this);

    }
}
