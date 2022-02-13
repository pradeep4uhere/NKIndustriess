package com.example.nkindustries.ui.wallet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WalletReportViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public WalletReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Wallet Report Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
