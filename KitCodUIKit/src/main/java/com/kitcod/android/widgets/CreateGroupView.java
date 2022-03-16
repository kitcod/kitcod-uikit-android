package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcCreateGroupViewBinding;

public class CreateGroupView extends FrameLayout {
    KcCreateGroupViewBinding binding;
    private String headerTitle = null;

    public CreateGroupView(@NonNull Context context) {
        this(context, null);
    }

    public CreateGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_create_group_style);
    }

    public CreateGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CreateGroupView, defStyleAttr, 0);
        this.binding = (KcCreateGroupViewBinding) DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.kc_create_group_view, this, true);
        if (headerTitle != null) {
            binding.btnCreateCommunity.getTitleTextView().setText(headerTitle);
//            binding.chvChannelHeader.getTitleTextView().setText(headerTitle);
        }
//        ColorStateList buttonTint = a.getColorStateList(R.styleable.CreateGroupView_kc_creategroup_button_backgroundtint_appearance);
//        this.binding.btnCreateCommunity.getTitleTextView().setBackgroundTintList(buttonTint);
        /*
        ColorStateList buttonTint = a.getColorStateList(R.styleable.CreateGroupView_kc_creategroup_button_backgroundtint_appearance);
        this.binding.btnCreateCommunity.setBackgroundTintList(buttonTint);
        int mButtonRedId = a.getResourceId(R.styleable.CreateGroupView_kc_creategroup_button_text_appearance, R.style.KitcodH2OnLight01);
        this.binding.btnCreateCommunity.setText("Save");
        this.binding.btnCreateCommunity.setTextAppearance(context, mButtonRedId);
        int dividerColorId = a.getResourceId(R.styleable.CreateGroupView_kc_creategroup_button_background_appearance, R.style.KitcodButtonPrimary300);
        this.binding.btnCreateCommunity.setBackgroundResource(dividerColorId);*/
    }

    public TextView getTitleTextView() {
        return binding.btnCreateCommunity.getTitleTextView();
    }

    public RadioButton getPublicRadioButton() {
        return binding.rbPublic;
    }

    public RadioButton getPrivateRadioButton() {
        return binding.rbPrivate;
    }

    public RelativeLayout uploadImage(){
        return binding.icCamera;
    }

}
