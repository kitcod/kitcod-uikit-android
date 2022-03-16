package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcViewAppBarBinding;
import com.kitcod.android.utils.DrawableUtils;

public class AppBarView extends FrameLayout {
    KcViewAppBarBinding binding;
    private boolean useRightButton = true;
    private boolean useLeftImageButton = true;

    public AppBarView(@NonNull Context context) {
        this(context, null);
    }

    public AppBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_appbar_style);
    }

    public AppBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AppBarView, defStyleAttr, 0);
        this.binding = (KcViewAppBarBinding) DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.kc_view_app_bar, this, true);
        CharSequence titleText = a.getString(R.styleable.AppBarView_kc_appbar_title);
        int titleTextAppearance = a.getResourceId(R.styleable.AppBarView_kc_appbar_title_appearance, R.style.KitcodH2OnLight01);
        CharSequence leftButtonText = a.getString(R.styleable.AppBarView_kc_appbar_left_button_text);
        int leftButtonIconRedId = a.getResourceId(R.styleable.AppBarView_kc_appbar_left_button_icon, 0);
        CharSequence rightButtonText = a.getString(R.styleable.AppBarView_kc_appbar_right_button_text);
        int rightButtonIconRedId = a.getResourceId(R.styleable.AppBarView_kc_appbar_right_button_icon, 0);
        ColorStateList buttonTint = a.getColorStateList(R.styleable.AppBarView_kc_appbar_button_tint);
        int dividerColorId = a.getResourceId(R.styleable.AppBarView_kc_appbar_divider_color, R.color.kitcodColorGrey);
        this.binding.tvAppBarTitle.setText(titleText);
        this.binding.tvAppBarTitle.setTextAppearance(context, titleTextAppearance);
        binding.elevationView.setBackgroundResource(dividerColorId);
        if (!TextUtils.isEmpty(leftButtonText)) {
            this.binding.ibtnLeft.setVisibility(GONE);
            this.binding.btnLeft.setText(leftButtonText);
        } else if (leftButtonIconRedId != 0) {
            this.binding.btnLeft.setVisibility(GONE);
            this.binding.ibtnLeft.setImageDrawable(DrawableUtils.setTintList(context, leftButtonIconRedId, buttonTint));
        }

        if (!TextUtils.isEmpty(rightButtonText)) {
            this.binding.ibtnRight.setVisibility(GONE);
            this.binding.btnRight.setText(rightButtonText);
        } else if (rightButtonIconRedId != 0) {
            this.binding.btnRight.setVisibility(GONE);
            this.binding.ibtnRight.setImageDrawable(DrawableUtils.setTintList(context, rightButtonIconRedId, buttonTint));
        }

    }

    public TextView getTitleTextView() {
        return binding.tvAppBarTitle;
    }

/*
    public TextView getDescriptionTextView() {
        return binding.tvAppBarDesc;
    }
*/

    public TextView getLeftTextButton() {
        return binding.btnLeft;
    }

    public ImageButton getLeftImageButton() {
        return binding.ibtnLeft;
    }

    public void setLeftImageButtonResource(@DrawableRes int drawableRes) {
        if (useLeftImageButton && binding != null) {
            binding.btnLeft.setVisibility(GONE);
            binding.ibtnLeft.setVisibility(VISIBLE);
            binding.ibtnLeft.setImageResource(drawableRes);
        }
    }

    public void setLeftImageButtonTint(ColorStateList tint) {
        if (useLeftImageButton && binding != null) {
            ImageViewCompat.setImageTintList(binding.ibtnLeft, tint);
        }
    }

    public void setLeftImageButtonClickListener(OnClickListener listener) {
        if (binding != null) {
            binding.ibtnLeft.setOnClickListener(listener);
        }
    }

    public void setUseLeftImageButton(boolean useLeftImageButton) {
        this.useLeftImageButton = useLeftImageButton;
        if (binding != null) {
            binding.flLeftPanel.setVisibility(useLeftImageButton ? VISIBLE : GONE);
        }
    }

    public TextView getRightTextButton() {
        return binding.btnRight;
    }

    public void setRightTextButtonString(String text) {
        if (useRightButton && binding != null) {
            binding.ibtnRight.setVisibility(GONE);
            binding.btnRight.setVisibility(VISIBLE);
            binding.btnRight.setText(text);
        }
    }

    public void setRightTextButtonClickListener(OnClickListener listener) {
        if (binding != null) {
            binding.btnRight.setOnClickListener(listener);
        }
    }

    public void setRightTextButtonEnabled(boolean enabled) {
        if (binding != null) {
            binding.btnRight.setEnabled(enabled);
        }
    }

    public ImageButton getRightImageButton() {
        return binding.ibtnRight;
    }

    public void setRightImageButtonResource(@DrawableRes int drawableRes) {
        if (useRightButton && binding != null) {
            binding.btnRight.setVisibility(GONE);
            binding.ibtnRight.setVisibility(VISIBLE);
            binding.ibtnRight.setImageResource(drawableRes);
        }
    }

    public void setRightImageButtonTint(ColorStateList tint) {
        if (useRightButton && binding != null) {
            ImageViewCompat.setImageTintList(binding.ibtnRight, tint);
        }
    }

    public void setRightImageButtonClickListener(OnClickListener listener) {
        if (binding != null) {
            binding.ibtnRight.setOnClickListener(listener);
        }
    }

    public void setUseRightButton(boolean useRightButton) {
        this.useRightButton = useRightButton;
        if (binding != null) {
            binding.ibtnRight.setVisibility(useRightButton ? VISIBLE : GONE);
            binding.btnRight.setVisibility(useRightButton ? VISIBLE : GONE);
        }
    }

}
