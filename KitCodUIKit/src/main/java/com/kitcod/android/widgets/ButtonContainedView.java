package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcButtonViewBinding;

import org.jetbrains.annotations.NotNull;

public class ButtonContainedView extends FrameLayout {
    KcButtonViewBinding binding;

    public ButtonContainedView(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public ButtonContainedView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_button_contained_style);
    }

    public ButtonContainedView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ButtonContainedView, defStyleAttr, 0);
        this.binding = (KcButtonViewBinding) DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.kc_button_view, this, true);
        ColorStateList buttonTint = a.getColorStateList(R.styleable.ButtonContainedView_kc_buttoncontained_button_backgroundtint_appearance);
        this.binding.button.setBackgroundTintList(buttonTint);
        int mButtonRedId = a.getResourceId(R.styleable.ButtonContainedView_kc_buttoncontained_button_text_appearance, R.style.KitcodH2OnLight01);
        this.binding.button.setText("Save");
        this.binding.button.setTextAppearance(context, mButtonRedId);
        int dividerColorId = a.getResourceId(R.styleable.ButtonContainedView_kc_buttoncontained_button_background_appearance, R.style.KitcodButtonPrimary300);
        this.binding.button.setBackgroundResource(dividerColorId);
    }

    public TextView getTitleTextView() {
        return binding.button;
    }

    public void setPostButtonClickListener(OnClickListener listener) {
        if (binding != null) {
            binding.button.setOnClickListener(listener);
        }
    }

}
