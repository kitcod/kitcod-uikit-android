package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcDialogPostTypeBinding;

public class PostTypeDialogView extends LinearLayout {
    KcDialogPostTypeBinding binding;

    public PostTypeDialogView(Context context) {
        this(context, null);
    }

    public PostTypeDialogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_bottomsheetdialog_style);
    }

    public PostTypeDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PostTypeDialogView, defStyleAttr, 0);
        try {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.kc_dialog_post_type, null, false);
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int selectedTypeBackgroundTintAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_background_tint, R.color.information);
//            int selectedTypeIconTintAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_icon_tint, R.color.kitcodColorBlack);
            ColorStateList selectedTypeBackgroundTintAppearance = a.getColorStateList(R.styleable.PostTypeDialogView_kc_post_type_dialog_background_tint);
            ColorStateList selectedTypeIconTintAppearance = a.getColorStateList(R.styleable.PostTypeDialogView_kc_post_type_dialog_icon_tint);

            int titleAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_title_appearence, R.style.KitcodDarkMed20);
            int nameAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_name_appearence, R.style.KitcodDarkMed16);
            int descAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_desc_appearence, R.style.KitcodLightReg14);
            binding.ivAnnouncement.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivAnnouncement, selectedTypeIconTintAppearance);
            binding.ivPost.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivPost, selectedTypeIconTintAppearance);
            binding.ivPoll.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivPoll, selectedTypeIconTintAppearance);
            binding.tvAnnouncementTitle.setTextAppearance(context, nameAppearance);
            binding.tvPostTitle.setTextAppearance(context, nameAppearance);
            binding.tvPollTitle.setTextAppearance(context, nameAppearance);

            binding.tvAnnouncementDesc.setTextAppearance(context, descAppearance);
            binding.tvPostDesc.setTextAppearance(context, descAppearance);
            binding.tvPollDesc.setTextAppearance(context, descAppearance);

            binding.tvPostLabel.setTextAppearance(context, titleAppearance);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConstraintLayout getAnnouncementType() {
        return binding.clAnnouncement;
    }

    public ConstraintLayout getPostType() {
        return binding.clPost;
    }

    public ConstraintLayout getPollType() {
        return binding.clPoll;
    }

    public TextView getAnnouncementTitle() {
        return binding.tvAnnouncementTitle;
    }

    public TextView getPostTitle() {
        return binding.tvPostTitle;
    }


    public TextView getPollTitle() {
        return binding.tvPollTitle;
    }

    public TextView getAnnouncementDesc() {
        return binding.tvAnnouncementDesc;
    }

    public TextView getPostDesc() {
        return binding.tvPostDesc;
    }


    public TextView getPollDesc() {
        return binding.tvPollDesc;
    }


}
