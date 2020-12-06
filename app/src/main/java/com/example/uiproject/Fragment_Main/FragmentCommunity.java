package com.example.uiproject.Fragment_Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.Acivity_Main.MainActivity;
import com.example.uiproject.Activity_Community.Community_Board;
import com.example.uiproject.Activity_Community.Community_gameList;
import com.example.uiproject.Adapter.FavoriteAdapter;
import com.example.uiproject.Adapter.PostPreAdapter;
import com.example.uiproject.R;
import com.example.uiproject.gamelist.gameThemeHelper;
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

import static com.example.uiproject.Activity_Community.Community_Board.numberOfGame;

public class FragmentCommunity extends Fragment{

    TextView wroteByme, ggultip, letsPlay;
    TextView favorite;
    long num_favorite;

    RecyclerView favoriteView;
    LinearLayoutManager linearLayoutManager;
    FavoriteAdapter favoriteAdapter;
    public static ArrayList<String> rowsArrayList[] = new ArrayList[2];

    FirebaseFirestore db;
    FirebaseAuth FA;
    FirebaseUser FU;
    DocumentReference docRef;
    Map<String, Object> favoriteList;
    Map<String, Object> favoriteTheme;

    String Theme[] = new String[6];

    Context con;

    View view;

    public static FragmentCommunity newInstance() {
        return new FragmentCommunity();
    }

    public FragmentCommunity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        FA = FirebaseAuth.getInstance();
        FU = FA.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Theme = new String[]{"etc", "euro", "party", "strategy", "theme", "wargame"};

        if (FU != null) {
            docRef = db.collection("ChatApp").document("account")
                    .collection("list").document(FU.getUid().trim());                     //로그인 안한채로 커뮤니티 탭 누르면 앱 튕김

            favoriteList = new HashMap<>();

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    if(favoriteTheme == null) {
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
        //파베에서 좋아하는 글 갯수를 받아와야함.

        favoriteView = view.findViewById(R.id.recycler_fav);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        favoriteView.setLayoutManager(linearLayoutManager);
        favoriteAdapter = new FavoriteAdapter(rowsArrayList[0], rowsArrayList[1]);
        favoriteView.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItmeClickListener(new FavoriteAdapter.OnItemClickListener() {
            gameThemeHelper gTH;
            Intent intent;
            @Override
            public void onItemClick(View v, int pos) {
                FavoriteAdapter.ViewHolder viewHolder = (FavoriteAdapter.ViewHolder)favoriteView.findViewHolderForAdapterPosition(pos);
                TextView textView1 = v.findViewById(R.id.favorite_text);
                TextView textView2 = v.findViewById(R.id.favorite_text_theme);
                String text1 = textView1.getText().toString();
                String text2 = textView2.getText().toString();

                intent = new Intent(getActivity(), Community_Board.class);
                intent.putExtra("name", text1);
                intent.putExtra("theme", text2);

                gTH = new gameThemeHelper(text1, text2);
                gTH.getChecked(db);
                gTH.getNumgame(db);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, 400);
            }
        });

        wroteByme = view.findViewById(R.id.wrote_by_me);
        ggultip = view.findViewById(R.id.GaeGgulTip);
        letsPlay = view.findViewById(R.id.LetsPlay);

        wroteByme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Community_Board.class);
                intent.putExtra("wrotebyme", "wrotebyme");
                startActivity(intent);
            }
        });

        ggultip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Community_gameList.class);//여기서 새로운 액티비티 띄울 때 관련 게시판에
                startActivity(intent);
            }
        });

        letsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        return view;
    }
}
