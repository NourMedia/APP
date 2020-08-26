package com.raismedia.taxiclient.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.fragment.customer.FragmentRideCompleted;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreditCard extends AppCompatActivity {
    private String id_ride,id_driver,position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        CardForm cardForm =  (CardForm)findViewById(R.id.cardForm);
        TextView txtDes = (TextView)findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);

        //Get Intent
        Intent intent = getIntent();

        id_ride = intent.getStringExtra("id_ride");
        id_driver = intent.getStringExtra("id_driver");
        position = intent.getStringExtra("position");
        txtDes.setText(intent.getStringExtra("amount")+" $");
        btnPay.setText(String.format("Payer %s", txtDes.getText()));
        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                M.showLoadingDialog(CreditCard.this);
                new PayRide().execute();
//                Toast.makeText(CreditCard.this, "Name : "+card.getName()+" | Last 4 digits : "+card.getLast4(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Payer un requête **/
    public class PayRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"pay_requete.php";
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
                                    FragmentRideCompleted.albumList_ride.get(Integer.parseInt(position)).setStatut_paiement("yes");
                                    FragmentRideCompleted.adapter_ride.notifyDataSetChanged();
                                    try {
                                        dialogSucess();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(CreditCard.this, "Failed", Toast.LENGTH_SHORT).show();
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
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        msg.setText(getResources().getString(R.string.payment_made_successfully));

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if(MainActivity.alertDialog.isShowing())
                    MainActivity.alertDialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
    }
}
