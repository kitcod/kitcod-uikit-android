package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcFeedviewBinding;

public class FeedsView extends FrameLayout {
    private KcFeedviewBinding binding;

    public FeedsView(@NonNull Context context) {
            this(context, null);
    }

    public FeedsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_feedview_style);
    }

    public FeedsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FeedsView, defStyle, 0);
        try {
            this.binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.kc_feedview, this, true);
            int background = a.getResourceId(R.styleable.FeedsView_kc_feedsview_item_background, R.drawable.bg_blue_stroke_20);
            int nameAppearance = a.getResourceId(R.styleable.FeedsView_kc_feedsview_name_appearance, R.style.KitcodH2OnLight01);
            binding.tvCreateLabel.setTextAppearance(context, nameAppearance);
            binding.constraintLayout.setBackgroundResource(background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecyclerView getGroupsView() {
        return binding.rvGroups;
    }

    public ConstraintLayout getCreatePostView() {
        return binding.constraintLayout;
    }

}
