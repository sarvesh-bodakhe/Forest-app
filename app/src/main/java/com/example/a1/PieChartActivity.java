package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class PieChartActivity extends AppCompatActivity {

    PieChart pieChart;
    float trueValues = 0, falseValues = 0;
    DatabaseReference myRef;
    ArrayList<ObjectInfo> myListOfObjects;
    private static final String TAG = "PieChartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        List<PieEntry> value = new ArrayList<>();

        for(ObjectInfo currentObjct : ListActivity.myListOfObjects){
            if(currentObjct.getDone()){
                Log.d(TAG, "True");
                trueValues++;
            }else{
                Log.d(TAG, "False");
                falseValues++;
            }
        }

        Log.d(TAG, " Out of function True = " + trueValues + " False = " + falseValues);

        float total = trueValues + falseValues;
        trueValues = (trueValues/total)*100 ;
        falseValues = (falseValues/total)*100 ;
        Log.d(TAG, "After Function -ve +ve" + trueValues + falseValues);

        PieDataSet pieDataSet = new PieDataSet( value, "All Time");
        value.add(new PieEntry( trueValues, "Successful"));
        value.add(new PieEntry(falseValues, "Unsuccessful"));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
        pieChart.setData(pieData);

    }




}
