package com.photozuri.photozuri.Data.Parsers;

import android.util.Log;

import com.photozuri.photozuri.Data.Models.SizesModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eric on 2/20/2018.
 */

public class SizesParser {
    public static ArrayList<SizesModels> parse(String response) {
        ArrayList<SizesModels> sizesModels = new ArrayList<>();
//        private String id;
//        private String  size;
//

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("sizes");
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                SizesModels size = new SizesModels();
                size.setId(jsonObject1.getString("id"));
                size.setSize(jsonObject1.getString("size"));


                sizesModels.add(size);

            }

        } catch (JSONException nm) {
            Log.d("response", nm.getMessage());
            nm.printStackTrace();
        }


        return sizesModels;

    }
}
