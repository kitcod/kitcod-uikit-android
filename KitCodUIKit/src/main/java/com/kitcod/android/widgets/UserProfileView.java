package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcProfileviewBinding;

public class UserProfileView extends FrameLayout {
    private KcProfileviewBinding binding;

    public UserProfileView(@NonNull Context context) {
        this(context, null);
    }

    public UserProfileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_userprofile_style);
    }

    public UserProfileView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UserProfileView, defStyle, 0);
        try {
            this.binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.kc_profileview, this, true);
            int nameAppearance = a.getResourceId(R.styleable.UserProfileView_kc_userprofile_name_appearance, R.style.KitcodDarkMed20);
            int labelAppearance = a.getResourceId(R.styleable.UserProfileView_kc_userprofile_label_appearance, R.style.KitcodLightReg14);
            int textAppearance = a.getResourceId(R.styleable.UserProfileView_kc_userprofile_text_appearance, R.style.KitcodDarkNormal16);
            binding.tvName.setTextAppearance(context, nameAppearance);
            binding.tvLabelCity.setTextAppearance(context, labelAppearance);
            binding.tvAboutLabel.setTextAppearance(context, labelAppearance);
            binding.tvEmailLabel.setTextAppearance(context, labelAppearance);
            binding.tvEmail.setTextAppearance(context, textAppearance);
            binding.tvAbout.setTextAppearance(context, textAppearance);
            binding.tvCity.setTextAppearance(context, textAppearance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getTitleTextView() {
        return binding.btnFollow.getTitleTextView();
    }

}
