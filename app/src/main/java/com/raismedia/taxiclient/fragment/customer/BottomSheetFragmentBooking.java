package com.raismedia.taxiclient.fragment.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.adapter.CategoryVehicleAdapter;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.directionhelpers.FetchURL;
import com.raismedia.taxiclient.model.CategoryVehiclePojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.onclick.ClickListener;
import com.raismedia.taxiclient.onclick.RecyclerTouchListener;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RaisMedia on 01/01/2019.
 */

public class BottomSheetFragmentBooking extends BottomSheetDialogFragment {
    private static Context context;
    private Activity activity;
    private TextView cout_ride, distance_ride, book, cancel, ride_duration,add_favorite;
    public static String cout, distance, distance_init, durationDriving, durationBicicling, depart_name,destination_name;
    public static Location loc1, loc2;
    private String[] tabDistance = {};
//    private ImageView cancel;

//    public static RecyclerView recycler_view_driver;
//    public static List<DriverPojo> albumList_driver;
//    public static DriverAdapter adapter_driver;
    public static RecyclerView recycler_view_category_vehicle;
    public static List<CategoryVehiclePojo> albumList_category_vehicle;
    public static CategoryVehicleAdapter adapter_category_vehicle;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    ConnectionDetector connectionDetector;
    private TextView save_fav_name,cancel_fav_name;
    private EditText input_fav_name;
    private TextInputLayout intput_layout_fav_name;
    public static ArrayList<LatLng> points; // where you add latLng in this list
    private static String  img_data="",img_name; // dr  img_data = getStringImage(bitmap);
    private static ProgressBar progressBar_ride;
    private static ImageView trajet_ride;
    private TextInputLayout intput_layout_place,intput_layout_people_number;
    private EditText input_place, input_people_number;
    private static BottomSheetFragmentBookingDriver bottomSheetFragmentBookingDriver;

    public BottomSheetFragmentBooking() {
        // Required empty public constructor
    }

    public BottomSheetFragmentBooking(Activity activity, Location loc1, Location loc2, String distance, String durationBicicling, String durationDriving, String depart_name , String destination_name) {
        this.activity = activity;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.distance = distance;
        this.durationDriving = durationDriving;
        this.durationBicicling = durationBicicling;
        this.depart_name = depart_name;
        this.destination_name = destination_name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_booking, container, false);

        context = getContext();
        connectionDetector=new ConnectionDetector(context);
        points = new ArrayList<>();

        cout_ride = (TextView) rootView.findViewById(R.id.cout_requete);
        distance_ride = (TextView) rootView.findViewById(R.id.distance_requete);
        cancel = (TextView) rootView.findViewById(R.id.cancel);
        book = (TextView) rootView.findViewById(R.id.commander);
        ride_duration = (TextView) rootView.findViewById(R.id.duree_requete);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        add_favorite = (TextView) rootView.findViewById(R.id.add_favorite);
        progressBar_ride = (ProgressBar) rootView.findViewById(R.id.progressBar_ride);
        trajet_ride = (ImageView) rootView.findViewById(R.id.trajet_ride);
        input_place = (EditText) rootView.findViewById(R.id.input_place);
        input_people_number = (EditText) rootView.findViewById(R.id.people_number);
        intput_layout_place = (TextInputLayout) rootView.findViewById(R.id.intput_layout_place);
        intput_layout_people_number = (TextInputLayout) rootView.findViewById(R.id.intput_layout_people_number);
//        cancel = (ImageView) rootView.findViewById(R.id.cancel);

        albumList_category_vehicle = new ArrayList<>();
        adapter_category_vehicle = new CategoryVehicleAdapter(context, albumList_category_vehicle, getActivity());
        recycler_view_category_vehicle = (RecyclerView) rootView.findViewById(R.id.recycler_view_category_vehicle);
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category_vehicle.setLayoutManager(verticalLayoutManager);
        recycler_view_category_vehicle.setItemAnimator(new DefaultItemAnimator());
        recycler_view_category_vehicle.setAdapter(adapter_category_vehicle);

        recycler_view_category_vehicle.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_category_vehicle, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                    CategoryVehiclePojo categoryVehiclePojo = albumList_category_vehicle.get(position);
                    submitRide(categoryVehiclePojo);

            }

            @Override
            public void onLongClick(View view, int position) {
                CategoryVehiclePojo categoryVehiclePojo = albumList_category_vehicle.get(position);
            }
        }));

        tabDistance = distance.split(" ");

        if(tabDistance[1].equals("m"))
            distance_init = tabDistance[0];
        else
            distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

        distance_ride.setText(distance);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogFavName();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        new getCategoryVehicle().execute();

        M.setCurrentFragment("course",context);
        LatLng latLng = new LatLng(loc1.getLatitude(),loc1.getLongitude());
        LatLng latLng2 = new LatLng(loc2.getLatitude(),loc2.getLongitude());
        new FetchURL(getActivity(),"bottom_book").execute(getUrl(latLng, latLng2, "driving"), "driving");

        //  this is for test, temporary
     //   points.add(latLng);
       // points.add(latLng2);
        getStaticMap();

        setCancelable(false);

        return rootView;
    }

    private boolean validatePlace() {
        if (input_place.getText().toString().trim().isEmpty()) {
//            intput_layout_place.setError(context.getResources().getString(R.string.enter_your_exactly_place));
            Toast.makeText(context, ""+context.getResources().getString(R.string.enter_your_exactly_place), Toast.LENGTH_SHORT).show();
            requestFocus(input_place);
            return false;
        } else {
            intput_layout_place.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePeopleNumber() {
        if (input_people_number.getText().toString().trim().isEmpty()) {
//            intput_layout_people_number.setError(context.getResources().getString(R.string.enter_your_number_of_people));
            Toast.makeText(context, ""+context.getResources().getString(R.string.enter_your_number_of_people), Toast.LENGTH_SHORT).show();
            requestFocus(input_people_number);
            return false;
        } if(input_people_number.getText().toString().trim().equals("0")){
//            intput_layout_people_number.setError(context.getResources().getString(R.string.enter_a_number_greater_than));
            Toast.makeText(context, ""+context.getResources().getString(R.string.enter_a_number_greater_than), Toast.LENGTH_SHORT).show();
            requestFocus(input_people_number);
            return false;
        }else {
            intput_layout_people_number.setErrorEnabled(false);
        }

        return true;
    }

    private void submitRide(CategoryVehiclePojo categoryVehiclePojo) {
        if (!validatePlace()) {
            return;
        }
        if (!validatePeopleNumber()) {
            return;
        }
        bottomSheetFragmentBookingDriver = new BottomSheetFragmentBookingDriver(activity, loc1, loc2, distance, durationDriving, String.valueOf(categoryVehiclePojo.getId()), depart_name, destination_name, categoryVehiclePojo.getPrice()
                ,categoryVehiclePojo.getStatut_commission(),categoryVehiclePojo.getCommission(),categoryVehiclePojo.getType_commission() ,categoryVehiclePojo.getStatut_commission_perc(),categoryVehiclePojo.getCommission_perc(),categoryVehiclePojo.getType_commission_perc(),img_data,input_place.getText().toString(),input_people_number.getText().toString());
        bottomSheetFragmentBookingDriver.show(((FragmentActivity) activity).getSupportFragmentManager(), bottomSheetFragmentBookingDriver.getTag());
    }

    public static void parseRouteDistance() {
        getStaticMap();
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {





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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private static void getStaticMap(){
        if(points.size() != 0){
            String path = "";
            for(int i=0; i<points.size(); i++){
                LatLng latLng = points.get(i);
                if(i==0)
                    path = latLng.latitude+","+latLng.longitude;
                else
                    path = path+"|"+latLng.latitude+","+latLng.longitude;
            }
            final String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=640x300&markers="+loc1.getLatitude()+","+loc1.getLongitude()+"|"+loc2.getLatitude()+","+loc2.getLongitude()+"&path=color:0x000000|"+path+"&key=AIzaSyCFpZocJxuBvqVvq6a1lZBD0ndRlI20x_A";


            // loading model cover using Glide library
            Glide.with((Activity) context)
                    .asBitmap()
                    .load(STATIC_MAP_API_ENDPOINT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {


                            // loading model cover using Glide library / put some static data and check it and one more thin put this block of code in Async/ chack tjis
                            Glide.with(context).load(bitmap)
                                    .skipMemoryCache(false)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar_ride.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            img_data = getStringImage(bitmap);
                                            progressBar_ride.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(trajet_ride);
                        }
                    });
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.please_wait_for_the_data_to_load), Toast.LENGTH_SHORT).show();
        }
    }

    //This method would confirm the otp
    private void dialogFavName() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_fav_name, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_fav_name = (TextView) confirmDialog.findViewById(R.id.save_fav_name);
        cancel_fav_name = (TextView) confirmDialog.findViewById(R.id.cancel_fav_name);
        input_fav_name = (EditText) confirmDialog.findViewById(R.id.input_edit_fav_name);
        intput_layout_fav_name = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_fav_name);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        save_fav_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormFavName();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_fav_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void submitFormFavName() {
        if (!validateFavName()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setFavoriteRide().execute(String.valueOf(loc1.getLatitude()),String.valueOf(loc1.getLongitude()),String.valueOf(loc2.getLatitude()),String.valueOf(loc2.getLongitude())
                ,distance_init,depart_name,destination_name,input_fav_name.getText().toString());
    }

    private boolean validateFavName() {
        if (input_fav_name.getText().toString().trim().isEmpty()) {
            intput_layout_fav_name.setError(context.getResources().getString(R.string.enter_your_favorite_name));
            requestFocus(input_fav_name);
            return false;
        } else {
            intput_layout_fav_name.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrer une balade favorite **/
    private class setFavoriteRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_favorite_ride.php";
            final String lat1 = params[0];
            final String lng1 = params[1];
            final String lat2 = params[2];
            final String lng2 = params[3];
            final String distance = params[4];
            final String depart_name = params[5];
            final String destination_name = params[6];
            final String fav_name = params[7];
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
                                M.hideLoadingDialog();
                                if(etat.equals("1")){
                                    dialogSucess();
                                }else if(etat.equals("3")){
                                    Toast.makeText(context, context.getResources().getString(R.string.this_ride_is_already_registered), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app",M.getID(context));
                    params.put("lat1",lat1);
                    params.put("lng1",lng1);
                    params.put("lat2",lat2);
                    params.put("lng2",lng2);
                    params.put("distance",distance);
                    params.put("depart_name",depart_name);
                    params.put("destination_name",destination_name);
                    params.put("fav_name",fav_name);
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
    private void dialogSucess() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        msg.setText(context.getResources().getString(R.string.your_favorite_ride_has_been_successfully_registered));

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
    }

    //** Récupération des conducteurs disponibles**/
    /*public class getDriver extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_conducteur_dispo.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                albumList_driver.clear();
                                adapter_driver.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxiclient = msg.getJSONObject(String.valueOf(i));
                                        albumList_driver.add(new DriverPojo(taxiclient.getInt("id"),taxiclient.getInt("idConducteur"),taxiclient.getString("nom")+" "+taxiclient.getString("prenom"),
                                                taxiclient.getString("immatriculation"), taxiclient.getString("numero")));
                                        adapter_driver.notifyDataSetChanged();
                                    }
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
                    params.put("lat1", String.valueOf(loc1.getLatitude()));
                    params.put("lng1", String.valueOf(loc1.getLongitude()));
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
    }*/

    //** Récupération des catégories de véhicule**/
    public class getCategoryVehicle extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_categorie_vehicle.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBar.setVisibility(View.GONE);
                                albumList_category_vehicle.clear();
                                adapter_category_vehicle.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
                                        if(taxi.getString("libelle").equals("Mini")){
                                            if(i == 0){
                                                albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                        durationBicicling,taxi.getString("prix"),"no",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
                                            }else{
                                                albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                        durationBicicling,taxi.getString("prix"),"no",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
                                            }
                                        }else{
                                            if(i == 0){
                                                albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                        durationDriving,taxi.getString("prix"),"no",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
                                            }else{
                                                albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                        durationDriving,taxi.getString("prix"),"no",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
                                            }
                                        }
                                        adapter_category_vehicle.notifyDataSetChanged();
                                    }
                                }else{

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
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

    private String getDistance(Location loc1, Location loc2){
        final String[][] tab = {{}};

        float distanceInMeters1 = loc1.distanceTo(loc2);
        tab[0] = String.valueOf(distanceInMeters1).split("\\.");
        String distance = tab[0][0];
        return distance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (mapFragment != null)
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    public static void dismissBottomSheetFragmentBookingDriver(){
        bottomSheetFragmentBookingDriver.dismiss();
    }

    /** Enregistrement d'une requête**/
    private class setRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_requete.php";
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
                                    Toast.makeText(activity, context.getResources().getString(R.string.your_booking_as_been_sent_successfully), Toast.LENGTH_LONG).show();
                                    dismiss();
                                }else{
                                    Toast.makeText(activity, context.getResources().getString(R.string.an_error_occurred_while_sending_your_booking), Toast.LENGTH_LONG).show();
                                }
                                M.hideLoadingDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", M.getID(context));
                    params.put("lat1", String.valueOf(loc1.getLatitude()));
                    params.put("lng1", String.valueOf(loc1.getLongitude()));
                    params.put("lat2", String.valueOf(loc2.getLatitude()));
                    params.put("lng2", String.valueOf(loc2.getLongitude()));
                    params.put("cout", cout);
                    params.put("distance", distance_init);
                    params.put("duree", durationDriving);
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
}
