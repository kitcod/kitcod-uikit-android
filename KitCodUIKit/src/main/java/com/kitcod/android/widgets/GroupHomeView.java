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
import com.kitcod.android.databinding.KcGroupHomeViewBinding;

public class GroupHomeView extends FrameLayout {
    KcGroupHomeViewBinding binding;

    public GroupHomeView(@NonNull Context context) {
        this(context, null);
    }

    public GroupHomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_group_home_style);
    }

    public GroupHomeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GroupHomeView, defStyle, 0);
        try {
            this.binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.kc_group_home_view, this, true);
            int titleAppearence = a.getResourceId(R.styleable.GroupHomeView_kc_group_home_title_appearence, R.style.KitcodDarkMed20);
            int grouptypeAppearence = a.getResourceId(R.styleable.GroupHomeView_kc_group_home_type_appearence, R.style.KitcodLightReg16);
            int memberCountAppearence = a.getResourceId(R.styleable.GroupHomeView_kc_group_home_aboutlabel_appearence, R.style.KitcodDarkMed16);
            int aboutdescAppearence = a.getResourceId(R.styleable.GroupHomeView_kc_group_home_aboutdesc_appearence, R.style.KitcodDarkNormal16);
            int nameAppearance = a.getResourceId(R.styleable.GroupHomeView_kc_group_home_createpostlabel_appearence, R.style.KitcodH2OnLight01);
            binding.tvGroupname.setTextAppearance(context, titleAppearence);
            binding.tvGrouptype.setTextAppearance(context, grouptypeAppearence);
            binding.tvMembercount.setTextAppearance(context, memberCountAppearence);
            binding.tvMemberLabel.setTextAppearance(context, grouptypeAppearence);
            binding.tvAboutLabel.setTextAppearance(context, memberCountAppearence);
            binding.tvAboutDesc.setTextAppearance(context, aboutdescAppearence);
            binding.tvCreateLabel.setTextAppearance(context, nameAppearance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getButtonView() {
        return binding.button3.getTitleTextView();
    }

}
