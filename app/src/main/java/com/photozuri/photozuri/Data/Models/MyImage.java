package com.photozuri.photozuri.Data.Models;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Eric on 1/17/2018.
 */

public class MyImage implements Serializable, Comparable<MyImage> {

    private int KEY_ID;

    private long id;
    private String name;
    private String path;
    private String caption;
    private String no;
    private String creatonTime;

    private int imageFrom;
    private boolean isSelected;
    private boolean isDownloadedComplete;
    private boolean isDownloadedInComplete;
    private boolean isCaptioned;
    private boolean isResized;
    private int status;
    private int title_id;
    private int imagePos;


    private int UPLOAD_STATUS;


    private int front_cover;
    private int back_cover;


    public MyImage(long id, String name, String path, int imageFrom, boolean isSelected, boolean isDownloadedComplete) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.imageFrom = imageFrom;
        this.isSelected = isSelected;
        this.isDownloadedComplete = isDownloadedComplete;
    }

    public MyImage(long id, String name, String path, int imageFrom, boolean isSelected, boolean isDownloadedComplete, boolean isCaptioned, boolean isResized) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.imageFrom = imageFrom;
        this.isSelected = isSelected;
        this.isDownloadedComplete = isDownloadedComplete;
        this.isCaptioned = isCaptioned;
        this.isResized = isResized;
    }

    public MyImage(long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    protected MyImage(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.path = in.readString();
    }

    public MyImage() {

    }

    public String getCreatonTime() {
        return creatonTime;
    }

    public void setCreatonTime(String creatonTime) {
        this.creatonTime = creatonTime;
    }

    public int getUPLOAD_STATUS() {
        return UPLOAD_STATUS;
    }

    public void setUPLOAD_STATUS(int UPLOAD_STATUS) {
        this.UPLOAD_STATUS = UPLOAD_STATUS;
    }

    public int getFront_cover() {
        return front_cover;
    }

    public void setFront_cover(int front_cover) {
        this.front_cover = front_cover;
    }

    public int getBack_cover() {
        return back_cover;
    }

    public void setBack_cover(int back_cover) {
        this.back_cover = back_cover;
    }

    public String getNo() {
        return no != null ? no : "Null";
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getImagePos() {
        return imagePos;
    }

    public void setImagePos(int imagePos) {
        this.imagePos = imagePos;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKEY_ID() {
        return KEY_ID;
    }

    public void setKEY_ID(int KEY_ID) {
        this.KEY_ID = KEY_ID;
    }

    public String getCaption() {
        return caption != null ? caption : "Add caption";
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getImageFrom() {
        return imageFrom;
    }

    public void setImageFrom(int imageFrom) {
        this.imageFrom = imageFrom;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDownloadedComplete() {
        return isDownloadedComplete;
    }

    public void setDownloadedComplete(boolean downloadedComplete) {
        isDownloadedComplete = downloadedComplete;
    }

    public boolean isDownloadedInComplete() {
        return isDownloadedInComplete;
    }

    public void setDownloadedInComplete(boolean downloadedInComplete) {
        isDownloadedInComplete = downloadedInComplete;
    }

    public boolean isCaptioned() {
        return isCaptioned;
    }

    public void setCaptioned(boolean captioned) {
        isCaptioned = captioned;
    }

    public boolean isResized() {
        return isResized;
    }

    public void setResized(boolean resized) {
        isResized = resized;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "Null";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path != null ? path : "Null";
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public int compareTo(@NonNull MyImage o) {


        int compareage = o.getImagePos();
        /* For Ascending order*/
        return this.imagePos - compareage;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
