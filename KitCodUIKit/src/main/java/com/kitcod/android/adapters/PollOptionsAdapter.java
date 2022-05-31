package com.kitcod.android.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcPollsPreviewBinding;
import com.kitcod.android.model.InteractionSummary;
import com.kitcod.android.model.Option;
import com.kitcod.android.model.Post;

import java.util.List;

public class PollOptionsAdapter extends RecyclerView.Adapter {
    List<Option> pollOptions;
    Context context;
    boolean isVoted;
    InteractionSummary interactionSummary;
    View.OnClickListener onPollOptionClickListener;
    Post postPosition;
    int Mainposition;
    boolean isPollEnded;
    KcPollsPreviewBinding binding;

    public PollOptionsAdapter(Context context, List<Option> pollOptions, boolean isVoted, InteractionSummary interactionSummary, View.OnClickListener onPollOptionClickListener,
                              Post postPosition, int position, boolean isPollEnded) {
        this.context = context;
        this.pollOptions = pollOptions;
        this.isVoted = isVoted;
        this.interactionSummary = interactionSummary;
        this.onPollOptionClickListener = onPollOptionClickListener;
        this.postPosition = postPosition;
        this.Mainposition = position;
        this.isPollEnded = isPollEnded;
    }

    public PollOptionsAdapter(Context context, List<Option> pollOptions, boolean isVoted, InteractionSummary interactionSummary,
                              Post postPosition, int position, boolean isPollEnded) {
        this.context = context;
        this.pollOptions = pollOptions;
        this.isVoted = isVoted;
        this.interactionSummary = interactionSummary;
        this.onPollOptionClickListener = onPollOptionClickListener;
        this.postPosition = postPosition;
        this.Mainposition = position;
        this.isPollEnded = isPollEnded;
    }


    public PollOptionsAdapter(Context context, List<Option> pollOptions, boolean isVoted, InteractionSummary interactionSummary, View.OnClickListener onPollOptionClickListener,
                              Post postPosition, boolean isPollEnded) {
        this.context = context;
        this.pollOptions = pollOptions;
        this.isVoted = isVoted;
        this.interactionSummary = interactionSummary;
        this.onPollOptionClickListener = onPollOptionClickListener;
        this.postPosition = postPosition;
        this.isPollEnded = isPollEnded;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kc_polls_preview, parent, false);
        binding = DataBindingUtil.bind(v);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewholder = (MyViewHolder) holder;

        if (isVoted || isPollEnded) {
            viewholder.tv_poll.setVisibility(View.GONE);
            viewholder.layout_selected_poll.setVisibility(View.VISIBLE);
            viewholder.tv_poll_text.setText(pollOptions.get(position).text);
            if (interactionSummary != null && interactionSummary.userInteractions != null && interactionSummary.interactionCounts != null) {

                for (int i = 0; i < interactionSummary.userInteractions.size(); i++) {
                    if (interactionSummary.userInteractions.get(i).resourceSubType != null && interactionSummary.userInteractions.get(i).resourceSubType.equalsIgnoreCase("POLL")) {
                        if (pollOptions.get(position).type.equalsIgnoreCase(interactionSummary.userInteractions.get(i).interactionType)) {
                            viewholder.iv_selected.setVisibility(View.VISIBLE);
                        }
                    }
                }

                int total = interactionSummary.interactionCounts.polloption1 + interactionSummary.interactionCounts.polloption2 + interactionSummary.interactionCounts.polloption3 +
                        interactionSummary.interactionCounts.polloption4 + interactionSummary.interactionCounts.polloption5;
                if (position == 0) {
                    if (interactionSummary.interactionCounts.polloption1 > 0 || interactionSummary.interactionCounts.polloption2 > 0 || interactionSummary.interactionCounts.polloption3 > 0 ||
                            interactionSummary.interactionCounts.polloption4 > 0 || interactionSummary.interactionCounts.polloption5 > 0) {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption1 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption1, interactionSummary.interactionCounts.polloption1);
                        viewholder.tv_votes.setText(itemsFound);
//                        viewholder.pollProgress.setProgress(percent1);
                    } else {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption1 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption1, interactionSummary.interactionCounts.polloption1);
                        viewholder.tv_votes.setText(itemsFound);
                    }
                } else if (position == 1) {
                    if (interactionSummary.interactionCounts.polloption1 > 0 || interactionSummary.interactionCounts.polloption2 > 0 || interactionSummary.interactionCounts.polloption3 > 0 ||
                            interactionSummary.interactionCounts.polloption4 > 0 || interactionSummary.interactionCounts.polloption5 > 0) {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption2 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption2, interactionSummary.interactionCounts.polloption2);
                        viewholder.tv_votes.setText(itemsFound);

                    } else {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption2 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption2, interactionSummary.interactionCounts.polloption2);
                        viewholder.tv_votes.setText(itemsFound);
                    }

                } else if (position == 2) {
                    if (interactionSummary.interactionCounts.polloption1 > 0 || interactionSummary.interactionCounts.polloption2 > 0 || interactionSummary.interactionCounts.polloption3 > 0 ||
                            interactionSummary.interactionCounts.polloption4 > 0 || interactionSummary.interactionCounts.polloption5 > 0) {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption3 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption3, interactionSummary.interactionCounts.polloption3);
                        viewholder.tv_votes.setText(itemsFound);
                    } else {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption3 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption3, interactionSummary.interactionCounts.polloption3);
                        viewholder.tv_votes.setText(itemsFound);
                    }

                } else if (position == 3) {
                    if (interactionSummary.interactionCounts.polloption1 > 0 || interactionSummary.interactionCounts.polloption2 > 0 || interactionSummary.interactionCounts.polloption3 > 0 ||
                            interactionSummary.interactionCounts.polloption4 > 0 || interactionSummary.interactionCounts.polloption5 > 0) {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption4 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption4, interactionSummary.interactionCounts.polloption4);
                        viewholder.tv_votes.setText(itemsFound);
                    } else {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption4 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption4, interactionSummary.interactionCounts.polloption4);
                        viewholder.tv_votes.setText(itemsFound);
                    }

                } else if (position == 4) {
                    if (interactionSummary.interactionCounts.polloption1 > 0 || interactionSummary.interactionCounts.polloption2 > 0 || interactionSummary.interactionCounts.polloption3 > 0 ||
                            interactionSummary.interactionCounts.polloption4 > 0 || interactionSummary.interactionCounts.polloption5 > 0) {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption5 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption5, interactionSummary.interactionCounts.polloption5);
                        viewholder.tv_votes.setText(itemsFound);
                    } else {
                        int percent1 = (int) (((double) interactionSummary.interactionCounts.polloption5 / total) * 100);
                        viewholder.tv_poll_perc.setText("" + percent1 + "%");
                        viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                        setProgressAnimate(viewholder.pollProgress, percent1);
                        String itemsFound = context.getResources().getQuantityString(R.plurals.numberofvotes, interactionSummary.interactionCounts.polloption5, interactionSummary.interactionCounts.polloption5);
                        viewholder.tv_votes.setText(itemsFound);
                    }

                }


            } else {
                int total = 0;
                if (position == 0) {
                    viewholder.tv_poll_perc.setText("" + 0 + "%");
                    viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                    setProgressAnimate(viewholder.pollProgress, 0);
                } else if (position == 1) {
                    viewholder.tv_poll_perc.setText("" + 0 + "%");
                    viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                    setProgressAnimate(viewholder.pollProgress, 0);
                } else if (position == 2) {
                    viewholder.tv_poll_perc.setText("" + 0 + "%");
                    viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                    setProgressAnimate(viewholder.pollProgress, 0);
                } else if (position == 4) {
                    viewholder.tv_poll_perc.setText("" + 0 + "%");
                    viewholder.pollProgress.setMax(100);
//                        viewholder.pollProgress.setProgress(percent1);
                    setProgressAnimate(viewholder.pollProgress, 0);
                }

            }

        } else {
            viewholder.tv_poll.setVisibility(View.VISIBLE);
            viewholder.layout_selected_poll.setVisibility(View.GONE);
            viewholder.tv_poll.setText(pollOptions.get(position).text);
            viewholder.tv_poll.setOnClickListener(onPollOptionClickListener);
            viewholder.tv_poll.setTag(R.id.childPollposition, position);
            viewholder.tv_poll.setTag(R.id.mainPost, postPosition);
            viewholder.tv_poll.setTag(R.id.mainPostpostion, Mainposition);
        }

    }

    private void setProgressAnimate(ProgressBar pb, int progressTo) {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public int getItemCount() {
        return pollOptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv_poll, tv_poll_perc, tv_poll_text, tv_votes;
        LinearLayout layout_selected_poll;
        ProgressBar pollProgress;
        ImageView iv_selected;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_votes = itemView.findViewById(R.id.tv_votes);
            tv_poll = (TextView) itemView.findViewById(R.id.tv_poll);
            tv_poll_perc = itemView.findViewById(R.id.tv_poll_perc);
            tv_poll_text = itemView.findViewById(R.id.tv_poll_text);
            layout_selected_poll = itemView.findViewById(R.id.layout_selected_poll);
            pollProgress = itemView.findViewById(R.id.pollProgress);
            iv_selected = itemView.findViewById(R.id.iv_selected);


        }

    }
}
