package com.yan.appwatch;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

/**
 * Created by bang on 2017/9/15.
 */

public class AppList {
    private Drawable image;
    private String AppName;
    private String PackageName;
    private String UseTime;

    public AppList(Drawable image, String AppName, String PackageName, String UseTime) {
        this.image = image;
        this.AppName = AppName;
        this.PackageName = PackageName;
        this.UseTime = UseTime;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String PackageName) {
        this.PackageName = PackageName;
    }

    public String getUseTime() {
        return UseTime;
    }

    public void setUseTime(String UseTime) {
        this.UseTime = UseTime;
    }

}
