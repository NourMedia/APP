package com.raismedia.taxiclient.adapter;

/**
 * Created by RaisMedia on 01/12/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.RideBookPojo;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideBookAdapter extends RecyclerView.Adapter<RideBookAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<RideBookPojo> albumList;
    private List<RideBookPojo> albumList_filter = new ArrayList<>();
    Activity activity;
    private String currentActivity;
    private String distance = "";
    final private String[][] tab = {{}};
    final private String[][] tab1 = { {} };
    private static final int REQUEST_PHONE_CALL = 1;
    public static AlertDialog alertDialog;
    private TextView cancel_note,save_note;
    private RatingBar rate_conducteur,rate_conducteur_princ;
    ConnectionDetector connectionDetector;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String current_date = "";

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView depart,destination,cost_ride,distance_ride,duration_ride,name_driver,
                statut_ride,date_ride,cancel,view_map,driver_rating,pay,start_ride;
//        private LinearLayout layout_distance_requete;
        private RelativeLayout relative_layout;
        private ImageView call_driver,call_driver_2,date_book,payment_method,round_trip;
        private LinearLayout layou_rate;
        private TextView place,number_people,heure_retour,date_retour,at;

        public MyViewHolder(View view) {
            super(view);
            if(currentActivity.equals("RideNewDriver")){
//                user_name_requete = (TextView) view.findViewById(R.id.user_name_requete);
//                distance_client_requete = (TextView) view.findViewById(R.id.distance_client_requete);
            }
            depart = (TextView) view.findViewById(R.id.depart);
            destination = (TextView) view.findViewById(R.id.destination);
            cost_ride = (TextView) view.findViewById(R.id.cost_ride);
            distance_ride = (TextView) view.findViewById(R.id.distance_ride);
            duration_ride = (TextView) view.findViewById(R.id.duration_ride);
            name_driver = (TextView) view.findViewById(R.id.name_driver);
            statut_ride = (TextView) view.findViewById(R.id.statut_ride);
            date_ride = (TextView) view.findViewById(R.id.date_ride);
            cancel = (TextView) view.findViewById(R.id.cancel);
            view_map = (TextView) view.findViewById(R.id.view_map);
            driver_rating = (TextView) view.findViewById(R.id.driver_rating);
            start_ride = (TextView) view.findViewById(R.id.start_ride);
            pay = (TextView) view.findViewById(R.id.pay);
//            layout_distance_requete = (LinearLayout) view.findViewById(R.id.layout_distance_requete);
            relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            call_driver = (ImageView) view.findViewById(R.id.call_driver);
            call_driver_2 = (ImageView) view.findViewById(R.id.call_driver_2);
            date_book = (ImageView) view.findViewById(R.id.date_book);
            payment_method = (ImageView) view.findViewById(R.id.payment_method);
            layou_rate = (LinearLayout) view.findViewById(R.id.layou_rate);
            place = (TextView) view.findViewById(R.id.place);
            number_people = (TextView) view.findViewById(R.id.number_people);
            round_trip = (ImageView) view.findViewById(R.id.round_trip);
            heure_retour = (TextView) view.findViewById(R.id.heure_retour);
            at = (TextView) view.findViewById(R.id.at);
            date_retour = (TextView) view.findViewById(R.id.date_retour);
            connectionDetector=new ConnectionDetector(context);

            Calendar c = null;
            int mYear = 0,mMonth = 0,mDay = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            }

            current_date = mYear +"-"+mMonth+"-"+ mDay;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RideBookPojo ridePojo = albumList.get(getAdapterPosition());

        }
    }

    public RideBookAdapter(Context context, List<RideBookPojo> albumList, Activity activity, String currentActivity) {
        this.context = context;
        this.albumList = albumList;
        this.albumList_filter = albumList;
        this.activity = activity;
        this.currentActivity = currentActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
//        if(currentActivity.equals("RideNewDriver"))
//            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_ride_new_rider, parent, false);
//        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_ride_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.values != null) {
                    albumList = (List<RideBookPojo>) results.values;
                }
                else {
                    albumList = albumList;
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<RideBookPojo> filteredList = new ArrayList<RideBookPojo>();
                String date = "";
                int mDay = 0,mMonth = 0,mYear = 0;
                String[] tabDate;

                //Do your filtering operation with the constraint String
                //Return result for publishResults to use it
                for(int i=0; i<albumList_filter.size(); i++){
                    if(albumList_filter.get(i).getDate_book().contains(constraint)) {
                        albumList_filter.get(i).setDate(String.valueOf(constraint));
                        tabDate = String.valueOf(constraint).split("-");
                        mYear = Integer.parseInt(tabDate[0]);
                        mMonth = Integer.parseInt(tabDate[1]);
                        mDay = Integer.parseInt(tabDate[2]);
                        date = mDay + " " + MONTHS[mMonth-1] + ". " + mYear;
                        albumList_filter.get(i).setDate(date);
                        filteredList.add(albumList_filter.get(i));
                        current_date = String.valueOf(constraint);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
                return results;
            }
        };
        return filter;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RideBookPojo ridePojo = albumList.get(position);
        holder.date_retour.setVisibility(View.INVISIBLE);
        holder.at.setVisibility(View.INVISIBLE);
        if(ridePojo.getStatut_round().equals("yes")){
            holder.round_trip.setVisibility(View.VISIBLE);
            holder.heure_retour.setVisibility(View.VISIBLE);
            holder.heure_retour.setText(ridePojo.getHeure_retour());
        }else {
            holder.round_trip.setVisibility(View.INVISIBLE);
            holder.heure_retour.setVisibility(View.INVISIBLE);
            holder.heure_retour.setText("");
        }
        holder.date_book.setVisibility(View.VISIBLE);
        holder.number_people.setText(ridePojo.getNumber_poeple());
        if(currentActivity.equals("RideNew")){
            holder.cancel.setVisibility(View.VISIBLE);
            holder.call_driver.setVisibility(View.VISIBLE);
            holder.call_driver_2.setVisibility(View.GONE);
        }else{
            holder.cancel.setVisibility(View.GONE);
            holder.call_driver.setVisibility(View.GONE);
            holder.call_driver_2.setVisibility(View.VISIBLE);
        }
        if(ridePojo.getStatut().equals("completed")){
            holder.pay.setVisibility(View.VISIBLE);
            if(ridePojo.getStatut_paiement().equals("yes")){
                holder.pay.setText("تم الدفع");
                holder.pay.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
                holder.pay.setEnabled(false);
                holder.pay.setTextColor(Color.WHITE);
            }else{
                holder.pay.setText("الدفع");
                holder.pay.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
                holder.pay.setEnabled(true);
                holder.pay.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
            }
        }else{
            holder.pay.setVisibility(View.GONE);
        }
        if(currentActivity.equals("RideOnRide")){
            holder.start_ride.setVisibility(View.VISIBLE);
        }else{
            holder.start_ride.setVisibility(View.GONE);
        }
        holder.driver_rating.setText(ridePojo.getMoyenne()+"/5");
        distance = ridePojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";
//        holder.status_ride.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
        holder.depart.setText(ridePojo.getDepart_name());
        holder.destination.setText(ridePojo.getDestination_name());
        holder.date_ride.setText(ridePojo.getDate()+" at "+ridePojo.getHeure_depart());
        if(currentActivity.equals("RideConfirmed"))
            holder.cost_ride.setText(ridePojo.getCu_ride()+" "+ M.getCurrency(context));
        else
            holder.cost_ride.setText(ridePojo.getCout()+" "+ M.getCurrency(context));
        holder.distance_ride.setText(distance);
        holder.duration_ride.setText(ridePojo.getDuree());
        holder.statut_ride.setText(ridePojo.getStatut());
        holder.name_driver.setText(ridePojo.getConducteur_name());
        holder.view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogViewMap(ridePojo.getTrajet());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.date_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogDateBook(ridePojo.getDate_book().replace(",",", "));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageCancel(ridePojo.getId(),position,ridePojo.getConducteur_id(),ridePojo.getUser_name());
            }
        });
        holder.call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(ridePojo.getDriver_phone());
            }
        });
        holder.call_driver_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(ridePojo.getDriver_phone());
            }
        });
        holder.layou_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogRate(ridePojo.getNote(), String.valueOf(ridePojo.getConducteur_id()), position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.start_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGoogleMapsInstalled())
                    loadNavigationView(ridePojo.getLatitude_destination(),ridePojo.getLongitude_destination());
                else
                    Toast.makeText(context, context.getResources().getString(R.string.you_need_to_install_google_map_app), Toast.LENGTH_SHORT).show();
            }
        });
//        holder.pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    MainActivity.dialogPaymentAmount(ridePojo.getCout(),String.valueOf(ridePojo.getId()),String.valueOf(position),String.valueOf(ridePojo.getConducteur_id()),ridePojo.getPayment_method());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        // loading model cover using Glide library
        Glide.with(context).load(AppConst.Server_urlMain+"assets/images/payment_method/"+ridePojo.getImg_payment_method())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
//                    .centerCrop()
//                .asBitmap()
//                .error(R.drawable.ic_thumb_placeholder)
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
                .into(holder.payment_method);
        holder.place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogPlace(ridePojo.getPlace());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //This method would confirm the otp
    private void dialogSucess(String message) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        msg.setText(message);

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

    //This method would confirm the otp
    private void dialogPlace(String place) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_exactly_place, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView input_place = (TextView) confirmDialog.findViewById(R.id.input_place);
        TextView cancel = (TextView) confirmDialog.findViewById(R.id.cancel);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        input_place.setText(place);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogDateBook(String date_book) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_date_book, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        msg.setText(date_book);

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
    }

    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(navigationIntent);
    }

    public boolean isGoogleMapsInstalled() {
        try{
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //This method would confirm the otp
    private void dialogRate(final String note_, final String id_driver, final int position) throws JSONException {
        final String[] note = {note_};
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_noter, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_note = (TextView) confirmDialog.findViewById(R.id.save_note);
        cancel_note = (TextView) confirmDialog.findViewById(R.id.cancel_note);
        rate_conducteur = (RatingBar) confirmDialog.findViewById(R.id.rate_conducteur);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

//        Toast.makeText(context, ""+note, Toast.LENGTH_SHORT).show();
        if(note[0].trim().length() > 0)
            rate_conducteur.setRating(Float.parseFloat(note[0]));
        else
            rate_conducteur.setRating(1f);
        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    M.showLoadingDialog(context);
                    new setNote().execute(note[0],id_driver, String.valueOf(position));
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
    }

    /** Enregistrer la note d'un conducteur **/
    private class setNote extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_note.php";
            final String note_value = params[0];
            final String id_conducteur = params[1];
            final String position = params[2];
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

                                    albumList.get(Integer.parseInt(position)).setNb_avis(nb_avis);
                                    albumList.get(Integer.parseInt(position)).setNote(niveau);
                                    albumList.get(Integer.parseInt(position)).setMoyenne(moyenne);

                                    notifyItemChanged(Integer.parseInt(position));

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

    public void showMessageCancel(final int idRide, final int position, final int id_driver, final String user_name){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.do_you_want_to_cancel_your_request))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        M.showLoadingDialog(context);
                        new cancelRide().execute(String.valueOf(idRide),String.valueOf(position),String.valueOf(id_driver),user_name);
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

    //This method would confirm the otp
    private void dialogViewMap(String img) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_view_map, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        ImageView image = (ImageView) confirmDialog.findViewById(R.id.image);
        final ProgressBar progressBar = (ProgressBar) confirmDialog.findViewById(R.id.progressBar);

        // loading model cover using Glide library
        Glide.with(context).load(AppConst.Server_url+"images/recu_trajet_course/"+ img)
                .skipMemoryCache(false)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
    }

    /** Annuler un requête **/
    public class cancelRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"canceled_requete.php";
            final String id_ride = params[0];
            final String position = params[1];
            final String id_driver = params[2];
            final String user_name = params[3];
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
                                if(etat.equals("1")){
                                    delete(Integer.parseInt(position));
                                    notifyDataSetChanged();
                                    dialogSucess(context.getResources().getString(R.string.canceled_successfull));
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
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_ride", id_ride);
                    params.put("id_driver", id_driver);
                    params.put("user_name", user_name);
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
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(int position){
        albumList.remove(position);
        notifyItemRemoved(position);
        return;
    }

    public RideBookPojo getRequete(int id){
        RideBookPojo ridePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                ridePojo = albumList.get(i);
                break;
            }
        }
        return ridePojo;
    }

    public void restoreItem(RideBookPojo ridePojo, int position) {
        albumList.add(position, ridePojo);
        notifyItemInserted(position);
    }

    public List<RideBookPojo> getData() {
        return albumList;
    }
}