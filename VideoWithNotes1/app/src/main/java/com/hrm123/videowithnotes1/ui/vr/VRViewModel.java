package com.hrm123.videowithnotes1.ui.vr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VRViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VRViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}