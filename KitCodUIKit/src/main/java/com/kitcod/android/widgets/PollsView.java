package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcRowPollsOptionsBinding;

public class PollsView extends FrameLayout {
    KcRowPollsOptionsBinding binding;

    public PollsView(@NonNull Context context) {
        this(context, null);
    }

    public PollsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_poll_style);
    }

    public PollsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PollsView, defStyle, 0);
        try {
            this.binding = KcRowPollsOptionsBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int nameappearence = a.getResourceId(R.styleable.PollsView_kc_polls_option_appearance, R.style.KitcodDarkNormal16);
            int bgPollOption = a.getResourceId(R.styleable.PollsView_kc_polls_option_bg_appearance, R.drawable.bg_border_radius_5);
            int bgProgress = a.getResourceId(R.styleable.PollsView_kc_polls_progress_bg_appearance, R.drawable.polls_progress_bar);
            int percentageappearence = a.getResourceId(R.styleable.PollsView_kc_polls_option_percentage_appearance, R.style.KitcodDarkMed);
            int textVotedappearence = a.getResourceId(R.styleable.PollsView_kc_polls_option_voted_text_appearance, R.style.KitcodDarkNormal16);
            int totalVotesappearence = a.getResourceId(R.styleable.PollsView_kc_polls_votes_appearance, R.style.KitcodLightReg14);
            this.binding.tvPoll.setTextAppearance(context, nameappearence);
            this.binding.tvPollPerc.setTextAppearance(context, percentageappearence);
            this.binding.tvPollText.setTextAppearance(context, textVotedappearence);
            this.binding.tvVotes.setTextAppearance(context, totalVotesappearence);
            this.binding.tvPoll.setBackgroundResource(bgPollOption);
            Drawable drawable = getResources().getDrawable(bgProgress);
            this.binding.pollProgress.setProgressDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
