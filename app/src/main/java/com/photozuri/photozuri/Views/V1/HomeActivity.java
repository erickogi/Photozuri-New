package com.photozuri.photozuri.Views.V1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.volley.error.VolleyError;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.utils.Pair;
import com.photozuri.photozuri.Adapter.CategoriesAdapter;
import com.photozuri.photozuri.Data.Models.ProductsModel;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.Parsers.ProductsParser;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.android.volley.VolleyError;

public class HomeActivity extends AppCompatActivity implements AuthListener, MpesaListener {
    Intent intent;
    String desc = "";
    String title = "";

    ArrayList<ProductsModel> productsModels;
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private CategoriesAdapter categoriesAdapter;

//    void initUI() {
//        STKPush.Builder builder = new STKPush.Builder("174379", "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919", 10, "174379", "0714406984");
//
//        //builder.setCallBackURL("192.168.120:47");
//        STKPush push = builder.build();
//
//        Mpesa.getInstance().pay(HomeActivity.this, push);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Mpesa.with(HomeActivity.this, "hAVnRxa2UOjyAnydVJMG31A0OuDDCxm5", "UcpmdCdI8bAakdgm", Mode.SANDBOX);
        super.onCreate(savedInstanceState);
        //productsModels = new ArrayList<>();

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //mRecyclerView = findViewById(R.id.recyclerView);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        });
        mRecyclerView=findViewById(R.id.recycler_view);

//        NetworkUtils networkUtils = new NetworkUtils(HomeActivity.this);
//
//        if (networkUtils.isNetworkAvailable()) {
//            getData();
//        } else {
//            alertDialog("No Internet Connection");
//        }
        // initView(productsModels);

        ImageView imgWallmount = findViewById(R.id.img_wallmount);
        ImageView imgPhotobook = findViewById(R.id.img_photobook);
        ImageView imgPassport = findViewById(R.id.img_passport);

        //PicassoClient.LoadImage(HomeActivity.);

        //Picasso p=new Picasso();
        Picasso.with(HomeActivity.this).load(R.drawable.mounted_centurion).into(imgWallmount);
        Picasso.with(HomeActivity.this).load(R.drawable.passport).into(imgPassport);
        Picasso.with(HomeActivity.this).load(R.drawable.photobook).into(imgPhotobook);



        fab.hide();*/
        //intent = new Intent(HomeActivity.this, PrintProcessActivity.class);
        //    getData(true);
    }

    private void initView(ArrayList<ProductsModel> productsModels) {


        categoriesAdapter = new CategoriesAdapter(HomeActivity.this, productsModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                Log.d("oncatclicked", productsModels.get(position).getId());
                if (productsModels.get(position).getId().equals("18")) {
                    startDialog(Constants.PHOTO_BOOK_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("19")) {
                    startDialog(Constants.PASSPORT_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("20")) {
                    startDialog(Constants.WALL_MOUNT_INTENT, productsModels.get(position).getCategory());
                }
            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });

        categoriesAdapter.notifyDataSetChanged();

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(categoriesAdapter);
    }



    public void other(View view) {
        // initUI();
        // GlobalConsts.TYPE_SELECTED=Constants.OTHER_INTENT;
        //intent.putExtra(Constants.INTENT_NAME, Constants.OTHER_INTENT);
        //startActivity(intent);
        // new PhotoFullPopupWindow(HomeActivity.this, R.layout.popup_photo_full, view, "/data/data/com.photozuri.photozuri/app_images/116254512513949thumbnail.png", null);
    }


    @Override
    public void onAuthError(Pair<Integer, String> result) {

        Log.d("mpesaerror", result.message);
    }

    @Override
    public void onAuthSuccess() {

        Log.d("mpesaerror", "succed");
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        Log.d("mpesaerror", result.message);
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {

    }


    private void getData() {

        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Getting Categories");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("Categories", "cat");

        DumbVolleyRequest.getPostData(ApiConstants.categories, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                alertDialog("Error fetching categories");
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        productsModels = ProductsParser.parse(response);
                        initView(productsModels);
                    } else {
                        alertDialog(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    alertDialog("Error occurred fetching images");
                    e.printStackTrace();
                }
            }
        });

    }

    private void getData(boolean t) {
        ArrayList<ProductsModel> productsModels = new ArrayList<>();

        ProductsModel p = new ProductsModel();
        p.setImage(R.drawable.wallj);
        p.setDescription("Great wallmounts to decorate your living room");
        p.setSizable("true");
        p.setCategory("Wallmount");
        p.setId("20");

        ProductsModel p2 = new ProductsModel();
        p2.setImage(R.drawable.pasp);
        p2.setDescription("Create awesome Passport photos");
        p2.setSizable("true");
        p2.setId("19");
        p2.setCategory("Passports");

        ProductsModel p3 = new ProductsModel();
        p3.setImage(R.drawable.bok);
        p3.setDescription("Put your memories in awesome photobooks");
        p3.setSizable("true");
        p3.setId("18");
        p3.setCategory("Photo book");

        ProductsModel p4 = new ProductsModel();
        p4.setImage(R.drawable.sinp);
        p4.setDescription("Order as single prints");
        p4.setSizable("true");
        p4.setId("21");
        p4.setCategory("Single Prints");

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        initView(productsModels);


    }


    private void startDialog(int type, String name) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(HomeActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.title_desc, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("New  " + name);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        RadioGroup radioGroupSize = mView.findViewById(R.id.radio_group_size);
        if (type == Constants.WALL_MOUNT_INTENT) {
            radioGroupSize.setVisibility(View.VISIBLE);
        } else {
            radioGroupSize.setVisibility(View.GONE);
        }

        // final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Next", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, type));
    }

    public void wallmount(View view) {


        startDialog(Constants.WALL_MOUNT_INTENT, "");
    }

    public void passport(View view) {

        startDialog(Constants.PASSPORT_INTENT, "");
    }

    public void photobook(View view) {
        startDialog(Constants.PHOTO_BOOK_INTENT, "");
    }

    private void alertDialog(String message) {
        final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:


                    dialog.dismiss();
                    getData();
                    //this.finish();
                    //saveData(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //saveData(true);
                    // prefrenceManager.clearBeneficiary();
                    dialog.dismiss();


                    break;
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        builder.setMessage(message).setPositiveButton("Retry", dialogClickListener)
                .setNegativeButton("Dismiss", dialogClickListener)
                .show();

    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        private int type;

        public CustomListener(Dialog dialog, int type) {
            this.dialog = dialog;
            this.type = type;

        }

        @Override
        public void onClick(View v) {
            Log.d("titlesave", "recieved onclick event");


            EditText edttitle = dialog.findViewById(R.id.txt_album_title);
            EditText edtdesc = dialog.findViewById(R.id.txt_album_desc);
            if (type == Constants.WALL_MOUNT_INTENT) {
                RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_size);

                if (radioGroup.getCheckedRadioButtonId() < 0) {
                    MyToast.toast("Select Size", getApplicationContext(), R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                    // Toast.makeText(HomeActivity.this, "Select Size", Toast.LENGTH_SHORT).show();
                } else {

                    GlobalConsts.WALLMOUNTSELECTED = radioGroup.getCheckedRadioButtonId() == R.id.radio_btn_a3 ? "A4" : "A3";
                    if (GeneralUtills.isFilledTextInputEditText(edttitle) && GeneralUtills.isFilledTextInputEditText(edtdesc)) {
                        desc = edtdesc.getText().toString();
                        title = edttitle.getText().toString();
                        Log.d("titlesave", "Fields are good");
                        //  Context context, String title, String desc, int type, int status,
                        new DbContentValues.savetitle(new TitleModel(), HomeActivity.this, title, desc, type, DbConstants.selected, new DbContentValues.MyInterfaceTitles() {
                            @Override
                            public void onComplete(int titleid) {
                                if (titleid != 9999) {
                                    GlobalConsts.TITLE_SELECTED = title;
                                    GlobalConsts.DESCRIPTION = desc;
                                    GlobalConsts.TITLE_ID = titleid;

                                    Log.d("titleID", "" + titleid);
                                    Intent intent;
                                    switch (type) {
                                        case Constants.WALL_MOUNT_INTENT:
                                            dialog.dismiss();
                                            intent = new Intent(HomeActivity.this, Wallmounts.class);
                                            GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                                            intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                                            startActivity(intent);

                                            break;
                                        case Constants.PASSPORT_INTENT:
                                            dialog.dismiss();
                                            intent = new Intent(HomeActivity.this, Passports.class);

                                            GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                                            intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                                            startActivity(intent);

                                            break;
                                        case Constants.PHOTO_BOOK_INTENT:
                                            dialog.dismiss();
                                            intent = new Intent(HomeActivity.this, Photobooks.class);

                                            GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                                            intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                                            //this.startActivity(intent);
                                            startActivity(intent);

                                            break;

                                        default:
                                            dialog.dismiss();
                                            Log.d("titlesave", "invalidoptionselected   " + type);
                                    }
                                } else {
                                    Log.d("titlesave", "Couldnit save " + titleid);
                                }
                            }
                        }).execute();

                    } else {
                        Log.d("titlesave", "Fields are empty");
                    }
                }

            } else {

                if (GeneralUtills.isFilledTextInputEditText(edttitle) && GeneralUtills.isFilledTextInputEditText(edtdesc)) {
                    desc = edtdesc.getText().toString();
                    title = edttitle.getText().toString();
                    Log.d("titlesave", "Fields are good");

                    new DbContentValues.savetitle(new TitleModel(), HomeActivity.this, title, desc, type, DbConstants.selected, titleid -> {
                        if (titleid != 9999) {
                            GlobalConsts.TITLE_SELECTED = title;
                            GlobalConsts.DESCRIPTION = desc;
                            GlobalConsts.TITLE_ID = titleid;
                            Intent intent;
                            switch (type) {
                                case Constants.WALL_MOUNT_INTENT:
                                    dialog.dismiss();
                                    intent = new Intent(HomeActivity.this, Wallmounts.class);
                                    GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                                    intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                                    startActivity(intent);

                                    break;
                                case Constants.PASSPORT_INTENT:
                                    dialog.dismiss();
                                    intent = new Intent(HomeActivity.this, Passports.class);

                                    GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                                    intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                                    startActivity(intent);

                                    break;
                                case Constants.PHOTO_BOOK_INTENT:
                                    dialog.dismiss();
                                    intent = new Intent(HomeActivity.this, Photobooks.class);

                                    GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                                    intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                                    startActivity(intent);

                                    break;

                                default:
                                    dialog.dismiss();
                                    Log.d("titlesave", "invalidoptionselected   " + type);
                            }
                        } else {
                            Log.d("titlesave", "Couldnit save " + titleid);
                        }
                    }).execute();

                } else {
                    Log.d("titlesave", "Fields are empty");
                }
            }


        }
    }


}
