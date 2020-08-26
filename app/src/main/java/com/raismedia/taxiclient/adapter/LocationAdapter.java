package com.raismedia.taxiclient.adapter;

/**
 * Created by RaisMedia on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.raismedia.taxiclient.model.LocationPojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private Context context;
    private List<LocationPojo> albumList;
    Activity activity;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};
    private static String global_url = AppConst.Server_url;
    ConnectionDetector connectionDetector;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView nom_vehicule, date, statut_location, prix_total,moreOptions,cancel;
        private ImageView img_vehicule;
        private ProgressBar progressBar;
        private LinearLayout layout_option;


        public MyViewHolder(View view) {
            super(view);
            nom_vehicule = (TextView) view.findViewById(R.id.nom_vehicule);
            statut_location = (TextView) view.findViewById(R.id.statut_location);
            prix_total = (TextView) view.findViewById(R.id.prix_total);
            date = (TextView) view.findViewById(R.id.date);
            moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            cancel = (TextView) view.findViewById(R.id.cancel);
            img_vehicule = (ImageView) view.findViewById(R.id.img_vehicule);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            layout_option = (LinearLayout) view.findViewById(R.id.layout_option);

            connectionDetector=new ConnectionDetector(context);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            LocationPojo locationPojo = albumList.get(getAdapterPosition());
            if(locationPojo.getStatut().equals("in progress"))
                showMessageAnnuler(String.valueOf(locationPojo.getId()));
        }
    }

    public LocationAdapter(Context context, List<LocationPojo> albumList, Activity activity) {
        this.context = context;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_location, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LocationPojo locationPojo = albumList.get(position);
        holder.nom_vehicule.setText(locationPojo.getNom());
//        holder.statut_location.setText(coursePojo.getStatut());
        holder.prix_total.setText(M.getCurrency(context)+" "+locationPojo.getPrix());
        String tabDateDebut[] = locationPojo.getDate_debut().split("-");
        String tabDateFin[] = locationPojo.getDate_fin().split("-");

        holder.date.setText(tabDateDebut[2] + " " + MONTHS[Integer.parseInt(tabDateDebut[1])-1] + ". to "+tabDateFin[2] + " " + MONTHS[Integer.parseInt(tabDateFin[1])-1] + ". ");
//        holder.statut_location.setText(tabDateFin[2] + " " + MONTHS[Integer.parseInt(tabDateFin[1])-1] + ". ");

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageAnnuler(String.valueOf(locationPojo.getId()));
            }
        });
        holder.statut_location.setText(locationPojo.getStatut());
        if(locationPojo.getStatut().equals("in progress")){
            holder.layout_option.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.statut_location.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
            holder.statut_location.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
        }else if(locationPojo.getStatut().equals("accepted")){
            holder.cancel.setVisibility(View.INVISIBLE);
            holder.layout_option.setVisibility(View.GONE);
            holder.statut_location.setTextColor(Color.WHITE);
            holder.statut_location.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
        }else if(locationPojo.getStatut().equals("refuse")){
            holder.cancel.setVisibility(View.INVISIBLE);
            holder.layout_option.setVisibility(View.GONE);
            holder.statut_location.setTextColor(Color.WHITE);
            holder.statut_location.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
        }else {
            holder.cancel.setVisibility(View.INVISIBLE);
            holder.layout_option.setVisibility(View.GONE);
            holder.statut_location.setTextColor(Color.WHITE);
            holder.statut_location.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
        }

        holder.moreOptions.setVisibility(View.GONE);
        /*holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.moreOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_action_mes_location_vehicule);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_annuler:
                                if(connectionDetector.isConnectingToInternet()){
                                    progressdialog.show();
                                    new canceledLocation().execute(String.valueOf(locationPojo.getId()));
                                }else{
                                    Toast.makeText(context, "Pas de connexion internet", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });*/

        // loading model cover using Glide library
        Glide.with(context).load(AppConst.Server_urlMain+"assets/images/type_vehicle_rental/"+locationPojo.getImage())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
//                    .centerCrop()
//                .asBitmap()
//                .error(R.drawable.ic_thumb_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img_vehicule);
    }

    public void showMessageAnnuler(final String idLocation){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.do_you_want_to_cancel_your_car_rental_application))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(connectionDetector.isConnectingToInternet()){
                            M.showLoadingDialog(context);
                            new canceledLocation().execute(idLocation);
                        }else{
                            Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                        }
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

    /** Annuler la location d'un utilisateur**/
    private class canceledLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String... params) {
            final String id_location = params[0];

            String url = global_url+"canceled_location.php";
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
                                    M.hideLoadingDialog();
                                    dialogSucess(context.getResources().getString(R.string.annule_avec_succes));
                                    delete(getPosition(Integer.parseInt(id_location)));
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.echec_dannulation), Toast.LENGTH_SHORT).show();
                                    M.hideLoadingDialog();
                                }

                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.echec_denvoi), Toast.LENGTH_SHORT).show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_location", id_location);
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

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(int position){
        albumList.remove(position);
        notifyItemRemoved(position);
        return;
    }

    public LocationPojo getLocation(int id){
        LocationPojo locationPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                locationPojo = albumList.get(i);
                break;
            }
        }
        return locationPojo;
    }

    public int getPosition(int id){
        int pos = 0;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                pos = i;
                break;
            }
        }
        return pos;
    }
}