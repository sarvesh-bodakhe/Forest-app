package com.example.a1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.database.core.Context;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
    private static final String TAG = "RecycleViewAdapter";

    private Context myContext;
    private ArrayList<ObjectInfo> myObjects;



    public RecycleViewAdapter(Context Context, ArrayList<ObjectInfo> Objects) {
        myContext = Context;
        myObjects = Objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(myContext).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: onBindViewHolder() called");
        ObjectInfo currentObject = myObjects.get(position);
        String fromTo = currentObject.getFrom() + " - " + currentObject.getTo();
        String endTime = currentObject.getEnd();
        Date date = currentObject.getDate();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

        String time = calculateTime(currentObject.getFrom(), currentObject.getEnd());
        String infoAboutTree;
        if(currentObject.getDone()){
            infoAboutTree = "Successfully planted";
        }else{
            infoAboutTree = "You've Killed a Tree";
        }
        holder.textViewFromTo.setText(fromTo);
        holder.textViewIsComplete.setText(time);
        holder.textViewInfo.setText(infoAboutTree);
        holder.textViewForDate.setText(ft.format(date));

        if(currentObject.getDone()) {
            holder.imageView.setImageResource(R.drawable.smiling_tree_card_view);
            holder.time.setImageResource(R.drawable.ic_time_green);
            holder.textViewInfo.setTextColor(ContextCompat.getColor(myContext, R.color.green));
        }
        else {
            holder.imageView.setImageResource(R.drawable.upset_tree_card_view);
            holder.time.setImageResource(R.drawable.ic_time_red);
            holder.textViewInfo.setTextColor(ContextCompat.getColor(myContext, R.color.red));
        }
    }

    private String calculateTime(String from, String end) {
        String time = "";
        Log.d(TAG, "calculateTime: from , to = " + from + " "+ end);
        String[] starts = from.split(":", 2);
        String[] ends = end.split(":", 2);
        int starthr, startmin, endhr, endmin;

        starthr = Integer.parseInt(starts[0]);
        startmin  = Integer.parseInt(starts[1]);
        endhr  = Integer.parseInt(ends[0]);
        endmin  = Integer.parseInt(ends[1]);
        Log.d(TAG, ""+ starthr +" " + startmin);
        Log.d(TAG, "" + endhr + " " + endmin);
        int timeTaken = ((endhr*60)+endmin) - ((starthr*60)+startmin);
        Log.d(TAG, "Timetaken = " + timeTaken);
        time = String.valueOf(timeTaken);
        return time;
    }


    @Override
    public int getItemCount() {
        return myObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView, time;
        TextView textViewInfo, textViewFromTo, textViewIsComplete, textViewForDate;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.myImageForViewHolder);
            textViewInfo = itemView.findViewById(R.id.myTextViewInfo);
            textViewFromTo = itemView.findViewById(R.id.myTextViewFromTo);
            textViewIsComplete = itemView.findViewById(R.id.myTextViewIsComplete);
            textViewForDate = itemView.findViewById(R.id.myTextViewForDate);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutForList);
            time = itemView.findViewById(R.id.time_taken);
        }
    }
}
