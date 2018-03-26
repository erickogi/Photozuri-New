package com.photozuri.photozuri.Data.Models;

import com.photozuri.photozuri.GlobalConsts;

import java.io.Serializable;

/**
 * Created by Eric on 1/24/2018.
 */

public class TitleModel implements Serializable {

//        fieldsName.put(DbConstants.TITLE_NAME, "varchar");
//        fieldsName.put(DbConstants.TITLE_DATE, "varchar");
//        fieldsName.put(DbConstants.IMAGE_NO, "varchar");
//        fieldsName.put(DbConstants.IMAGE_TYPE, "INTEGER ");
//        fieldsName.put(DbConstants.IMAGE_STATUS, "INTEGER ");

    private int title_id = 0;
    private String order_id;
    private String title_name;
    private String title_date;
    private String title_desc;
    private int image_type = 0;
    private int image_status = 0;
    private int image_no = 0;

    private String amount;
    private String address;
    private String contactphone;


    private int UPLOAD_STATUS = 0;
    private int UPLOADED_COUNT = 0;
    private int NOT_UPLOADED_COUNT = 0;
    private String UPLOAD_ORDER_ID;

    private String FRONT_COVER;
    private String BACK_COVER;

    private String ISPAID;

    public String getOrder_id() {
        return order_id != null ? order_id : GlobalConsts.ORDER_DEFAULT_ID;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getISPAID() {
        return ISPAID != null ? ISPAID : GlobalConsts.TITLE_IS_NOT_PAID;
    }

    public void setISPAID(String ISPAID) {
        this.ISPAID = ISPAID;
    }

    public String getFRONT_COVER() {
        return FRONT_COVER;
    }

    public void setFRONT_COVER(String FRONT_COVER) {
        this.FRONT_COVER = FRONT_COVER;
    }

    public String getBACK_COVER() {
        return BACK_COVER;
    }

    public void setBACK_COVER(String BACK_COVER) {
        this.BACK_COVER = BACK_COVER;
    }

    public int getUPLOAD_STATUS() {
        return UPLOAD_STATUS;
    }

    public void setUPLOAD_STATUS(int UPLOAD_STATUS) {
        this.UPLOAD_STATUS = UPLOAD_STATUS;
    }

    public int getUPLOADED_COUNT() {
        return UPLOADED_COUNT;
    }

    public void setUPLOADED_COUNT(int UPLOADED_COUNT) {
        this.UPLOADED_COUNT = UPLOADED_COUNT;
    }

    public int getNOT_UPLOADED_COUNT() {
        return NOT_UPLOADED_COUNT;
    }

    public void setNOT_UPLOADED_COUNT(int NOT_UPLOADED_COUNT) {
        this.NOT_UPLOADED_COUNT = NOT_UPLOADED_COUNT;
    }

    public String getUPLOAD_ORDER_ID() {
        return (UPLOAD_ORDER_ID != null) ? UPLOAD_ORDER_ID : GlobalConsts.ORDER_DEFAULT_ID;
    }

    public void setUPLOAD_ORDER_ID(String UPLOAD_ORDER_ID) {
        this.UPLOAD_ORDER_ID = UPLOAD_ORDER_ID;
    }

    public String getAmount() {
        return amount != null ? amount : "0.00";
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address != null ? address : "Null";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public String getTitle_name() {
        return title_name != null ? title_name : "Null";
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getTitle_date() {
        return title_date != null ? title_date : "Null";
    }

    public void setTitle_date(String title_date) {
        this.title_date = title_date;
    }

    public String getTitle_desc() {
        return title_desc != null ? title_desc : "Null";
    }

    public void setTitle_desc(String title_desc) {
        this.title_desc = title_desc;
    }

    public int getImage_type() {
        return image_type;
    }

    public void setImage_type(int image_type) {
        this.image_type = image_type;
    }

    public int getImage_status() {
        return image_status;
    }

    public void setImage_status(int image_status) {
        this.image_status = image_status;
    }

    public int getImage_no() {
        return image_no;
    }

    public void setImage_no(int image_no) {
        this.image_no = image_no;
    }
}
