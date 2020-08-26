package com.raismedia.taxiclient.fragment.customer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RaisMedia on 01/01/2019.
 */

public class BottomSheetFragmentLocation extends BottomSheetDialogFragment {
    private static Context context;
    private Activity activity;
    private TextView nombre, prix_reservation, annuler, envoyer,date_debut,date_fin;
    private EditText input_phone;
    private TextInputLayout input_layout_phone;
    private int pu;
    private static String global_url = AppConst.Server_url;
    ConnectionDetector connectionDetector;
    private LinearLayout layout_date_debut, layout_date_fin;
    private int mYear_debut, mMonth_debut, mDay_debut;
    private int mYear_fin, mMonth_fin, mDay_fin;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};
    String val_date_fin = "", val_date_debut = "";
    private long nb_days;
    private String mMonth_1;
    private int id_vehicule;

    public BottomSheetFragmentLocation() {
        // Required empty public constructor
    }

    public BottomSheetFragmentLocation(Activity activity, int pu, int id_vehicule) {
        this.activity = activity;
        this.pu = pu;
        this.id_vehicule = id_vehicule;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_location, container, false);
        input_phone = (EditText)rootView.findViewById(R.id.input_phone);
        input_layout_phone = (TextInputLayout)rootView.findViewById(R.id.input_layout_phone);
        nombre = (TextView) rootView.findViewById(R.id.nombre);
        prix_reservation = (TextView) rootView.findViewById(R.id.prix_reservation);
        envoyer = (TextView) rootView.findViewById(R.id.envoyer);
        annuler = (TextView) rootView.findViewById(R.id.annuler);
        date_debut = (TextView) rootView.findViewById(R.id.date_debut);
        date_fin = (TextView) rootView.findViewById(R.id.date_fin);
        layout_date_debut = (LinearLayout) rootView.findViewById(R.id.layout_date_debut);
        layout_date_fin = (LinearLayout) rootView.findViewById(R.id.layout_date_fin);

        context = getContext();
        connectionDetector=new ConnectionDetector(context);
//
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    M.showLoadingDialog(context);
                    new setLocation().execute(String.valueOf(nb_days), mYear_debut +"-"+mMonth_1+"-"+ mDay_debut, mYear_fin +"-"+mMonth_1+"-"+ mDay_fin, input_phone.getText().toString());
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final Calendar c;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            mYear_debut = c.get(Calendar.YEAR);
            mMonth_debut = c.get(Calendar.MONTH);
            mDay_debut = c.get(Calendar.DAY_OF_MONTH);
            mYear_fin = c.get(Calendar.YEAR);
            mMonth_fin = c.get(Calendar.MONTH);
            mDay_fin = c.get(Calendar.DAY_OF_MONTH)+1;
        }
        date_debut.setText(mDay_debut + " " + MONTHS[mMonth_debut] + ". " + mYear_debut);
        date_fin.setText(mDay_fin + " " + MONTHS[mMonth_fin] + ". " + mYear_fin);
        mMonth_1 = String.valueOf(mMonth_debut +1);
        if(mMonth_1.trim().length() == 1){
            mMonth_1 = '0'+mMonth_1;
        }

        val_date_fin = mDay_fin +"-"+mMonth_1+"-"+ mYear_fin;
        val_date_debut = mDay_debut +"-"+mMonth_1+"-"+ mYear_debut;
        nb_days = DateDifference(val_date_debut,val_date_fin);
        nb_days = nb_days+1;
        if(nb_days >= 0) {
            nombre.setText(String.valueOf(nb_days));
            prix_reservation.setText(String.valueOf(pu * nb_days));
        }

        setCancelable(false);

        layout_date_debut.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                                date_debut.setText(newdayOfMonth + " " + MONTHS[monthOfYear] + ". " + year);
                                val_date_debut = newdayOfMonth+"-"+newmonthOfYear+"-"+year;

                                nb_days = DateDifference(val_date_debut,val_date_fin);
                                nb_days = nb_days+1;
                                if(nb_days >= 0) {
                                    nombre.setText(String.valueOf(nb_days));
                                    prix_reservation.setText(String.valueOf(pu * nb_days));
                                }else{
                                    nombre.setText(String.valueOf(0));
                                    prix_reservation.setText(String.valueOf(0));
                                }

                                mYear_debut = year;
                                mMonth_debut = monthOfYear;
                                mDay_debut = Integer.parseInt(newdayOfMonth);
                            }
                        }, mYear_debut, mMonth_debut, mDay_debut);
                datePickerDialog.show();
            }
        });

        layout_date_fin.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

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
                                date_fin.setText(newdayOfMonth + " " + MONTHS[monthOfYear] + ". " + year);
                                val_date_fin = newdayOfMonth+"-"+newmonthOfYear+"-"+year;

                                nb_days = DateDifference(val_date_debut,val_date_fin);
                                nb_days = nb_days+1;
                                if(nb_days >= 0) {
                                    nombre.setText(String.valueOf(nb_days));
                                    prix_reservation.setText(String.valueOf(pu * nb_days));
                                }else{
                                    nombre.setText(String.valueOf(0));
                                    prix_reservation.setText(String.valueOf(0));
                                }

                                mYear_fin = year;
                                mMonth_fin = monthOfYear;
                                mDay_fin = Integer.parseInt(newdayOfMonth);
                            }
                        }, mYear_fin, mMonth_fin, mDay_fin);
                datePickerDialog.show();
            }
        });

        return rootView;
    }

    public long DateDifference(String date_jour, String date_drv) {
        // Creates two calendars instances
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();

        String[] tab_jour = date_jour.split("-");
        String[] tab_rdv = date_drv.split("-");

        // Set the date for both of the calendar instance
        cal1.set(Integer.parseInt(tab_jour[2]), Integer.parseInt(tab_jour[1]), Integer.parseInt(tab_jour[0]));
        cal2.set(Integer.parseInt(tab_rdv[2]), Integer.parseInt(tab_rdv[1]), Integer.parseInt(tab_rdv[0]));

        // Get the represented date in milliseconds
        long millis1 = cal1.getTimeInMillis();
        long millis2 = cal2.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = millis2 - millis1;

        // Calculate difference in seconds
        long diffSeconds = diff / 1000;

        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);

        // Calculate difference in hours
        long diffHours = diff / (60 * 60 * 1000);

        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /** Envoi d'une demande de réservation **/
    private class setLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String... params) {
            final String nb_jour = params[0];
            final String date_debut = params[1];
            final String date_fin = params[2];
            final String contact = params[3];

            String url = global_url+"set_location.php";
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
                                    dialogSucess(context.getResources().getString(R.string.envoyer_avec_succes));
                                    dismiss();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.echec_denvoi), Toast.LENGTH_SHORT).show();
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
                    params.put("nb_jour", nb_jour);
                    params.put("date_debut", date_debut);
                    params.put("date_fin", date_fin);
                    params.put("contact", contact);
                    params.put("id_user_app", M.getID(context));
                    params.put("id_vehicule", String.valueOf(id_vehicule));
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
}
