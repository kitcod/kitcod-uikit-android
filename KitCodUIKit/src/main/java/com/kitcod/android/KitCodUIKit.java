package com.kitcod.android;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.content.res.AppCompatResources;

public class KitCodUIKit {
    /**
     * UIKit theme mode.
     */
    private static volatile KitCodUIKit.ThemeMode defaultThemeMode;
    public enum ThemeMode {
        Light(R.style.KitCod, R.color.primary_300, R.color.secondary_300, R.color.onlight_03),
        Dark(R.style.KitCod, R.color.primary_200, R.color.secondary_200, R.color.ondark_03);

        @StyleRes
        int resId;
        @ColorRes
        int primaryTintColorResId;
        @ColorRes int secondaryTintColorResId;
        @ColorRes int monoTintColorResId;

        ThemeMode(@StyleRes int resId, @ColorRes int primaryTintColorResId, @ColorRes int secondaryTintColorResId, @ColorRes int monoTintColorResId) {
            this.resId = resId;
            this.primaryTintColorResId = primaryTintColorResId;
            this.secondaryTintColorResId = secondaryTintColorResId;
            this.monoTintColorResId = monoTintColorResId;
        }

        @StyleRes
        public int getResId() {
            return resId;
        }

        @ColorRes
        public int getPrimaryTintResId() {
            return primaryTintColorResId;
        }

        @ColorRes
        public int getSecondaryTintResId() {
            return secondaryTintColorResId;
        }

        @ColorRes
        public int getMonoTintResId() {
            return monoTintColorResId;
        }

        public ColorStateList getPrimaryTintColorStateList(@NonNull Context context) {
            return AppCompatResources.getColorStateList(context, primaryTintColorResId);
        }

        public ColorStateList getSecondaryTintColorStateList(@NonNull Context context) {
            return AppCompatResources.getColorStateList(context, secondaryTintColorResId);
        }

        public ColorStateList getMonoTintColorStateList(@NonNull Context context) {
            return AppCompatResources.getColorStateList(context, monoTintColorResId);
        }
    }
    public static KitCodUIKit.ThemeMode getDefaultThemeMode() {
        return defaultThemeMode;
    }

    public static void setDefaultThemeMode(KitCodUIKit.ThemeMode themeMode) {
        defaultThemeMode = themeMode;
    }
}
