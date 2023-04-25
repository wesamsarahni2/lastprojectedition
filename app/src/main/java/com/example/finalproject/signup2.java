package com.example.finalproject;

import static android.content.ContentValues.TAG;

import static com.example.finalproject.LoginFragment.SHARED_PREF;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup2.
     */
    // TODO: Rename and change types and number of parameters

            private FirebaseFirestore fstore= FirebaseFirestore.getInstance();
    private EditText email2,password2,confirmpassword2,Username;
    private Button signup, bk;
    private FirebaseAuth mAuth;
    BottomNavigationView bn;
    public void onStart(){
        super.onStart();
        bn=getActivity().findViewById(R.id.bNavBarMain);

        signup=getView().findViewById(R.id.btnSignUp);
        bk=getView().findViewById(R.id.bk);
        Username=getView().findViewById(R.id.ETtUserName);
        email2= getView().findViewById(R.id.etEmail2);
        password2= getView().findViewById(R.id.etPassword2);
        confirmpassword2  = getView().findViewById(R.id.etPasswordConfirm);

        mAuth = FirebaseAuth.getInstance();
        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new LoginFragment());
                ft.commit();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();


            }


        });


    }
    private boolean ispasswordvalid(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public boolean isEmailvalid(String email){
        String expression = "[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void signup() {
        String email,password,confirmpassword;
        email = email2.getText().toString();
        password = password2.getText().toString();
        confirmpassword= confirmpassword2.getText().toString();
        if( email.trim().isEmpty()||  password.trim().isEmpty()||  confirmpassword.trim().isEmpty())
        {
            Toast.makeText(getContext()
                    , "some fields are missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEmailvalid(email))
        {
            Toast.makeText(getContext()
                    , "Email is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ispasswordvalid(password))
        {
            Toast.makeText(getContext()
                    , "Password is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmpassword))
        {
            Toast.makeText(getContext()
                    , "the confirmed password Dosent match", Toast.LENGTH_SHORT).show();
        }
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "acc created", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name" , "true");
                        editor.apply();
                        CreateUser(getView());
                        Log.d(TAG, "onSuccess: acc created");
                        FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainFrame, new HomeFragment());
                        ft.commit();
                        bn.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: failed");
                    }
                });

                }
    public void CreateUser(View view) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", Username.getText().toString().trim());
        user.put("Uid", mAuth.getUid());


        fstore.collection("Users")
                .add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
       /* fstore.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(view.getContext(), "User has been added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "failed to add User", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });*/

    public static signup2 newInstance(String param1, String param2) {
        signup2 fragment = new signup2();
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
        return inflater.inflate(R.layout.fragment_signup2, container, false);
    }
}