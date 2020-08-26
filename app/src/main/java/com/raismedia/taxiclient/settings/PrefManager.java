package com.raismedia.taxiclient.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    SharedPreferences pref3;
    SharedPreferences.Editor editor3;
    SharedPreferences pref4;
    SharedPreferences.Editor editor4;
    SharedPreferences pref5;
    SharedPreferences.Editor editor5;
    SharedPreferences pref6;
    SharedPreferences.Editor editor6;
    SharedPreferences pref7;
    SharedPreferences.Editor editor7;
    SharedPreferences pref8;
    SharedPreferences.Editor editor8;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "pharmaso";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LAUNCH1 = "IsFirstTimeLaunch1";
    private static final String IS_FIRST_TIME_LAUNCH2 = "IsFirstTimeLaunch2";
    private static final String IS_FIRST_TIME_LAUNCH3 = "IsFirstTimeLaunch3";
    private static final String IS_FIRST_TIME_LAUNCH4 = "IsFirstTimeLaunch4";
    private static final String IS_FIRST_TIME_LAUNCH5 = "IsFirstTimeLaunch5";
    private static final String IS_FIRST_TIME_LAUNCH6 = "IsFirstTimeLaunch6";
    private static final String IS_FIRST_TIME_LAUNCH7 = "IsFirstTimeLaunch7";
    private static final String IS_FIRST_TIME_LAUNCH8 = "IsFirstTimeLaunch8";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        pref1 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor1 = pref1.edit();
        pref2 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor2 = pref2.edit();
        pref3 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor3 = pref3.edit();
        pref4 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor4 = pref4.edit();
        pref5 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor5 = pref5.edit();
        pref6 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor6 = pref6.edit();
        pref7 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor7 = pref7.edit();
        pref8 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor8 = pref8.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch1(boolean isFirstTime) {
        editor1.putBoolean(IS_FIRST_TIME_LAUNCH1, isFirstTime);
        editor1.commit();
    }

    public boolean isFirstTimeLaunch1() {
        return pref1.getBoolean(IS_FIRST_TIME_LAUNCH1, true);
    }

    public void setFirstTimeLaunch2(boolean isFirstTime) {
        editor2.putBoolean(IS_FIRST_TIME_LAUNCH2, isFirstTime);
        editor2.commit();
    }

    public boolean isFirstTimeLaunch2() {
        return pref2.getBoolean(IS_FIRST_TIME_LAUNCH2, true);
    }

    public void setFirstTimeLaunch3(boolean isFirstTime) {
        editor3.putBoolean(IS_FIRST_TIME_LAUNCH3, isFirstTime);
        editor3.commit();
    }

    public boolean isFirstTimeLaunch3() {
        return pref3.getBoolean(IS_FIRST_TIME_LAUNCH3, true);
    }

    public void setFirstTimeLaunch4(boolean isFirstTime) {
        editor4.putBoolean(IS_FIRST_TIME_LAUNCH4, isFirstTime);
        editor4.commit();
    }

    public boolean isFirstTimeLaunch4() {
        return pref4.getBoolean(IS_FIRST_TIME_LAUNCH4, true);
    }

    public void setFirstTimeLaunch5(boolean isFirstTime) {
        editor5.putBoolean(IS_FIRST_TIME_LAUNCH5, isFirstTime);
        editor5.commit();
    }

    public boolean isFirstTimeLaunch5() {
        return pref5.getBoolean(IS_FIRST_TIME_LAUNCH5, true);
    }

    public void setFirstTimeLaunch6(boolean isFirstTime) {
        editor6.putBoolean(IS_FIRST_TIME_LAUNCH6, isFirstTime);
        editor6.commit();
    }

    public boolean isFirstTimeLaunch6() {
        return pref6.getBoolean(IS_FIRST_TIME_LAUNCH6, false);
    }

    public void setFirstTimeLaunch7(boolean isFirstTime) {
        editor7.putBoolean(IS_FIRST_TIME_LAUNCH7, isFirstTime);
        editor7.commit();
    }

    public boolean isFirstTimeLaunch7() {
        return pref7.getBoolean(IS_FIRST_TIME_LAUNCH7, true);
    }

    public void setFirstTimeLaunch8(boolean isFirstTime) {
        editor8.putBoolean(IS_FIRST_TIME_LAUNCH8, isFirstTime);
        editor8.commit();
    }

    public boolean isFirstTimeLaunch8() {
        return pref8.getBoolean(IS_FIRST_TIME_LAUNCH8, true);
    }

}
