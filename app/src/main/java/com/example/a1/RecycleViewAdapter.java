package com.example.a1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.database.core.Context;
import android.content.Context;
import java.util.ArrayList;

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
        String infoAboutTree;
        if(currentObject.getDone()){
            infoAboutTree = "Successfully planted a tree";
        }else{
            infoAboutTree = "You've Killed a Tree";
        }
        holder.textViewFromTo.setText(fromTo);
        holder.textViewIsComplete.setText(currentObject.getDone().toString());
        holder.textViewInfo.setText(infoAboutTree);
    }

    @Override
    public int getItemCount() {
        return myObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewInfo, textViewFromTo, textViewIsComplete;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.myImageForViewHolder);
            textViewInfo = itemView.findViewById(R.id.myTextViewInfo);
            textViewFromTo = itemView.findViewById(R.id.myTextViewFromTo);
            textViewIsComplete = itemView.findViewById(R.id.myTextViewIsComplete);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutForList);

        }
    }
}
