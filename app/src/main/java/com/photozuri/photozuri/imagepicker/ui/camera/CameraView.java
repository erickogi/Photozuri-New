package com.photozuri.photozuri.imagepicker.ui.camera;

import com.photozuri.photozuri.imagepicker.model.Image;
import com.photozuri.photozuri.imagepicker.ui.common.MvpView;

import java.util.List;

/**
 * Created by hoanglam on 8/22/17.
 */

public interface CameraView extends MvpView {

    void finishPickImages(List<Image> images);
}
