package com.example.a1;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    private static final String TAG = "AppAdapter";
    List<PackageInfo> myPackageInfoList;
    Activity context;
    PackageManager myPackageManager;
    
    public AppAdapter(Activity context, List<PackageInfo> myPackageInfoList,
                      PackageManager myPackageManager) {
        this.myPackageInfoList = myPackageInfoList;
        this.context = context;
        this.myPackageManager = myPackageManager;
    }

    public class ViewHolderForApp{
        TextView appName;
    }

    @Override
    public int getCount() {
        return myPackageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPackageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: ");
        ViewHolderForApp myHolderForApp;
        LayoutInflater myinflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = myinflater.inflate(R.layout.applist_item, null);
            myHolderForApp = new ViewHolderForApp();
            myHolderForApp.appName = (TextView) convertView.findViewById(R.id.appname);
            convertView.setTag(myHolderForApp);
        } else {
            myHolderForApp = (ViewHolderForApp) convertView.getTag();
        }

        PackageInfo myPackageInfo = (PackageInfo) getItem(position);
        Drawable appIcon = myPackageManager.getApplicationIcon(myPackageInfo.applicationInfo);
        String appName = myPackageManager.getApplicationLabel(myPackageInfo.applicationInfo).toString();
        appIcon.setBounds(0, 0, 40, 40);
        myHolderForApp.appName.setCompoundDrawables(appIcon, null, null, null);
        myHolderForApp.appName.setCompoundDrawablePadding(15);
        myHolderForApp.appName.setText(appName);
        return convertView;

    }
}
