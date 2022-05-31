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
import com.kitcod.android.databinding.KcRowGroupFeedBinding;

public class GroupsListFeedView extends FrameLayout {
    KcRowGroupFeedBinding binding;

    public GroupsListFeedView(@NonNull Context context) {
        this(context, null);
    }

    public GroupsListFeedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_group_feed_style);
    }

    public GroupsListFeedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GroupsListFeedView, defStyle, 0);
        try {
            this.binding = KcRowGroupFeedBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int nameappearence = a.getResourceId(R.styleable.GroupsListFeedView_kc_groupslistfeed_tv_name_appearance, R.style.KitcodDarkMed14);
            this.binding.tvGroupname.setTextAppearance(context, nameappearence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
