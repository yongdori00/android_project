package com.example.uiproject.gamelist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.uiproject.Activity_Community.Community_Board;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Etc extends Fragment implements View.OnClickListener{

    ImageView davinci, haligali;
    CheckBox favorite;

    String valueTheme = "etc";
    String valueName;

    FirebaseFirestore db;

    Intent intent;

    gameThemeHelper gTH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etc, container, false);

        db = FirebaseFirestore.getInstance();
        favorite = view.findViewById(R.id.favorite_check);

        davinci = view.findViewById(R.id.davinci);
        davinci.setOnClickListener(this);
        haligali = view.findViewById(R.id.haligali);
        haligali.setOnClickListener(this);

        return view;
    }

    public void onClick(View v){
        intent = new Intent(getActivity(), Community_Board.class);
        intent.putExtra(Community_Board.theme, valueTheme);
        switch (v.getId()){
            case R.id.davinci:
                valueName = "davinci";
                break;
            case R.id.haligali:
                valueName = "haligali";
                break;
        }

        getInfo();

        intent.putExtra(Community_Board.name, valueName);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 400);
    }

    public void getInfo(){
        gTH = new gameThemeHelper(valueName, valueTheme);
        gTH.getChecked(db);
        gTH.getNumgame(db);
    }
}