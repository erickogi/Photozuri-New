package com.photozuri.photozuri.NetworkUtills;

/**
 * Created by Eric on 12/15/2017.
 */

public class ApiConstants {
    public static final String categories = "https://www.photozuri.com/app/code/pd_categories.php";
    public static final String sizes = "https://www.photozuri.com/app/code/pb_printsizes.phppd_categories";
    public static final String categoryimgurl = "http://photozuri.com/app/images/categories/";
    //http://photozuri.com/app/images/categories/
    private static final String online4 = "http://192.168.0.29";
    private static final String online2 = "http://192.168.43.52/photozuriserver/";
    private static final String onlineb = "http://www.erickogi.co.ke/photozuri/";
    private static final String online = "https://www.photozuri.com/photozuri/";
    //http://localhost/photozuri/photozuri/Login/Update_Profile.php
    public static final String login_endpoint = online + "Login/Login.php?";//Apend userId and password ie UserID=6287&Password=6287
    public static final String register_endpoint = online + "Login/Register.php?";//Apend userId and password ie UserID=6287&Password=6287
    public static final String update_profile_endpoint = online + "Login/Update_Profile.php?";//Apend userId and password ie UserID=6287&Password=6287
    public static final String upload = online + "Upload/upload_images.php";
    public static final String orders = online + "Orders/getOrders.php";
    private static final String companyId = "4";
    private static final String baseUrl = "http://akida.azurewebsites.net/api/";
}
