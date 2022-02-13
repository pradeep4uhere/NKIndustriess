package com.example.nkindustries.ui.walletpay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WalletPayViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public WalletPayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pay Wallet Transfer");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
