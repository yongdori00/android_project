package com.example.uiproject.Activity_Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uiproject.Acivity_Main.MainActivity;
import com.example.uiproject.Adapter.PostPreAdapter;
import com.example.uiproject.CommunityHelper.board_list;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Community_Board extends AppCompatActivity {

    public static String theme = "theme";
    public static String name = "name";

    SwipeRefreshLayout simple;
    CheckBox favorite;
    RecyclerView board_scroll;
    PostPreAdapter PostpreAdapter;

    Context con;

    public static ArrayList<board_list> rowsArrayList = new ArrayList<>();
    ArrayList<Boolean> Blist = new ArrayList<>();                   //top기능 보여줄지 말지 위해서 존재
    Spinner dateSpinner;
    ArrayAdapter dateAdapter;
    LinearLayoutManager linearLayoutManager;

    boolean isLoading = false;
    public static boolean isChecked = false;

    String intentTheme, intentName;
    String wrotebyme;
    String title;

    FirebaseAuth FA;
    FirebaseUser FU;
    FirebaseFirestore db;
    DocumentReference docRef;

    long numofgame = 0;
    public static long numberOfGame;
    int i = 0;
    public static String title_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commnunity__board);

        spinnerDate();

        FA = FirebaseAuth.getInstance();
        FU = FA.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        boolean checked = pref.getBoolean(FU.getUid() + intentName, isChecked);

        Intent intent = getIntent();
        intentTheme = intent.getExtras().getString(theme);
        intentName = intent.getExtras().getString(name);
        //wrotebyme = intent.getExtras().getString("wrotebyme");

        //if(wrotebyme != null && wrotebyme.equals("wrotebyme")){

        //}

        TextView TV = findViewById(R.id.game_board);
        TV.setText(intentName);

        getAccountInfo();
        i = 0;
        populateDate();

        favorite = findViewById(R.id.favorite_check);
        favorite.setChecked(checked);
        favoriteChecked();

        Initialize_by_game();

        simple = findViewById(R.id.community_swipelayout);
        simple.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                i = 0;
                getAccountInfo();
                populateDate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Initialize_by_game();
                    }
                }, 500);
                simple.setRefreshing(false);
            }
        });

        //boardScroll();
    }

    @Override
    protected void onRestart() {
        getAccountInfo();
        i = 0;
        populateDate();
        Initialize_by_game();
        simple = findViewById(R.id.community_swipelayout);
        simple.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                i = 0;
                getAccountInfo();
                populateDate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Initialize_by_game();
                    }
                }, 500);
                simple.setRefreshing(false);
            }
        });
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        CheckBox favoriteCheck = findViewById(R.id.favorite_check);
        editor.putBoolean(FU.getUid() + intentName, favoriteCheck.isChecked());

        editor.commit();
    }

    private void Initialize_by_game() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                board_scroll = findViewById(R.id.board_scroll);
                linearLayoutManager = new LinearLayoutManager(Community_Board.this);
                board_scroll.setLayoutManager(linearLayoutManager);
                PostpreAdapter = new PostPreAdapter(rowsArrayList);
                board_scroll.setAdapter(PostpreAdapter);
                PostpreAdapter.setsOnItemClickListener(new PostPreAdapter.OnsItemClickListener() {
                    Intent intent = new Intent(getApplicationContext(), Post.class);

                    @Override
                    public void onsItemClick(View v, int pos) {
                        docRef = db.collection("ChatApp").document("Post")
                                .collection(intentTheme).document(intentName);

                        intent.putExtra("gametheme", intentTheme);
                        intent.putExtra("gamename", intentName);
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 400);
                    }
                });
            }
        }, 1000);
    }

    private void populateDate() {
        rowsArrayList = new ArrayList<>();
        int k = 0;

        docRef = db.collection("ChatApp").document("Post")
                .collection(intentTheme).document(intentName);

        while (k < 10 && (numberOfGame - i) > 0) {
            docRef.collection("list").document((numberOfGame - i) + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        try {
                            if (document != null) {
                                long num = (long) document.get("num");
                                title = (String) document.get("title");
                                board_list board = new board_list((int) num, title);

                                rowsArrayList.add(board);
                            }
                            for (int j = 0; j < rowsArrayList.size() - 1; j++) {
                                for (int f = j + 1; f < rowsArrayList.size(); f++) {
                                    if (rowsArrayList.get(j).num < rowsArrayList.get(f).num) {
                                        Collections.swap(rowsArrayList, j, f);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.d("num", "갯수가 비어있습니다.");
                        }

                    }
                }
            });
            //파베에서 받은 문자열 넘겨주기
            i++;
            k++;
        }
    }


    public void write_onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Writing_Board.class);
        intent.putExtra(theme, intentTheme);
        intent.putExtra(name, intentName);
        startActivity(intent);
    }

    public void board_back(View v) {
        finish();
    }

    public void getAccountInfo() {
        docRef = db.collection("ChatApp").document("account")
                .collection("list").document(FU.getUid().trim());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        try {
                            Map<String, Object> favoriteMap;
                            Map<String, Object> game = new HashMap<>();

                            favoriteMap = (Map<String, Object>) document.get("favorite");
                            numofgame = (long) favoriteMap.get("numofgame");
                        } catch (Exception e) {
                            Map<String, Object> favoriteMap = new HashMap<>();
                            Map<String, Object> game = new HashMap<>();

                            game.put("numofgame", numofgame);
                            favoriteMap.put("favorite", game);

                            docRef.set(favoriteMap, SetOptions.merge());
                        }
                    }
                }
            }
        });
    }

    public void spinnerDate() {
        dateSpinner = (Spinner) findViewById(R.id.date);
        dateAdapter = ArrayAdapter.createFromResource(this, R.array.date, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        Spinner showSpinner = (Spinner) findViewById(R.id.show);
        ArrayAdapter showAdapter = ArrayAdapter.createFromResource(this, R.array.show, android.R.layout.simple_spinner_item);
        showAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showSpinner.setAdapter(showAdapter);
    }

    /*
        public void boardScroll() {
            board_scroll.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());

                        LinearLayout board_sentence = (LinearLayout) rv.getChildViewHolder(child).itemView.findViewById(R.id.board_sentence);
                        Intent intent = new Intent(getApplicationContext(), Post.class);
                        //.putExtra("number", /*여기에 게시글 번호가 들어가야함.*///);
    //여기 문장을 통해서 recyclerview를 터치했을 때 새로운 게시글 activity가 열려야한다.
    //파이어베이스를 통해서 게시글이 정렬이 될 때 글 번호도 같이 로드 되게 하는게 좋을 듯
                /*}
                return false;
            }


            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
*/
    public void favoriteChecked() {
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef = db.collection("ChatApp").document("account")
                        .collection("list").document(FU.getUid().trim());
                if (((CheckBox) v).isChecked()) {
                    Map<String, Object> favoriteMap = new HashMap<>();
                    Map<String, Object> game = new HashMap<>();
                    Map<String, Object> theme = new HashMap<>();

                    numofgame++;

                    game.put(intentName, true);
                    game.put("numofgame", numofgame);
                    theme.put(intentTheme, game);
                    favoriteMap.put("favorite", theme);

                    docRef.set(favoriteMap, SetOptions.merge());

                } else if (!((CheckBox) v).isChecked()) {
                    Map<String, Object> favoriteMap = new HashMap<>();
                    Map<String, Object> game = new HashMap<>();
                    Map<String, Object> theme = new HashMap<>();

                    numofgame--;

                    game.put(intentName, false);
                    game.put("numofgame", numofgame);
                    theme.put(intentTheme, game);
                    favoriteMap.put("favorite", theme);

                    docRef.set(favoriteMap, SetOptions.merge());

                }
                storeChecked(v);
            }
        });

    }

    public void storeChecked(View v) {
        docRef = db.collection("ChatApp").document("Post")
                .collection(intentTheme).document(intentName);
        Map<String, Boolean> checkbox = new HashMap<>();
        checkbox.put("checked", ((CheckBox) v).isChecked());

        docRef.set(checkbox, SetOptions.merge());
    }
}