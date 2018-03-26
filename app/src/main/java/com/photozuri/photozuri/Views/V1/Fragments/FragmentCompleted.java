package com.photozuri.photozuri.Views.V1.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.photozuri.photozuri.Data.Models.Order;
import com.photozuri.photozuri.Data.Parsers.OrderParser;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.TimeLine.OrderStatus;
import com.photozuri.photozuri.Utills.TimeLine.Orientation;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineAdapter;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineModel;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;
import com.photozuri.photozuri.Views.Login.LoginActivity;
import com.photozuri.photozuri.Views.V1.ViewOrders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.android.volley.VolleyError;

/**
 * Created by Eric on 1/24/2018.
 */

public class FragmentCompleted extends Fragment {
    ArrayList<Order> orders;
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private LinearLayout linearLayoutMenuItems;

    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private PrefManager prefManager;
    private NetworkUtils networkUtils;
    private ProgressDialog progressDialog;

    private View view;
    private Context context;

    private SwipeRefreshLayout swipe_refresh_layout;
    private TextView txtEmpty;

    public static List<TimeLineModel> setDataListItems(ArrayList<Order> orders) {
        List<TimeLineModel> mDataList = new ArrayList<>();

        if (orders != null && orders.size() > 0) {

            for (Order order : orders) {

                int a = 999;
                try {
                    a = Integer.valueOf(order.getOrder_id());
                } catch (Exception nm) {
                }
                mDataList.add(new TimeLineModel(
                        order.getDescription(),
                        String.valueOf(order.getCount()),
                        order.getCreated_at(),
                        order.getFront_cover(),
                        OrderStatus.ACTIVE,
                        order.getTitle(),
                        new String[4],

                        a,
                        order.getStatus()


                ));


            }
        }
        return mDataList;

    }

    private Context getMyContext() {
        if (context != null) {
            return context;
        } else {
            return null;
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getActivity().setTitle("Delivered");

        CollapsingToolbarLayout collapsingToolbarLayout = getActivity().findViewById(R.id.collapsingAB_layout);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
        //collapsingToolbarLayout.setcol
        this.view = view;
        context = getContext();
        orders = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        prefManager = new PrefManager(getContext());
        networkUtils = new NetworkUtils(getContext());

        txtEmpty = view.findViewById(R.id.txt_empty);
        txtEmpty.setText("Refreshing");

        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;


        swipe_refresh_layout = view.findViewById(R.id.swipeRefreshView);
        swipe_refresh_layout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        swipe_refresh_layout.setBackgroundResource(android.R.color.white);
        swipe_refresh_layout.setColorSchemeResources(android.R.color.white, android.R.color.holo_purple, android.R.color.white);
        swipe_refresh_layout.setRefreshing(true);

        // initviews(loanListPojos);


        swipe_refresh_layout.setOnRefreshListener(() -> {


            swipe_refresh_layout.setRefreshing(true);
            if (prefManager.isLoggedIn() && networkUtils.isNetworkAvailable()) {

                getImages();

            } else if (prefManager.isLoggedIn() && !networkUtils.isNetworkAvailable()) {
                swipe_refresh_layout.setRefreshing(false);
                alertNoInternet("You have no Internet Connection");
                setEmptyState(false);

            } else {
                setEmptyState(false);
                alertLogin("Please Log in to view yout previous orders");
            }
        });
        if (prefManager.isLoggedIn() && networkUtils.isNetworkAvailable()) {


            if (prefManager.isLoggedIn()) {
                getImages();
            }

        } else if (prefManager.isLoggedIn() && !networkUtils.isNetworkAvailable()) {
            swipe_refresh_layout.setRefreshing(false);
            alertNoInternet("You have no Internet Connection");
            setEmptyState(false);

        } else {
            alertLogin("Please Log in to view yout previous orders");
            setEmptyState(false);
        }

//        if (!prefManager.isLoggedIn()) {
//
//        }

//        if(networkUtils.isNetworkAvailable()) {
//
//            getImages();
//
//        }else {
//            MyToast.toast("No Internet Connection",getContext(),R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
//        }
    }

    private void alertLogin(String s) {
        final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:


                    try {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("Action", Constants.LOGIN_EMPTY);
                        startActivity(intent);

                    } catch (Exception nm) {

                    }
                    dialog.dismiss();


                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    dialog.dismiss();

                    break;
            }
        };


        if (getMyContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());

            builder.setMessage(s).setPositiveButton("Login/Register", dialogClickListener)
                    .setNegativeButton("Dismiss", dialogClickListener)
                    .show();
        }
    }

    private void getImages() {

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(prefManager.getUserData().getUserId()));
        DumbVolleyRequest.getPostData(ApiConstants.orders, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {

                List<TimeLineModel> timeLineModels = new ArrayList<>();
                initView(timeLineModels);
                //alertNoInternet("Error Getting your Images");
                Log.d("OrderError", error.toString());
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {
                Log.d("OrderResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        orders = OrderParser.parse(response);
                        initView(setDataListItems(orders));

                    } else {

                        List<TimeLineModel> timeLineModels = new ArrayList<>();
                        initView(timeLineModels);


                        alertNoInternet(jsonObject.getString("message"));
                        Log.d("OrderError", jsonObject.getString("message"));
                    }
                } catch (JSONException e) {

                    List<TimeLineModel> timeLineModels = new ArrayList<>();
                    initView(timeLineModels);
                    // alertNoInternet("Error 2 Getting your Images");
                    Log.d("OrderError", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView(List<TimeLineModel> mDataList) {
        // setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

                Intent intent = new Intent(getActivity(), ViewOrders.class);
                intent.putExtra("data", orders.get(position));
                startActivity(intent);

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });
        mRecyclerView.setAdapter(mTimeLineAdapter);

            if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(false);

            }

        setEmptyState(mTimeLineAdapter.getItemCount() > 0);
    }

    void setEmptyState(boolean state) {
        LinearLayout linearLayoutEmpty = view.findViewById(R.id.layout_empty);
        if (state) {
            txtEmpty.setText("No Orders to display, Pull down to refresh");

            linearLayoutEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        } else {
            txtEmpty.setText("No Orders to display, Pull down to refresh");
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }

    private void alertNoInternet(String message) {

            final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:


                        dialog.dismiss();


                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        dialog.dismiss();

                        break;
                }
            };
        if (getMyContext() != null) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());

            builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                    // .setNegativeButton("Download Form", dialogClickListener)
                    .show();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (networkUtils.isNetworkAvailable()) {

            getImages();

        } else {
            if (swipe_refresh_layout != null && swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(false);
            }
            setEmptyState(false);
            // alertNoInternet("You have no Internet Connection");

        }
    }
}
