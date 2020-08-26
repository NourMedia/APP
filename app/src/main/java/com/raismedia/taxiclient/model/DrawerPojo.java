package com.raismedia.taxiclient.model;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

public class DrawerPojo {

    private long mId;
    private String mImageURL;
    private String mText;
    private int mIconRes;

    public DrawerPojo() {
    }

    public DrawerPojo(long mId, String mImageURL, String mText, int mIconRes) {
        this.mId = mId;
        this.mImageURL = mImageURL;
        this.mText = mText;
        this.mIconRes = mIconRes;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmIconRes() {
        return mIconRes;
    }

    public void setmIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
    }
}
