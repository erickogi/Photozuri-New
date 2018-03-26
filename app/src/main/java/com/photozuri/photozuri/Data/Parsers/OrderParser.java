package com.photozuri.photozuri.Data.Parsers;

import android.util.Log;

import com.photozuri.photozuri.Data.Models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eric on 2/20/2018.
 */

public class OrderParser {

    public static ArrayList<Order> parse(String response) {
        ArrayList<Order> orders = new ArrayList<>();
//        private String order_id;
//        private String user_id;
//        private String title;
//        private String location;
//        private String category;
//        private String amount;
//        private String count;
//        private String description;
//        private String front_cover;
//        private String back_cover;
//        private String status;
//        private String created_at;
//        private ArrayList<Order > order s;
//

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("orders");
            for (int a = jsonArray.length() - 1; a >= 0; a--) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                Order order = new Order();
                order.setOrder_id(jsonObject1.getString("order_id"));
                order.setUser_id(jsonObject1.getString("user_id"));
                order.setTitle(jsonObject1.getString("title"));

                order.setLocation(jsonObject1.getString("location"));
                order.setCategory(jsonObject1.getString("category"));

                order.setAmount(jsonObject1.getString("amount"));
                order.setCount(jsonObject1.getString("count"));
                order.setDescription(jsonObject1.getString("description"));
                order.setFront_cover(jsonObject1.getString("front_cover"));
                order.setBack_cover(jsonObject1.getString("back_cover"));
                order.setStatus(jsonObject1.getString("status"));
                order.setCreated_at(jsonObject1.getString("created_at"));
                JSONArray jsonArray1 = jsonObject1.getJSONArray("images");

                order.setImages(ImageParser.parse(jsonArray1));


                orders.add(order);

            }

        } catch (JSONException nm) {
            Log.d("response", nm.getMessage());
            nm.printStackTrace();
        }


        return orders;


    }
}
