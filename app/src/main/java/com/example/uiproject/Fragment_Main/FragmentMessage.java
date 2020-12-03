package com.example.uiproject.Fragment_Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uiproject.R;

public class FragmentMessage extends Fragment {

    public static FragmentMessage newInstance() {
        return new FragmentMessage();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}

