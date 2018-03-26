package com.photozuri.photozuri.Utills;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Views.V1.Usables.Constants;
import com.photozuri.photozuri.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric on 1/17/2018.
 */

public class AppUtils {
    private static String FILTERTAG = "filtertag";
    private Context context;
    private Picasso authPicasso = null;

    public AppUtils(Context context) {
        this.context = context;
    }

    public static String addCommify(String no) {
        String commified = "0.0";
        Double number = 0.0;
        try {
            number = Double.valueOf(no);
        } catch (Exception nfe) {

        }
        NumberFormat anotherFormatU = NumberFormat.getNumberInstance(Locale.US);
        if (anotherFormatU instanceof DecimalFormat) {
            DecimalFormat anotherDFormat = (DecimalFormat) anotherFormatU;
            anotherDFormat.applyPattern("#.00");
            anotherDFormat.setGroupingUsed(true);
            anotherDFormat.setGroupingSize(3);

            commified = anotherDFormat.format(number);
        }


        return commified;
    }

    public static String removeCommify(String no) {
        String regex = "(?<=[\\d])(,)(?=[\\d])";
        Pattern p = Pattern.compile(regex);
        String str = no;
        Matcher m = p.matcher(str);
        str = m.replaceAll("");
        return str;
    }

    public ArrayList<MyImage> convertImages(ArrayList<Image> images, int a) {
        ArrayList<MyImage> imagees = new ArrayList<>();

        for (Image image : images) {

            MyImage image1 = new MyImage();
            image1.setCreatonTime(GeneralUtills.getImageCreationDate(image.getPath()));
            image1.setId(image.getId());
            image1.setName(image.getName());
            image1.setPath(image.getPath());
            imagees.add(image1);
        }
        return imagees;
    }

    public ArrayList<Image> convertImages(ArrayList<MyImage> images) {
        ArrayList<Image> imagees = new ArrayList<>();

        for (MyImage image : images) {
            Image image1 = new Image(image.getId(), image.getName(), image.getPath());
            imagees.add(image1);
        }
        return imagees;
    }

    public ArrayList<MyImage> updateList(ArrayList<MyImage> imageList, ArrayList<MyImage> newImageList) {
        ArrayList<MyImage> sortedList = new ArrayList<>();

        sortedList.addAll(newImageList);
        sortedList.addAll(imageList);

        for (MyImage image : sortedList) {
            Log.d("returnedd", "inside updatelist util  " + image.getPath() + "" + image.getName());
        }
        HashMap<String, MyImage> myImageHashMap = new HashMap<>();

        for (MyImage image : sortedList) {
            String path = image.getPath() + "" + image.getName();
            myImageHashMap.put(path, image);
        }

        ArrayList<MyImage> myImages = new ArrayList<>();

        for (String key : myImageHashMap.keySet()) {

            MyImage myImage = myImageHashMap.get(key);
            myImages.add(myImage);

        }


        return myImages;

    }

    public ArrayList<GDModel> filterPhotos(ArrayList<GDModel> data) {
        ArrayList<GDModel> gdModels = new ArrayList<>();


        Map<String, GDModel> map = new HashMap<>();
        for (GDModel image : data) {
            map.put(image.getFullImageLink() + "" + image.getTitle(), image);
        }


        for (String key : map.keySet()) {

            GDModel gdModel = map.get(key);
            gdModels.add(gdModel);

        }


        return gdModels;
    }

    public ArrayList<GDModel> addPhotos(ArrayList<GDModel> photos, GDModel newPhoto) {
        ArrayList<GDModel> gdModels = new ArrayList<>();

        if (!photos.contains(newPhoto)) {

            Map<String, GDModel> map = new HashMap<>();
            for (GDModel image : photos) {
                map.put(image.getFullImageLink() + image.getTitle(), image);
            }
            map.put(newPhoto.getFullImageLink(), newPhoto);


            for (String key : map.keySet()) {

                GDModel gdModel = map.get(key);
                gdModels.add(gdModel);

            }

            return gdModels;

        } else {
            return photos;
        }


    }

    public ArrayList<MyImage> getNewUniqueImages(ArrayList<MyImage> oldImages, ArrayList<MyImage> newImages) {
        // ArrayList<MyImage> uniqueImages=new ArrayList<>();

        ArrayList<MyImage> sortedList = new ArrayList<>();

        sortedList.addAll(oldImages);
        sortedList.addAll(newImages);

        for (MyImage image : sortedList) {
            Log.d("returnedd", "inside updatelist util  " + image.getPath() + "" + image.getName());
        }


        HashMap<String, MyImage> myImageHashMap = new HashMap<>();

        for (MyImage image : sortedList) {
            String path = image.getPath() + "" + image.getName();
            myImageHashMap.put(path, image);
        }

        ArrayList<MyImage> myImages = new ArrayList<>();

        for (String key : myImageHashMap.keySet()) {

            MyImage myImage = myImageHashMap.get(key);
            myImages.add(myImage);

        }
        myImages.removeAll(oldImages);


        return myImages;
    }

    /**
     * Get a reference to Picasso, modified to add Authorization header to network calls
     *
     *
     * @param authToken
     * @return
     */
    public Picasso getAuthPicasso(final String authToken) {
        if (authPicasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);

            builder.downloader(new UrlConnectionDownloader(context) {
                @Override
                protected HttpURLConnection openConnection(Uri uri) throws IOException {
                    HttpURLConnection connection = super.openConnection(uri);
                    connection.setRequestProperty(Constants.HEADER_NAME_AUTH,
                            com.photozuri.photozuri.Views.V1.Usables.Constants.HEADER_AUTH_VAL_PRFX + authToken);
                    return connection;
                }
            });

            authPicasso = builder.build();
        }
        return authPicasso;
    }
}
