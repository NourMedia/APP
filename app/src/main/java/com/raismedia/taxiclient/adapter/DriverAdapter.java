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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.circleimage.CircleImageView;
import com.raismedia.taxiclient.fragment.customer.BottomSheetFragmentBookingDriver;
import com.raismedia.taxiclient.model.DriverPojo;
import com.raismedia.taxiclient.settings.AppConst;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {

    private Context context;
    private List<DriverPojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public TextView driver_name;
        private TextView driver_rating;
        private ProgressBar progressBar;
        private CircleImageView img_driver,online;

        private LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);
            driver_name = (TextView) view.findViewById(R.id.driver_name);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            img_driver = (CircleImageView) view.findViewById(R.id.img_driver);
            online = (CircleImageView) view.findViewById(R.id.online);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            CoursePojo coursePojo = albumList.get(getAdapterPosition());
        }
    }

    public DriverAdapter(Context context, List<DriverPojo> albumList, Activity activity) {
        this.context = context;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_driver, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DriverPojo driverPojo = albumList.get(position);
        holder.driver_name.setText(driverPojo.getName());
//        if(driverPojo.getRate().equals("0")) {
//            holder.driver_rating.setVisibility(View.GONE);
//            holder.img_rate.setVisibility(View.GONE);
//        }else {
//            holder.driver_rating.setVisibility(View.VISIBLE);
//            holder.img_rate.setVisibility(View.VISIBLE);
//        }

//        if(driverPojo.getStatut().equals("yes")) {
//            holder.linear.setBackground(context.getResources().getDrawable(R.drawable.custom_driver_select));
//            BottomSheetFragmentBookingDriver.id_driver = String.valueOf(driverPojo.getId());
//        }else
//            holder.linear.setBackground(null);

        if(driverPojo.getOnline().equals("yes"))
            holder.online.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_statut_on));
        else
            holder.online.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_statut_off));

        if(!driverPojo.getPhoto().equals("")) {
            // loading model cover using Glide library
            Glide.with(context).load(AppConst.Server_url + "images/app_user/" + driverPojo.getPhoto())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
//                    .centerCrop()
//                .asBitmap()
//                .error(R.drawable.ic_thumb_placeholder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
 //                           holder.progressBar.setVisibility(View.GONE);
                            holder.img_driver.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile2));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
 //                           holder.progressBar.setVisibility(View.GONE);



                            return false;
                        }
                    })
                    .into(holder.img_driver);
        }else{
            holder.img_driver.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile2));
        }
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverPojo driverPojo = albumList.get(position);
                driverPojo.setStatut("yes");
                BottomSheetFragmentBookingDriver.id_driver = String.valueOf(driverPojo.getId());
                for(int i=0; i<albumList.size(); i++){
                    if(albumList.get(i).getId() != driverPojo.getId())
                        albumList.get(i).setStatut("non");
                }
                notifyDataSetChanged();
            }
        });
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

    public DriverPojo getConducteurDispo(int id){
        DriverPojo driverPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                driverPojo = albumList.get(i);
                break;
            }
        }
        return driverPojo;
    }
}