package com.photozuri.photozuri.Data.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eric on 1/11/2018.
 */

public class DbClass extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "photozuri.db";


    DbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @NonNull
    private String createTable(String tableName, HashMap<String, String> fieldNames) {

        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE " + tableName + " (");

        Set set = fieldNames.entrySet();
        Iterator iterator = set.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();

            if ((i + 1) != fieldNames.size()) {
                builder.append(mentry.getKey() + " " + mentry.getValue() + ", ");
            } else {
                builder.append(mentry.getKey() + " " + mentry.getValue());
            }
            i++;
        }
        builder.append(");");


        return builder.toString();


    }

    private HashMap<String, String> creatCategories() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");
        // fieldsName.put(DbConstants.CATEGORIES, "varchar ");


        //fieldsName.put(DbConstants., "varchar ");

        return fieldsName;
    }

    private HashMap<String, String> createSA() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");
        fieldsName.put(DbConstants.SAtitle, "varchar ");
        fieldsName.put(DbConstants.SAdescription, "varchar");
        fieldsName.put(DbConstants.SAcount, "varchar");
        fieldsName.put(DbConstants.SAdate, "varchar");
        fieldsName.put(DbConstants.SAcover_image, "varchar ");


        //fieldsName.put(DbConstants., "varchar ");

        return fieldsName;
    }

    private HashMap<String, String> createDeliveryDetails() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");
        fieldsName.put(DbConstants.town, "varchar ");
        fieldsName.put(DbConstants.street, "varchar");
        fieldsName.put(DbConstants.building, "varchar");
        fieldsName.put(DbConstants.lat, "varchar");
        fieldsName.put(DbConstants.lon, "varchar ");
        fieldsName.put(DbConstants.mobile, "varchar ");


        //fieldsName.put(DbConstants., "varchar ");

        return fieldsName;
    }

    private HashMap<String, String> createSP() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");
        fieldsName.put(DbConstants.SPpath, "varchar ");
        fieldsName.put(DbConstants.SPtitle, "varchar");
        fieldsName.put(DbConstants.SPAlbum_ID, "varchar");
        fieldsName.put(DbConstants.SPdescription, "varchar");
        fieldsName.put(DbConstants.SPdate, "varchar");
        fieldsName.put(DbConstants.SPposition, "varchar ");


        //fieldsName.put(DbConstants., "varchar ");

        return fieldsName;
    }
    private HashMap<String, String> createSavedData() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");
        fieldsName.put(DbConstants.IMAGE_ID, "INTEGER UNIQUE");
        fieldsName.put(DbConstants.IMAGE_NAME, "varchar UNIQUE");
        fieldsName.put(DbConstants.IMAGE_PATH, "varchar ");
        fieldsName.put(DbConstants.IMAGE_CAPTION, "varchar");
        fieldsName.put(DbConstants.IMAGE_FROM, "INTEGER ");
        fieldsName.put(DbConstants.IMAGE_UPLOADED, "INTEGER ");
        fieldsName.put(DbConstants.IMAGE_POSITION, "INTEGER ");
        fieldsName.put(DbConstants.TITLE_ID, "INTEGER ");
        fieldsName.put(DbConstants.CREATION_TIME, "varchar ");

        fieldsName.put(DbConstants.UPLOAD_STATUS, "varchar ");


        //fieldsName.put(DbConstants., "varchar ");

        return fieldsName;

    }

    private HashMap<String, String> createSavedDataTitles() {
        HashMap<String, String> fieldsName = new HashMap<>();
        fieldsName.put(DbConstants.KEY_ID, "INTEGER PRIMARY KEY  AUTOINCREMENT");

        fieldsName.put(DbConstants.TITLE_NAME, "varchar UNIQUE ");
        fieldsName.put(DbConstants.TITLE_DATE, "varchar");
        fieldsName.put(DbConstants.IMAGE_NO, "varchar");
        fieldsName.put(DbConstants.IMAGE_TYPE, "INTEGER ");
        fieldsName.put(DbConstants.IMAGE_STATUS, "INTEGER ");
        fieldsName.put(DbConstants.TITLE_DESC, "varchar ");
        fieldsName.put(DbConstants.TITLE_AMOUNT, "varchar ");
        fieldsName.put(DbConstants.TITLE_ADDRESS, "varchar ");
        fieldsName.put(DbConstants.TITLE_PHONE, "varchar ");
        fieldsName.put(DbConstants.ISPAID, "varchar ");
        fieldsName.put(DbConstants.ORDER_ID, "varchar ");


        fieldsName.put(DbConstants.UPLOAD_ORDER_ID, "varchar ");
        fieldsName.put(DbConstants.UPLOADED_COUNT, "varchar ");
        fieldsName.put(DbConstants.UPLOAD_STATUS, "varchar ");
        fieldsName.put(DbConstants.NOT_UPLOADED_COUNT, "varchar ");
        fieldsName.put(DbConstants.FRONT_COVER, "varchar ");
        fieldsName.put(DbConstants.BACK_COVER, "varchar ");




        return fieldsName;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(createTable(DbConstants.TABLE_SAVED_DATA, createSavedData()));
        db.execSQL(createTable(DbConstants.TABLE_SAVED_TITLES, createSavedDataTitles()));
        db.execSQL(createTable(DbConstants.TABLE_SA, createSA()));
        db.execSQL(createTable(DbConstants.TABLE_SP, createSP()));
        db.execSQL(createTable(DbConstants.TABLE_DELIVERY, createDeliveryDetails()));


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_SAVED_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_SAVED_TITLES);
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_SA);
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_SP);
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.TABLE_DELIVERY);


        onCreate(db);
    }

}
