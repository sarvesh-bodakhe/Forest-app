package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";

    DatabaseReference myRef;
    RecyclerView myRecyclerView;
    ArrayList<ObjectInfo> myListOfObjects;
    RecycleViewAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        myRecyclerView = findViewById(R.id.myRecyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myListOfObjects = new ArrayList<>();



        myRef = FirebaseDatabase.getInstance().getReference().child("profiles");
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
                myAdapter = new RecycleViewAdapter(ListActivity.this, myListOfObjects);
                myRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: ");
            }
        });
    }
}
