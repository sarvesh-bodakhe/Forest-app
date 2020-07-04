package com.example.a1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment {

    private View view;
    PieChart pieChart;
    float trueValues = 0, falseValues = 0;
    DatabaseReference myRef;
    private static final String TAG = "PieChartActivity";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Step 1");
        view = inflater.inflate(R.layout.activity_pie_chart, container, false);


         pieChart = view.findViewById(R.id.pieChart);

         pieChart.setUsePercentValues(true);


        Log.d(TAG, "onCreateView: Step2");
        Initilization();

        return view;
    }

    private void Initilization() {
        List<PieEntry> value = new ArrayList<>();
        Log.d(TAG, "Initilization: In function");
        Log.d(TAG, "myListOfObjects.size() =  " + HomeFragment.myListOfObjects.size());
        for(ObjectInfo currentObject : HomeFragment.myListOfObjects){
            if (currentObject.getDone()){
                Log.d(TAG, "True");trueValues++;
                Log.d(TAG, "" + falseValues);
            }
            else{
                Log.d(TAG, "false");falseValues++;
                Log.d(TAG, " " + falseValues);
            }
        }

        Log.d(TAG, " Out of function True = " + trueValues + " False = " + falseValues);
//
        float total = trueValues + falseValues;
        float trues = (trueValues/total)*100 ;
        float falses = (falseValues/total)*100 ;
        Log.d(TAG, "After Function -ve +ve" + trues + falses);
//
        PieDataSet pieDataSet = new PieDataSet( value, "All Time");
        value.add(new PieEntry( trues, "Successful"));
        value.add(new PieEntry(falses, "Unsuccessful"));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
        pieChart.setData(pieData);
    }
}
