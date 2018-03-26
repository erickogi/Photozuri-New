package com.photozuri.photozuri.NetworkUtills;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.photozuri.photozuri.Data.Models.ImageUpload;
import com.photozuri.photozuri.NetworkUtills.volley.MyApplication;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;

import java.util.HashMap;
import java.util.Map;

//import com.android.volley.toolbox.StringRequest;

/**
 * Created by Eric on 12/15/2017.
 */

public class DumbVolleyRequest {
    static String responseObj = null;

    public static String getPostData(String url, HashMap<String, String> params, RequestListener listener) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, response -> {


            // responseObj = response;
            Log.d("dvrR", response);
            listener.onSuccess(response);


        }, error -> {
            Log.e("dvrVE", "Error: " + error.getMessage());
            listener.onError(error);

        }) {

            @Override
            protected Map<String, String> getParams() {

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }

        };
       // strReq.setHeaders();
        //strReq.setHeaders();



        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);


        return responseObj;

    }

    public static String getGetData(String url, RequestListener requestListener) {

        //responseObj = response;
// Log.d("dvrR", response);
        // Log.e("dvrVE", "Error: " + error.getMessage());
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, requestListener::onSuccess, requestListener::onError) {


        };


        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);


        return responseObj;

    }


    public static void imageUpload(final String imagePath, ImageUpload imageUpload, String url, RequestListener listener) {


        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response", response);

                    Log.d("dvrR", response);
                    listener.onSuccess(response);

                },
                error -> {
                    Log.e("dvrVE", "Error: " + error.getMessage());
                    listener.onError(error);
                }) {


        };

        smr.addMultipartParam("userId", "text/plain", imageUpload.getUserId());
        smr.addMultipartParam("title", "text/plain", imageUpload.getTitle());
        smr.addMultipartParam("category", "text/plain", imageUpload.getCategory());
        smr.addMultipartParam("photoname", "text/plain", "");
        smr.addMultipartParam("location", "text/plain", imageUpload.getLocation());
        smr.addMultipartParam("amount", "text/plain", imageUpload.getAmount());
        smr.addMultipartParam("count", "text/plain", imageUpload.getCount());
        smr.addMultipartParam("description", "text/plain", imageUpload.getDescription());

        smr.addMultipartParam("front_cover", "text/plain", imageUpload.getFront_cover());
        smr.addMultipartParam("back_cover", "text/plain", imageUpload.getBack_cover());
        smr.addMultipartParam("status", "text/plain", "new");
        smr.addMultipartParam("mobile", "text/plain", imageUpload.getMobile());
        smr.addMultipartParam("order_id", "text/plain", imageUpload.getOrder_id());
        smr.addMultipartParam("image", "text/plain", imageUpload.getImage());
        smr.addMultipartParam("caption", "text/plain", imageUpload.getCaption());
        smr.addMultipartParam("no_count", "text/plain", imageUpload.getNo_count());
        smr.addMultipartParam("size", "text/plain", imageUpload.getSize());

        smr.addMultipartParam("page_no", "text/plain", imageUpload.getPage_no());
        smr.addMultipartParam("uploadtype", "text/plain", imageUpload.getUploadtype());


        smr.addFile("imagep", imagePath);



        // smr.addStringParam()
        MyApplication.getInstance().addToRequestQueue(smr);

    }

}
