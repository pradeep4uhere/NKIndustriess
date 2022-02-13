package com.example.nkindustries.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nkindustries.R;
import com.example.nkindustries.model.NotificationListResponse;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {

    private final List<NotificationListResponse.Datum> mData;
    Context context;
    public NotificationListAdapter(List<NotificationListResponse.Datum> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.notification_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final NotificationListResponse.Datum temp = mData.get(position);

        holder.txtTitle.setText(mData.get(position).title);
        holder.txtDescription.setText( mData.get(position).desriptions);
        holder.txtTime.setText(mData.get(position).time);
        //holder.person_image.setImageResource(mData.get(position).getThumbnail())

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtDescription;
        TextView txtTime;
        public MyViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtTime= itemView.findViewById(R.id.txtTime);

        }
    }
}
