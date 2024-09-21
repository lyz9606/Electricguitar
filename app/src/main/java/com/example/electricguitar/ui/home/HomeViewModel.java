package com.example.electricguitar.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electricguitar.MyDAO;
import com.example.electricguitar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;



    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}