package com.raismedia.taxiclient.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.User;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {
    private TextView send,i_have_account;
    private String val_firstname_subs, val_email_subs, val_phone_subs, val_password_subs, val_password_conf_subs,account_type;
    private static EditText phone_subs,password_subs,password_conf,firstname_subs,email_insc;
    private Context context;
    private static ProgressBar progressBar_subs;
    private TextInputLayout input_layout_firstname_subs, input_layout_phone_subs,input_layout_password_inc
            ,input_layout_password_conf,input_layout_email_insc;
    private static String global_url = AppConst.Server_url;
    private TextView title;
    private String id_driver = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        context = SubscribeActivity.this;

        Bundle objetbundle = this.getIntent().getExtras();
        account_type = objetbundle.getString("account_type");

        send = (TextView)findViewById(R.id.send);
        i_have_account = (TextView)findViewById(R.id.i_have_account);
        phone_subs = (EditText) findViewById(R.id.phone_subs);
        password_subs = (EditText) findViewById(R.id.password_subs);
        password_conf = (EditText) findViewById(R.id.password_conf);
        firstname_subs = (EditText)findViewById(R.id.firstname_subs);
        email_insc = (EditText)findViewById(R.id.email_insc);
        progressBar_subs = (ProgressBar) findViewById(R.id.progressBar_subs);
        input_layout_firstname_subs = (TextInputLayout)findViewById(R.id.input_layout_firstname_subs);
        input_layout_phone_subs = (TextInputLayout)findViewById(R.id.input_layout_phone_subs);
        input_layout_password_inc = (TextInputLayout)findViewById(R.id.input_layout_password_inc);
        input_layout_password_conf = (TextInputLayout)findViewById(R.id.input_layout_password_conf);
        input_layout_email_insc = (TextInputLayout)findViewById(R.id.input_layout_email_insc);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(AppConst.font_quicksand_medium(context));

        if(account_type.equals("customer"))
            input_layout_email_insc.setVisibility(View.GONE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_firstname_subs = firstname_subs.getText().toString();
                val_email_subs = email_insc.getText().toString();
                val_phone_subs = phone_subs.getText().toString();
                val_password_subs = password_subs.getText().toString();
                val_password_conf_subs = password_conf.getText().toString();
//                startActivity(new Intent(SubscribeActivity.this, ChoosePhotoActivity.class));
                if(account_type.equals("customer"))
                    submitFormSubscribeCustomer();
                else
                    submitFormSubscribe();
            }
        });
        i_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent;
        if(account_type.equals("customer")) {
            intent = new Intent(SubscribeActivity.this, MainActivity.class);
        }else {
            intent = new Intent(SubscribeActivity.this, DriverVehicleActivity.class);
            intent.putExtra("id_driver",id_driver);
        }
        intent.putExtra("fragment_name", "");
        startActivity(intent);
        finish();
    }

    /**
     * Validating form
     */
    private void submitFormSubscribeCustomer() {
        if (!validatefirstname()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validatePhoneValid()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordValid()) {
            return;
        }
        if (!validatepasswordConf()) {
            return;
        }
        if(val_password_conf_subs.equals(val_password_subs)){
            progressBar_subs.setVisibility(View.VISIBLE);
            new createUser().execute();
        }else{
            Toast.makeText(context, getResources().getString(R.string.password_requires), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitFormSubscribe() {
        if (!validatefirstname()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validatePhoneValid()) {
            return;
        }
        if (!validateEmailValid()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordValid()) {
            return;
        }
        if (!validatepasswordConf()) {
            return;
        }
        if(val_password_conf_subs.equals(val_password_subs)){
            progressBar_subs.setVisibility(View.VISIBLE);
            new createUser().execute();
        }else{
            Toast.makeText(context, getResources().getString(R.string.password_requires), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatefirstname() {
        if (firstname_subs.getText().toString().trim().isEmpty()) {
            input_layout_firstname_subs.setError(getResources().getString(R.string.enter_your_password));
            requestFocus(firstname_subs);
            return false;
        } else {
            input_layout_firstname_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (phone_subs.getText().toString().trim().isEmpty()) {
            input_layout_phone_subs.setError(getResources().getString(R.string.enter_your_phone_number));
            requestFocus(phone_subs);
            return false;
        } else {
            input_layout_phone_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoneValid() {
        if (phone_subs.getText().toString().trim().length() < 10) {
            input_layout_phone_subs.setError(getResources().getString(R.string.enter_a_good_phone_number));
            requestFocus(phone_subs);
            return false;
        } else {
            input_layout_phone_subs.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmailValid() {
        if (email_insc.getText().toString().trim().isEmpty()) {
            input_layout_email_insc.setError(getResources().getString(R.string.enter_your_email_address));
            requestFocus(email_insc);
            return false;
        } else {
            input_layout_email_insc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password_subs.getText().toString().trim().isEmpty()) {
            input_layout_password_inc.setError(getResources().getString(R.string.enter_your_password));
            requestFocus(password_subs);
            return false;
        } else {
            input_layout_password_inc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswordValid() {
        if (password_subs.getText().toString().trim().length() < 8) {
            input_layout_password_inc.setError(getResources().getString(R.string.passwor_requires_at_least_characters));
            requestFocus(password_subs);
            return false;
        } else {
            input_layout_password_inc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatepasswordConf() {
        if (password_conf.getText().toString().trim().isEmpty()) {
            input_layout_password_conf.setError(getResources().getString(R.string.confirm_password));
            requestFocus(password_conf);
            return false;
        } else {
            input_layout_password_conf.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrement d'un utilisateur **/
    private class createUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = global_url+"user_register.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBar_subs.setVisibility(View.INVISIBLE);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    JSONObject user = json.getJSONObject("user");

                                    phone_subs.setText("");
                                    password_subs.setText("");
                                    password_conf.setText("");
                                    firstname_subs.setText("");
                                    email_insc.setText("");
//                                    Toast.makeText(context, "Successfully completed", Toast.LENGTH_SHORT).show();

                                    if(account_type.equals("customer")){
                                        saveProfile(new User(user.getString("id"),user.getString("nom"),user.getString("prenom"),user.getString("phone")
                                                ,user.getString("email"),user.getString("statut"),user.getString("login_type"),user.getString("tonotify"),user.getString("device_id"),
                                                user.getString("fcm_id"),user.getString("creer"),user.getString("modifier"),user.getString("photo_path"),user.getString("user_cat"),"",user.getString("currency")
                                                ,"","" ,"","","","","",user.getString("country")));
                                    }else{
                                        id_driver = user.getString("id");
                                        saveProfile(new User(user.getString("id"),user.getString("nom"),user.getString("prenom"),user.getString("phone")
                                                ,user.getString("email"),user.getString("statut"),user.getString("login_type"),user.getString("tonotify"),user.getString("device_id"),
                                                user.getString("fcm_id"),user.getString("creer"),user.getString("modifier"),user.getString("photo_path"),user.getString("user_cat"),"",user.getString("currency")
                                                ,user.getString("statut_licence"),user.getString("statut_nic"),"","","","",user.getString("statut_vehicule"),user.getString("country")));
                                    }

//                                    phone_subs.setText(phone);
//                                    Connexion.input_phone.setText(user.getString("phone"));
                                    launchHomeScreen();
                                }else if(etat.equals("2")){
                                    Toast.makeText(context, "This number already exists", Toast.LENGTH_SHORT).show();
                                    requestFocus(phone_subs);
                                    input_layout_phone_subs.setError("Enter another phone number");
                                }else
                                    Toast.makeText(context, "Failure to register", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar_subs.setVisibility(View.INVISIBLE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firstname", val_firstname_subs);
                    params.put("phone", val_phone_subs);
                    params.put("password", val_password_subs);
                    params.put("email", val_email_subs);
                    params.put("account_type", account_type);
                    params.put("login_type", "phone");
                    params.put("tonotify", "yes");
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
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

    private void saveProfile(User user){
        M.setNom(user.getNom(),context);
        M.setPrenom(user.getPrenom(),context);
        M.setPhone(user.getPhone(),context);
        M.setEmail(user.getEmail(),context);
        M.setID(user.getId(),context);
        M.setlogintype(user.getLogin_type(),context);
        M.setUsername(user.getNom(),context);
        M.setUserCategorie(user.getUser_cat(),context);
//        M.setCoutByKm(user.getCost(),context);
        M.setCurrentFragment("",context);
        M.setCurrency(user.getCurrency(),context);
        if(!user.getUser_cat().equals("user_app"))
            M.setStatutConducteur(user.getStatut_online(),context);

        M.setVehicleBrand(user.getVehicle_brand(),context);
        M.setVehicleColor(user.getVehicle_color(),context);
        M.setVehicleModel(user.getVehicle_model(),context);
        M.setVehicleNumberPlate(user.getVehicle_numberplate(),context);
        M.setCountry(user.getCountry(),context);
        if(user.getTonotify().equals("yes"))
            M.setPushNotification(true, context);
        else
            M.setPushNotification(false, context);

        updateFCM(M.getID(context));
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
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
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
