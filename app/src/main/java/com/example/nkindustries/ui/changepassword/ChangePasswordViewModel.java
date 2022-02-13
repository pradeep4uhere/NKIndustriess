package com.example.nkindustries.ui.changepassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangePasswordViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public ChangePasswordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Change Password Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
