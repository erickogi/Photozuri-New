package com.photozuri.photozuri.Data.Parsers;

import android.util.Log;

import com.photozuri.photozuri.Data.Models.ProductsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eric on 2/20/2018.
 */

public class ProductsParser {
    public static ArrayList<ProductsModel> parse(String response) {
        ArrayList<ProductsModel> productsModels = new ArrayList<>();
//        private String id;
//        private String category;
//        private  String sizable;
//        private String imageurl;
//        private String description;
//

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("categories");
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                ProductsModel productsModel = new ProductsModel();
                productsModel.setId(jsonObject1.getString("id"));
                productsModel.setCategory(jsonObject1.getString("category"));
                productsModel.setSizable(jsonObject1.getString("sizable"));

                productsModel.setImageurl(jsonObject1.getString("imageurl"));
                productsModel.setDescription(jsonObject1.getString("description"));


                productsModels.add(productsModel);

            }

        } catch (JSONException nm) {
            Log.d("response", nm.getMessage());
            nm.printStackTrace();
        }


        return productsModels;
    }
}
