package com.photozuri.photozuri.Data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 1/26/2018.
 */

public class UserModelParse {
//    {
//        "error": false,
//            "profile": {
//        "id": 1,
//                "name": "Eric kogi",
//                "email": "erickogi14@gmail.com",
//                "mobile": "0714406984",
//                "apikey": "63bd9dd81d0135e67c1a131810f8129b",
//                "image": "http://www.erickogi.co.ke/photozuri/uploads/0714406984.png",
//                "password": "700c8b805a3e2a265b01c77614cd8b21"
//    }

    public static UserModel getUser(String response) {
        UserModel userModel = new UserModel();

        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONObject jsonObject1 = jsonObject.getJSONObject("profile");
            userModel.setUserName(jsonObject1.getString("name"));
            userModel.setFirstName(userModel.getUserName());
            userModel.setLastName(userModel.getUserName());

            userModel.setEmail(jsonObject1.getString("email"));
            userModel.setPhoneNumber(jsonObject1.getString("mobile"));
            userModel.setUserId(jsonObject1.getInt("id"));
            userModel.setPhoto(jsonObject1.getString("image"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return userModel;

    }
}
