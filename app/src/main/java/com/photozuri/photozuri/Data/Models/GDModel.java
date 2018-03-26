package com.photozuri.photozuri.Data.Models;

import java.io.Serializable;

/**
 * Created by Eric on 1/10/2018.
 */

public class GDModel implements Serializable {
    private String title;
    private String thumbnailLink;
    private String fullImageLink;
    private boolean isSelected;
    private boolean isDownloadedComplete;
    private boolean isDownloadedInComplete;
    private boolean isCaptioned;
    private boolean isResized;
    private String creatonTime;

    public String getCreatonTime() {
        return creatonTime;
    }

    public void setCreatonTime(String creatonTime) {
        this.creatonTime = creatonTime;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getFullImageLink() {
        return fullImageLink;
    }

    public void setFullImageLink(String fullImageLink) {
        this.fullImageLink = fullImageLink;
    }
}
