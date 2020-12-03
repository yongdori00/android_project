package com.example.uiproject.Activity_Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uiproject.Activity_Community.Post;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password, nickname, phonenum;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseFirestore db;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email_regi);
        password = findViewById(R.id.password_regi);
        nickname = findViewById(R.id.nickname_regi);
        phonenum = findViewById(R.id.phone_regi);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_nickname = nickname.getText().toString();
                String txt_phonenum = phonenum.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(com.example.uiproject.Activity_Mypage.RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(com.example.uiproject.Activity_Mypage.RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password, txt_nickname, txt_phonenum);
                }
            }
        });


    }

    private void register(final String username, final String email, final String password, final String nickname, final String phonenum) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                             docRef = db.collection("ChatApp").document("account")                                                 //변수 db는 해당 계정의 firestore의 상태를 가져오는거임.
                                            .collection("list").document(auth.getCurrentUser().getUid());                                       //docref 변수를 통해서 정보를 저장할 경로를 지정해줌.

                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("nickname_info", nickname);                                                                                           //아래 4개를 필드로 저장함.
                            hashMap.put("email_info", email);
                            hashMap.put("phone_info", phonenum);
                            hashMap.put("name_info", username);

                            docRef.set(hashMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {                                       //docref경로에 set메소드를 이용하여 필드들을 저장함.
                                    Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(com.example.uiproject.Activity_Mypage.RegisterActivity.this, "You can't use this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}