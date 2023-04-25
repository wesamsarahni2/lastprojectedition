package com.example.finalproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }
    TextView noItem;
    ListAdapter listAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<String> itemList;
    FirebaseFirestore db;
    ProgressBar pb;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connect();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        if(itemList.size()==0){
            noItem.setVisibility(View.GONE);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
        }
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemList.add(document.getString(" Product name:"));
                                Log.d(getTag(), document.getId() + " => " + document.getData());
                            }
                            pb.setVisibility(View.GONE);
                            Log.d(TAG, "onComplete() called with: task = [" + task + "]" + "item list = "+ itemList);
                            listAdapter.setList(itemList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(listAdapter);

                        } else {
                            Log.w(getTag(), "Error getting documents.", task.getException());
                            noItem.setVisibility(View.GONE);
                        }
                    }
                });
//        searchView= getView().findViewById(R.id.searchView);
//        searchView.clearFocus();

    }

    // TODO: zed category m3 al search (Y)
    private void filterList(String text) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String item:
                itemList) {
            if (item.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
            if (filteredList.isEmpty()){
                listAdapter.setFilteredList(filteredList);
                noItem.setVisibility(View.VISIBLE);
            }else{
                listAdapter.setFilteredList(filteredList);
                noItem.setVisibility(View.GONE);

            }
        }
    }

    private void connect() {
        itemList= new ArrayList<>();
        recyclerView=getView().findViewById(R.id.recyclerView);
        pb = getView().findViewById(R.id.progressBar);
        noItem = getView().findViewById(R.id.noItem);
        db = FirebaseFirestore.getInstance();
        searchView=getView().findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setQueryHint("Search here");
        listAdapter=new ListAdapter(itemList,getContext());
    }
}