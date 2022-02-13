package com.example.nkindustries.ui.recharge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nkindustries.R;
import com.example.nkindustries.model.PaymentLinkResponse;

import java.util.List;

public class RechargeViewAdapter extends RecyclerView.Adapter<RechargeViewAdapter.MyViewHolder> {

    private final List<PaymentLinkResponse.Datum> mData;
    Context context;
    public RechargeViewAdapter(List<PaymentLinkResponse.Datum> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.fragment_recharge_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final PaymentLinkResponse.Datum temp = mData.get(position);

        holder.txtTitle.setText(mData.get(position).title);
        holder.txtDescription.setText( mData.get(position).descriptions);
        holder.btnLink.setText(mData.get(position).button_name);
        //holder.person_image.setImageResource(mData.get(position).getThumbnail());

        holder.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentLinkActivity.class);
                intent.putExtra("link",  mData.get(position).payment_link);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtDescription;
        Button btnLink;
        public MyViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnLink= itemView.findViewById(R.id.btnLink);

        }
    }
}
