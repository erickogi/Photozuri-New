package com.photozuri.photozuri.Data.Models;

/**
 * Created by Eric on 2/22/2018.
 */

public class ProductsModel {
    private String id;
    private String category;
    private String sizable;
    private String imageurl;
    private String description;
    private Integer image;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSizable() {
        return sizable;
    }

    public void setSizable(String sizable) {
        this.sizable = sizable;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
