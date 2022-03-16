package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcRowFeedsBinding;

public class FeedsPreview extends FrameLayout {

    KcRowFeedsBinding binding;

    public FeedsPreview(@NonNull Context context) {
        this(context, null);
    }

    public FeedsPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_feedview_preview_style);
    }

    public FeedsPreview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FeedsPreview, defStyle, 0);
        try {
            this.binding = KcRowFeedsBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int nameappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_name_appearance, R.style.KitcodDarkMed16);
            int groupnameappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_groupname_appearance, R.style.KitcodBlueNormal14);
            int datetimeappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_datetime_appearence, R.style.KitcodCaption2OnLight02);
            int contentappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_content_appearence, R.style.KitcodDarkNormal16);
            int likeappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_like_appearence, R.style.KitcodDarkNormal16);
            int commentappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_comment_appearence, R.style.KitcodDarkNormal16);
            int shareappearence = a.getResourceId(R.styleable.FeedsPreview_kc_member_preview_share_appearence, R.style.KitcodDarkNormal16);
            this.binding.tvName.setTextAppearance(context, nameappearence);
            this.binding.tvGroup.setTextAppearance(context, groupnameappearence);
            this.binding.tvTime.setTextAppearance(context, datetimeappearence);
            this.binding.tvContent.setTextAppearance(context, contentappearence);
            this.binding.tvLike.setTextAppearance(context, likeappearence);
            this.binding.tvComment.setTextAppearance(context, commentappearence);
            this.binding.tvShare.setTextAppearance(context, shareappearence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
