package com.example.uiproject.Activity_Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uiproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.uiproject.Activity_Community.Community_Board.numberOfGame;

public class Writing_Board extends AppCompatActivity {

    int REQUEST_IMAGE_CODE = 1001;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    EditText title, sentence;

    ImageView ivUser;
    ImageView imgView[] = new ImageView[10];
    int id[] = new int[]{R.id.wimage1, R.id.wimage2, R.id.wimage3, R.id.wimage4, R.id.wimage5, R.id.wimage6, R.id.wimage7, R.id.wimage8, R.id.wimage9, R.id.wimage10};

    FirebaseFirestore db;
    FirebaseAuth fa;
    DocumentReference docRef;
    FirebaseStorage storage;

    String gameName, gameTheme;
    long accountNum;

    Uri[] urione = new Uri[10];
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing__board);

        Intent intent = getIntent();
        gameName = intent.getExtras().getString(Community_Board.name);
        gameTheme = intent.getExtras().getString(Community_Board.theme);

        loadImage();
    }

    public void loadImage() {
        for (int i = 0; i < 10; i++) {
            imgView[i] = findViewById(id[i]);
        }

        ivUser = findViewById(R.id.ivUser);
        title = findViewById(R.id.titlewriting);
        sentence = findViewById(R.id.sentencewriting);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);      //사진 권한 받아옴.
            }
        }
        ivUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < 10; i++) {
                        if (i < clipData.getItemCount()) {
                            urione[i] = clipData.getItemAt(i).getUri();
                            switch (i) {
                                case 0:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 1:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 2:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 3:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 4:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 5:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 6:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 7:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 8:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                case 9:
                                    imgView[i].setVisibility(View.VISIBLE);
                                    imgView[i].setImageURI(urione[i]);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } else if (uri != null) {
                    imgView[0].setImageURI(uri);
                }
            }
        }
    }

    private void uploadFile() {
        storage = FirebaseStorage.getInstance();
        int k = 1;
        //업로드할 파일이 있으면 수행
        if (clipData != null) {
            for (int i = 0; i < 10; i++) {
                if (i < clipData.getItemCount()) {
                    //Unique한 파일명을 만들자.
                    SimpleDateFormat formatter = new SimpleDateFormat("" + k);
                    Date now = new Date();
                    String filename = formatter.format(now) + ".png";
                    //storage 주소와 폴더 파일명을 지정해 준다.
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://chatapp-2fa6e.appspot.com/").child("ChatApp/Post/" + gameTheme + "/" + gameName + "/list/" + numberOfGame + "/" + filename);
                    //올라가거라...
                    storageRef.putFile(urione[i]);
                    k++;
                }
            }
            //storage
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Back_click(View view) {
        finish();
    }

    public void write(View v) {
        storeBoard();
        uploadFile();
    }

    public void storeBoard() {
        fa = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("ChatApp").document("Post")
                .collection(gameTheme).document(gameName);

        String titleS = title.getText().toString();
        String sentenceS = sentence.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("title", titleS);
        user.put("sentence", sentenceS);
        user.put("uid", fa.getCurrentUser().getUid());
        if (clipData == null)
            user.put("imageNum", 0);
        else
            user.put("imageNum", clipData.getItemCount());

        numberOfGame++;
        Map<String, Object> num = new HashMap<>();
        num.put("numberofpost", numberOfGame);

        user.put("num", numberOfGame);

        docRef.set(num, SetOptions.merge());

        docRef.collection("list").document("" + numberOfGame).set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_LONG).show();
                accountNum = numberOfGame;
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        });

        putNumaccount();
    }

    public void putNumaccount() {
        Map<String, Object> Game = new HashMap<>();
        Map<String, Object> Theme = new HashMap<>();

        Game.put(gameName, accountNum);
        Theme.put(gameTheme, Game);

        db.collection("ChatApp").document("account")
                .collection(fa.getCurrentUser().getUid().trim()).document().set(Theme, SetOptions.merge());

    }
}