package com.raismedia.taxiclient.fragment;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.activity.MainActivity;
import com.raismedia.taxiclient.adapter.TransactionAdapter;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.TransactionPojo;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.raismedia.taxiclient.settings.AppConst.API_CHECK_OUT;
import static com.raismedia.taxiclient.settings.AppConst.API_GET_TOKEN;

public class FragmentMyWallet extends Fragment{

    ViewPager pager;
    TabLayout tabs;
    View view;
    public static Context context;
    public static ConnectionDetector connectionDetector;
    String TAG="FragmentHome";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_transaction;
    public static List<TransactionPojo> albumList_transaction;
    public static TransactionAdapter adapter_transaction;

    View mapView;
    private LocationManager locationManager;
    private final int REQUEST_FINE_LOCATION = 1234;
    private String provider;
    private int PLACE_PICKER_REQUEST = 1;

    public static SwipeRefreshLayout swipe_refresh;

    private EditText input_edit_amount;
    private TextInputLayout intput_layout_amount;
    private TextView balance,add;
    private ProgressBar progressBar,progressBar2;
    private RelativeLayout layout_transaction;

    String token,amount;
    HashMap<String, String> paramsHash;
    private static final int REQUEST_CODE = 1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            currpos = getArguments().getInt("tab_pos",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_my_wallet, container, false);

        context=getActivity();
        if(M.getCountry(context).equals("All"))
            MainActivity.setTitle("My wallet");
        else
            MainActivity.setTitle("My wallet - "+M.getCountry(context));
        connectionDetector=new ConnectionDetector(context);

        albumList_transaction = new ArrayList<>();
        adapter_transaction = new TransactionAdapter(context, albumList_transaction, getActivity());

        layout_transaction = (RelativeLayout) view.findViewById(R.id.layout_transaction);
        input_edit_amount = (EditText) view.findViewById(R.id.input_edit_amount);
        intput_layout_amount = (TextInputLayout) view.findViewById(R.id.intput_layout_amount);
        balance = (TextView) view.findViewById(R.id.balance);
        add = (TextView) view.findViewById(R.id.add);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);

        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_transaction = (RecyclerView) view.findViewById(R.id.recycler_view_transaction);
        @SuppressLint("WrongConstant") LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_transaction.setLayoutManager(verticalLayoutManager);
        recycler_view_transaction.setItemAnimator(new DefaultItemAnimator());
        recycler_view_transaction.setAdapter(adapter_transaction);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getAllTransaction().execute();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAmount();
            }
        });

        new getToken().execute();
        new getWallet().execute();

        if(!M.getUserCategorie(context).equals("user_app")){
            layout_transaction.setVisibility(View.GONE);
            input_edit_amount.setVisibility(View.GONE);
            intput_layout_amount.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        }else
            new getAllTransaction().execute();

        return view;
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(context), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!input_edit_amount.getText().toString().isEmpty()) {
                    amount = input_edit_amount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);

                    sendPayment();
                } else {
                    Toast.makeText(context, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(context, "User Cancel", Toast.LENGTH_SHORT).show();
            else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("ERROR", error.toString());
            }
        }
    }

    private void sendPayment() {
//        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")) {
                            M.showLoadingDialog(context);
                            new setAmount().execute(input_edit_amount.getText().toString());
                        }else {
                            Toast.makeText(context, "Transaction failed!", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR_BRAINTREE", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR_BRAINTREE", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for(String key:paramsHash.keySet()){
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask {
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context, android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Please wait");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("ERROR_BRAINTREE", exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mDialog.dismiss();
        }
    }

    private void submitAmount() {
        if (!validateAmount()) {
            return;
        }
//        M.showLoadingDialog(context);
        submitPayment();
    }

    private boolean validateAmount() {
        if (input_edit_amount.getText().toString().trim().isEmpty()) {
            intput_layout_amount.setError(context.getResources().getString(R.string.enter_amount));
            requestFocus(input_edit_amount);
            return false;
        } else {
            intput_layout_amount.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Récupération toutes les transactions d'un utilisateur **/
    public class getAllTransaction extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_transaction.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                if(swipe_refresh.isRefreshing())
                                    swipe_refresh.setRefreshing(false);
                                progressBar2.setVisibility(View.GONE);
                                albumList_transaction.clear();
                                adapter_transaction.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject transaction = msg.getJSONObject(String.valueOf(i));
                                        albumList_transaction.add(new TransactionPojo(transaction.getInt("id"),transaction.getString("amount"),transaction.getString("creer")));
                                        adapter_transaction.notifyDataSetChanged();
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
                    progressBar2.setVisibility(View.GONE);
                    if(swipe_refresh.isRefreshing())
                        swipe_refresh.setVisibility(View.GONE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app", M.getID(context));
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

    /** Récupération user wallet**/
    public class setAmount extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_amount.php";
            String amount = params[0];
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
                                    balance.setText(M.getCurrency(context)+" "+msg.getString("amount"));
                                    MainActivity.balance.setText(M.getCurrency(context)+" "+msg.getString("amount"));
//                                    Toast.makeText(context, "Transaction successfuly", Toast.LENGTH_SHORT).show();
                                    dialogSucess(context.getResources().getString(R.string.transaction_successfull));
                                    new getAllTransaction().execute();
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
                    params.put("id_user", M.getID(context));
                    params.put("cat_user", M.getUserCategorie(context));
                    params.put("amount", amount);
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
                                progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
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
