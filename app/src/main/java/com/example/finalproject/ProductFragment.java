package com.example.finalproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView etProductName, CompanyName,Description;
    private Button AddItem,bk;
    private CheckBox available ;
    private FirebaseFirestore db=FireBaseServices.getInstance().getFire();



    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connect();
        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new HomeFragment());
                ft.commit();
            }
        });
        db.collection("Products").get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete: "+ document.getString(" Product name:"));
                        Log.d(TAG, "onComplete: "+ getArguments().getString("productname"));
                     if (document.getString(" Product name:").equals(getArguments().getString("productname"))){
                         etProductName.setText(document.getString(" Product name:"));
                         CompanyName.setText(document.getString(" Company name:"));
                         Description.setText(document.getString(" Additional Description about item:"));
                         Log.d(TAG, "onComplete: "+ document.getBoolean("Isavailable"));
                         available.setChecked(document.getBoolean("Isavailable"));
                         if (document.getBoolean("Isavailable")){
                             available.setText("available");
                         }else{
                             available.setText("unavailable");
                         }
                         break;
                     }

                    }
                }
            }
        });
    }
    private void connect() {
        bk=getView().findViewById(R.id.bkProduct);
        etProductName=getView().findViewById(R.id.ProductName);
        CompanyName=getView().findViewById(R.id.companyname);
        Description=getView().findViewById(R.id.desc);
        available=getView().findViewById(R.id.available);
    }
}