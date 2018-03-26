package com.photozuri.photozuri.Data.Parsers;

import android.util.Log;

import com.photozuri.photozuri.Data.Models.Images;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eric on 2/20/2018.
 */

public class ImageParser {
    public static ArrayList<Images> parse(JSONArray jsonArray) {
        ArrayList<Images> images = new ArrayList<>();
//        private String image_id;
//        private String order_id;
//        private String image;
//        private String caption;
//        private String count;
//        private String size;
//        private String page_no;
//

        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                Images image = new Images();
                image.setImage_id(jsonObject1.getString("image_id"));
                image.setOrder_id(jsonObject1.getString("order_id"));
                image.setImage(jsonObject1.getString("image"));

                image.setCaption(jsonObject1.getString("caption"));
                image.setCount(jsonObject1.getString("count"));

                image.setSize(jsonObject1.getString("size"));
                image.setPage_no(jsonObject1.getString("page_no"));


                images.add(image);

            }

        } catch (JSONException nm) {
            Log.d("response", nm.getMessage());
            nm.printStackTrace();
        }


        return images;


    }
}
