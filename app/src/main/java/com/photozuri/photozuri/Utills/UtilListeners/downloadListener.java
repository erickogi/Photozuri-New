package com.photozuri.photozuri.Utills.UtilListeners;

/**
 * Created by Eric on 1/17/2018.
 */

public interface downloadListener {
    void onDownloaded(String path);

    void onError(String error);

    void onDownloaded(String[][] datam);
}
