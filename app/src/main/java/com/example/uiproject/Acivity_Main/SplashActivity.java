package com.example.uiproject.Acivity_Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.uiproject.Fragment_Main.FragmentCommunity.rowsArrayList;

public class SplashActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.splashactivity);

        favortie();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    public void favortie() {
        FirebaseUser FU = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        if (FU != null) {
            docRef = db.collection("ChatApp").document("account")
                    .collection("list").document(FU.getUid().trim());                     //로그인 안한채로 커뮤니티 탭 누르면 앱 튕김


            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                Map<String, Object> favoriteList;
                Map<String, Object> favoriteTheme;
                String Theme[] = new String[]{"etc", "euro", "party", "strategy", "theme", "wargame"};

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    int k = 0;
                    Iterator<String> keys;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            try {
                                int i = 0;
                                rowsArrayList = new ArrayList[2];
                                rowsArrayList[0] = new ArrayList<>();
                                rowsArrayList[1] = new ArrayList<>();

                                favoriteList = (Map<String, Object>) document.get("favorite");
                                while (i < 6) {
                                    favoriteTheme = (Map<String, Object>) favoriteList.get(Theme[i]);
                                    if (favoriteTheme == null) {
                                        i++;
                                        continue;
                                    }
                                    keys = favoriteTheme.keySet().iterator();
                                    try {
                                        while (keys.hasNext()) {
                                            String tempString = keys.next();
                                            if ((Boolean) favoriteTheme.get(tempString) == true) {
                                                rowsArrayList[0].add(tempString);
                                                rowsArrayList[1].add(Theme[i]);
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                    i++;
                                }

                            } catch (Exception e) {

                            }
                        }
                    }
                }
            });
        }
    }
}
