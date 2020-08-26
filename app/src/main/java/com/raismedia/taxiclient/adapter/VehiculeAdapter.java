package com.raismedia.taxiclient.adapter;

/**
 * Created by RaisMedia on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.fragment.customer.BottomSheetFragmentLocation;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.VehiculePojo;
import com.raismedia.taxiclient.settings.AppConst;

import java.util.List;

public class VehiculeAdapter extends RecyclerView.Adapter<VehiculeAdapter.MyViewHolder> {

    private Context mContext;
    private List<VehiculePojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView nom_vehicule, nb_place_vehicule, status_vehicule, prix_vehicule,moreOptions,book_now;
        private ImageView status_vehicule_img,img_vehicule;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nom_vehicule = (TextView) view.findViewById(R.id.nom_vehicule);
            status_vehicule = (TextView) view.findViewById(R.id.status_vehicule);
            prix_vehicule = (TextView) view.findViewById(R.id.prix_vehicule);
            nb_place_vehicule = (TextView) view.findViewById(R.id.nb_place_vehicule);
            moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            book_now = (TextView) view.findViewById(R.id.book_now);
            status_vehicule_img = (ImageView) view.findViewById(R.id.status_vehicule_img);
            img_vehicule = (ImageView) view.findViewById(R.id.img_vehicule);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            VehiculePojo vehiculePojo = albumList.get(getAdapterPosition());
            BottomSheetFragmentLocation bottomSheetFragmentLocation = new BottomSheetFragmentLocation(activity,Integer.parseInt(vehiculePojo.getPrix()),vehiculePojo.getId());
            bottomSheetFragmentLocation.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentLocation.getTag());
        }
    }

    public VehiculeAdapter(Context mContext, List<VehiculePojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_vehicule, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final VehiculePojo vehiculePojo = albumList.get(position);
        holder.nom_vehicule.setText(vehiculePojo.getNom());
//        holder.status_vehicule.setText(coursePojo.getStatut());
        holder.prix_vehicule.setText(vehiculePojo.getPrix()+" "+ M.getCurrency(mContext));
        holder.nb_place_vehicule.setText(vehiculePojo.getNb_place());
        if(vehiculePojo.getNb_reserve() < vehiculePojo.getNombre()) {
            holder.status_vehicule_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_disponibilite_green));
            holder.status_vehicule.setText(mContext.getResources().getString(R.string.disponible));
            holder.moreOptions.setVisibility(View.VISIBLE);
        }else {
            holder.status_vehicule_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_disponibilite_red));
            holder.status_vehicule.setText(mContext.getResources().getString(R.string.indisponible));
            holder.moreOptions.setVisibility(View.GONE);

        }
        holder.book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragmentLocation bottomSheetFragmentLocation = new BottomSheetFragmentLocation(activity,Integer.parseInt(vehiculePojo.getPrix()),vehiculePojo.getId());
                bottomSheetFragmentLocation.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentLocation.getTag());
            }
        });

        holder.moreOptions.setVisibility(View.GONE);
//        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //creating a popup menu
//                PopupMenu popup = new PopupMenu(mContext, holder.moreOptions);
//                //inflating menu from xml resource
//                popup.inflate(R.menu.menu_action_location_vehicule);
//                //adding click listener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_louer:
//                                BottomSheetFragmentLocation bottomSheetFragmentLocation = new BottomSheetFragmentLocation(activity,Integer.parseInt(vehiculePojo.getPrix()),vehiculePojo.getId());
//                                bottomSheetFragmentLocation.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentLocation.getTag());
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                //displaying the popup
//                popup.show();
//            }
//        });

        // loading model cover using Glide library
        Glide.with(mContext).load(AppConst.Server_urlMain+"assets/images/type_vehicle_rental/"+vehiculePojo.getImage())
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

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(int position){
        albumList.remove(position);
        notifyItemRemoved(position);
        return;
    }

    public VehiculePojo getVehicule(int id){
        VehiculePojo vehiculePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                vehiculePojo = albumList.get(i);
                break;
            }
        }
        return vehiculePojo;
    }
}