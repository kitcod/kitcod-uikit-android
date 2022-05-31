package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcRowGroupBinding;

public class GroupsListView extends FrameLayout {
    KcRowGroupBinding binding;

    public GroupsListView(@NonNull Context context) {
        this(context, null);
    }

    public GroupsListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_groups_list_style);
    }

    public GroupsListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GroupsListView, defStyle, 0);
        try {
            this.binding = KcRowGroupBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int nameappearence = a.getResourceId(R.styleable.GroupsListView_kc_groupslist_tv_name_appearance, R.style.KitcodH2OnLight01);
            this.binding.tvGroupname.setTextAppearance(context, nameappearence);

            int buttonappearence = a.getResourceId(R.styleable.GroupsListView_kc_groupslist_tv_button_appearance, R.style.KitcodButtonWhiteMed16);
            this.binding.buttonJoin.setTextAppearance(context, buttonappearence);
            ColorStateList buttonTint = a.getColorStateList(R.styleable.GroupsListView_kc_groupslist_tv_button_tint);
            this.binding.buttonJoin.setBackgroundTintList(buttonTint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
