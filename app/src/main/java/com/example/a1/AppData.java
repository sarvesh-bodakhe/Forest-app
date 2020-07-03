package com.example.a1;

import android.app.Application;
import android.content.pm.PackageInfo;

public class AppData extends Application {

    PackageInfo myPackageInfo;

    public PackageInfo getMyPackageInfo() {
        return myPackageInfo;
    }

    public void setMyPackageInfo(PackageInfo myPackageInfo) {
        this.myPackageInfo = myPackageInfo;
    }
}
