package com.photozuri.photozuri.Data.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eric on 2/21/2018.
 */

public class PhotoList implements Serializable {
    private String date;
    private ArrayList<StoryPhoto> storyPhotos;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<StoryPhoto> getStoryPhotos() {
        return storyPhotos;
    }

    public void setStoryPhotos(ArrayList<StoryPhoto> storyPhotos) {
        this.storyPhotos = storyPhotos;
    }
}
