package com.example.nkindustries.ui.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentReportViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public PaymentReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Payment Report Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
