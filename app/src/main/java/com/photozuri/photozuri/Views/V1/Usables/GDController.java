package com.photozuri.photozuri.Views.V1.Usables;

import android.util.Log;

import com.photozuri.photozuri.Data.Models.GDModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Controller class to abstract the logic of interfacing with Google Drive APIs
 */
public class GDController implements  Serializable {

    /** Hold the Google Auth token */
    private  static String googleAuthToken = null;
    /** Singleton instance of this controller **/
    private static GDController myInstance = null;
    /**
     * DataStructure to hold search results from Google Photos
     */
    private ArrayList<GDModel> imageFeed = new ArrayList<>();
    /** URL to the paginated next set of images */
    private String nextLink = null;

    /** Tag for Logging messages */
    private String TAG = "GDController";

    /**
     * Placeholder singleton constructor
     */
    private GDController() {}

    /**
     * Return the singleton instance of Google Play Controller
     *
     * @return
     */
    public static GDController getInstance() {
        if(myInstance == null) {
            myInstance = new GDController();
        }
        return myInstance;
    }

    /**
     * Set the Auth Token to be used; this is to be set before firing the getPhotos() method
     * @param token
     */
    public static void setAPIToken(String token) {
        googleAuthToken = token;
    }

    /**
     * Get the list of MyImage data objects
     * @return
     */
    public ArrayList<GDModel> getImageFeed() {
        return imageFeed;
    }

    /**
     * Check if more images are available to be loaded
     * @return
     */
    public boolean isMoreAvailable() {
        return nextLink != null;
    }

    /**
     * Get the photos response from server, and parse the JSON response and populate the
     * DataStructure that is to hold the results
     *
     * @param currentPage
     * @return
     */
    public ArrayList<GDModel> getPhotos(int currentPage) {

        ArrayList<GDModel> photos = new ArrayList<>();

        String serverResponse = fetchFromServer(getUrl(currentPage));

        if (!serverResponse.isEmpty()) {
            try {

                JSONObject response = new JSONObject(serverResponse);
                // If response doesnt have nextLink, then set the variable to null so that
                // we can detect that there are no more pages to come
                if(response.has(Constants.JSON_FIELD_NEXT_LINK)) {
                    nextLink = response.getString(Constants.JSON_FIELD_NEXT_LINK);
                } else {
                    nextLink = null;
                }
                // Extract the JSONArray containing objects representing each image.
                JSONArray itemsArray = response.getJSONArray(Constants.JSON_FIELD_ITEMS);
                Log.d(TAG,"Received " + itemsArray.length() + " photos");
                String mimeType;

                // Iterate the list of images, extract information and build the photo list
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jObj = itemsArray.getJSONObject(i);

                    //Result will have images and videos. Skip if a video is encountered
                    mimeType = jObj.getString(Constants.JSON_FIELD_MIME_TYPE);
                    if(null != mimeType && mimeType.startsWith(Constants.JSON_FIELD_MIME_IMG)) {
                        //   Log.d("gdgphotos",jObj.toString());
                        GDModel photo = new GDModel();
//                        public static String URL_EXTN_API_KEY = "&key=AIzaSyBjnB1757MwS7b97oZdc-4Z0jsDCOjzoVY";
//                        /** URLs and extensions required for the API calls */
//                        public static String URL_FILES = "https://www.googleapis.com/drive/v2/files?spaces=photos";
//                        /** Specify the fields required, so as to reduce response data size */
//                        public static String URL_FILES_FIELDS = "&fields=items(mimeType,selfLink,thumbnailLink,title),nextLink";
//                        /** Used to construct the URL for a full-size image */
//                        public static String URL_FILE_EXTN = "?alt=media";
//                        /** Header information required for added authorization to API calls */
//                        public static String HEADER_NAME_AUTH = "Authorization";
//                        public static String HEADER_AUTH_VAL_PRFX = "Bearer ";
//                        /** JSON field names from the Google API result */
//                        public static String JSON_FIELD_ITEMS = "items";
//                        public static String JSON_FIELD_NEXT_LINK = "nextLink";
//                        public static String JSON_FIELD_SELF_LINK = "selfLink";
//                        public static String JSON_FIELD_TITLE = "title";
//                        public static String JSON_FIELD_THUMBNAIL = "thumbnailLink";
//                        public static String JSON_FIELD_MIME_TYPE = "mimeType";
//                        public static String JSON_FIELD_MIME_IMG = "image";





                        photo.setTitle(jObj.getString(Constants.JSON_FIELD_TITLE));
                        photo.setThumbnailLink(jObj.getString(Constants.JSON_FIELD_THUMBNAIL));
                        photo.setFullImageLink(jObj.getString(Constants.JSON_FIELD_SELF_LINK).trim() +
                                Constants.URL_FILE_EXTN + Constants.URL_EXTN_API_KEY);
                        Log.d("gdgphotos", photo.getFullImageLink());
                        photos.add(photo);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return photos;
    }


    /**
     * Construct and return the search url
     *
     * @param currentPage
     * @return
     */
    private String getUrl(int currentPage) {
        String url = null;
        // If its the initial API inocation, construct the URL for Files API, else, use the 'nextLink'
        if(currentPage == 1) {
            url = Constants.URL_FILES + Constants.URL_FILES_FIELDS + Constants.URL_EXTN_API_KEY;
        } else {
            url = nextLink + Constants.URL_EXTN_API_KEY;
        }
        return url;
    }

    /**
     * Invoke the API URL, extract images out of the whole response, construct image objects
     * and populate them into the imageFeed ArrayList.
     * @param strURL
     * @return
     */
    private String fetchFromServer(String strURL) {
        StringBuffer response = new StringBuffer();
        HttpURLConnection urlConnection = null;
        String line = "";

        try {
            URL url = new URL(strURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                // Add the AuthToken to header
                urlConnection.setRequestProperty (Constants.HEADER_NAME_AUTH,
                        Constants.HEADER_AUTH_VAL_PRFX + googleAuthToken);
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
            InputStream in;
            int status = urlConnection.getResponseCode();
            Log.d(TAG,"Server response: " + status);


            if(status >= 400)
                in = urlConnection.getErrorStream();
            else
                in = urlConnection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            Log.d(TAG,e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        Log.d(TAG,response.toString());
        Log.d(TAG, "Server response: " + response);

        return response.toString();
    }

    /**
     * Data model for a photo object
     */
//    public class GDModel implements Serializable {
//        public String title;
//        public String thumbnailLink;
//        public String fullImageLink;
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getThumbnailLink() {
//            return thumbnailLink;
//        }
//
//        public void setThumbnailLink(String thumbnailLink) {
//            this.thumbnailLink = thumbnailLink;
//        }
//
//        public String getFullImageLink() {
//            return fullImageLink;
//        }
//
//        public void setFullImageLink(String fullImageLink) {
//            this.fullImageLink = fullImageLink;
//        }
//    }
}
