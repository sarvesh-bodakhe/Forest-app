package com.example.a1;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppListFragment extends Fragment {
    private static final String TAG = "AppListFragment";
    PackageManager myPackageManager;
    ListView myAppList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getContext(), "Work In Progress. Just Showing the apps", Toast.LENGTH_SHORT).show();

        view = inflater.inflate(R.layout.applist_layout, container, false);
        Log.d(TAG, "onCreate: ");
        myPackageManager = Objects.requireNonNull(getContext()).getPackageManager();
        List<PackageInfo> myPackageInfoList = myPackageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        List<PackageInfo> myPackageInfoList2 = new ArrayList<PackageInfo>();
        for(PackageInfo pi : myPackageInfoList){
            boolean b = isSystemPackage(pi);
            if(!b){
                myPackageInfoList2.add(pi);
            }
        }
        Log.d(TAG, "onCreate: 1 " + myPackageInfoList.size());
        Log.d(TAG, "onCreate: 2 " + myPackageInfoList2.size());
        myAppList = (ListView) view.findViewById(R.id.myAppList);
        myAppList.setAdapter(new AppAdapter(getActivity(), myPackageInfoList2, myPackageManager));



        myAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Item CLicked", Toast.LENGTH_SHORT).show();

                //TODO
                //What to do when this is clicked
            }
        });
        return view;

    }

    private boolean isSystemPackage(PackageInfo pkginfo) {
        return (pkginfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
