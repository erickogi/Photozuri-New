package com.photozuri.photozuri.imagepicker.ui.camera;


import com.photozuri.photozuri.imagepicker.model.Image;

import java.util.List;

public interface OnImageReadyListener {
    void onImageReady(List<Image> images);
}
