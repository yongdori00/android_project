package com.example.uiproject.Fragment_Main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.uiproject.Activity_Mypage.ChangeInfo;
import com.example.uiproject.Activity_Mypage.FindPw;
import com.example.uiproject.Activity_Mypage.RegisterActivity;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentMypage extends Fragment {

    EditText email, password;
    Button btn_login, register, findPass;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference docRef;

    View view;
    View viewNotlogin, viewlogin;

    boolean logedin = false;

    public static FragmentMypage newInstance() {
        return new FragmentMypage();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewNotlogin = inflater.inflate(R.layout.login, container, false);
        viewlogin = inflater.inflate(R.layout.fragment_mypage, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user == null) {
            view = viewNotlogin;
            email = view.findViewById(R.id.email_login);
            password = view.findViewById(R.id.password_login);
            btn_login = view.findViewById(R.id.btn_login);
            register = view.findViewById(R.id.register);
            findPass = view.findViewById(R.id.findPass);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(getContext(), "All fileds are required", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.signInWithEmailAndPassword(txt_email, txt_password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
                                            Intent intent = getActivity().getIntent();
                                            getActivity().finish();
                                            startActivity(intent);
                                            logedin = true;
                                        } else {
                                            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                }
            });

            findPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FindPw.class);
                    startActivity(intent);
                }
            });
            if (logedin == true)
                loggedin();
                //MainActivity mainActivity = (MainActivity)getActivity();
               // mainActivity.mypageReplaceFragment(this);
            return view;

        } else {
            loggedin();

            return view;
        }
    }

    public void loggedin() {
        view = viewlogin;

        docRef = db.collection("ChatApp").document("account")
                .collection("list").document(user.getUid());

        final TextView name_info, email_info, phone_info, nickname_info;
        Button logOut, changer;

        name_info = view.findViewById(R.id.name_info);
        phone_info = view.findViewById(R.id.phone_info);
        email_info = view.findViewById(R.id.email_info);
        nickname_info = view.findViewById(R.id.nickname_info);
        logOut = view.findViewById(R.id.logOut);
        changer = view.findViewById(R.id.changer);

        changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeInfo.class);
                intent.putExtra("email", email_info.getText().toString());
                intent.putExtra("name", name_info.getText().toString());
                intent.putExtra("phone", phone_info.getText().toString());
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                view = viewNotlogin;
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        name_info.setText((String) document.get("name_info"));
                        phone_info.setText((String) document.get("phone_info"));
                        email_info.setText((String) document.get("email_info"));
                        nickname_info.setText((String) document.get("nickname_info"));
                    }
                }
            }
        });
    }
}