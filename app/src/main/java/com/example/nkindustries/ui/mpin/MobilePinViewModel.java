package com.example.nkindustries.ui.mpin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MobilePinViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public MobilePinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Mpin Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
