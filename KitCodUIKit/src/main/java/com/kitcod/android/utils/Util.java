package com.kitcod.android.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.Locale;

public class Util {
    public static final String POST = "post";
    public static final String GROUP = "group";
    public static final String USERID = "userid";

    public static String loadJSONFromAsset(String fileName, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static boolean validateString(String object) {
        boolean flag = false;
        if (object != null && !object.isEmpty() && object.trim().length() > 0 && !object.equalsIgnoreCase("null") && !object.equalsIgnoreCase("") && !object.equalsIgnoreCase("0") && !object.equalsIgnoreCase(" ") && !object.equalsIgnoreCase("  ")) {
            flag = true;
        }
        return flag;
    }

    public static String getFileSize(long file) {
        DecimalFormat format = new DecimalFormat("#.##");
        long MiB = 1024 * 1024;
        long KiB = 1024;
        final double length = Double.parseDouble(String.valueOf(file));

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

}
