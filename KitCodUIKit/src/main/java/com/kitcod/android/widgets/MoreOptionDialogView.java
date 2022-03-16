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
import com.kitcod.android.databinding.KcDialogMoreOptionsBinding;
import com.kitcod.android.databinding.KcDialogPostTypeBinding;

public class MoreOptionDialogView extends LinearLayout {
    KcDialogMoreOptionsBinding binding;

    public MoreOptionDialogView(Context context) {
        this(context, null);
    }

    public MoreOptionDialogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_bottomsheetdialog_style);
    }

    public MoreOptionDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MoreOptionDialogView, defStyleAttr, 0);
        try {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.kc_dialog_more_options, null, false);
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int selectedTypeBackgroundTintAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_background_tint, R.color.information);
//            int selectedTypeIconTintAppearance = a.getResourceId(R.styleable.PostTypeDialogView_kc_post_type_dialog_icon_tint, R.color.kitcodColorBlack);
            ColorStateList selectedTypeBackgroundTintAppearance = a.getColorStateList(R.styleable.PostTypeDialogView_kc_post_type_dialog_background_tint);
            ColorStateList selectedTypeIconTintAppearance = a.getColorStateList(R.styleable.PostTypeDialogView_kc_post_type_dialog_icon_tint);

            int titleAppearance = a.getResourceId(R.styleable.MoreOptionDialogView_kc_more_option_dialog_title_appearence, R.style.KitcodDarkMed20);
            int nameAppearance = a.getResourceId(R.styleable.MoreOptionDialogView_kc_more_option_dialog_name_appearence, R.style.KitcodDarkMed16);
            int descAppearance = a.getResourceId(R.styleable.MoreOptionDialogView_kc_more_option_dialog_desc_appearence, R.style.KitcodLightReg14);
            binding.ivImage.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivImage, selectedTypeIconTintAppearance);
            binding.ivAudio.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivAudio, selectedTypeIconTintAppearance);
            binding.ivVideo.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivVideo, selectedTypeIconTintAppearance);
            binding.ivCta.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivCta, selectedTypeIconTintAppearance);

            //Title Appearance
            binding.tvImageTitle.setTextAppearance(context, nameAppearance);
            binding.tvVideoTitle.setTextAppearance(context, nameAppearance);
            binding.tvAudioTitle.setTextAppearance(context, nameAppearance);
            binding.tvButtonTitle.setTextAppearance(context, nameAppearance);

            //Description Appearance
            binding.tvImageDesc.setTextAppearance(context, descAppearance);
            binding.tvVideoDesc.setTextAppearance(context, descAppearance);
            binding.tvAudioDesc.setTextAppearance(context, descAppearance);
            binding.tvButtonDesc.setTextAppearance(context, descAppearance);

            binding.tvPostLabel.setTextAppearance(context, titleAppearance);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConstraintLayout getImageLayout() {
        return binding.clImage;
    }

    public ConstraintLayout getAudioLayout() {
        return binding.clAudio;
    }

    public ConstraintLayout getVideoLayout() {
        return binding.clVideo;
    }

    public ConstraintLayout getCTALayout() {
        return binding.clButton;
    }




}
