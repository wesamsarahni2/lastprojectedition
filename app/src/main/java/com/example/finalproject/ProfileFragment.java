package com.example.finalproject;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView userName;
    private FirebaseFirestore firestore=FireBaseServices.getInstance().getFire();
    private StorageReference storageReference;
    private FirebaseAuth mAuth=FireBaseServices.getInstance().getAuth();
    private Button button;
    private EditText et;
    private ImageView proimg;
    private Uri selectedImage;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    boolean flag = true;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // TODO add profile data
        return view;


    }
// TODO: Review section and think of profile page xml
    @Override
    public void onStart() {
        super.onStart();
        connect();
        pagesetup();
        savelistener();
        setavatar();
        profileimage();

    }

    private void setavatar() {
        if (flag){
            flag=false;
        storageReference= FireBaseServices.getInstance().getStorage().getReference("avatars/"+mAuth.getUid()+".jpg");
        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    Log.d(TAG, "setavatar() called" + bitmap);
                    progressBar.setVisibility(View.GONE);
                    proimg.setImageBitmap(bitmap);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Drawable res = getResources().getDrawable(R.drawable.baseline_add_photo_alternate_24);

                    proimg.setImageDrawable(res);
                    progressBar.setVisibility(View.GONE);
                }
            });


        }catch (IOException e){
            Log.e(TAG, "setavatar: exep" , e );
        }
        }

    }

    private void profileimage() {
        proimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            selectedImage = data .getData();
            proimg.setImageURI(selectedImage);

        }
    }


    private void pagesetup() {
        firestore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if (document != null){
                                    if (document.get("Uid").equals(mAuth.getUid())){
                                        userName.setText(document.get("name").toString());
                                        if (document.getString("bio")==null){
                                            et.setText("");
                                        }else{
                                            et.setText(document.getString("bio"));
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void savelistener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "you left the bio empty :(", Toast.LENGTH_SHORT).show();
                }
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("saving...");
                progressDialog.show();
                    firestore.collection(("Users")).get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    if (document != null){
                                        if (document.get("Uid").equals(mAuth.getUid())){
                                            firestore.collection("Users").document(document.getId()).update("bio",et.getText().toString());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });

                storageReference = FireBaseServices.getInstance().getStorage().getReference("avatars/"+mAuth.getUid()+".jpg");
                storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "saved.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "failed to save.", Toast.LENGTH_SHORT).show();
                    }
                });
                }

        });
    }

    private void connect() {
        userName= getView().findViewById(R.id.name_text_view);
        button= getView().findViewById(R.id.button);
        et= getView().findViewById(R.id.bio);
        proimg=getView().findViewById(R.id.profile_image);
        progressBar=getView().findViewById(R.id.pbavatar);
        if (flag){
            progressBar.setVisibility(View.VISIBLE);

        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}