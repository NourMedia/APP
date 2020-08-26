package com.raismedia.taxiclient.fragment.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.util.Calendar;
//import android.icu.util.Calendar;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.adapter.CommentAdapter;
import com.raismedia.taxiclient.adapter.DriverAdapter;
import com.raismedia.taxiclient.adapter.PaymentMethodAdapter;
import com.raismedia.taxiclient.circleimage.CircleImageView;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.CommentPojo;
import com.raismedia.taxiclient.model.DriverPojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.PaymentMethodPojo;
import com.raismedia.taxiclient.onclick.ClickListener;
import com.raismedia.taxiclient.onclick.RecyclerTouchListener;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RaisMedia on 01/01/2019.
 */

public class BottomSheetFragmentBookingDriver extends BottomSheetDialogFragment {
    private static Context context;
    private Activity activity;
    private TextView cout_ride/*, distance_ride*/, book, book_later/*cancel*/, ride_duration, passenger;
    private String cout, distance, distance_init, duration;
    private static Location loc1, loc2;
    private String[] tabDistance = {};
    private String id_type_vehicle;
    private LinearLayout layout_driver_detail;
    private ImageView cancel;

    public static RecyclerView recycler_view_payment;
    public static List<PaymentMethodPojo> albumList_payment;
    public static PaymentMethodAdapter adapter_payment;

    public static RecyclerView recycler_view_driver;
    public static List<DriverPojo> albumList_driver;
    public static DriverAdapter adapter_driver;
    private TextView vehicule_name,model_vehicule,numberplate_vehicule,driver_rating,cout_requete,driver_name;
    private CircleImageView driver_image;
    private FloatingActionButton write,call;
    private ProgressBar progressBar;
    private View line_divider_detail;
    public static String id_driver = "", id_payment = "";
    private TextView nothing;
    private String driver_phone = "", driver_id = "", depart_name,destination_name;

    private static String img_data,img_name,price,statut_commission,commission,type_commission,statut_commission_perc,commission_perc,type_commission_perc;
    private static final int REQUEST_PHONE_CALL = 1;
    private SwitchCompat switch_round;
    private TextView heure_retour,date_retour;
    private String statut_round = "no";
    private int mYear_depart, mMonth_depart, mDay_depart;
    private int mYear_fin, mMonth_fin, mDay_fin;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String val_date_depart,place,number_people;
    private static BottomSheetFragmentBookingLater bottomSheetFragmentBookingLater;
    private RecyclerView recycler_view_comments;
    private List<CommentPojo> albumList_comments;
    private CommentAdapter adapter_comments;
    private LinearLayout layout_nothing_comment;
    private ProgressBar progressBar_comment;

    public BottomSheetFragmentBookingDriver() {
        // Required empty public constructor
    }

    public BottomSheetFragmentBookingDriver(Activity activity, Location loc1, Location loc2, String distance, String duration, String id_type_vehicle, String depart_name , String destination_name , String price
            , String statut_commission, String commission, String type_commission, String statut_commission_perc, String commission_perc, String type_commission_perc, String img_data, String place, String number_people) {
        this.activity = activity;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.distance = distance;
        this.duration = duration;
        this.id_type_vehicle = id_type_vehicle;
        this.depart_name = depart_name;
        this.destination_name = destination_name;
        this.price = price;
        this.statut_commission = statut_commission;
        this.commission = commission;
        this.type_commission = type_commission;
        this.statut_commission_perc = statut_commission_perc;
        this.commission_perc = commission_perc;
        this.type_commission_perc = type_commission_perc;
        this.img_data = img_data;
        this.place = place;
        this.number_people = number_people;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_booking_driver, container, false);

        context = getContext();

        cout_ride = (TextView) rootView.findViewById(R.id.cout_requete);
//        distance_ride = (TextView) rootView.findViewById(R.id.distance_requete);
        cancel = (ImageView) rootView.findViewById(R.id.cancel);
        book = (TextView) rootView.findViewById(R.id.commander);
        book_later = (TextView) rootView.findViewById(R.id.book_later);
        ride_duration = (TextView) rootView.findViewById(R.id.duration_requete);
        layout_driver_detail = (LinearLayout) rootView.findViewById(R.id.layout_driver_detail);
        driver_rating = (TextView) rootView.findViewById(R.id.driver_rating);
        model_vehicule = (TextView) rootView.findViewById(R.id.model_vehicule);
        numberplate_vehicule = (TextView) rootView.findViewById(R.id.numberplate_vehicule);
        cout_requete = (TextView) rootView.findViewById(R.id.cout_requete);
        driver_name = (TextView) rootView.findViewById(R.id.driver_name);
        vehicule_name = (TextView) rootView.findViewById(R.id.vehicule_name);
        driver_image = (CircleImageView) rootView.findViewById(R.id.driver_image);
        write = (FloatingActionButton) rootView.findViewById(R.id.write);
        call = (FloatingActionButton) rootView.findViewById(R.id.call);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        line_divider_detail = (View) rootView.findViewById(R.id.line_divider_detail);
        nothing = (TextView) rootView.findViewById(R.id.nothing);
        switch_round = (SwitchCompat) rootView.findViewById(R.id.switch_round);
        heure_retour = (TextView) rootView.findViewById(R.id.heure_retour);
        date_retour = (TextView) rootView.findViewById(R.id.date_retour);
        passenger = (TextView) rootView.findViewById(R.id.passenger);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!driver_phone.equals("")) {
                    callNumber(driver_phone);
                }
            }
        });
        driver_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogComment(driver_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar calendar;
        final int currentHour;
        final int currentMinute;
        final String[] amPm = new String[1];

        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        heure_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm[0] = "PM";
                        } else {
                            amPm[0] = "AM";
                        }
                        heure_retour.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            mYear_depart = c.get(Calendar.YEAR);
            mMonth_depart = c.get(Calendar.MONTH);
            mDay_depart = c.get(Calendar.DAY_OF_MONTH);
            mYear_fin = c.get(Calendar.YEAR);
            mMonth_fin = c.get(Calendar.MONTH);
            mDay_fin = c.get(Calendar.DAY_OF_MONTH)+1;
        }
        date_retour.setText(mDay_depart + " " + MONTHS[mMonth_depart] + ". " + mYear_depart);
        val_date_depart = mYear_depart+"-"+mMonth_depart+"-"+mDay_depart;
        date_retour.setEnabled(false);
        date_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String newdayOfMonth = String.valueOf(dayOfMonth);
                                String newmonthOfYear = String.valueOf(monthOfYear+1);
                                if(String.valueOf(dayOfMonth).trim().length() == 1){
                                    newdayOfMonth = '0'+newdayOfMonth;
                                }
                                if(newmonthOfYear.trim().length() == 1){
                                    newmonthOfYear = '0'+newmonthOfYear;
                                }
                                date_retour.setText(newdayOfMonth + " " + MONTHS[monthOfYear] + ". " + year);
                                val_date_depart = year+"-"+newmonthOfYear+"-"+newdayOfMonth;

                                mYear_depart = year;
                                mMonth_depart = monthOfYear;
                                mDay_depart = Integer.parseInt(newdayOfMonth);
                            }
                        }, mYear_depart, mMonth_depart, mDay_depart);
                datePickerDialog.show();
            }
        });

        heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
        heure_retour.setEnabled(false);
        switch_round.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    statut_round = "yes";
                    heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
                    heure_retour.setEnabled(true);
                    date_retour.setEnabled(true);
                }else{
                    statut_round = "no";
                    heure_retour.setText(String.format("%02d:%02d", currentHour, currentMinute));
                    heure_retour.setEnabled(false);
                    date_retour.setEnabled(false);
                }
            }
        });

        albumList_payment = new ArrayList<>();
        adapter_payment = new PaymentMethodAdapter(context, albumList_payment, getActivity());
        recycler_view_payment = (RecyclerView) rootView.findViewById(R.id.recycler_view_payment_method);
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager_payment = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_payment.setLayoutManager(verticalLayoutManager_payment);
        recycler_view_payment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_payment.setAdapter(adapter_payment);

        albumList_driver = new ArrayList<>();
        adapter_driver = new DriverAdapter(context, albumList_driver, getActivity());
        recycler_view_driver = (RecyclerView) rootView.findViewById(R.id.recycler_view_category_vehicle);
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_driver.setLayoutManager(verticalLayoutManager);
        recycler_view_driver.setItemAnimator(new DefaultItemAnimator());
        recycler_view_driver.setAdapter(adapter_driver);

        recycler_view_driver.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_driver, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DriverPojo driverPojo = albumList_driver.get(position);
                layout_driver_detail.setVisibility(View.VISIBLE);
                line_divider_detail.setVisibility(View.VISIBLE);
                model_vehicule.setText(driverPojo.getModel());
                numberplate_vehicule.setText(driverPojo.getNumberplate());
                driver_name.setText(driverPojo.getName());
                driver_rating.setText(driverPojo.getRate()+"/5");
                vehicule_name.setText(driverPojo.getTypeVehicule());
                cout_requete.setText(price+M.getCurrency(context));
                passenger.setText(driverPojo.getPassenger());
                driver_phone = driverPojo.getPhone();
                driver_id = String.valueOf(driverPojo.getId());
//                Toast.makeText(activity, ""+driverPojo.getPhoto(), Toast.LENGTH_SHORT).show();

                // loading model cover using Glide library
                Glide.with(context).load(AppConst.Server_url+"images/app_user/"+ driverPojo.getPhoto())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
//                    .centerCrop()
//                .asBitmap()
//                .error(R.drawable.ic_thumb_placeholder)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
//                                driver_image.setImageDrawable(getResources().getDrawable(R.drawable.user_profile));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .into(driver_image);
            }

            @Override
            public void onLongClick(View view, int position) {
                DriverPojo driverPojo = albumList_driver.get(position);
            }
        }));

//        distance = getDistance(loc1,loc2);
//        if(M.getRouteDistance(context).trim().length() != 0)
//            distance = M.getRouteDistance(context);
        tabDistance = distance.split(" ");

        if(tabDistance[1].equals("m"))
            distance_init = tabDistance[0];
        else
            distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

        cout = (price);
        if(statut_commission.equals("yes") && statut_commission_perc.equals("yes")){
            Float cout_fixed = Float.parseFloat(commission);
            Float cout_perc = Float.parseFloat(cout) + (Float.parseFloat(cout) * Float.parseFloat(commission_perc));
            cout = String.valueOf(cout_fixed + cout_perc);
        }else if(statut_commission.equals("yes")){
            cout = String.valueOf(Float.parseFloat(cout) + Float.parseFloat(commission));
        }else{
            cout = (price);
        }

        double i = Float.parseFloat(cout);
        double i2 = i/60000;
        float cost = (float) Math.round(i2 * 100) / 100;
        cost = cost * Float.parseFloat(distance_init);

        cout_ride.setText(price+ M.getCurrency(context));
//        distance_ride.setText(distance);
        ride_duration.setText(duration);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id_driver.equals("")){
                    if(!id_payment.equals("")){
//                            submitRide();
                            M.showLoadingDialog(context);
                            new setRide().execute(img_data);

                    }else{
                        Toast.makeText(activity, context.getResources().getString(R.string.choose_a_payment_method), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, context.getResources().getString(R.string.choose_a_rider), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        book_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id_driver.equals("")){
                    if(!id_payment.equals("")){
                        if(!img_data.equals("")) {
//                            submitRideBook();
                            bottomSheetFragmentBookingLater = new BottomSheetFragmentBookingLater(activity, loc1, loc2, distance, duration, id_type_vehicle, depart_name, destination_name, cout, img_data, id_driver,id_payment, place, number_people,
                                    statut_round);
                            bottomSheetFragmentBookingLater.show(((FragmentActivity) activity).getSupportFragmentManager(), bottomSheetFragmentBookingLater.getTag());
                        }else{
                            Toast.makeText(activity, ""+context.getResources().getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, context.getResources().getString(R.string.choose_a_payment_method), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, context.getResources().getString(R.string.choose_a_rider), Toast.LENGTH_SHORT).show();
                }
            }
        });

        new getPaymentMethod().execute();
        new getDriver().execute();

        setCancelable(false);

        return rootView;
    }

    //This method would confirm the otp
    private void dialogComment(String driver_id) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_comments, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close_note = (TextView) confirmDialog.findViewById(R.id.close_note);
        layout_nothing_comment = (LinearLayout) confirmDialog.findViewById(R.id.layout_nothing_comment);
        progressBar_comment = (ProgressBar) confirmDialog.findViewById(R.id.progressBar_comment);
        recycler_view_comments = (RecyclerView) confirmDialog.findViewById(R.id.recycler_view_comments);
        albumList_comments = new ArrayList<>();
        adapter_comments = new CommentAdapter(context, albumList_comments, getActivity());
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_comments.setLayoutManager(verticalLayoutManager);
        recycler_view_comments.setItemAnimator(new DefaultItemAnimator());
        recycler_view_comments.setAdapter(adapter_comments);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        close_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        new getDriverReview().execute(driver_id);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    /*String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }*/

    /** Appeler numéro de téléphone **/
    public void callNumber(String numero){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+numero.trim()));
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(callIntent);
        }
    }

    /** Récupération des conducteurs disponibles**/
    public class getDriver extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_driver.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                id_driver = "";
                                albumList_driver.clear();
                                adapter_driver.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    nothing.setVisibility(View.GONE);
                                    recycler_view_driver.setVisibility(View.VISIBLE);

                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
                                        if(i == 0){
                                            albumList_driver.add(new DriverPojo(taxi.getInt("id"),taxi.getString("nom")+" "+taxi.getString("prenom"),taxi.getString("phone"),
                                                    taxi.getString("email"), taxi.getString("online"), taxi.getString("photo"), taxi.getString("idVehicule")
                                                    , taxi.getString("brand"), taxi.getString("model"), taxi.getString("color")
                                                    , taxi.getString("numberplate"), taxi.getString("typeVehicule"), taxi.getString("moyenne"), "no", taxi.getString("passenger")));
                                        }else{
                                            albumList_driver.add(new DriverPojo(taxi.getInt("id"),taxi.getString("nom")+" "+taxi.getString("prenom"),taxi.getString("phone"),
                                                    taxi.getString("email"), taxi.getString("online"), taxi.getString("photo"), taxi.getString("idVehicule")
                                                    , taxi.getString("brand"), taxi.getString("model"), taxi.getString("color")
                                                    , taxi.getString("numberplate"), taxi.getString("typeVehicule"), taxi.getString("moyenne"), "no", taxi.getString("passenger")));
                                        }
                                        adapter_driver.notifyDataSetChanged();
                                    }
                                }else{
                                    nothing.setVisibility(View.VISIBLE);
                                    recycler_view_driver.setVisibility(View.GONE);
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
                    params.put("type_vehicle", id_type_vehicle);
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

    /** Récupération des conducteurs disponibles**/
    public class getDriverReview extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_driver_review.php";
            String driver_id = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                albumList_comments.clear();
                                adapter_comments.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_nothing_comment.setVisibility(View.GONE);
                                    progressBar_comment.setVisibility(View.GONE);
                                    recycler_view_comments.setVisibility(View.VISIBLE);
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject comment = msg.getJSONObject(String.valueOf(i));
                                        albumList_comments.add(new CommentPojo(comment.getInt("idNote"),comment.getString("prenom")+" "+comment.getString("nom"),comment.getString("photo_path"),comment.getString("comment"),comment.getString("niveau")));
                                        adapter_comments.notifyDataSetChanged();
                                    }
                                }else{
                                    layout_nothing_comment.setVisibility(View.VISIBLE);
                                    progressBar_comment.setVisibility(View.GONE);
                                    recycler_view_comments.setVisibility(View.GONE);
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
                    params.put("driver_id", driver_id);
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

    /** Récupération des méthodes de paiement**/
    public class getPaymentMethod extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_payment_method.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                id_payment = "";
                                albumList_payment.clear();
                                adapter_payment.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject payment = msg.getJSONObject(String.valueOf(i));
                                        if(payment.getString("libelle").equals("Cash")) {
                                            id_payment = payment.getString("id");
                                            albumList_payment.add(new PaymentMethodPojo(payment.getInt("id"), payment.getString("libelle"), payment.getString("image"), "yes"));
                                        }else
                                            albumList_payment.add(new PaymentMethodPojo(payment.getInt("id"),payment.getString("libelle"),payment.getString("image"),"no"));
                                        adapter_payment.notifyDataSetChanged();
                                    }
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNumber(driver_phone);
                }
                else
                {

                }
                return;
            }
        }
    }

    /*private String getDistance(Location loc1, Location loc2){
        final String[][] tab = {{}};

        float distanceInMeters1 = loc1.distanceTo(loc2);
        tab[0] = String.valueOf(distanceInMeters1).split("\\.");
        String distance = tab[0][0];
        return distance;
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (mapFragment != null)
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    /** Enregistrement d'une requête**/
    private class setRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_requete.php";
            final String img_data = params[0];
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
                                    dialogSucess();
//                                    Toast.makeText(activity, context.getResources().getString(R.string.your_booking_as_been_sent_successfully), Toast.LENGTH_LONG).show();
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
                    params.put("duree", duration);
                    params.put("id_conducteur", id_driver);
                    params.put("id_payment", id_payment);
                    params.put("depart_name", depart_name);
                    params.put("destination_name", destination_name);
                    params.put("place", place);
                    params.put("number_poeple", number_people);
                    params.put("image", img_data);
                    params.put("image_name", "recu_trajet_image_"+System.currentTimeMillis()+".jpg");
                    params.put("statut_round", statut_round);
                    if(statut_round.equals("yes")) {
                        params.put("date_retour", val_date_depart);
                        params.put("heure_retour", heure_retour.getText().toString());
                    }else{
                        params.put("date_retour", "");
                        params.put("heure_retour", "");
                    }
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

        msg.setText(context.getResources().getString(R.string.your_booking_as_been_sent_successfully));

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
                FragmentHome.dismissBottomSheetFragmentBooking();
                dismiss();
            }
        });
        alertDialog.setCancelable(false);
    }
}
