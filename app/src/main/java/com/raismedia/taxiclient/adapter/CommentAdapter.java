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
import android.widget.RatingBar;
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
import com.raismedia.taxiclient.model.CommentPojo;
import com.raismedia.taxiclient.settings.AppConst;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context mContext;
    private List<CommentPojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView comment;
        private CircleImageView user_photo;
        private RatingBar rate_conducteur;


        public MyViewHolder(View view) {
            super(view);
            comment = (TextView) view.findViewById(R.id.comment);
            user_photo = (CircleImageView) view.findViewById(R.id.user_photo);
            rate_conducteur = (RatingBar) view.findViewById(R.id.rate_conducteur);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            CoursePojo coursePojo = albumList.get(getAdapterPosition());
        }
    }

    public CommentAdapter(Context mContext, List<CommentPojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_comments, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CommentPojo commentPojo = albumList.get(position);
        holder.comment.setText(commentPojo.getComment());

        if(commentPojo.getNote().trim().length() > 0)
            holder.rate_conducteur.setRating(Float.parseFloat(commentPojo.getNote()));
        else
            holder.rate_conducteur.setRating(0f);

        if(!commentPojo.getCustomer_photo().equals("")) {
            // loading model cover using Glide library
            Glide.with(mContext).load(AppConst.Server_url + "images/app_user/" + commentPojo.getCustomer_photo())
                    .skipMemoryCache(true)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.user_photo);
        }else{
            holder.user_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.user_profile));
        }
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

    public CommentPojo getComment(int id){
        CommentPojo commentPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                commentPojo = albumList.get(i);
                break;
            }
        }
        return commentPojo;
    }
}