package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassword extends Fragment {
    private FireBaseServices fbs;
    private Button BackForgot;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForgotPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPassword newInstance(String param1, String param2) {
        ForgotPassword fragment = new ForgotPassword();
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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }
    private Button ResetPassword;
    private EditText ResetEmail;

    @Override
    public void onStart() {
        fbs=FireBaseServices.getInstance();
        BackForgot=getView().findViewById(R.id.bkforgot);
        super.onStart();
        ResetEmail=getView().findViewById(R.id.etEmailReset);

        ResetPassword=getView().findViewById(R.id.btnResetPass);
        BackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new LoginFragment());
                ft.commit();
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fbs.getAuth().sendPasswordResetEmail(ResetEmail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Your Password reset Email has been sent", Toast.LENGTH_SHORT).show();

                                            FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.mainFrame, new LoginFragment());
                                            ft.commit();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Failed, check the email you entered", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



            }
        });
    }



}