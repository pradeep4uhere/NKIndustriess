package com.example.nkindustries.ui.wallet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nkindustries.R;
import com.example.nkindustries.model.WalletData;

import java.util.LinkedList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<WalletData> dataList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        dataList = new LinkedList<>();
    }

    public void setDataList(List<WalletData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.wallet_report_item, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        WalletData movie = dataList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.txtTitle.setText(movie.title);
                movieViewHolder.txtDescription.setText(movie.description);
                String refNo = "0";
                String date="";
                if(movie.debit_amount>0){
                    movieViewHolder.txtAmount.setText("- "+movie.debit_amount);
                    movieViewHolder.txtAmount.setTextColor(Color.parseColor("#ff0000"));
                }
                else{
                    movieViewHolder.txtAmount.setText("+ "+movie.credit_amount);
                    movieViewHolder.txtAmount.setTextColor(Color.parseColor("#03DAC5"));
                }
                if(movie.wallet_recharge_payment_id>0){
                    refNo = String.valueOf(movie.wallet_recharge_payment_id);
                    date = String.valueOf(movie.payment_date);
                }
                else {
                    refNo = movie.transaction_number;
                    date = String.valueOf(movie.transaction_date);
                }
                movieViewHolder.txtRefNo.setText("Ref No: "+refNo + " on "+ movie.transaction_time);
                movieViewHolder.txtWalletBalance.setText("Wallet Balance: Rs. "+ movie.wallet_balance);
                movieViewHolder.txtStatus.setText(movie.status);
                movieViewHolder.txtDate.setText(date);
               // Glide.with(context).load(movie.getImageUrl()).apply(RequestOptions.centerCropTransform()).into(movieViewHolder.movieImage);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dataList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new WalletData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataList.size() - 1;
        WalletData  result = getItem(position);

        if (result != null) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(WalletData  movie) {
        dataList.add(movie);
        notifyItemInserted(dataList.size() - 1);
    }

    public void addAll(List<WalletData > moveResults) {
        for (WalletData  result : moveResults) {
            add(result);
        }
    }

    public WalletData getItem(int position) {
        return dataList.get(position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle,txtDescription,txtRefNo,txtWalletBalance,
        txtAmount,txtStatus,txtDate;

        public MovieViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTransferTo);
            txtDescription = (TextView) itemView.findViewById(R.id.txtTransferFrom);
            txtRefNo = (TextView) itemView.findViewById(R.id.txtRefNo);
            txtWalletBalance = (TextView) itemView.findViewById(R.id.txtWalletBalance);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }
    }

}
