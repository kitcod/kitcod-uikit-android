package com.kitcod.android.model;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.Serializable;

public class ExoplayerModel implements Serializable {
    public String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public SimpleExoPlayer exoPlayer;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String className;
}
