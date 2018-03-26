package com.photozuri.photozuri.imagepicker.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.photozuri.photozuri.R;
import com.photozuri.photozuri.imagepicker.model.Config;

/**
 * Created by hoanglam on 8/11/17.
 */

public class ImagePickerToolbar extends RelativeLayout {

    private TextView titleText, doneText, titleCount;
    private AppCompatImageView backImage, cameraImage;

    public ImagePickerToolbar(Context context) {
        super(context);
        init(context);
    }

    public ImagePickerToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImagePickerToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.imagepicker_toolbar, this);
        if (isInEditMode()) {
            return;
        }

        titleText = findViewById(R.id.text_toolbar_title);
        titleCount = findViewById(R.id.text_toolbar_coint);
        doneText = findViewById(R.id.text_toolbar_done);
        backImage = findViewById(R.id.image_toolbar_back);
        cameraImage = findViewById(R.id.image_toolbar_camera);
    }

    public void config(Config config) {
        setBackgroundColor(config.getToolbarColor());

        titleText.setText(config.isFolderMode() ? config.getFolderTitle() : config.getImageTitle());
        titleText.setTextColor(config.getToolbarTextColor());

        titleCount.setTextColor(config.getToolbarTextColor());

        doneText.setText(config.getDoneTitle());
        doneText.setTextColor(config.getToolbarTextColor());

        backImage.setColorFilter(config.getToolbarIconColor());

        cameraImage.setColorFilter(config.getToolbarIconColor());
        cameraImage.setVisibility(config.isShowCamera() ? VISIBLE : GONE);

        doneText.setVisibility(GONE);
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void showDoneButton(boolean isShow) {
        doneText.setVisibility(isShow ? VISIBLE : GONE);
        //  doneText.setText("DONE "+count);
    }

    public void setOnBackClickListener(OnClickListener clickListener) {
        backImage.setOnClickListener(clickListener);
    }

    public void setOnCameraClickListener(OnClickListener clickListener) {
        cameraImage.setOnClickListener(clickListener);
    }

    public void setOnDoneClickListener(OnClickListener clickListener) {
        doneText.setOnClickListener(clickListener);
    }

    public void setCount(int size) {

        titleCount.setText(String.valueOf(size));
        titleCount.setVisibility(VISIBLE);
    }
}
