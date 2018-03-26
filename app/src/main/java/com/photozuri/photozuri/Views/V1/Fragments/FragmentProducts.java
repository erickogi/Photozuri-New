package com.photozuri.photozuri.Views.V1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.photozuri.photozuri.Adapter.CategoriesAdapter;
import com.photozuri.photozuri.Adapter.ProductsAdapter;
import com.photozuri.photozuri.Data.Models.ProductsModel;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.Parsers.ProductsParser;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.ResideMenu.ResideMenu;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;
import com.photozuri.photozuri.Views.V1.Passports;
import com.photozuri.photozuri.Views.V1.Photobooks;
import com.photozuri.photozuri.Views.V1.SinglePrint;
import com.photozuri.photozuri.Views.V1.Wallmounts;
import com.photozuri.photozuri.imagepicker.model.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 3/6/2018.
 */

public class FragmentProducts extends Fragment {
    private static ArrayList<ProductsModel> images = new ArrayList<>();
    Intent intent;
    String desc = "";
    String title = "";
    //ShimmerRecyclerView shimmerRecycler;
    //ShimmerLayout shimmerText;
    private View view;
    private AppUtils appUtils;
    private GridView gridView;
    private ResideMenu resideMenu;
    private RecyclerView recyclerView;
    private Config config;
    private ProductsAdapter adapter;
    private CategoriesAdapter imageAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PrefManager prefManager;
    private LinearLayout linearLayoutEmpty;
    private SwipeRefreshLayout swipe_refresh_layout;
    private TextView txtEmpty;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefManager = new PrefManager(getContext());
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        context = getContext();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private Context getMyContext() {
        if (context != null) {
            return context;
        } else {
            return null;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        swipe_refresh_layout = view.findViewById(R.id.swipeRefreshView);
        txtEmpty = view.findViewById(R.id.txt_empty);
        txtEmpty.setText("Refreshing");
        // swipe_refresh_layout = view.findViewById(R.id.swipeContainer);
        swipe_refresh_layout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        swipe_refresh_layout.setBackgroundResource(android.R.color.white);
        swipe_refresh_layout.setColorSchemeResources(android.R.color.white, android.R.color.holo_purple, android.R.color.white);
        swipe_refresh_layout.setRefreshing(true);

        // initviews(loanListPojos);


        swipe_refresh_layout.setOnRefreshListener(() -> {

            txtEmpty.setText("Refreshing");
            swipe_refresh_layout.setRefreshing(true);
            getData();


        });
        // initViews(getProducts());
        //  shimmerRecycler = (ShimmerRecyclerView) view.findViewById(R.id.shimmer_recycler_view);
        // shimmerRecycler.showShimmerAdapter();
        recyclerView = view.findViewById(R.id.recyclerView);
        //recyclerView.setVisibility(View.generateViewId());


        //shimmerText = view.findViewById(R.id.shimmer_text);
        //shimmerText.setAnimation(Animatio);


        getData();
    }

    void initViews(ArrayList<ProductsModel> productsModels) {
        //shimmerRecycler.setVisibility(View.GONE);


        // ArrayList<ProductsModel> productsModels = getProducts();

        //  shimmerText.
        imageAdapter = new CategoriesAdapter(getContext(), productsModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                Log.d("oncatclicked", productsModels.get(position).getId());
                if (productsModels.get(position).getId().equals("18")) {
                    startDialog(Constants.PHOTO_BOOK_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("19")) {
                    startDialog(Constants.PASSPORT_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("20")) {
                    startDialog(Constants.WALL_MOUNT_INTENT, productsModels.get(position).getCategory());
                } else if (productsModels.get(position).getId().equals("21")) {
                    startDialog(Constants.SINGLE_PRINT_INTENT, productsModels.get(position).getCategory());
                }
            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });

        imageAdapter.notifyDataSetChanged();

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //setEmptyState(imageAdapter.getItemCount()>0);
        recyclerView.setAdapter(imageAdapter);

        if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing()) {
            swipe_refresh_layout.setRefreshing(false);
        }
        setEmptyState(imageAdapter.getItemCount() > 0);
        //recyclerView.setVisibility(View.VISIBLE);

    }

    void setEmptyState(boolean state) {
        LinearLayout linearLayoutEmpty = view.findViewById(R.id.layout_empty);
        if (state) {
            txtEmpty.setText("No products to display, Pull down to refresh");

            linearLayoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            txtEmpty.setText("No products to display, Pull down to refresh");
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private ArrayList<ProductsModel> getProducts() {

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

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        return productsModels;

    }

    private void startDialog(int type, String name) {
        if (type == Constants.WALL_MOUNT_INTENT) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
            View mView = layoutInflaterAndroid.inflate(R.layout.title_desc, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
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
        } else {
            doP(type);
        }
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

                    // prefrenceManager.clearBeneficiary();
                    dialog.dismiss();


                    break;
            }
        };


        if (getMyContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());

            builder.setMessage(message).setPositiveButton("Retry", dialogClickListener)
                    .setNegativeButton("Dismiss", dialogClickListener)
                    .show();
        }

    }

    public void resideShow(View view) {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    private void doP(int type) {
        // if (GeneralUtills.isFilledTextInputEditText(edttitle) && GeneralUtills.isFilledTextInputEditText(edtdesc)) {
        desc = "desc";
        title = DateTimeUtils.getNow() + "" + type;
        Log.d("titlesave", "Fields are good");

        new DbContentValues.savetitle(new TitleModel(), getContext(), title, desc, type, DbConstants.selected, titleid -> {
            if (titleid != 9999) {
                GlobalConsts.TITLE_SELECTED = title;
                GlobalConsts.DESCRIPTION = desc;
                GlobalConsts.TITLE_ID = titleid;
                Intent intent;
                switch (type) {
                    case Constants.WALL_MOUNT_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(getContext(), Wallmounts.class);
                        GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.PASSPORT_INTENT:
                        //  dialog.dismiss();
                        intent = new Intent(getContext(), Passports.class);

                        GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.PHOTO_BOOK_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(getContext(), Photobooks.class);

                        GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.SINGLE_PRINT_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(getContext(), SinglePrint.class);

                        GlobalConsts.TYPE_SELECTED = Constants.SINGLE_PRINT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.SINGLE_PRINT_INTENT);
                        startActivity(intent);

                        break;

                    default:
                        //dialog.dismiss();
                        Log.d("titlesave", "invalidoptionselected   " + type);
                }
            } else {
                Log.d("titlesave", "Couldnit save " + titleid);
            }
        }).execute();

//        } else {
//            Log.d("titlesave", "Fields are empty");
//        }
    }

    private void getData() {

        //if(getMyContext()!=null) {
//            ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Getting Categories");
//            progressDialog.setCancelable(false);
//            // progressDialog.show();

        // }
        HashMap<String, String> params = new HashMap<>();
        params.put("Categories", "cat");

        DumbVolleyRequest.getPostData(ApiConstants.categories, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
                initViews(new ArrayList<ProductsModel>());
                //alertDialog("Error fetching categories");
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
                Log.d("responseee", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {

                        ArrayList<ProductsModel> productsModels = ProductsParser.parse(response);
                        initViews(productsModels);


                    } else {
                        initViews(new ArrayList<ProductsModel>());
                        alertDialog(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    initViews(new ArrayList<ProductsModel>());
                    //alertDialog("Error occurred fetching images");
                    e.printStackTrace();
                }
            }
        });

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
                    MyToast.toast("Select Size", getContext(), R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                    // Toast.makeText(getContext(), "Select Size", Toast.LENGTH_SHORT).show();
                } else {

                    GlobalConsts.WALLMOUNTSELECTED = radioGroup.getCheckedRadioButtonId() == R.id.radio_btn_a3 ? "A4" : "A3";
                    desc = "desc";
                    title = DateTimeUtils.getNow() + "" + type;
                    Log.d("titlesave", "Fields are good");
                    //  Context context, String title, String desc, int type, int status,
                    new DbContentValues.savetitle(new TitleModel(), getContext(), title, desc, type, DbConstants.selected, new DbContentValues.MyInterfaceTitles() {
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
                                        intent = new Intent(getContext(), Wallmounts.class);
                                        GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                                        intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                                        startActivity(intent);

                                        break;
                                    case Constants.PASSPORT_INTENT:
                                        dialog.dismiss();
                                        intent = new Intent(getContext(), Passports.class);

                                        GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                                        intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                                        startActivity(intent);

                                        break;
                                    case Constants.PHOTO_BOOK_INTENT:
                                        dialog.dismiss();
                                        intent = new Intent(getContext(), Photobooks.class);

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


                }

            } else {


            }


        }
    }


}
