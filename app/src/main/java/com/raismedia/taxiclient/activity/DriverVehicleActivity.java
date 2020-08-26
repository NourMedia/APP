package com.raismedia.taxiclient.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.adapter.CategoryVehicleAdapter;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.CategoryVehiclePojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverVehicleActivity extends AppCompatActivity {
    private String val_brand_subs, val_model_subs, val_color_subs, val_numberplate_subs, val_number_passengers_subs;
    private static EditText brand_subs,model_subs, color_subs,numberplate_subs,number_passengers_subs;
    private TextInputLayout input_layout_brand_subs, input_layout_model_subs, input_layout_color_subs, input_layout_number_passengers_subs
            , input_layout_numberplate_subs;
    private Context context;
//    private static ProgressBar progressBar_subs;
    private FloatingActionButton button_next;
    private static String global_url = AppConst.Server_url;
    public static RecyclerView recycler_view_category_vehicle;
    public static List<CategoryVehiclePojo> albumList_category_vehicle;
    public static CategoryVehicleAdapter adapter_category_vehicle;
    public static String id_categorie_vehicle = "",id_driver="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_vehicle);
        context = DriverVehicleActivity.this;

        Bundle objetbundle = this.getIntent().getExtras();
        id_driver = objetbundle.getString("id_driver");

        brand_subs = (EditText) findViewById(R.id.brand_subs);
        model_subs = (EditText) findViewById(R.id.model_subs);
        color_subs = (EditText)findViewById(R.id.color_subs);
        numberplate_subs = (EditText)findViewById(R.id.numberplate_subs);
        number_passengers_subs = (EditText)findViewById(R.id.number_passengers_subs);
//        progressBar_subs = (ProgressBar) findViewById(R.id.progressBar_subs);
        input_layout_brand_subs = (TextInputLayout)findViewById(R.id.input_layout_brand_subs);
        input_layout_model_subs = (TextInputLayout)findViewById(R.id.input_layout_model_subs);
        input_layout_color_subs = (TextInputLayout)findViewById(R.id.input_layout_color_subs);
        input_layout_numberplate_subs = (TextInputLayout)findViewById(R.id.input_layout_numberplate_subs);
        input_layout_number_passengers_subs = (TextInputLayout)findViewById(R.id.input_layout_number_passengers_subs);
        button_next = (FloatingActionButton) findViewById(R.id.button_next);

        albumList_category_vehicle = new ArrayList<>();
        adapter_category_vehicle = new CategoryVehicleAdapter(context, albumList_category_vehicle,this);
        recycler_view_category_vehicle = (RecyclerView) findViewById(R.id.recycler_view_category_vehicle);
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category_vehicle.setLayoutManager(verticalLayoutManager);
        recycler_view_category_vehicle.setItemAnimator(new DefaultItemAnimator());
        recycler_view_category_vehicle.setAdapter(adapter_category_vehicle);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!id_categorie_vehicle.equals("")){
                    val_brand_subs = brand_subs.getText().toString();
                    val_model_subs = model_subs.getText().toString();
                    val_color_subs = color_subs.getText().toString();
                    val_numberplate_subs = numberplate_subs.getText().toString();
                    val_number_passengers_subs = number_passengers_subs.getText().toString();
                    submitFormSubscribe();
                }else{
                    Toast.makeText(context, getResources().getString(R.string.please_select_your_vehicle_type), Toast.LENGTH_SHORT).show();
                }
            }
        });

        new getCategoryVehicle().execute();
    }

    /** Récupération des catégories de véhicule**/
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
                                albumList_category_vehicle.clear();
                                adapter_category_vehicle.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
                                        if(i==0){
                                            albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                    "",taxi.getString("prix"),"yes",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
                                        }else{
                                            albumList_category_vehicle.add(new CategoryVehiclePojo(taxi.getInt("id"),taxi.getString("libelle"),taxi.getString("image"),
                                                    "",taxi.getString("prix"),"no",taxi.getString("statut_commission"),taxi.getString("commission"),taxi.getString("type"),taxi.getString("statut_commission_perc"),taxi.getString("commission_perc"),taxi.getString("type_perc")));
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

    /**
     * Validating form
     */
    private void submitFormSubscribe() {
        if (!validateBrand()) {
            return;
        }
        if (!validateModel()) {
            return;
        }
        if (!validateColor()) {
            return;
        }
        if (!validateNumberPlate()) {
            return;
        }
        if (!validateNumberPassanger()) {
            return;
        }
//        progressBar_subs.setVisibility(View.VISIBLE);
        M.showLoadingDialog(context);
        new createDriverVehicle().execute();
    }

    private boolean validateBrand() {
        if (brand_subs.getText().toString().trim().isEmpty()) {
            input_layout_brand_subs.setError(getResources().getString(R.string.enter_your_vehicle_brand));
            requestFocus(brand_subs);
            return false;
        } else {
            input_layout_brand_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateModel() {
        if (model_subs.getText().toString().trim().isEmpty()) {
            input_layout_model_subs.setError(getResources().getString(R.string.enter_your_vehicle_model));
            requestFocus(model_subs);
            return false;
        } else {
            input_layout_model_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateColor() {
        if (color_subs.getText().toString().trim().isEmpty()) {
            input_layout_color_subs.setError(getResources().getString(R.string.enter_your_vehicle_color));
            requestFocus(color_subs);
            return false;
        } else {
            input_layout_color_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNumberPlate() {
        if (numberplate_subs.getText().toString().trim().isEmpty()) {
            input_layout_numberplate_subs.setError(getResources().getString(R.string.enter_your_vehicle_numberplate));
            requestFocus(numberplate_subs);
            return false;
        } else {
            input_layout_numberplate_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNumberPassanger() {
        if (number_passengers_subs.getText().toString().trim().isEmpty()) {
            input_layout_number_passengers_subs.setError(getResources().getString(R.string.enter_your_number_of_passenger));
            requestFocus(number_passengers_subs);
            return false;
        } else {
            input_layout_number_passengers_subs.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrement d'un utilisateur **/
    private class createDriverVehicle extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = global_url+"driver_vehicle_register.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {



                        @Override
                        public void onResponse(String response) {
                            try {
                                M.hideLoadingDialog();
//                                progressBar_subs.setVisibility(View.INVISIBLE);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    JSONObject vehicle = json.getJSONObject("vehicle");
                                    brand_subs.setText("");
                                    model_subs.setText("");
                                    color_subs.setText("");
                                    numberplate_subs.setText("");
//                                    Toast.makeText(context, "Successfully completed", Toast.LENGTH_SHORT).show();

                                    M.setVehicleBrand( vehicle.getString("brand"),context);
                                    M.setVehicleModel( vehicle.getString("model"),context);
                                    M.setVehicleColor( vehicle.getString("color"),context);
                                    M.setVehicleNumberPlate( vehicle.getString("numberplate"),context);

                                    Intent intent = new Intent(DriverVehicleActivity.this, LoginActivity.class);
                                    intent.putExtra("id_driver",id_driver);
                                    startActivity(intent);
                                    finish();
                                    if(etat.equals("1")){
                                        dialogSucess();
//                                    Toast.makeText(activity, context.getResources().getString(R.string.your_booking_as_been_sent_successfully), Toast.LENGTH_LONG).show();
                                    }

                                }


                                else if(etat.equals("2")){

                                }else
                                    Toast.makeText(context, "Failure to register", Toast.LENGTH_SHORT).show();

                            }


                            catch (JSONException e) {

                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    progressBar_subs.setVisibility(View.INVISIBLE);
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("brand", val_brand_subs);
                    params.put("model", val_model_subs);
                    params.put("color", val_color_subs);
                    params.put("numberplate", val_numberplate_subs);
                    params.put("passenger", val_number_passengers_subs);
                    params.put("id_categorie_vehicle", id_categorie_vehicle);
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
    private void dialogSucess() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);

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
                Intent intent = new Intent(DriverVehicleActivity.this, LoginActivity.class);
                intent.putExtra("id_driver", "");
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setCancelable(false);
    }

}
