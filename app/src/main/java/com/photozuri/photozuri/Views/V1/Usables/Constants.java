package com.photozuri.photozuri.Views.V1.Usables;

/**
 * Created by Eric on 1/8/2018.
 */

public class Constants {
    /** Codes for onActivityResult identifications */
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQ_SIGN_IN_REQUIRED = 1003;
    public static final int GRID_REQUEST = 1004;
    /** Parameter name to store preferred google account in shared pref */
    public static final String PREF_ACCOUNT_NAME = "accountName";
    /** 'Scopes' required to request Google permissions for our specific case */
    public final static String GPHOTOS_SCOPE
            = "oauth2:profile email https://www.googleapis.com/auth/drive.photos.readonly";
    public static final String INSTAGRAM_CLIENT_ID = "d12b0ce6772746e4971a6b5aef818d87";
    public static final String INSTAGRAM_CLIENT_SECRET = "8a0c71cbfd6e4bff8d5b8efb07656d5d";
    public static final String INSTAGRAM_CALLBACK_URL = "http://www.erickogi.co.ke";
    static final int Wallmount = 0;
    static final int passport = 1;
    static final int photobook = 2;
    /** Google API KEY */
    public static String URL_EXTN_API_KEY = "&key=AIzaSyCDHpo__xcASFR9B8zAoGrc1wVlHUr_dwo";

    /** URLs and extensions required for the API calls */
    public static String URL_FILES = "https://www.googleapis.com/drive/v2/files?spaces=photos";

    /** Specify the fields required, so as to reduce response data size */
    public static String URL_FILES_FIELDS = "&fields=items(mimeType,selfLink,thumbnailLink,title),nextLink";

    /** Used to construct the URL for a full-size image */
    public static String URL_FILE_EXTN = "?alt=media";

    /** Header information required for added authorization to API calls */
    public static String HEADER_NAME_AUTH = "Authorization";
    public static String HEADER_AUTH_VAL_PRFX = "Bearer ";

    /** JSON field names from the Google API result */
    public static String JSON_FIELD_ITEMS = "items";
    public static String JSON_FIELD_NEXT_LINK = "nextLink";
    public static String JSON_FIELD_SELF_LINK = "selfLink";
    public static String JSON_FIELD_TITLE = "title";
    public static String JSON_FIELD_THUMBNAIL = "thumbnailLink";
    public static String JSON_FIELD_MIME_TYPE = "mimeType";
    public static String JSON_FIELD_MIME_IMG = "image";
    public static String CLIENTID = "961390962251-pbetcpvq2vsv39nlterq10uh6kn314m8.apps.googleusercontent.com";
}
