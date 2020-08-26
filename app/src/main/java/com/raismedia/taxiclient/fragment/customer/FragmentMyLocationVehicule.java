package com.raismedia.taxiclient.fragment.customer;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.raismedia.taxiclient.adapter.LocationAdapter;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.LocationPojo;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMyLocationVehicule extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_location;
    public static List<LocationPojo> albumList_location;
    public static LocationAdapter adapter_location;
    public static SwipeRefreshLayout swipe_refresh;
    public ProgressBar progressBar_failed;

    private LinearLayout layout_not_found,layout_failed;
    private RelativeLayout layout_liste;

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
        view= inflater.inflate(R.layout.fragment_location_vehicule, container, false);

        context=getActivity();
        if(M.getCountry(context).equals("All"))
            MainActivity.setTitle("Rented vehicle");
        else
            MainActivity.setTitle("Rented vehicle - "+M.getCountry(context));

        albumList_location = new ArrayList<>();
        adapter_location = new LocationAdapter(context, albumList_location, getActivity());

        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        recycler_view_location = (RecyclerView) view.findViewById(R.id.recycler_view_location_vehicule);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_location.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_location.setItemAnimator(new DefaultItemAnimator());
        recycler_view_location.setAdapter(adapter_location);

//        recycler_view_location.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_location, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                LocationPojo locationPojo = albumList_location.get(position);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                LocationPojo locationPojo = albumList_location.get(position);
//            }
//        }));

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getLocation().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getLocation().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getLocation().execute();

        return view;
    }

    /** Récupération des véhicules d'un utilisateur**/
    private class getLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_location.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                albumList_location.clear();
                                adapter_location.notifyDataSetChanged();
                                swipe_refresh.setRefreshing(false);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_liste.setVisibility(View.VISIBLE);
                                    layout_not_found.setVisibility(View.GONE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);

                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject location = msg.getJSONObject(String.valueOf(i));
                                        albumList_location.add(new LocationPojo(location.getInt("id"),location.getString("libTypeVehicule"),location.getString("prix"),location.getString("date_debut"),location.getString("date_fin"),location.getString("statut"),location.getString("image")
                                                ));
                                        adapter_location.notifyDataSetChanged();
                                    }
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

}
