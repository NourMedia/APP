package com.raismedia.taxiclient.fragment.driver;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.activity.MainActivity;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentDashboard extends Fragment{

    ViewPager pager;
    TabLayout tabs;
    View view;
    public static Context context;
    public static ConnectionDetector connectionDetector;
    String TAG="FragmentHome";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;
    private RelativeLayout layout_new,layout_confirmed,layout_onride,layout_completed,layout_sales,layout_today_earning;
    public static ProgressBar progressBar_failed;
    public static LinearLayout layout_not_found,layout_failed;
    public static LinearLayout layout_liste;
    private static TextView dash_new_nb,dash_confirmed_nb,dash_onride_nb,dash_completed_nb,dash_today_earning_nb, dash_sales_nb;
    private int mYear_depart, mMonth_depart, mDay_depart;
//    private static String today;

    public static SwipeRefreshLayout swipe_refresh;

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
        view= inflater.inflate(R.layout.fragment_dashboard, container, false);

        context=getActivity();
        if(M.getCountry(context).equals("All"))
            MainActivity.setTitle("Dashboard");
        else
            MainActivity.setTitle("Dashboard - "+M.getCountry(context));
        connectionDetector=new ConnectionDetector(context);

        layout_new = (RelativeLayout) view.findViewById(R.id.layout_new);
        layout_confirmed = (RelativeLayout) view.findViewById(R.id.layout_confirmed);
        layout_onride = (RelativeLayout) view.findViewById(R.id.layout_onride);
        layout_completed = (RelativeLayout) view.findViewById(R.id.layout_completed);
        layout_sales = (RelativeLayout) view.findViewById(R.id.layout_sales);
        layout_today_earning = (RelativeLayout) view.findViewById(R.id.layout_today_earning);
        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        layout_liste = (LinearLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        dash_new_nb = (TextView) view.findViewById(R.id.dash_new_nb);
        dash_confirmed_nb = (TextView) view.findViewById(R.id.dash_confirmed_nb);
        dash_onride_nb = (TextView) view.findViewById(R.id.dash_onride_nb);
        dash_completed_nb = (TextView) view.findViewById(R.id.dash_completed_nb);
        dash_today_earning_nb = (TextView) view.findViewById(R.id.dash_today_earning_nb);
        dash_sales_nb = (TextView) view.findViewById(R.id.dash_sales_nb);

        layout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectItem(1);
            }
        });
        layout_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectItem(2);
            }
        });
        layout_onride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectItem(3);
            }
        });
        layout_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectItem(4);
            }
        });

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getDashboard().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getDashboard().execute();
            }
        });

        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            mYear_depart = c.get(Calendar.YEAR);
            mMonth_depart = c.get(Calendar.MONTH);
            mDay_depart = c.get(Calendar.DAY_OF_MONTH);
        }
//        today = mYear_depart+"-"+mMonth_depart+"-"+mDay_depart;

        swipe_refresh.setRefreshing(true);
        new getDashboard().execute();

        return view;
    }



    /** Récupération des nombres par statut**/
    public static class getDashboard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_dashboard.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_liste.setVisibility(View.VISIBLE);
                                    layout_not_found.setVisibility(View.GONE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);

                                    JSONObject nombre = msg.getJSONObject(String.valueOf(0));
                                    dash_new_nb.setText(nombre.getString("nb_new"));
                                    dash_confirmed_nb.setText(nombre.getString("nb_confirmed"));
                                    dash_onride_nb.setText(nombre.getString("nb_onride"));
                                    dash_completed_nb.setText(nombre.getString("nb_completed"));
                                    dash_sales_nb.setText(nombre.getString("nb_sales"));
                                    dash_today_earning_nb.setText(M.getCurrency(context)+" "+nombre.getString("today_earning"));
                                }else{
                                    layout_liste.setVisibility(View.GONE);
                                    layout_not_found.setVisibility(View.VISIBLE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    layout_liste.setVisibility(View.GONE);
                    layout_not_found.setVisibility(View.GONE);
                    layout_failed.setVisibility(View.VISIBLE);
                    progressBar_failed.setVisibility(View.GONE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_diver", M.getID(context));
//                    params.put("today", today);
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
