package com.example.uiproject.gamelist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.uiproject.Activity_Community.Community_Board;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class gameThemeHelper {

    DocumentReference docRef;
    String SvalueName;
    String SvalueTheme;


    public gameThemeHelper(String valueName, String valueTheme){
        SvalueName = valueName;
        SvalueTheme = valueTheme;
    }
    public void getNumgame(FirebaseFirestore db){
        docRef = db.collection("ChatApp").document("Post")
                .collection(SvalueTheme).document(SvalueName);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        try {
                            Community_Board.numberOfGame = (long) document.get("numberofpost");//게시글 번호 sharedprefernce에 저wkd
                        }catch (Exception e){
                            Map<String, Object> number = new HashMap<>();

                            number.put("numberofpost", 0);
                            docRef.set(number, SetOptions.merge());
                        }
                    }
                }
            }
        });
    }
    public void getChecked(FirebaseFirestore db){
        docRef = db.collection("ChatApp").document("account")
                .collection("list").document(FirebaseAuth.getInstance().getCurrentUser().getUid().trim());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        try {
                            Map<String, Object> favorite = (Map<String, Object>)document.get("favorite");
                            Map<String, Object> theme = (Map<String, Object>)favorite.get(SvalueTheme);
                            Community_Board.isChecked = (Boolean) theme.get(SvalueName);
                        }catch (Exception e){
                            Map<String, Object> favorite = new HashMap<>();
                            Map<String, Object> theme = new HashMap<>();
                            Map<String, Object> checked = new HashMap<>();

                            checked.put(SvalueName, false);
                            theme.put(SvalueTheme, checked);
                            favorite.put("favorite", theme);
                            docRef.set(favorite, SetOptions.merge());
                        }
                    }
                }
            }
        });
    }

}
