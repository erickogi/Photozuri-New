package com.photozuri.photozuri.Views.V1.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.Data.TestDataGen;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.Payments.Payments;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.TimeLine.Orientation;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineAdapter;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineModel;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Views.V1.Passports;
import com.photozuri.photozuri.Views.V1.Photobooks;
import com.photozuri.photozuri.Views.V1.SinglePrint;
import com.photozuri.photozuri.Views.V1.Wallmounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/24/2018.
 */

public class FragmentInProgress extends Fragment {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private LinearLayout linearLayoutMenuItems;

    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private PrefManager prefManager;
    private DbOperations dbOperations;
    private DbContentValues dbContentValues;


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbContentValues = new DbContentValues();
        dbOperations = new DbOperations(getContext());

        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TestDataGen.initData();

        this.view = view;
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }

    private TitleModel getTitle(int id) {
        TitleModel titleModel = new TitleModel();

        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, id);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<TitleModel> titleModels = dbContentValues.getSavedDataTitles(cursor);
            if (titleModels != null && titleModels.size() > 0) {
                titleModel = titleModels.get(0);
                return titleModel;
            } else {
                return null;
            }
        } else {
            return null;
        }


    }

    private void initView(View view) {
        // setDataListItems();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        // getActivity().setTitle("In Progress");


        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;
        TestDataGen testDataGen = new TestDataGen(getContext());
        List<TimeLineModel> timeLineModels = testDataGen.getInProgress();
        mTimeLineAdapter = new TimeLineAdapter(timeLineModels, mOrientation, mWithLinePadding, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                int id = timeLineModels.get(position).getId();


                TitleModel titleModel = getTitle(id);
                GlobalConsts.TYPE_SELECTED = titleModel.getImage_type();
                GlobalConsts.TITLE_SELECTED = titleModel.getTitle_name();
                GlobalConsts.DESCRIPTION = titleModel.getTitle_desc();
                GlobalConsts.TITLE_ID = titleModel.getTitle_id();
                Intent intent;
                if (titleModel.getISPAID().equals(GlobalConsts.TITLE_IS_PAID)) {

                    startActivity(new Intent(getActivity(), Payments.class));
                    //finish();
                } else {
                    switch (titleModel.getImage_type()) {
                        case Constants.WALL_MOUNT_INTENT:

                            intent = new Intent(getActivity(), Wallmounts.class);
                            GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                            intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                            startActivity(intent);

                            break;
                        case Constants.PASSPORT_INTENT:

                            intent = new Intent(getActivity(), Passports.class);

                            GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                            intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                            startActivity(intent);

                            break;
                        case Constants.PHOTO_BOOK_INTENT:

                            intent = new Intent(getActivity(), Photobooks.class);
                            GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                            intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                            startActivity(intent);

                            break;

                        case Constants.SINGLE_PRINT_INTENT:

                            intent = new Intent(getActivity(), SinglePrint.class);
                            GlobalConsts.TYPE_SELECTED = Constants.SINGLE_PRINT_INTENT;
                            intent.putExtra(Constants.INTENT_NAME, Constants.SINGLE_PRINT_INTENT);
                            startActivity(intent);

                            break;

                        default:

                            Log.d("titlesave", "invalidoptionselected   " + titleModel.getImage_type());
                    }
                }
            }

            @Override
            public void onLongClickListener(int position) {
                int id = timeLineModels.get(position).getId();

                alertDialogDelete("Continue to delete this item ? ", id);
            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }


    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }

    private void alertDialogDelete(final String message, int id) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //popOutFragments();
                        // int id = timeLineModels.get(position).getId();


                        TitleModel titleModel = getTitle(id);
                        Boolean titledel = dbOperations.delete(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, titleModel.getTitle_id());
                        Boolean photodel = dbOperations.delete(DbConstants.TABLE_SAVED_DATA, DbConstants.SPAlbum_ID, titleModel.getTitle_id());

                        dialog.dismiss();
                        initView(view);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();

    }

}
