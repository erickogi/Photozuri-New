package com.photozuri.photozuri.Data.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.photozuri.photozuri.Data.Models.LocationModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.StoryAlbum;
import com.photozuri.photozuri.Data.Models.StoryPhoto;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.DateTimeUtils;

import java.util.ArrayList;

/**
 * Created by Eric on 1/17/2018.
 */

public class DbContentValues {
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

    public ArrayList<MyImage> getSavedData(Cursor cursor) {


        ArrayList<MyImage> myImages = new ArrayList<>();


        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                MyImage myImage = new MyImage();

                myImage.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_ID)));
                myImage.setName(cursor.getString(cursor.getColumnIndex(DbConstants.IMAGE_NAME)));
                myImage.setPath(cursor.getString(cursor.getColumnIndex(DbConstants.IMAGE_PATH)));
                myImage.setCaption(cursor.getString(cursor.getColumnIndex(DbConstants.IMAGE_CAPTION)));
                myImage.setImageFrom(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_FROM)));
                myImage.setKEY_ID(cursor.getInt(cursor.getColumnIndex(DbConstants.KEY_ID)));
                myImage.setTitle_id(cursor.getInt(cursor.getColumnIndex(DbConstants.TITLE_ID)));
                myImage.setCreatonTime(cursor.getString(cursor.getColumnIndex(DbConstants.CREATION_TIME)));

                myImage.setImagePos(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_POSITION)));
                myImage.setUPLOAD_STATUS(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOAD_STATUS)));
                myImages.add(myImage);

            }
        }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }


        return myImages;
    }

    public ArrayList<LocationModel> getSavedDelivery(Cursor cursor) {


        ArrayList<LocationModel> deliverys = new ArrayList<>();


        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                LocationModel delivery = new LocationModel();

                delivery.setTown(cursor.getString(cursor.getColumnIndex(DbConstants.town)));
                delivery.setBuilding(cursor.getString(cursor.getColumnIndex(DbConstants.building)));
                delivery.setStreet(cursor.getString(cursor.getColumnIndex(DbConstants.street)));
                delivery.setMobile(cursor.getString(cursor.getColumnIndex(DbConstants.mobile)));
                delivery.setLat(cursor.getString(cursor.getColumnIndex(DbConstants.lat)));
                delivery.setLon(cursor.getString(cursor.getColumnIndex(DbConstants.lon)));
                deliverys.add(delivery);

            }
        }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }


        return deliverys;
    }


    public ArrayList<TitleModel> getSavedDataTitles(Cursor cursor) {


        ArrayList<TitleModel> titles = new ArrayList<>();


        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                TitleModel titleModel = new TitleModel();


                titleModel.setTitle_id(cursor.getInt(cursor.getColumnIndex(DbConstants.KEY_ID)));
                titleModel.setTitle_name(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_NAME)));
                titleModel.setTitle_desc(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DESC)));
                titleModel.setTitle_date(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_DATE)));
                titleModel.setImage_type(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_TYPE)));
                titleModel.setImage_status(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_STATUS)));
                titleModel.setImage_no(cursor.getInt(cursor.getColumnIndex(DbConstants.IMAGE_NO)));
                titleModel.setContactphone(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_PHONE)));
                titleModel.setAddress(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_ADDRESS)));
                titleModel.setAmount(cursor.getString(cursor.getColumnIndex(DbConstants.TITLE_AMOUNT)));


                titleModel.setUPLOAD_ORDER_ID(cursor.getString(cursor.getColumnIndex(DbConstants.UPLOAD_ORDER_ID)));
                titleModel.setUPLOAD_STATUS(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOAD_STATUS)));
                titleModel.setUPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.UPLOADED_COUNT)));
                titleModel.setUPLOAD_ORDER_ID(cursor.getString(cursor.getColumnIndex(DbConstants.UPLOAD_ORDER_ID)));
                titleModel.setNOT_UPLOADED_COUNT(cursor.getInt(cursor.getColumnIndex(DbConstants.NOT_UPLOADED_COUNT)));

                titleModel.setBACK_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.BACK_COVER)));
                titleModel.setFRONT_COVER(cursor.getString(cursor.getColumnIndex(DbConstants.FRONT_COVER)));
                titleModel.setISPAID(cursor.getString(cursor.getColumnIndex(DbConstants.ISPAID)));
                titleModel.setOrder_id(cursor.getString(cursor.getColumnIndex(DbConstants.ORDER_ID)));

                titles.add(titleModel);

            }
        }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }


        return titles;
    }


    public ArrayList<StoryAlbum> getSavedAlbums(Cursor cursor) {


        ArrayList<StoryAlbum> myImages = new ArrayList<>();


        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                StoryAlbum myImage = new StoryAlbum();

                myImage.setKEY_ID(cursor.getString(cursor.getColumnIndex(DbConstants.KEY_ID)));
                myImage.setTitle(cursor.getString(cursor.getColumnIndex(DbConstants.SAtitle)));
                myImage.setDescription(cursor.getString(cursor.getColumnIndex(DbConstants.SAdescription)));
                myImage.setCount(cursor.getString(cursor.getColumnIndex(DbConstants.SAcount)));
                myImage.setDate(cursor.getString(cursor.getColumnIndex(DbConstants.SAdate)));
                myImage.setCover_image(cursor.getString(cursor.getColumnIndex(DbConstants.SAcover_image)));
                myImages.add(myImage);

            }
        }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }


        return myImages;
    }

    public ArrayList<StoryPhoto> getSavedPhotos(Cursor cursor) {


        ArrayList<StoryPhoto> myImages = new ArrayList<>();


        if (!cursor.isLast()) {

            while (cursor.moveToNext()) {

                StoryPhoto myImage = new StoryPhoto();

                myImage.setKEY_ID(cursor.getString(cursor.getColumnIndex(DbConstants.KEY_ID)));
                myImage.setAlbum_ID(cursor.getString(cursor.getColumnIndex(DbConstants.SPAlbum_ID)));
                myImage.setPath(cursor.getString(cursor.getColumnIndex(DbConstants.SPpath)));
                myImage.setTitle(cursor.getString(cursor.getColumnIndex(DbConstants.SPtitle)));
                myImage.setDescription(cursor.getString(cursor.getColumnIndex(DbConstants.SPdescription)));
                myImage.setDate(cursor.getString(cursor.getColumnIndex(DbConstants.SPdate)));
                myImage.setPosition(cursor.getString(cursor.getColumnIndex(DbConstants.SPposition)));
                myImage.setPosition(cursor.getString(cursor.getColumnIndex(DbConstants.SPposition)));
                myImages.add(myImage);

            }
        }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }


        return myImages;
    }


    public interface MyInterface {
        void onComplete(boolean result);


    }

    public interface MyInterfaceTitles {

        void onComplete(int titleid);
    }

    public static class loadImg extends AsyncTask<Void, Void, Boolean> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private ArrayList<MyImage> myImages;
        private MyInterface mListener;

        private int status;
        private DbOperations dbOperations;

        private int title;

        public loadImg(ArrayList<MyImage> myImages, Context context, int status, int title, MyInterface myInterface) {

            this.context.set(context);
            this.myImages = myImages;
            this.mListener = myInterface;
            this.status = status;
            this.title = title;
            dbOperations = new DbOperations(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean inserted = false;
            for (int a = 0; a < myImages.size(); a++) {
                MyImage myImage = myImages.get(a);
                ContentValues cv = new ContentValues();
                //cv.put(DbConstants.IMAGE_STATUS,status);
                cv.put(DbConstants.IMAGE_ID, myImage.getId());
                cv.put(DbConstants.IMAGE_NAME, myImage.getName());
                cv.put(DbConstants.IMAGE_PATH, myImage.getPath());
                cv.put(DbConstants.IMAGE_CAPTION, myImage.getCaption());
                cv.put(DbConstants.IMAGE_FROM, myImage.getImageFrom());
                cv.put(DbConstants.IMAGE_POSITION, a + 1);
                cv.put(DbConstants.CREATION_TIME, myImage.getCreatonTime());
                cv.put(DbConstants.TITLE_ID, title);

                //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
                inserted = dbOperations.insert(DbConstants.TABLE_SAVED_DATA, cv);

                Log.d("todpath", myImage.getPath() + " \n");
                Log.d("todname", myImage.getName() + " \n");
                Log.d("todboth", myImage.getPath() + "" + myImage.getName() + " \n");
            }

            return inserted;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mListener.onComplete(aBoolean);


        }


    }

    public static class savetitle extends AsyncTask<Void, Void, Integer> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private MyInterfaceTitles mListener;

        private DbOperations dbOperations;

        private String title, desc;
        private int type, status;
        private TitleModel titleModel;


        public savetitle(TitleModel titleModel, Context context, String title, String desc, int type, int status, MyInterfaceTitles myInterface) {

            this.context.set(context);
            this.title = title;
            this.mListener = myInterface;
            this.desc = desc;
            this.type = type;
            this.status = status;
            dbOperations = new DbOperations(context);
            this.titleModel = titleModel;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            boolean inserted = false;
            ContentValues cv = new ContentValues();
            cv.put(DbConstants.TITLE_NAME, title);
            cv.put(DbConstants.TITLE_DESC, desc);
            cv.put(DbConstants.TITLE_DATE, DateTimeUtils.getNow());
            cv.put(DbConstants.IMAGE_STATUS, status);
            cv.put(DbConstants.IMAGE_TYPE, type);
            cv.put(DbConstants.IMAGE_NO, 0);
            cv.put(DbConstants.TITLE_ADDRESS, "");
            cv.put(DbConstants.TITLE_AMOUNT, "0");
            cv.put(DbConstants.TITLE_PHONE, "0");
            cv.put(DbConstants.ISPAID, titleModel.getISPAID());


            cv.put(DbConstants.UPLOAD_STATUS, titleModel.getUPLOAD_STATUS());
            cv.put(DbConstants.UPLOADED_COUNT, titleModel.getUPLOADED_COUNT());
            cv.put(DbConstants.NOT_UPLOADED_COUNT, titleModel.getNOT_UPLOADED_COUNT());
            cv.put(DbConstants.UPLOAD_ORDER_ID, titleModel.getUPLOAD_ORDER_ID());

            cv.put(DbConstants.BACK_COVER, titleModel.getBACK_COVER());
            cv.put(DbConstants.FRONT_COVER, titleModel.getFRONT_COVER());
            cv.put(DbConstants.ORDER_ID, titleModel.getOrder_id());



            //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
            inserted = dbOperations.insert(DbConstants.TABLE_SAVED_TITLES, cv);

            if (inserted) {
                Log.d("titlesave", "title inserted successfully fetching key id");
                int id = dbOperations.getID(DbConstants.TABLE_SAVED_TITLES, DbConstants.TITLE_NAME, title);
                Log.d("titlesave", " key id fetched was " + id);
                return id;
            } else {
                Log.d("titlesave", "error insertingg tittle returning 9999");
                return 9999;
            }


        }

        @Override
        protected void onPostExecute(Integer titleid) {
            super.onPostExecute(titleid);

            mListener.onComplete(titleid);


        }


    }


    public static class saveAlbum extends AsyncTask<Void, Void, Integer> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private MyInterfaceTitles mListener;

        private DbOperations dbOperations;


        private StoryAlbum storyAlbum;


        public saveAlbum(StoryAlbum album, Context context, MyInterfaceTitles myInterface) {


            mListener = myInterface;
            dbOperations = new DbOperations(context);
            this.storyAlbum = album;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            boolean inserted = false;
            ContentValues cv = new ContentValues();
            cv.put(DbConstants.SAtitle, storyAlbum.getTitle());
            cv.put(DbConstants.SAdescription, storyAlbum.getDescription());
            cv.put(DbConstants.SAcover_image, storyAlbum.getCover_image());
            cv.put(DbConstants.SAdate, DateTimeUtils.getToday());
            cv.put(DbConstants.SAcount, storyAlbum.getCount());


            //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
            inserted = dbOperations.insert(DbConstants.TABLE_SA, cv);

            if (inserted) {
                Log.d("titlesave", "title inserted successfully fetching key id");
                int id = dbOperations.getID2(DbConstants.TABLE_SA, DbConstants.SAtitle, storyAlbum.getTitle());
                Log.d("titlesave", " key id fetched was " + id);
                return id;
            } else {
                Log.d("titlesave", "error insertingg tittle returning 9999");
                return 9999;
            }


        }

        @Override
        protected void onPostExecute(Integer titleid) {
            super.onPostExecute(titleid);

            mListener.onComplete(titleid);


        }


    }

    public static class savephoto extends AsyncTask<Void, Void, Boolean> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private ArrayList<StoryPhoto> myImages;
        private MyInterface mListener;


        private DbOperations dbOperations;

        private int title;

        public savephoto(ArrayList<StoryPhoto> myImages, Context context, MyInterface myInterface) {

            this.context.set(context);
            this.myImages = myImages;
            this.mListener = myInterface;

            dbOperations = new DbOperations(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean inserted = false;
            for (int a = 0; a < myImages.size(); a++) {
                StoryPhoto myImage = myImages.get(a);
                ContentValues cv = new ContentValues();
                //cv.put(DbConstants.IMAGE_STATUS,status);
                cv.put(DbConstants.SPtitle, myImage.getTitle());
                cv.put(DbConstants.SPdescription, myImage.getDescription());
                cv.put(DbConstants.SPdate, DateTimeUtils.getToday());
                cv.put(DbConstants.SPpath, myImage.getPath());
                cv.put(DbConstants.SPAlbum_ID, myImage.getAlbum_ID());
                cv.put(DbConstants.SPposition, myImage.getPosition());

                //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
                inserted = dbOperations.insert(DbConstants.TABLE_SP, cv);

                Log.d("todpath", myImage.getPath() + " \n");
                Log.d("todname", myImage.getTitle() + " \n");
                Log.d("todboth", myImage.getPath() + "" + myImage.getTitle() + " \n");
            }

            return inserted;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mListener.onComplete(aBoolean);


        }


    }

    public static class updateAlbum extends AsyncTask<Void, Void, Boolean> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private StoryAlbum storyAlbum;
        private MyInterface mListener;


        private DbOperations dbOperations;

        private int title;

        public updateAlbum(StoryAlbum myImages, Context context, MyInterface myInterface) {

            this.context.set(context);
            this.storyAlbum = myImages;
            this.mListener = myInterface;

            dbOperations = new DbOperations(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean inserted = false;

            StoryAlbum myImage = storyAlbum;
            ContentValues cv = new ContentValues();
            //cv.put(DbConstants.IMAGE_STATUS,status);
            cv.put(DbConstants.SAtitle, myImage.getTitle());
            cv.put(DbConstants.SAdescription, myImage.getDescription());
            cv.put(DbConstants.SAcount, myImage.getCount());
            //cv.put(DbConstants.SAcount, myImage.getCount());
            cv.put(DbConstants.SAcover_image, myImage.getCover_image());
            cv.put(DbConstants.SAdate, DateTimeUtils.getToday());


            //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
            inserted = dbOperations.update(DbConstants.TABLE_SA, DbConstants.KEY_ID, Integer.valueOf(myImage.getKEY_ID()), cv);

            Log.d("todpath", myImage.getTitle() + " \n");
            Log.d("todname", myImage.getTitle() + " \n");
            Log.d("todboth", myImage.getTitle() + "" + myImage.getTitle() + " \n");


            return inserted;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mListener.onComplete(aBoolean);


        }


    }

    public static class saveLocation extends AsyncTask<Void, Void, Boolean> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private MyInterface mListener;

        private DbOperations dbOperations;


        private LocationModel locationModel;


        public saveLocation(LocationModel locationModel, Context context, MyInterface myInterface) {


            mListener = myInterface;
            dbOperations = new DbOperations(context);
            this.locationModel = locationModel;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean inserted = false;
            ContentValues cv = new ContentValues();
            cv.put(DbConstants.town, locationModel.getTown());
            cv.put(DbConstants.building, locationModel.getBuilding());
            cv.put(DbConstants.street, locationModel.getStreet());
            cv.put(DbConstants.lon, locationModel.getLon());
            cv.put(DbConstants.lat, locationModel.getLat());
            cv.put(DbConstants.mobile, locationModel.getMobile());


            //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
            return inserted = dbOperations.insert(DbConstants.TABLE_DELIVERY, cv);

//            if (inserted) {
//                Log.d("titlesave", "title inserted successfully fetching key id");
//                int id = dbOperations.getID2(DbConstants.TABLE_DELIVERY, DbConstants.SAtitle, storyAlbum.getTitle());
//                Log.d("titlesave", " key id fetched was " + id);
//                return id;
//            } else {
//                Log.d("titlesave", "error insertingg tittle returning 9999");
//                return 9999;
//            }


        }

        @Override
        protected void onPostExecute(Boolean titleid) {
            super.onPostExecute(titleid);

            mListener.onComplete(titleid);


        }


    }


    public static class updateTitle extends AsyncTask<Void, Void, Boolean> {

        private final ThreadLocal<Context> context = new ThreadLocal<>();
        private TitleModel storyAlbum;
        private MyInterface mListener;


        private DbOperations dbOperations;

        private int title;

        public updateTitle(TitleModel myImages, Context context, MyInterface myInterface) {

            this.context.set(context);
            this.storyAlbum = myImages;
            this.mListener = myInterface;

            dbOperations = new DbOperations(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean inserted = false;

            TitleModel title = storyAlbum;
            ContentValues cv = new ContentValues();
            cv.put(DbConstants.TITLE_NAME, title.getTitle_name());
            cv.put(DbConstants.ORDER_ID, title.getOrder_id());
//            cv.put(DbConstants.TITLE_DESC, title.getTitle_desc());
//            cv.put(DbConstants.TITLE_DATE, title.getTitle_date());
//            cv.put(DbConstants.IMAGE_STATUS, title.getImage_status());
//            cv.put(DbConstants.IMAGE_TYPE, title.getImage_type());
//            cv.put(DbConstants.IMAGE_NO, title.getImage_no());
//            cv.put(DbConstants.TITLE_ADDRESS, title.getAddress());
//            cv.put(DbConstants.TITLE_AMOUNT, title.getAmount());
//            //cv.put(DbConstants.TITLE_PHONE, title.getP);
//
//
//            cv.put(DbConstants.UPLOAD_STATUS, titleModel.getUPLOAD_STATUS());
//            cv.put(DbConstants.UPLOADED_COUNT, titleModel.getUPLOADED_COUNT());
//            cv.put(DbConstants.NOT_UPLOADED_COUNT, titleModel.getNOT_UPLOADED_COUNT());
//            cv.put(DbConstants.UPLOAD_ORDER_ID, titleModel.getUPLOAD_ORDER_ID());
//
//            cv.put(DbConstants.BACK_COVER, titleModel.getBACK_COVER());
//            cv.put(DbConstants.FRONT_COVER, titleModel.getFRONT_COVER());

            //if(dbOperations.isThere(DbConstants.TABLE_SAVED_DATA,DbConstants.IMAGE_PATH,))
            return inserted = dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, title.getTitle_id(), cv);

//            Log.d("todpath", myImage.getTitle() + " \n");
//            Log.d("todname", myImage.getTitle() + " \n");
//            Log.d("todboth", myImage.getTitle() + "" + myImage.getTitle() + " \n");


            //  return inserted;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mListener.onComplete(aBoolean);


        }


    }


}
