package com.example.uiproject.Activity_Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ChangeInfo extends AppCompatActivity {

    FirebaseUser FU;
    FirebaseFirestore db;
    FirebaseAuth FA;
    DocumentReference docRef;

    TextView Email, name, phone;
    EditText nickname, pass1, pass2;
    Button accept;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw_nickname);

        FA = FirebaseAuth.getInstance();
        FU = FA.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Email = findViewById(R.id.email_nonchange);
        name = findViewById(R.id.name_nonchange);
        phone = findViewById(R.id.phone_nonchange);
        nickname = findViewById(R.id.nickname_change);
        pass1 = findViewById(R.id.password_change);
        pass2 = findViewById(R.id.password_change2);
        accept = findViewById(R.id.changer);

        Intent intent = getIntent();
        String Sname = intent.getExtras().getString("name");
        String Sphone = intent.getExtras().getString("phone");
        String Semail = intent.getExtras().getString("email");

        Email.setText(Semail);
        name.setText(Sname);
        phone.setText(Sphone);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(pass1.getText().toString().equals("") || pass2.getText().toString().equals("") || nickname.getText().toString().equals(""))) {
                    if (FU != null && pass1.getText().toString().equals(pass2.getText().toString())) {
                        FU.updatePassword(pass1.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void aVoid) {
                                docRef = db.collection("ChatApp").document("account")
                                        .collection("list").document(FU.getUid());

                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("nickname_info", nickname.getText().toString());

                                docRef.set(hashMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "빈칸이 있으면 안됩니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}