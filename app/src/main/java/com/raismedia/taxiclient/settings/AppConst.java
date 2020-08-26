package com.raismedia.taxiclient.settings;

import android.content.Context;
import android.graphics.Typeface;



public class AppConst {

    public static final String Server_url ="https://taxi.raismedia.com/demo_webservice/";
    public static final String Server_urlMain ="https://taxi.raismedia.com/demo/";
    public static final String API_GET_TOKEN = "https://taxi.raismedia.com/demo_webservice/Braintree/main.php";
    public static final String API_CHECK_OUT = "https://taxi.raismedia.com/demo_webservice/Braintree/checkout.php";
    public static final String MAIN = Server_url;

    public static String fcm_id;

    public static final String ADMOB_ID = "121212121212"; // app id

    public static final String INTERSTIAL_ID = "91397163"; // app id

    public static Typeface font_quicksand_medium(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandMedium.ttf");
        return typeface;
    }

    public static Typeface font_quicksand_regular(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandRegular.ttf");
        return typeface;
    }

    public static Typeface font_quicksand_semibold(Context context){
        Typeface typeface= Typeface.createFromAsset(context.getAssets(), "fonts/QuicksandSemiBold.ttf");
        return typeface;
    }

}
