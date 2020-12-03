package com.example.uiproject.Activity_Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.uiproject.Activity_Community.Community_Board.rowsArrayList;
import static com.example.uiproject.Activity_Community.Community_Board.title_;

public class Post extends AppCompatActivity {
    ImageView imgView[] = new ImageView[10];
    int id[] = new int[]{R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5, R.id.image6, R.id.image7, R.id.image8, R.id.image9, R.id.image10};

    FirebaseFirestore db;
    FirebaseAuth da;
    DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference stoRef;
    FirebaseUser FU;

    String gameName;
    String gameTheme;

    int numberOfPost;
    int k = 1;
    int to;

    TextView title, sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        gameTheme = intent.getExtras().getString("gametheme");
        gameName = intent.getExtras().getString("gamename");

        to = rowsArrayList.indexOf(title_);

        title = findViewById(R.id.Post_title);
        sentence = findViewById(R.id.Post_sentence);
        for (int i = 0; i < 10; i++) {
            imgView[i] = findViewById(id[i]);
        }

        da = FirebaseAuth.getInstance();
        FU = da.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("ChatApp").document("Post")
                .collection(gameTheme).document(gameName).collection("list").document("" + to);


        getTexts();
        getImages();

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
        stoRef = FirebaseStorage.getInstance().getReference().child("ChatApp/Post/" + gameTheme + "/" + gameName + "/list/" + numberOfPost + "/");
        String filename = k + ".png";
        while (stoRef.child(filename) != null && k < 10) {
            stoRef.child(filename).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(Post.this)
                                .load(task.getResult())
                                .centerCrop()
                                .into(imgView[k]);
                    }
                }
            });
            k++;
            filename = k + ".png";
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
