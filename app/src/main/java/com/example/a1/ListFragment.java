package com.example.a1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    View view;
    DatabaseReference myRef;
    FirebaseAuth myAuth;
    RecyclerView myRecyclerView;
    public static ArrayList<ObjectInfo> myListOfObjects;
    RecycleViewAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list, container, false);

        myRecyclerView = view.findViewById(R.id.myRecyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myListOfObjects = new ArrayList<>();

        initialize();

        return view;
    }

    private void initialize() {
        String userId = FirebaseAuth.getInstance().getUid();
        if(userId == null)
            userId = "NoUserId";
        myRef = FirebaseDatabase.getInstance().getReference().child("profiles/").child(userId);
        Log.d(TAG, "initialize: myRef =  " + myRef);
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    ObjectInfo currObject = myDataSnapshot.getValue(ObjectInfo.class);
                    Log.d(TAG, "onDataChange: Adding User");
                    myListOfObjects.add(currObject);
                }
                Log.d(TAG, "onDataChange: Size Of myListOfObjects" + myListOfObjects.size());
                myAdapter = new RecycleViewAdapter(getActivity(), myListOfObjects);
                myRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: ");
            }
        });
    }
}





