package com.example.nkindustries.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nkindustries.PaymentDetailsActivity;
import com.example.nkindustries.R;
import com.example.nkindustries.model.PaymentReportData;
import java.util.LinkedList;
import java.util.List;

public class PaymentReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PaymentReportData> dataList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public PaymentReportAdapter(Context context) {
        this.context = context;
        dataList = new LinkedList<>();
    }

    public void setDataList(List<PaymentReportData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.payment_report_item, parent, false);
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

        PaymentReportData movie = dataList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.txtTitle.setText(movie.firstname);
                movieViewHolder.txtDescription.setText(movie.email + "("+movie.phone +")");
                movieViewHolder.txtAmount.setText("Rs. "+movie.net_amount_credit);
                movieViewHolder.txtRefNo.setText("Ref No: "+movie.payment_ref_key + " on "+ movie.time);
                movieViewHolder.txtWalletBalance.setText("Wallet Balance: Rs. "+ movie.updated_wallet);
                movieViewHolder.txtStatus.setText(movie.status);
                movieViewHolder.txtDate.setText(movie.payment_date);

                movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PaymentDetailsActivity.class);
                        intent.putExtra("id", movie.id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
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
        add(new PaymentReportData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataList.size() - 1;
        PaymentReportData  result = getItem(position);

        if (result != null) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(PaymentReportData  movie) {
        dataList.add(movie);
        notifyItemInserted(dataList.size() - 1);
    }

    public void addAll(List<PaymentReportData > moveResults) {
        for (PaymentReportData  result : moveResults) {
            add(result);
        }
    }

    public PaymentReportData getItem(int position) {
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
