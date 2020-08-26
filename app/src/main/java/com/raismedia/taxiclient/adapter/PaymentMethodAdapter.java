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
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.fragment.customer.BottomSheetFragmentBookingDriver;
import com.raismedia.taxiclient.model.PaymentMethodPojo;
import com.raismedia.taxiclient.settings.AppConst;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    private Context mContext;
    private List<PaymentMethodPojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private ImageView img;
        private ProgressBar progressBar;
        private RelativeLayout linear;


        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            linear = (RelativeLayout) view.findViewById(R.id.linear);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            CoursePojo coursePojo = albumList.get(getAdapterPosition());
        }
    }

    public PaymentMethodAdapter(Context mContext, List<PaymentMethodPojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_payment_method, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PaymentMethodPojo paymentMethodPojo = albumList.get(position);

        if(paymentMethodPojo.getStatut().equals("yes")) {
            holder.img.setBackground(mContext.getResources().getDrawable(R.drawable.custom_driver_select));
            BottomSheetFragmentBookingDriver.id_payment = String.valueOf(paymentMethodPojo.getId());
        }else
            holder.img.setBackground(mContext.getResources().getDrawable(R.drawable.custom_cash));

        // loading model cover using Glide library
        Glide.with(mContext).load(AppConst.Server_urlMain+"assets/images/payment_method/"+paymentMethodPojo.getImage())
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
                .into(holder.img);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentMethodPojo paymentMethodPojo = albumList.get(position);
                paymentMethodPojo.setStatut("yes");
                BottomSheetFragmentBookingDriver.id_payment = String.valueOf(paymentMethodPojo.getId());
                for(int i=0; i<albumList.size(); i++){
                    if(albumList.get(i).getId() != paymentMethodPojo.getId())
                        albumList.get(i).setStatut("no");
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

    public PaymentMethodPojo getPaymentMethod(int id){
        PaymentMethodPojo paymentMethodPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                paymentMethodPojo = albumList.get(i);
                break;
            }
        }
        return paymentMethodPojo;
    }
}