package com.example.uiproject.Activity_Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.example.uiproject.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.uiproject.Activity_Community.Community_Board.numberOfGame;
import static com.example.uiproject.Activity_Community.Community_Board.rowsArrayList;
import static com.example.uiproject.Activity_Community.Community_Board.title_;

public class Post extends AppCompatActivity {
    ImageView imgView[] = new ImageView[10];
    ImageView img;
    int id[] = new int[]{R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5, R.id.image6, R.id.image7, R.id.image8, R.id.image9, R.id.image10};

    FirebaseFirestore db;
    FirebaseAuth da;
    DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference stoRef;
    StorageReference childstor;
    FirebaseUser FU;

    String gameName;
    String gameTheme;

    int numberOfPost;
    long number;
    int k = 1;
    public static int to;
    String filename;
    Context con;

    TextView title, sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        gameTheme = intent.getExtras().getString("gametheme");
        gameName = intent.getExtras().getString("gamename");

        title = findViewById(R.id.Post_title);
        sentence = findViewById(R.id.Post_sentence);
        for (int i = 0; i < 10; i++) {
            imgView[i] = findViewById(id[i]);
        }
        img = findViewById(R.id.image1);

        da = FirebaseAuth.getInstance();
        FU = da.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("ChatApp").document("Post")
                .collection(gameTheme).document(gameName).collection("list").document("" + to);

        con = this;

        getTexts();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getImages();
            }
        }, 1000);
    }

    public void getTexts() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    try {
                        if (document != null) {
                            title.setText((String) document.get("title"));
                            sentence.setText((String) document.get("sentence"));
                            number = (long) document.get("imageNum");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }

    public void getImages() {
        filename = k + ".png";
        stoRef = FirebaseStorage.getInstance().getReference().child("ChatApp/Post/" + gameTheme + "/" + gameName + "/list/" + to + "/");
        childstor = stoRef.child(filename);
        childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                {
                    Glide.with(Post.this)
                            .load(uri)
                            .into(imgView[0]);
                    Log.e("DF", uri + "");
                    k++;
                }
            }
        });
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[1]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[2]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[3]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[4]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[5]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[6]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[6]);
                        Log.e("DF", uri + "");

                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[7]);
                        Log.e("DF", uri + "");
                    }
                }
            });
        }
        k++;
        if (k <= number) {
            filename = k + ".png";
            childstor = stoRef.child(filename);
            childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    {

                        Glide.with(Post.this)
                                .load(uri)
                                .into(imgView[8]);
                        Log.e("DF", uri + "");

                    }
                }
            });
            k++;
            if (k <= number) {
                filename = k + ".png";
                childstor = stoRef.child(filename);
                childstor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        {

                            Glide.with(Post.this)
                                    .load(uri)
                                    .into(imgView[9]);
                            Log.e("DF", uri + "");

                        }
                    }
                });
            }
        }
    }


    public void commentOnClick(View v) {
        EditText commentText = findViewById(R.id.comment_text);
        CheckBox commentCheck = findViewById(R.id.comment_check);

        String comment = commentText.getText().toString();

        if (commentCheck.isChecked()) {

        } else {

        }
    }

    public void Back_click(View v) {
        finish();
    }
}
