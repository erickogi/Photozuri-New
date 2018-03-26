package com.photozuri.photozuri.Data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.TimeLine.OrderStatus;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/5/2018.
 */

public class TestDataGen {
    private DbOperations dbOperations;
    private DbContentValues dbContentValues;

    public TestDataGen(Context context) {
        dbOperations = new DbOperations(context);
    }

    public static List<TimeLineModel> setDataListItems(int selected, int uploaded) {
        List<TimeLineModel> mDataList = new ArrayList<>();


        return mDataList;

    }

    public static int getPrice(int typeSelected) {
        switch (typeSelected) {
            case Constants.PHOTO_BOOK_INTENT:
                return 1000;

            case Constants.PASSPORT_INTENT:
                return 200;

            case Constants.WALL_MOUNT_INTENT:
                return 2500;
            case Constants.SINGLE_PRINT_INTENT:
                return 200;


            default:
                return 100;

        }
    }

    public List<TimeLineModel> getInProgress() {
        List<TimeLineModel> mDataList = new ArrayList<>();

        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_TITLES,
                DbConstants.IMAGE_STATUS,
                String.valueOf(DbConstants.selected),
                String.valueOf(DbConstants.uploaded),
                String.valueOf(DbConstants.saved));
//        if (dbOperations.getCount(DbConstants.TABLE_SAVED_TITLES, DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.selected)) > 0) {
//
//            //startDialog();
//        }

        Cursor cursor2 = dbOperations.select(DbConstants.TABLE_SAVED_TITLES, DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.uploaded));

        ArrayList<TitleModel> myImages = new ArrayList<>();


        if (cursor != null) {
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    TitleModel titleModel = new TitleModel();


                    titleModel.setTitle_date(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DATE)));
                    titleModel.setTitle_id(cursor.getInt(cursor.getColumnIndex(DbConstants.KEY_ID)));
                    titleModel.setTitle_name(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_NAME)));
                    titleModel.setTitle_desc(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DESC)));
                    titleModel.setImage_no(dbOperations.getCount(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, String.valueOf(titleModel.getTitle_id())));
                    titleModel.setImage_type(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_TYPE)));
                    titleModel.setImage_status(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_STATUS)));
                    titleModel.setContactphone(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_PHONE)));
                    titleModel.setAddress(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_ADDRESS)));
                    titleModel.setAmount(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_AMOUNT)));
                    titleModel.setISPAID(cursor.getString(cursor.getColumnIndex(DbConstants.ISPAID)));
                    titleModel.setOrder_id(cursor.getString(cursor.getColumnIndex(DbConstants.ORDER_ID)));


                    titleModel.setUPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOADED_COUNT)));
                    titleModel.setUPLOAD_STATUS(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOAD_STATUS)));
                    titleModel.setUPLOAD_ORDER_ID(cursor.getString(cursor.getColumnIndex(DbConstants.UPLOAD_ORDER_ID)));
                    titleModel.setNOT_UPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.NOT_UPLOADED_COUNT)));
                    titleModel.setFRONT_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.FRONT_COVER)));
                    titleModel.setBACK_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.BACK_COVER)));





                    Log.d("dataprint", "testdata" + titleModel.getTitle_name() + "     " + titleModel.getTitle_desc() + "   " + titleModel.getTitle_id());
                    //  String mMessage,String mNoOfPhotos, String mDate,String mCoverImage, OrderStatus mStatus,String title,String[] paths,int id


                    myImages.add(titleModel);

                }
            }

            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }


            if (myImages != null && myImages.size() > 0) {

                for (TitleModel titleModel : myImages) {

//                    if (titleModel.getUPLOAD_STATUS() != GlobalConsts.SOME_UPLOADED && titleModel.getUPLOAD_STATUS() != GlobalConsts.ALL_UPLOADED)
//                    {
                        mDataList.add(new TimeLineModel(
                                titleModel.getTitle_desc(),
                                String.valueOf(titleModel.getImage_no()),
                                titleModel.getTitle_date(),
                                getCover(titleModel.getTitle_id()),
                                OrderStatus.ACTIVE,
                                titleModel.getTitle_name(),
                                new String[4],
                                titleModel.getTitle_id()


                        ));

                    //}

                }
            }

        }

        return mDataList;
    }

    public List<TimeLineModel> getInSaved() {
        List<TimeLineModel> mDataList = new ArrayList<>();

        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_TITLES,
                DbConstants.IMAGE_STATUS,
                String.valueOf(DbConstants.saved),
                String.valueOf(DbConstants.selected),
                String.valueOf(DbConstants.uploaded)
//                String.valueOf(DbConstants.saved),
//                String.valueOf(DbConstants.selected),
//                String.valueOf(DbConstants.uploaded)



        );





        Cursor cursor2 = dbOperations.select(DbConstants.TABLE_SAVED_TITLES, DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.uploaded));

        ArrayList<TitleModel> myImages = new ArrayList<>();


        if (cursor != null) {
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    TitleModel titleModel = new TitleModel();


                    titleModel.setTitle_date(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DATE)));
                    titleModel.setTitle_id(cursor.getInt(cursor.getColumnIndex(DbConstants.KEY_ID)));
                    titleModel.setTitle_name(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_NAME)));
                    titleModel.setTitle_desc(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DESC)));
                    titleModel.setImage_no(dbOperations.getCount(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID,
                            String.valueOf(titleModel.getTitle_id())));

                    Log.d("noofphotos", "FrDb" + String.valueOf(titleModel.getImage_no()));

                    titleModel.setImage_type(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_TYPE)));
                    titleModel.setImage_status(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_STATUS)));
                    titleModel.setContactphone(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_PHONE)));
                    titleModel.setAddress(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_ADDRESS)));
                    titleModel.setAmount(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_AMOUNT)));
                    titleModel.setISPAID(cursor.getString(cursor.getColumnIndex(DbConstants.ISPAID)));
                    titleModel.setOrder_id(cursor.getString(cursor.getColumnIndex(DbConstants.ORDER_ID)));


                    titleModel.setUPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOADED_COUNT)));
                    titleModel.setUPLOAD_STATUS(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOAD_STATUS)));
                    titleModel.setUPLOAD_ORDER_ID(cursor.getString(cursor.getColumnIndex(DbConstants.UPLOAD_ORDER_ID)));
                    titleModel.setNOT_UPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.NOT_UPLOADED_COUNT)));
                    titleModel.setFRONT_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.FRONT_COVER)));
                    titleModel.setBACK_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.BACK_COVER)));


                    Log.d("dataprint", "testdata" + titleModel.getTitle_name() + "     " + titleModel.getTitle_desc() + "   " + titleModel.getTitle_id());
                    //  String mMessage,String mNoOfPhotos, String mDate,String mCoverImage, OrderStatus mStatus,String title,String[] paths,int id


                    myImages.add(titleModel);

                }
            }

            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }


            if (myImages != null && myImages.size() > 0) {

                for (int a = myImages.size() - 1; a >= 0; a--) {
                    TitleModel titleModel = myImages.get(a);
//                    if (     titleModel.getUPLOAD_STATUS() != GlobalConsts.SOME_UPLOADED
//                            && titleModel.getUPLOAD_STATUS() != GlobalConsts.ALL_UPLOADED) {

                    if (titleModel.getImage_no() > 0) {
                        mDataList.add(new TimeLineModel(
                                titleModel.getTitle_desc(),
                                String.valueOf(titleModel.getImage_no()),
                                titleModel.getTitle_date(),
                                getCover(titleModel.getTitle_id()),
                                OrderStatus.ACTIVE,
                                titleModel.getTitle_name(),
                                new String[4],
                                titleModel.getTitle_id()


                        ));
                    } else {
                        dbOperations.delete(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, titleModel.getTitle_id());
                    }
                    // }


                }
            }

        }

        return mDataList;
    }

    private String getCover(int id) {
        return dbOperations.getItem(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, id);
    }
}
