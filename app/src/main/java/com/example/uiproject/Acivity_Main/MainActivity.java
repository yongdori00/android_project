package com.example.uiproject.Acivity_Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.uiproject.Fragment_Main.FragmentCommunity;
import com.example.uiproject.Fragment_Main.FragmentHome;
import com.example.uiproject.Fragment_Main.FragmentMessage;
import com.example.uiproject.Fragment_Main.FragmentMypage;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentHome fraghome;
    private FragmentMessage fragmsg;
    private FragmentCommunity fragcom;
    private FragmentMypage fragpag;

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        page = 0;
                        setFrag(page);
                        break;
                    case R.id.message:
                        page = 1;
                        setFrag(page);
                        break;
                    case R.id.community:
                        page = 2;
                        setFrag(page);
                        break;
                    case R.id.mypage:
                        page = 3;
                        setFrag(page);
                        break;

                }
                return true;
            }
        });

        fraghome=new FragmentHome();
        fragmsg=new FragmentMessage();
        fragcom=new FragmentCommunity();
        fragpag=new FragmentMypage();

        setFrag(page); // 첫 프래그먼트 화면 지정
    }

    @Override
    protected void onRestart() {
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        page = 0;
                        setFrag(page);
                        break;
                    case R.id.message:
                        page = 1;
                        setFrag(page);
                        break;
                    case R.id.community:
                        page = 2;
                        setFrag(page);
                        break;
                    case R.id.mypage:
                        page = 3;
                        setFrag(page);
                        break;

                }
                return true;
            }
        });

        fraghome=new FragmentHome();
        fragmsg=new FragmentMessage();
        fragcom=new FragmentCommunity();
        fragpag=new FragmentMypage();

        setFrag(page); // 첫 프래그먼트 화면 지정
         super.onRestart();
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.main_frame,fraghome);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_frame,fragmsg);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.main_frame,fragcom);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame,fragpag);
                ft.commit();
                break;


        }
    }

}
