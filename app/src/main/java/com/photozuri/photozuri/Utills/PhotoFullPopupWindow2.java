package com.photozuri.photozuri.Utills;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.PhotoPopUpListner;
import com.github.chrisbanes.photoview.PhotoView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Eric on 12/18/2017.
 */

public class PhotoFullPopupWindow2 extends PopupWindow {

    private static PhotoFullPopupWindow2 instance = null;
    View view;
    Context mContext;
    PhotoView photoView;
    ProgressBar loading;
    ViewGroup parent;

    String img_url;
    Bitmap bitmap;
    String caption;
    int id;
    OnclickRecyclerListener listener;
    View v;

    public PhotoFullPopupWindow2(Context ctx, int layout, View v, String imageUrl, Bitmap bitmap) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full_pss2, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        Button closeButton = this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dismiss();
            }
        });
        //---------Begin customising this popup--------------------

        photoView = view.findViewById(R.id.image);
        loading = view.findViewById(R.id.loading);
        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();
        // ImageUtils.setZoomable(imageView);
        //----------------------------
        if (bitmap != null) {
            loading.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 16) {
                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
            } else {
                onPalette(Palette.from(bitmap).generate());

            }
            photoView.setImageBitmap(bitmap);
        } else {
            loading.setIndeterminate(true);
            loading.setVisibility(View.VISIBLE);
//            Picasso.with(ctx).load(imageUrl).placeholder(R.drawable.ic_person_black_24dp).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    if (Build.VERSION.SDK_INT >= 16) {
//                        parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
//                    } else {
//                        onPalette(Palette.from(bitmap).generate());
//
//                    }
//                    photoView.setImageBitmap(bitmap);
//
//                    loading.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                    loading.setIndeterminate(false);
//                    loading.setBackgroundColor(Color.LTGRAY);
//                    //return false;
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });


            Glide.with(ctx)
                    .load(imageUrl)
                    //.apply(options)
                    .into(photoView);
//            if (Build.VERSION.SDK_INT >= 16) {
//                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
//            } else {
//                onPalette(Palette.from(bitmap).generate());
//
//            }
            // photoView.setImageBitmap(bitmap);

            loading.setVisibility(View.GONE);
//
//
//            GlideApp.with(ctx) .asBitmap()
//                    .load(imageUrl)
//
//                    .error(R.drawable.no_image)
//                    .listener(new RequestListener<Bitmap>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            loading.setIndeterminate(false);
//                            loading.setBackgroundColor(Color.LTGRAY);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            if (Build.VERSION.SDK_INT >= 16) {
//                                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
//                            } else {
//                                onPalette(Palette.from(resource).generate());
//
//                            }
//                            photoView.setImageBitmap(resource);
//
//                            loading.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
//
//
//
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(photoView);

            showAtLocation(v, Gravity.CENTER, 0, 0);
        }
        //------------------------------

    }

    public PhotoFullPopupWindow2(Context ctx,
                                 int layout,

                                 View v,

                                 String imageUrl,
                                 @Nullable Bitmap bitmap,
                                 String caption,
                                 int id
    ) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full_pss2, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        this.img_url = imageUrl;
        this.bitmap = bitmap;
        this.caption = caption;
        this.id = id;
        // this.listener=onDismissListener;
        this.v = view;


    }
    //------------------------------


    public void start(PhotoPopUpListner listener) {

        Button closeButton = this.view.findViewById(R.id.ib_close);
        Button closeDissmis = this.view.findViewById(R.id.ib_done);
        closeDissmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDismiss();
            }
        });
        TextInputEditText textCaption = this.view.findViewById(R.id.caption);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                DbOperations dbOperations = new DbOperations(mContext);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbConstants.IMAGE_CAPTION, textCaption.getText().toString());
                if (dbOperations.update(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, id, contentValues)) {

                    listener.onDismissave(textCaption.getText().toString());

                } else {
                    Log.d("php", "error updating KEY IS   " + id);

                }


            }
        });
        //---------Begin customising this popup--------------------

        if (caption.isEmpty() || !caption.equals("Add caption")) {
            textCaption.setText(caption);
        } else {
            textCaption.setText("1");
        }

        photoView = view.findViewById(R.id.image);
        loading = view.findViewById(R.id.loading);
        photoView.setMaximumScale(6);

        parent = (ViewGroup) photoView.getParent();
        // ImageUtils.setZoomable(imageView);
        //----------------------------
        if (bitmap != null) {
            loading.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 16) {
                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
            } else {
                onPalette(Palette.from(bitmap).generate());

            }
            photoView.setImageBitmap(bitmap);
        } else {
            loading.setIndeterminate(true);
            loading.setVisibility(View.VISIBLE);
//            Picasso.with(ctx).load(imageUrl).placeholder(R.drawable.ic_person_black_24dp).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    if (Build.VERSION.SDK_INT >= 16) {
//                        parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
//                    } else {
//                        onPalette(Palette.from(bitmap).generate());
//
//                    }
//                    photoView.setImageBitmap(bitmap);
//
//                    loading.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                    loading.setIndeterminate(false);
//                    loading.setBackgroundColor(Color.LTGRAY);
//                    //return false;
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });


            Glide.with(mContext)
                    .load(img_url)
                    //.apply(options)
                    .into(photoView);
//            if (Build.VERSION.SDK_INT >= 16) {
//                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
//            } else {
//                onPalette(Palette.from(bitmap).generate());
//
//            }
            // photoView.setImageBitmap(bitmap);

            loading.setVisibility(View.GONE);
//
//
//            GlideApp.with(ctx) .asBitmap()
//                    .load(imageUrl)
//
//                    .error(R.drawable.no_image)
//                    .listener(new RequestListener<Bitmap>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            loading.setIndeterminate(false);
//                            loading.setBackgroundColor(Color.LTGRAY);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            if (Build.VERSION.SDK_INT >= 16) {
//                                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
//                            } else {
//                                onPalette(Palette.from(resource).generate());
//
//                            }
//                            photoView.setImageBitmap(resource);
//
//                            loading.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
//
//
//
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(photoView);

            showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    public void stop() {
        dismiss();
    }

    public void onPalette(Palette palette) {
        if (null != palette) {
            ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
        }
    }
}
