package com.example.nkindustries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nkindustries.model.AccountListresponse;
import com.example.nkindustries.model.ContactResponse;
import com.example.nkindustries.ui.contact.ContactFragment;

import java.io.Serializable;
import java.util.List;

public class ContactViewAdapter extends RecyclerView.Adapter<ContactViewAdapter.MyViewHolder> {

    private List<ContactResponse.Datum> mData;
    Context context;
    ContactFragment contactFragment;
    public ContactViewAdapter(List<ContactResponse.Datum> mData, Context context,ContactFragment contactFragment) {
        this.mData = mData;
        this.context = context;
        this.contactFragment = contactFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.cardview_item_contact,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ContactResponse.Datum temp = mData.get(position);

        holder.person_title.setText(mData.get(position).name);
        holder.person_mobile.setText( mData.get(position).mobileNumber);
        holder.person_email.setText(mData.get(position).emailAddress);
        //holder.person_image.setImageResource(mData.get(position).getThumbnail());
        Log.d("CheckSelectedUser", "Contact View Adapter: "+mData.get(position).id+"\n Name: "+mData.get(position).name);
        holder.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactFragment.openBottomSheet(position, mData.get(position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ContactDetail.class);
                intent.putExtra("contactDetails", (Serializable) mData.get(position));
                intent.putExtra("accountType","dmt2");
//                intent.putExtra("Contact Email",temp.getEmail());
//                intent.putExtra("Contact Email",temp.getMobile());
//                intent.putExtra("Contact Email",temp.getThumbnail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }
    //Filter for search . . .
    public void filterList(List<ContactResponse.Datum> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        mData = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView person_title;
        ImageView person_image;
        TextView person_mobile;
        TextView person_email;
        ImageView popMenu;
        public MyViewHolder(View itemView) {
            super(itemView);

            person_image = itemView.findViewById(R.id.contact_image_id);
            person_title = itemView.findViewById(R.id.contact_name_id);
            person_mobile= itemView.findViewById(R.id.contact_number_id);
            person_email = itemView.findViewById(R.id.contact_email_id);
            popMenu = itemView.findViewById(R.id.popup_menu);

        }
    }
}
