package com.example.nkindustries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class WalletViewAdapter extends RecyclerView.Adapter<WalletViewAdapter.MyViewHolder> {
    private final List<Wallet> mData;
    Context context;
    public WalletViewAdapter(List<Wallet> mData, Context context) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.cardview_item_wallet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Wallet temp = mData.get(position);

        holder.wallet_txndate.setText(mData.get(position).getTxndate());
        holder.wallet_title.setText(mData.get(position).getTitle());
        holder.wallet_txxno.setText(mData.get(position).getTxnno());
        holder.wallet_amount.setText(mData.get(position).getAmount());
        holder.wallet_balance.setText(mData.get(position).getBalamount());
        holder.wallet_image.setImageResource(mData.get(position).getThumbnail());

        holder.wallet_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Dashbaord.class);
                intent.putExtra("Wallet Date",temp.getTxndate());
                intent.putExtra("Wallet Tittle",temp.getTitle());
                intent.putExtra("Wallet Txn Number",temp.getTxnno());
                intent.putExtra("Wallet Status",temp.getStatus());
                intent.putExtra("Wallet Amount",temp.getAmount());
                intent.putExtra("Wallet Balance Amount",temp.getBalamount());
                intent.putExtra("Wallet Thumbnail Amount",temp.getThumbnail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivities(new Intent[]{intent});

            }
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView    wallet_txndate;
        TextView    wallet_title;
        TextView    wallet_txxno;
        ImageView   wallet_status;
        TextView    wallet_amount;
        TextView    wallet_balance;
        ImageView   wallet_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            wallet_title = itemView.findViewById(R.id.wallet_title);
            wallet_image = itemView.findViewById(R.id.wallet_image);
            wallet_balance= itemView.findViewById(R.id.wallet_balance);
            wallet_txndate = itemView.findViewById(R.id.wallet_txndate);
            wallet_status = itemView.findViewById(R.id.wallet_status);
            wallet_amount = itemView.findViewById(R.id.wallet_amount);
            wallet_txxno = itemView.findViewById(R.id.wallet_txxno);

        }
    }
}
