package com.raismedia.taxiclient.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.raismedia.taxiclient.ExampleService;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.circleimage.CircleImageView;
import com.raismedia.taxiclient.config.Config;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.directionhelpers.FetchURL;
import com.raismedia.taxiclient.directionhelpers.TaskLoadedCallback;
import com.raismedia.taxiclient.fragment.FragmentMyWallet;
import com.raismedia.taxiclient.fragment.customer.FragmentFavoriteRide;
import com.raismedia.taxiclient.fragment.customer.FragmentLocationVehicule;
import com.raismedia.taxiclient.fragment.customer.FragmentMyLocationVehicule;
import com.raismedia.taxiclient.fragment.driver.FragmentDashboard;
import com.raismedia.taxiclient.fragment.customer.FragmentHome;
import com.raismedia.taxiclient.fragment.FragmentProfile;
import com.raismedia.taxiclient.fragment.customer.FragmentRideBookingConfirmed;
import com.raismedia.taxiclient.fragment.driver.FragmentRideBookingConfirmedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideBookingNew;
import com.raismedia.taxiclient.fragment.driver.FragmentRideBookingNewDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideBookingCanceled;
import com.raismedia.taxiclient.fragment.driver.FragmentRideBookingRejectedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideCanceled;
import com.raismedia.taxiclient.fragment.driver.FragmentRideCanceledDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideCompleted;
import com.raismedia.taxiclient.fragment.driver.FragmentRideCompletedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideConfirmed;
import com.raismedia.taxiclient.fragment.driver.FragmentRideConfirmedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideNew;
import com.raismedia.taxiclient.fragment.driver.FragmentRideNewDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideOnRide;
import com.raismedia.taxiclient.fragment.driver.FragmentRideOnRideDriver;
import com.raismedia.taxiclient.model.DrawerPojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;
import com.raismedia.taxiclient.settings.PrefManager;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class MainActivity extends AppCompatActivity implements TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private EditText editTextInput;


    private RecyclerView mDrawerList;
    public static DrawerLayout mDrawerLayout;
    public static Context context;
    private static ConnectionDetector connectionDetector;
    DrawerAdapter drawerAdapter;
    public static ArrayList<DrawerPojo> list=new ArrayList<>();
    public static PrefManager prefManager;
    private FrameLayout drawer_conducteur;
    private LinearLayout drawer_user;
    public static TextView user_name, user_phone,statut_conducteur, balance;
    private SwitchCompat switch_statut;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static Activity activity;
    private Boolean notification = false;
    public static CircleImageView user_photo;
    private static TextView save_note;
    private static TextView cancel_note;
    private static TextInputLayout intput_layout_comment;
    private static EditText input_edit_comment;
    private static RatingBar rate_conducteur;

    /** MAP **/
    public static GoogleMap mMap;
    public static Location currentLocation;

    /** GOOGLE API CLIENT **/
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationManager locationManager;
    private String provider;
    public static String amount,id_ride,position,id_driver,note_,img,comment;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // use Sanbox because we on test
            .clientId(Config.PAYPAL_CLIENT_ID);
    public static AlertDialog alertDialog;
    private static TextView title;
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    private static TextView ok, time;
    private static ProgressBar progressBar2;
    private static CountDownTimer countDownTimer = null;

    public MainActivity(ExampleService exampleService) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextInput = findViewById(R.id.edit_text_input);


        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        context = MainActivity.this;
        activity = this;
        connectionDetector=new ConnectionDetector(context);
        prefManager = new PrefManager(this);

        title = (TextView) findViewById(R.id.title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle objetbundle = this.getIntent().getExtras();
        String fragment_name = objetbundle.getString("fragment_name");

        if(!fragment_name.equals("")){
            notification = true;
            if(M.getUserCategorie(context).equals("user_app")){
                if(fragment_name.equals("ridenew"))
                    selectItem(2);
                else if(fragment_name.equals("rideconfirmed_book"))
                    selectItem(3);
                /*else if(fragment_name.equals("rideconfirmed")) {

                }*/else if(fragment_name.equals("rideonride"))
                    selectItem(4);
                else if(fragment_name.equals("ridecompleted"))
                    selectItem(5);
                else if(fragment_name.equals("riderejected"))
                    selectItem(6);
//                    fragmentManager.setBackStackEntryCount();
            }else{
                if(fragment_name.equals("ridenewrider"))
                    selectItem(1);
                else if(fragment_name.equals("ridecompleted"))
                    selectItem(4);
                /*else if(fragment_name.equals("ridecanceledrider"))
                    selectItem(1);*/
            }
        }

        balance = (TextView) findViewById(R.id.balance);
        user_photo = (CircleImageView) findViewById(R.id.user_photo);
        switch_statut = (SwitchCompat) findViewById(R.id.switch_statut);
        user_name = (TextView) findViewById(R.id.user_name);
        user_phone = (TextView) findViewById(R.id.user_phone);
        statut_conducteur = (TextView) findViewById(R.id.statut_conducteur);
        drawer_conducteur = (FrameLayout) findViewById(R.id.drawer_conducteur);
        drawer_user = (LinearLayout) findViewById(R.id.drawer_user);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerLayout.setScrimColor(Color.GRAY);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
//        toggle.getDrawerArrowDrawable().setVehicleColor(getResources().getColor(R.color.white));
        toggle.syncState();

        NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
//        int width = R.dimen.drawer_width;//getResources().getDisplayMetrics().widthPixels;
//        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationViewLeft.getLayoutParams();
//        params.width = width;
//        navigationViewLeft.setLayoutParams(params);
        mDrawerList = (RecyclerView) findViewById(R.id.rvdrawer);
        mDrawerList.setLayoutManager(new LinearLayoutManager(context));
        mDrawerList.setHasFixedSize(true);

        setDrawer();

        if (savedInstanceState == null) {
            if(fragment_name.equals(""))
                selectItem(0);
        }

        if(!M.getUserCategorie(context).equals("user_app")) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//            return;
            }

            if(!isLocationEnabled(context))
                showMessageEnabledGPS();
        }

        if(!M.getUserCategorie(context).equals("user_app")) {
            balance.setVisibility(View.GONE);
            if (M.getStatutConducteur(context).equals("yes")) {
                switch_statut.setChecked(true);
                statut_conducteur.setText("نشط");
            } else {
                switch_statut.setChecked(false);
                statut_conducteur.setText("خارج العمل");
            }
        }

        switch_statut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch_statut.isChecked()) {
                    M.showLoadingDialog(context);
                    new changerStatut().execute("yes");
                }else {
                    M.showLoadingDialog(context);
                    new changerStatut().execute("no");
                }
            }
        });

        // loading model cover using Glide library
        if(!M.getPhoto(context).equals("")) {
            // loading model cover using Glide library
            Glide.with(context).load(AppConst.Server_url + "images/app_user/" + M.getPhoto(context))
                    .skipMemoryCache(false)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
                            user_photo.setImageDrawable(getResources().getDrawable(R.drawable.user_profile));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(user_photo);
        }else{
            user_photo.setImageDrawable(getResources().getDrawable(R.drawable.user_profile));
        }

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null) {
            currentLocation = locationManager.getLastKnownLocation(provider);
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(getActivity(),0,this)
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        updateFCM(M.getID(context));
        new getWallet().execute();

        // Start PayPal Service
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getApplication().startService(intent);
    }

    public void startService(View v) {
        String input = editTextInput.getText().toString();
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }


    //This method would confirm the otp
    public static void dialogTimeOut(String lat_conducteur, String lng_conducteur, String lat_client, String lng_client) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_timeout, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        ok = (TextView) confirmDialog.findViewById(R.id.ok);
        time = (TextView) confirmDialog.findViewById(R.id.time);
        progressBar2 = (ProgressBar) confirmDialog.findViewById(R.id.progressBar2);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                if(countDownTimer != null)
                    countDownTimer.cancel();
                selectItem(3);
            }
        });
        alertDialog.setCancelable(false);
        LatLng latLng_conducteur = new LatLng(Double.parseDouble(lat_conducteur),Double.parseDouble(lng_conducteur));
        LatLng latLng_client = new LatLng(Double.parseDouble(lat_client),Double.parseDouble(lng_client));
        new FetchURL(context,"timeout").execute(getUrl(latLng_conducteur, latLng_client, "driving"), "driving");

    }

    private static String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + context.getString(R.string.google_maps_key);
        return url;
    }

    public static void parseRouteDistanceTimeOut(JSONObject jObject) {

        JSONArray jRoutes;
        JSONArray jLegs;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    String duration = ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text");
                    progressBar2.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    String[] duration_ = {};
                    int hour=0,minute=0;
                    Calendar c = Calendar.getInstance();

                    if(duration.contains("hour")) {
                        duration_ = duration.split(" ");
                        hour = Integer.parseInt(duration_[0]);
                        minute = Integer.parseInt(duration_[2]);
                        minute = minute + hour*60;
                    }else {
                        duration_ = duration.split(" ");
                        minute = Integer.parseInt(duration_[0]);
                    }
                    long min= minute*60*1000;

                    countDownTimer = new CountDownTimer(min, 1000) { // adjust the milli seconds here
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                            time.setText(String.format("%d:%d:%d", hours, minutes, seconds));
                        }
                        public void onFinish() {
                            time.setText("0:0:0");
                        }
                    }.start();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    public static void setTitle(String title_){
        title.setText(title_);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(context, PayPalService.class));
        currentLocation = null;
        super.onDestroy();
    }

    /** Récupération user wallet**/
    public class getWallet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_wallet.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    balance.setText(M.getCurrency(context)+" "+msg.getString("amount"));
                                }else{

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user", M.getID(context));
                    params.put("cat_user", M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    //This method would confirm the otp
    public static void dialogPaymentAmount(final String amount_, final String id_ride_, final String position_, final String id_driver_, final String name_payment_method,
                                           final String note_, final String img, final String comment) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_payment_amount, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView devise = (TextView) confirmDialog.findViewById(R.id.devise);
        TextView amount = (TextView) confirmDialog.findViewById(R.id.amount);
        TextView payment_method = (TextView) confirmDialog.findViewById(R.id.payment_method);
        TextView cancel = (TextView) confirmDialog.findViewById(R.id.cancel);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        amount.setText(amount_);
        devise.setText(M.getCurrency(context));
        payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    dialogPaymentMethode(amount_,id_ride,position,id_driver,name_payment_method);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if(name_payment_method.equals("Cash")){
                    id_ride = id_ride_;
                    id_driver = id_driver_;
                    position = position_;
                    M.showLoadingDialog(context);
                    new PayRide().execute(note_,id_driver,position,img,comment);
                }else if(name_payment_method.equals("PayPal")){
                    processPayment(amount_,id_ride_,position_,id_driver_);
                }else if(name_payment_method.equals("My Wallet")){
                    id_ride = id_ride_;
                    id_driver = id_driver_;
                    position = position_;
                    M.showLoadingDialog(context);
                    new PayRideWallet().execute(amount_,note_,id_driver,position,img,comment);
                }else{
                    context.startActivity(new Intent(context, CreditCard.class)
                            .putExtra("amount", amount_)
                            .putExtra("id_ride",id_ride_)
                            .putExtra("id_driver",id_driver_)
                            .putExtra("position",position_)
                    );
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    public static void dialogPaymentMethode(final String amount_, final String id_ride, final String position, final String id_driver, final String name_payment_method) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_payment_method, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        ImageView creditcard = (ImageView) confirmDialog.findViewById(R.id.creditcard);
        ImageView paypal = (ImageView) confirmDialog.findViewById(R.id.paypal);
//        TextView confitm = (TextView) confirmDialog.findViewById(R.id.confitm);
        TextView cancel = (TextView) confirmDialog.findViewById(R.id.cancel);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment(amount_,id_ride,position,id_driver);
            }
        });
        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CreditCard.class)
                        .putExtra("amount", amount_)
                        .putExtra("id_ride",id_ride)
                        .putExtra("id_driver",id_driver)
                        .putExtra("position",position)
                );
                alertDialog.cancel();
            }
        });
    }

    private static void processPayment(String amount_, String id_ride_, String position_, String id_driver_) {
        amount = amount_;
        id_ride = id_ride_;
        position = position_;
        id_driver = id_driver_;
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",
                "Donate for Taxi Cab On Demand Taxi", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        getCurrentFragment().startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
//        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

        if (currentLocation != null) {
            if(!M.getUserCategorie(context).equals("user_app"))
                new setCurrentLocation().execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
        }
    }

    /** MAJ de la position d'un conducteur **/
    public static class setCurrentLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_position.php";
            final String latitude = params[0];
            final String longitude = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }

            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user", M.getID(context));
                    params.put("user_cat", M.getUserCategorie(context));
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }


    /** Start COOGLE API Client **/
    @Override
    public void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();


        }
    }


    @Override
    public void onStop() {
        super.onStop();

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        startLocationUpdates();
    }



    private void startLocationUpdates() {



        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }





    /** Change driver status **/
    private class changerStatut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"change_statut.php";
            final String online = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                M.hideLoadingDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                String online = msg.getString("online");
                                if(etat.equals("1")){
                                    if(online.equals("yes")) {
                                        switch_statut.setChecked(true);
                                        statut_conducteur.setText("enabled");
                                        M.setStatutConducteur(online,context);
                                    }else {
                                        switch_statut.setChecked(false);
                                        statut_conducteur.setText("disabled");
                                        M.setStatutConducteur(online,context);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
                    if(switch_statut.isChecked())
                        switch_statut.setChecked(false);
                    else
                        switch_statut.setChecked(true);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_driver", M.getID(context));
                    params.put("online", online);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if(M.getUserCategorie(context).equals("user_app")) {
            if (M.getCurrentFragment(context).equals("home")) {
                if (FragmentHome.currentPolyline != null)
                    FragmentHome.currentPolyline.remove();
                FragmentHome.currentPolyline = FragmentHome.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentHome.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentHome.departLocationReservation.getLatitude(), FragmentHome.departLocationReservation.getLongitude());
                LatLng latLng2 = new LatLng(FragmentHome.destinationLocationReservation.getLatitude(), FragmentHome.destinationLocationReservation.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentHome.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            }/* else if (M.getCurrentFragment(context).equals("mes_requetes_accueil")){
                if (FragmentHome.currentPolyline != null)
                    FragmentHome.currentPolyline.remove();
                FragmentHome.currentPolyline = FragmentHome.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentHome.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentHome.departLocationMesRequetes.getLatitude(), FragmentHome.departLocationMesRequetes.getLongitude());
                LatLng latLng2 = new LatLng(FragmentHome.destinationLocationMesRequetes.getLatitude(), FragmentHome.destinationLocationMesRequetes.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentHome.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            } else if (M.getCurrentFragment(context).equals("historic")){
                if (BottomSheetFragmentHistoric.currentPolyline != null)
                    BottomSheetFragmentHistoric.currentPolyline.remove();
                BottomSheetFragmentHistoric.currentPolyline = BottomSheetFragmentHistoric.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentHistoric.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentHistoric.clientLocation.getLatitude(),BottomSheetFragmentHistoric.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentHistoric.destinationLocation.getLatitude(),BottomSheetFragmentHistoric.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//            BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }else{
                if (BottomSheetFragmentMyRide.currentPolyline != null)
                    BottomSheetFragmentMyRide.currentPolyline.remove();
                BottomSheetFragmentMyRide.currentPolyline = BottomSheetFragmentMyRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentMyRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentMyRide.clientLocation.getLatitude(),BottomSheetFragmentMyRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentMyRide.destinationLocation.getLatitude(),BottomSheetFragmentMyRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
                BottomSheetFragmentMyRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }*/
        }else{
            /*if(M.getCurrentFragment(context).equals("requete")){
                if (BottomSheetFragmentRide.currentPolyline != null)
                    BottomSheetFragmentRide.currentPolyline.remove();
                BottomSheetFragmentRide.currentPolyline = BottomSheetFragmentRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentRide.clientLocation.getLatitude(),BottomSheetFragmentRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentRide.destinationLocation.getLatitude(),BottomSheetFragmentRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }else{
                if (BottomSheetFragmentCourse.currentPolyline != null)
                    BottomSheetFragmentCourse.currentPolyline.remove();
                BottomSheetFragmentCourse.currentPolyline = BottomSheetFragmentCourse.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentCourse.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentCourse.clientLocation.getLatitude(),BottomSheetFragmentCourse.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentCourse.destinationLocation.getLatitude(),BottomSheetFragmentCourse.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentCourse.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if(!M.getUserCategorie(context).equals("user_app")) {
                    if(!isLocationEnabled(context))
                        showMessageEnabledGPS();
                }else{
                    if(!isLocationEnabled(context))
                        showMessageEnabledGPSClient();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showMessageEnabledGPSClient(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.this_service_requires_the_activation_of_the_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Activez le service GPS pour partager votre position avec les clients. Activez le GPS maintenant ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void setDrawer() {
        mDrawerLayout.setFocusable(false);
        list.clear();
        if(M.getUserCategorie(context).equals("user_app")){
            drawer_conducteur.setVisibility(View.GONE);
            switch_statut.setVisibility(View.GONE);
            drawer_user.setVisibility(View.VISIBLE);
            user_name.setText(M.getPrenom(context)+" "+M.getNom(context));
            user_phone.setText(M.getPhone(context));

            list.add(new DrawerPojo(1, "", getString(R.string.item_home), R.drawable.ic_home_outline));
 //           list.add(new DrawerPojo(13, "", getString(R.string.item_favorite), R.drawable.ic_star_outline));
            list.add(new DrawerPojo(2, "", getString(R.string.item_new), R.drawable.ic_new));
            list.add(new DrawerPojo(3, "", getString(R.string.item_confirmed), R.drawable.ic_confirm));
            list.add(new DrawerPojo(4, "", getString(R.string.item_onride), R.drawable.ic_completed));
            list.add(new DrawerPojo(5, "", getString(R.string.item_completed), R.drawable.ic_rent_outline));
            list.add(new DrawerPojo(6, "", getString(R.string.item_canceled), R.drawable.ic_error));

           // list.add(new DrawerPojo(10, "", getString(R.string.item_new_booking), R.drawable.ic_calendar));
           // list.add(new DrawerPojo(11, "", getString(R.string.item_confirmed_booking), R.drawable.ic_calendar_check));
           // list.add(new DrawerPojo(12, "", getString(R.string.item_canceled_booking), R.drawable.ic_calendar_cancel));
           // list.add(new DrawerPojo(14, "", getString(R.string.item_louer_vehicule), R.drawable.ic_rent_outline));
           // list.add(new DrawerPojo(15, "", getString(R.string.item_vehicule_loue), R.drawable.ic_rent_outline));
            //list.add(new DrawerPojo(16, "", getString(R.string.item_wallet), R.drawable.ic_wallet));
            list.add(new DrawerPojo(7, "", getString(R.string.item_profile), R.drawable.ic_profile_outline));
            list.add(new DrawerPojo(0, "", getString(R.string.item_logout), R.drawable.ic_logout_outline));
            list.add(new DrawerPojo(8, "", "divider", 0));
            list.add(new DrawerPojo(9, "", getString(R.string.item_help), 0));
            list.add(new DrawerPojo(17, "", getString(R.string.item_contact_us), R.drawable.ic_assistance_outline));
        }else{
            drawer_conducteur.setVisibility(View.VISIBLE);
            switch_statut.setVisibility(View.VISIBLE);
            drawer_user.setVisibility(View.GONE);
            drawer_user.setVisibility(View.GONE);
            user_name.setText("");
            user_phone.setText("");

            list.add(new DrawerPojo(1, "", getString(R.string.item_dashboard), R.drawable.ic_dashboard));
            list.add(new DrawerPojo(2, "", getString(R.string.item_new), R.drawable.ic_new));
            list.add(new DrawerPojo(3, "", getString(R.string.item_confirmed), R.drawable.ic_confirm));
            list.add(new DrawerPojo(4, "", getString(R.string.item_onride), R.drawable.ic_completed));
            list.add(new DrawerPojo(5, "", getString(R.string.item_completed), R.drawable.ic_completed));
            list.add(new DrawerPojo(6, "", getString(R.string.item_rejected), R.drawable.ic_error));

         //   list.add(new DrawerPojo(10, "", getString(R.string.item_new_booking), R.drawable.ic_calendar));
          //  list.add(new DrawerPojo(11, "", getString(R.string.item_confirmed_booking), R.drawable.ic_calendar_check));
          //  list.add(new DrawerPojo(12, "", getString(R.string.item_rejected_booking), R.drawable.ic_calendar_cancel));
//           list.add(new DrawerPojo(13, "", getString(R.string.item_wallet), R.drawable.ic_wallet));
            list.add(new DrawerPojo(7, "", getString(R.string.item_profile), R.drawable.ic_profile_outline));
            list.add(new DrawerPojo(0, "", getString(R.string.item_logout), R.drawable.ic_logout_outline));
            list.add(new DrawerPojo(8, "", "divider", 0));
            list.add(new DrawerPojo(9, "", getString(R.string.item_help), 0));
            list.add(new DrawerPojo(13, "", getString(R.string.item_contact_us), R.drawable.ic_assistance_outline));
        }

        drawerAdapter=new DrawerAdapter(list,context);
        mDrawerList.setAdapter(drawerAdapter);
    }

    public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<DrawerPojo> mDrawerItems;
        Context context;

        public DrawerAdapter(List<DrawerPojo> list, Context mcontext) {
            context = mcontext;
            mDrawerItems = list;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolderPosts(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
            final ViewHolderPosts holder = (ViewHolderPosts) holder1;
            DrawerPojo item = mDrawerItems.get(position);

            if(item.getmText().equals("divider")) {
                holder.line_divider.setVisibility(View.VISIBLE);
                holder.layout_item.setVisibility(View.GONE);
            }else {
                holder.line_divider.setVisibility(View.GONE);
                holder.layout_item.setVisibility(View.VISIBLE);
                holder.title.setText(item.getmText());
                holder.llrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectItem(position);
                    }
                });
            }

            if(item.getmIconRes() == 0) {
                holder.img.setVisibility(View.GONE);
            }else {
                holder.img.setVisibility(View.VISIBLE);
                holder.img.setImageDrawable(getResources().getDrawable(item.getmIconRes()));
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mDrawerItems.size();
        }

        public class ViewHolderPosts extends RecyclerView.ViewHolder {
            TextView title;
            LinearLayout llrow;
            ImageView img;
            View line_divider;
            LinearLayout layout_item;

            public ViewHolderPosts(View convertView) {
                super(convertView);
                llrow=(LinearLayout)convertView.findViewById(R.id.llrow);
                title = (TextView) convertView.findViewById(R.id.tvtitle);
                img = (ImageView) convertView.findViewById(R.id.img);
                line_divider = (View) convertView.findViewById(R.id.line_divider);
                layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
            }
        }
    }

    public static void selectItem(int pos1){
        Fragment fragment = null;
        long pos=list.get(pos1).getmId();
        String item=list.get(pos1).getmText();
        String tag = "home";

//        Toast.makeText(context, ""+pos, Toast.LENGTH_SHORT).show();
        if(M.getUserCategorie(context).equals("user_app")){
            if(pos==1) {
                fragment = new FragmentHome();
                tag = "home";
            }else if(pos==2){
                fragment=new FragmentRideNew();
                tag = "new";
            }else if(pos==3){
                fragment=new FragmentRideConfirmed();
                tag = "confirmed";
            }else if(pos==4){
                fragment=new FragmentRideOnRide();
                tag = "on_ride";
            }else if(pos==5){
                fragment=new FragmentRideCompleted();
                tag = "completed";
            }else if(pos==6){
                fragment=new FragmentRideCanceled();
                tag = "canceled";
            }else if(pos==7){
                fragment=new FragmentProfile();
                tag = "profil";
            }else if(pos==10){
                fragment=new FragmentRideBookingNew();
                tag = "new_book";
            }else if(pos==11){
                fragment=new FragmentRideBookingConfirmed();
                tag = "confrimed_book";
            }else if(pos==12){
                fragment=new FragmentRideBookingCanceled();
                tag = "rejected_book";
            }else if(pos==13){
                fragment=new FragmentFavoriteRide();
                tag = "rejected_book";
            }else if(pos==14){
                fragment=new FragmentLocationVehicule();
                tag = "vehicle_rent";
            }else if(pos==15){
                fragment=new FragmentMyLocationVehicule();
                tag = "vehicle_rented";
            }else if(pos==16){
                fragment=new FragmentMyWallet();
                tag = "my_wallet";
            }else if(pos==17){
                openBrowser(context, "https://raismedia.com");
            }/*else if(pos==10){
                fragment=new Fragment();
                tag = "historic";
//            else if(pos==9)
//                Toast.makeText(context, "Aide", Toast.LENGTH_SHORT).show();
//        else if(pos==6)
//            openBrowser();
            }*/else if(pos==0){
                M.logOut(context);
                prefManager.setFirstTimeLaunch7(true);
                Intent mIntent = new Intent(context, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                context.startActivity(mIntent);
            }
        }else{
            if(pos==1) {
                fragment = new FragmentDashboard();
                tag = "dashboard";
            }else if(pos==2){
                fragment=new FragmentRideNewDriver();
                tag = "new";
            }else if(pos==3){
                fragment=new FragmentRideConfirmedDriver();
                tag = "confirmed";
            }else if(pos==4){
                fragment=new FragmentRideOnRideDriver();
                tag = "on_ride";
            }else if(pos==5){
                fragment=new FragmentRideCompletedDriver();
                tag = "completed";
            }else if(pos==6){
                fragment=new FragmentRideCanceledDriver();
                tag = "canceled";
            }else if(pos==7){
                fragment=new FragmentProfile();
                tag = "profil";
            }else if(pos==10){
                fragment=new FragmentRideBookingNewDriver();
                tag = "new_book";
            }else if(pos==11){
                fragment=new FragmentRideBookingConfirmedDriver();
                tag = "confrimed_book";
            }else if(pos==12){
                fragment=new FragmentRideBookingRejectedDriver();
                tag = "rejected_book";
            }else if(pos==13){
                openBrowser(context, "https://raismedia.com");
            }/*else if(pos==2){
                fragment=new FragmentMesCourses();
                tag = "mes_courses";
//            else if(pos==3)
//                fragment=new FragmentMessage();
            }else if(pos==4){
                fragment=new FragmentProfile();
                tag = "profile";
//        else if(pos==6)
//            openBrowser();
            }*/else if(pos==0){
                M.logOut(context);
                prefManager.setFirstTimeLaunch7(true);
                Intent mIntent = new Intent(context, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                context.startActivity(mIntent);
            }
        }

        if(fragment!=null) {
            fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            if(pos!=1)
                fragmentTransaction.addToBackStack(item);
            else{
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public static void openBrowser(final Context context, String url){
        if(!url.startsWith(HTTP)&& !url.startsWith(HTTPS)){
            url = HTTP+url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choisir un naviguateur"));
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }else {
            if(notification == true){
                selectItem(0);
                notification = false;
            }else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                if(fragmentTag==null)
                    finish();
                else {
//                    Toast.makeText(context, ""+fragmentManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
                    if(fragmentManager.getBackStackEntryCount()>=2){
                        String fragmentTag1 = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getName();
//                        Toast.makeText(context, ""+fragmentTag1, Toast.LENGTH_SHORT).show();
                        if(fragmentTag1==null)
                            selectItem(0);
                        else
                            super.onBackPressed();
                    }else
                        super.onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==401 && resultCode==RESULT_OK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    /*try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(context, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    M.showLoadingDialog(context);
                    new PayRide().execute(note_,id_driver,position,img,comment);
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    /** Payer un requête **/
    public static class PayRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"pay_requete.php";
            String note = params[0];
            String id_driver = params[1];
            String position = params[2];
            String img = params[3];
            String comment = params[4];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                M.hideLoadingDialog();
                                alertDialog.cancel();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setStatut_paiement("yes");
                                    FragmentRideCompleted.adapter_ride.notifyDataSetChanged();
                                    try {
                                        dialogSucess(note,id_driver, Integer.parseInt(position),img,comment);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
//                    Toast.makeText(context, ""+id_driver+" "+id_ride+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_ride", id_ride);
                    params.put("id_driver", id_driver);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    /** Payer un requête **/
    public static class PayRideWallet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"pay_requete_wallet.php";
            String amount_ = params[0];
            String note = params[1];
            String id_driver = params[2];
            String position = params[3];
            String img = params[4];
            String comment = params[5];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                M.hideLoadingDialog();
                                alertDialog.cancel();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    MainActivity.balance.setText(M.getCurrency(context)+" "+msg.getString("amount"));
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setStatut_paiement("yes");
                                    FragmentRideCompleted.adapter_ride.notifyDataSetChanged();
                                    try {
                                        dialogSucess(note,id_driver, Integer.parseInt(position),img,comment);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
//                    Toast.makeText(context, ""+id_driver+" "+id_ride+" "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_ride", id_ride);
                    params.put("id_driver", id_driver);
                    params.put("id_user_app", M.getID(context));
                    params.put("amount", amount_);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    //This method would confirm the otp
    private static void dialogSucess(final String note_, final String id_driver, final int position, final String img, final String comment) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        msg.setText(context.getResources().getString(R.string.payment_made_successfully));

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                try {
                    dialogRate(note_, id_driver, position, img, comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setCancelable(false);
    }

    //This method would confirm the otp
    private static void dialogRate(final String note_, final String id_driver, final int position, final String img, final String comment) throws JSONException {
        final String[] note = {note_};
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_noter, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_note = (TextView) confirmDialog.findViewById(R.id.save_note);
        cancel_note = (TextView) confirmDialog.findViewById(R.id.cancel_note);
        rate_conducteur = (RatingBar) confirmDialog.findViewById(R.id.rate_conducteur);
        user_photo = (CircleImageView) confirmDialog.findViewById(R.id.user_photo);
        intput_layout_comment = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_comment);
        input_edit_comment = (EditText) confirmDialog.findViewById(R.id.input_edit_comment);

        input_edit_comment.setText(comment);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

//        Toast.makeText(context, ""+note, Toast.LENGTH_SHORT).show();
        if(note[0].trim().length() > 0)
            rate_conducteur.setRating(Float.parseFloat(note[0]));
        else
            rate_conducteur.setRating(0f);
        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
//                    M.showLoadingDialog(context);
//                    new setNote().execute(note[0],id_driver, String.valueOf(position));
                    submitNote(note[0],id_driver, String.valueOf(position));
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        rate_conducteur.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                note[0] = String.valueOf(ratingBar.getRating());
            }
        });

        if(!img.equals("")) {
            // loading model cover using Glide library
            Glide.with(context).load(AppConst.Server_url + "images/app_user/" + img)
                    .skipMemoryCache(true)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(user_photo);
        }else{
            user_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile));
        }
//        new getPhoto().execute(id_driver);
    }

    private static void submitNote(String note, String id_driver, String position) {
        if (!validateNote()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setNote().execute(note,id_driver,position,input_edit_comment.getText().toString());
    }

    private static boolean validateNote() {
        if (input_edit_comment.getText().toString().trim().isEmpty()) {
            intput_layout_comment.setError(context.getResources().getString(R.string.enter_your_comment));
            requestFocus(input_edit_comment);
            return false;
        } else {
            intput_layout_comment.setErrorEnabled(false);
        }

        return true;
    }

    private static void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrer la note d'un conducteur **/
    private static class setNote extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_note.php";
            final String note_value = params[0];
            final String id_conducteur = params[1];
            final String position = params[2];
            final String comment = params[3];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                M.hideLoadingDialog();
                                if(etat.equals("1")){
                                    alertDialog.cancel();
                                    JSONObject note = msg.getJSONObject("note");
                                    String nb_avis = note.getString("nb_avis");
                                    String niveau = note.getString("niveau");
                                    String moyenne = note.getString("moyenne");
                                    String comment = note.getString("comment");

                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setNb_avis(nb_avis);
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setNote(niveau);
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setMoyenne(moyenne);
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setComment(comment);

                                    FragmentRideCompleted.adapter_ride.notifyItemChanged(Integer.parseInt(position));

                                    Toast.makeText(context, context.getResources().getString(R.string.rating_awarded_successfully), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.failed_to_assign), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.failed_to_edit), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app",M.getID(context));
                    params.put("id_conducteur",id_conducteur);
                    params.put("note_value",note_value);
                    params.put("comment",comment);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    public void updateFCM(final String user_id) {
        final String[] fcmid = {""};
        final String[] deviceid = { "" };
        if(AppConst.fcm_id==null){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
//                                Toast.makeText(Demarrage.this, "getInstanceId failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            fcmid[0] = token;
                            // Log and toast
                            deviceid[0] = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                            Toast.makeText(Demarrage.this, ""+ fcmid[0] +" "+ deviceid[0], Toast.LENGTH_SHORT).show();
                            if(fcmid[0] !=null && fcmid[0].trim().length()>0 && deviceid[0] !=null && deviceid[0].trim().length()>0) {
                                new setUserFCM().execute(user_id, fcmid[0], deviceid[0]);
                            }
                        }
                    });
        }else{
            fcmid[0] = AppConst.fcm_id;
        }
    }

    /** Mettre à jour le token de l'utilisateur**/
    private class setUserFCM extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"update_fcm.php";
            final String user_id = params[0];
            final String fcmid = params[1];
            final String deviceid = params[2];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id",user_id);
                    params.put("fcm_id",fcmid);
                    params.put("device_id",deviceid);
                    params.put("user_cat",M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    public static Fragment getCurrentFragment(){
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
        return currentFragment;
    }
}
