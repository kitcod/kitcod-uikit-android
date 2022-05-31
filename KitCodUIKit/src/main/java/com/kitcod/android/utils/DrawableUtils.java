package com.kitcod.android.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableUtils {
    public DrawableUtils() {
    }

    public static Drawable createDividerDrawable(int height, int color) {
        GradientDrawable divider = new GradientDrawable();
        divider.setShape(GradientDrawable.RECTANGLE);
        divider.setSize(0, height);
        divider.setColor(color);
        return divider;
    }

    public static Drawable createRoundedRectrangle(int radius, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius((float) radius);
        drawable.setColor(color);
        return drawable;
    }

    public static Drawable setTintList(Context context, int resId, int colorRes) {
        return colorRes == 0 ? AppCompatResources.getDrawable(context, resId) : setTintList(AppCompatResources.getDrawable(context, resId), context.getResources().getColorStateList(colorRes));
    }

    public static Drawable setTintList(Context context, int resId, ColorStateList colorStateList) {
        return setTintList(AppCompatResources.getDrawable(context, resId), colorStateList);
    }

    public static Drawable setTintList(Drawable drawable, ColorStateList colorStateList) {
        if (drawable != null && colorStateList != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable, colorStateList);
            return drawable;
        } else {
            return drawable;
        }
    }
}
