package com.example.a1;

import com.example.a1.AppData;
import com.example.a1.AppAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {
    private static final String TAG = "AppListActivity";
    PackageManager myPackageManager;
    ListView myAppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist_layout);
        Log.d(TAG, "onCreate: ");
        myPackageManager = getPackageManager();
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
        myAppList = (ListView) findViewById(R.id.myAppList);
        myAppList.setAdapter(new AppAdapter(this, myPackageInfoList2, myPackageManager));



        myAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AppListActivity.this, "Item CLicked", Toast.LENGTH_SHORT).show();

                //TODO
                //What to do when this is clicked
            }
        });

    }



    private boolean isSystemPackage(PackageInfo pkginfo) {
        return ((pkginfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;

    }

}
