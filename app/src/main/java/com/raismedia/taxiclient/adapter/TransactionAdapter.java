package com.raismedia.taxiclient.adapter;

/**
 * Created by RaisMedia on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.model.TransactionPojo;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private Context mContext;
    private List<TransactionPojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView date, amount;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            CoursePojo coursePojo = albumList.get(getAdapterPosition());
        }
    }

    public TransactionAdapter(Context mContext, List<TransactionPojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_transaction, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TransactionPojo transactionPojo = albumList.get(position);
        holder.amount.setText(M.getCurrency(mContext)+" "+transactionPojo.getAmount());
        holder.date.setText(transactionPojo.getDate());

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

    public TransactionPojo getComment(int id){
        TransactionPojo transactionPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                transactionPojo = albumList.get(i);
                break;
            }
        }
        return transactionPojo;
    }
}