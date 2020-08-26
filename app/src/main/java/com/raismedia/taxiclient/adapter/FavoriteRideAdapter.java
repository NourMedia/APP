package com.raismedia.taxiclient.adapter;

/**
 * Created by RaisMedia on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.fragment.customer.FragmentHome;
import com.raismedia.taxiclient.model.FavoriteRidePojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteRideAdapter extends RecyclerView.Adapter<FavoriteRideAdapter.MyViewHolder> {

    private Context context;
    private List<FavoriteRidePojo> albumList;
    Activity activity;
    private String currentActivity;
    public static AlertDialog alertDialog;
    ConnectionDetector connectionDetector;
    private String distance = "";

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView depart,destination,distance_ride,date_ride, delete,fav_name;
//        private LinearLayout layout_distance_requete;
        private LinearLayout layout_down;

        public MyViewHolder(View view) {
            super(view);
            depart = (TextView) view.findViewById(R.id.depart);
            destination = (TextView) view.findViewById(R.id.destination);
            distance_ride = (TextView) view.findViewById(R.id.distance_ride);
            date_ride = (TextView) view.findViewById(R.id.date_ride);
            delete = (TextView) view.findViewById(R.id.delete);
            fav_name = (TextView) view.findViewById(R.id.fav_name);
            layout_down = (LinearLayout) view.findViewById(R.id.layout_down);
//            relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            connectionDetector=new ConnectionDetector(context);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(currentActivity.equals("FavRideHome")){
                FavoriteRidePojo ridePojo = albumList.get(getAdapterPosition());
                FragmentHome.alertDialog.cancel();
                FragmentHome.selectFavRide(ridePojo.getLatitude_depart(),ridePojo.getLongitude_depart(),ridePojo.getLatitude_destination(),ridePojo.getLongitude_destination(),ridePojo.getDepart_name(),ridePojo.getDestination_name());
            }
        }
    }

    public FavoriteRideAdapter(Context context, List<FavoriteRidePojo> albumList, Activity activity, String currentActivity) {
        this.context = context;
        this.albumList = albumList;
        this.activity = activity;
        this.currentActivity = currentActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_favorite_ride, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FavoriteRidePojo ridePojo = albumList.get(position);
        if(currentActivity.equals("FavRideHome")){
            holder.layout_down.setVisibility(View.GONE);
        }
        distance = ridePojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";
        holder.distance_ride.setText(distance);
        holder.depart.setText(ridePojo.getDepart_name());
        holder.destination.setText(ridePojo.getDestination_name());
        holder.date_ride.setText(ridePojo.getDate());
        holder.fav_name.setText(ridePojo.getFav_name());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageDelete(ridePojo.getId(),position);
            }
        });
    }

    public void showMessageDelete(final int idRide, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.do_you_want_to_delete_this_request))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        M.showLoadingDialog(context);
                        new deleteFavoriteRide().execute(String.valueOf(idRide),String.valueOf(position));
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
    }

    /** Supprimer un favoris **/
    public class deleteFavoriteRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"delete_favorite_ride.php";
            final String id_ride = params[0];
            final String position = params[1];
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
                                    dialogSucess(context.getResources().getString(R.string.deleted_successfull));
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
                    params.put("id_ride_fav", id_ride);
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

    public FavoriteRidePojo getRequete(int id){
        FavoriteRidePojo ridePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                ridePojo = albumList.get(i);
                break;
            }
        }
        return ridePojo;
    }

    public void restoreItem(FavoriteRidePojo ridePojo, int position) {
        albumList.add(position, ridePojo);
        notifyItemInserted(position);
    }

    public List<FavoriteRidePojo> getData() {
        return albumList;
    }
}