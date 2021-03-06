package com.example.uiproject.gamelist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Party extends Fragment implements View.OnClickListener{

    ImageView blackjack;

    String valueTheme = "party";
    String valueName;

    DocumentReference docRef;
    FirebaseFirestore db;

    Intent intent;

    gameThemeHelper gTH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_party, container, false);

        db = FirebaseFirestore.getInstance();

        blackjack = view.findViewById(R.id.blackjack);
        blackjack.setOnClickListener(this);

        return view;
    }

    public void onClick(View v){
        intent = new Intent(getActivity(), Community_Board.class);
        intent.putExtra(Community_Board.theme, valueTheme);
        switch (v.getId()){
            case R.id.blackjack:
                valueName = "blackjack";
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