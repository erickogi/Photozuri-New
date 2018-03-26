package com.photozuri.photozuri.imagepicker.model;

import java.util.ArrayList;

/**
 * Created by boss1088 on 8/22/16.
 */
public class Folder {

    private String folderName;
    private ArrayList<com.photozuri.photozuri.imagepicker.model.Image> images;

    public Folder(String bucket) {
        folderName = bucket;
        images = new ArrayList<>();
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<com.photozuri.photozuri.imagepicker.model.Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<com.photozuri.photozuri.imagepicker.model.Image> images) {
        this.images = images;
    }
}
