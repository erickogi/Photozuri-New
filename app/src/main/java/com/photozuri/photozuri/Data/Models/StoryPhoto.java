package com.photozuri.photozuri.Data.Models;

import java.io.Serializable;

/**
 * Created by Eric on 2/21/2018.
 */

public class StoryPhoto implements Serializable {
    private String KEY_ID;
    private String Album_ID;
    private String path;
    private String title;
    private String description;
    private String date;
    private String position;

    public String getAlbum_ID() {
        return Album_ID;
    }

    public void setAlbum_ID(String album_ID) {
        Album_ID = album_ID;
    }

    public String getKEY_ID() {
        return KEY_ID;
    }

    public void setKEY_ID(String KEY_ID) {
        this.KEY_ID = KEY_ID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
