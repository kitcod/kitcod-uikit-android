package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcCreateGroupViewBinding;

public class CreateGroupViewOld2 extends FrameLayout {
    KcCreateGroupViewBinding binding;
    private String headerTitle = null;

    public CreateGroupViewOld2(@NonNull Context context) {
        this(context, null);
    }

    public CreateGroupViewOld2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_create_group_style);
    }

    public CreateGroupViewOld2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CreateGroupView, defStyleAttr, 0);
        this.binding = (KcCreateGroupViewBinding) DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.kc_create_group_view_old2, this, true);
        if (headerTitle != null) {
            binding.btnCreateCommunity.getTitleTextView().setText(headerTitle);
        }
    }

    public TextView getTitleTextView() {
        return binding.btnCreateCommunity.getTitleTextView();
    }



}
