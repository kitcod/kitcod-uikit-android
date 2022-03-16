package com.kitcod.android.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.kitcod.android.R;

public class RadioButtonView extends androidx.appcompat.widget.AppCompatRadioButton {
    public RadioButtonView(Context context) {
        this(context, null);
    }

    public RadioButtonView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_radio_button_style);
    }

    public RadioButtonView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
