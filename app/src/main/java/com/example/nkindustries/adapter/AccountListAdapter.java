package com.example.nkindustries.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nkindustries.ContactDetail;
import com.example.nkindustries.R;
import com.example.nkindustries.model.AccountListresponse;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder> {

    private List<AccountListresponse.AccountList> accountList;
    Context context;
    ContactDetail contactDetail;
    public AccountListAdapter(List<AccountListresponse.AccountList> accountList, Context context,ContactDetail contactDetail) {
        List<AccountListresponse.AccountList> accountList1;
        this.accountList = accountList;
        this.context = context;
        this.contactDetail = contactDetail;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.account_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtBankName.setText(accountList.get(position).bankName);
        holder.txtAccountNo.setText( accountList.get(position).account_number);
        holder.txtIfsc.setText(accountList.get(position).ifsc_code);
        if(accountList.get(position).is_validated==1) {
            holder.txtValidated.setText("Validated");
            holder.transferMoney.setText("Transfer");
            holder.transferMoney.setBackgroundColor(Color.parseColor("#4CAF50"));
        }
        else{
            holder.txtValidated.setText("Not Validated");
            holder.transferMoney.setText("Validate");
            holder.transferMoney.setBackgroundColor(Color.parseColor("#BB86FC"));
        }
        holder.txtName.setText(accountList.get(position).beneficiary_name);
        Glide.with(context)
                .load(accountList.get(position).bankIcon)
                .into(holder.imageView);

        holder.transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( holder.transferMoney.getText().toString().equalsIgnoreCase("Validate")){
                   contactDetail.addFundContactDMT1AndDMT2(String.valueOf(accountList.get(position).id));
               }
               else {
                   contactDetail.transferMoney(accountList.get(position));
               }

            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, TransferMoney.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//
//            }
//        });

    }

    // method for filtering our recyclerview items.
    public void filterList(List<AccountListresponse.AccountList> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        accountList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtBankName;
        ImageView imageView;
        TextView txtName;
        TextView txtAccountNo;
        TextView txtIfsc;
        TextView txtValidated;
        Button transferMoney;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtBankName = itemView.findViewById(R.id.txtBankName);
            txtAccountNo= itemView.findViewById(R.id.txtAccountNo);
            txtIfsc = itemView.findViewById(R.id.txtIfsc);
            txtValidated = itemView.findViewById(R.id.txtValidated);
            txtName = itemView.findViewById(R.id.txtName);
            transferMoney = itemView.findViewById(R.id.button_transfer_money);

        }
    }
}
