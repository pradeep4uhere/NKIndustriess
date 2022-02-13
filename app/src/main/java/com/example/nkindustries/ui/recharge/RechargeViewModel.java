package com.example.nkindustries.ui.recharge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RechargeViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public RechargeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("All Payment Links");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
