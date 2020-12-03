package com.example.uiproject.Activity_Community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.uiproject.R;
import com.example.uiproject.gamelist.Etc;
import com.example.uiproject.gamelist.Euro;
import com.example.uiproject.gamelist.Party;
import com.example.uiproject.gamelist.Strategy;
import com.example.uiproject.gamelist.Theme;
import com.example.uiproject.gamelist.Wargame;
import com.google.android.material.tabs.TabLayout;

public class Community_gameList extends AppCompatActivity {

    private Euro euro;
    private Party party;
    private Strategy strategy;
    private Theme theme;
    private Etc etc;
    private Wargame wargame;

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_community_game_list);

        TabLayout gameTab = findViewById(R.id.game_tab);

        Intent intent = getIntent();

        euro = new Euro();
        party = new Party();
        strategy = new Strategy();
        theme = new Theme();
        etc = new Etc();
        wargame = new Wargame();

        WhichTab(0);
        gameTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                WhichTab(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void WhichTab(int pos){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch(pos){
            case 0:
                ft.replace(R.id.game_list_frag, euro);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.game_list_frag, party);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.game_list_frag, theme);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.game_list_frag, strategy);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.game_list_frag, wargame);
                ft.commit();
                break;
            case 5:
                ft.replace(R.id.game_list_frag, etc);
                ft.commit();
                break;
        }
    }
    public void backOnClick(View v){
        finish();
    }
}