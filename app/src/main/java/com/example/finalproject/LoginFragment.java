package com.example.finalproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    BottomNavigationView bn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        CheckUser();


    }
    private void CheckUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("name" , "");
        if (check.equals("true")){
            FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new HomeFragment());
            ft.commit();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }
    private Button ForgotPass,SignIn;
    private EditText etEmail, etPassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    public static final String SHARED_PREF = "sharedPrefs";


    public void onStart() {

        super.onStart();
        bn=getActivity().findViewById(R.id.bNavBarMain);
        bn.setVisibility(View.GONE);
        SignIn=getView().findViewById(R.id.btnSignIn);
        btnSignUp=getView().findViewById(R.id.btnSignup);
        etEmail= getView().findViewById(R.id.etEmail);
        etPassword= getView().findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                login();

            }
        });


        ForgotPass=getView().findViewById(R.id.btnForgot);
        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "You can reset your password now!", Toast.LENGTH_SHORT).show();

                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new ForgotPassword());
                ft.commit();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame
                        , new signup2());
                ft.commit();

            }

        });
    }
    private boolean IsPasswordValid(String password)
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
    private boolean IsEmailValid(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public void login() {
        String email, password;
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getContext()
                    , "Some fields are missing ", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!IsEmailValid(email)) {
            Toast.makeText(getContext()
                    , "Email is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if (false) {
            Toast.makeText(getContext()
                    , "Password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("name" , "true");
                                    editor.apply();
                                    Toast.makeText(getContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.mainFrame, new HomeFragment());
                                    ft.commit();
                                    bn.setVisibility(View.VISIBLE);


                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();


                                }
    }

                        });
}
    }
