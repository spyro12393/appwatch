package com.yan.appwatch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private PackageManager packageManager = null;
    private ListView lv;
    private CustomListAdapter adapter;
    ArrayList<AppList> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        lv = findViewById(R.id.listView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayList.clear();
        CustomListAdapter.map.clear();
        for(Map.Entry<Integer, String> entry : AppConfig.whitelist.entrySet()) {
            CustomListAdapter.map.put(entry.getKey(),true);
        }
        getAppList();
        adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.list_layout, arrayList
        );
        lv.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Intent intent = getPackageManager().getLaunchIntentForPackage("com.yan.appwatch");
        //startActivity(intent);

    }



    public void openService(View view) {
        boolean isRunning = isServiceRunning(getApplicationContext(),"com.yan.appwatch.MonitorService");
        if (isRunning) {
            Toast.makeText(this, "服務運行中！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "服務已開啟！", Toast.LENGTH_SHORT).show();
            startService(new Intent(MainActivity.this, MonitorService.class));
        }


    }

    public void closeService(View view) {
        AppConfig.allowuseTime = 86400000L; //設定可用時間超過一天不會自動在啟動service
        stopService(new Intent(MainActivity.this, MonitorService.class));
        Toast.makeText(this, "服務已關閉！", Toast.LENGTH_SHORT).show();
    }

    public void selectAll(View view) {
        for (int i = 0;i< arrayList.size();i++ ) {
            CustomListAdapter.map.put(i,true);
        }
        adapter.notifyDataSetChanged();
    }

    public void unselectAll(View view) {
        for (int i = 0;i< arrayList.size();i++ ) {
           CustomListAdapter.map.remove(i);
        }
        adapter.notifyDataSetChanged();
    }

    public void setwhitelist(View view) {
        AppConfig.whitelist.clear();
        if(!CustomListAdapter.map.isEmpty()){
            for(Map.Entry<Integer, Boolean> entry : CustomListAdapter.map.entrySet()) {
                if (entry.getValue().equals(true)) {
                    AppConfig.whitelist.put(entry.getKey(),arrayList.get(entry.getKey()).getPackageName());
                }
            }
        }
    }

    private boolean isServiceRunning(Context context, String serviceName) {
        if (!TextUtils.isEmpty(serviceName) && context != null) {
            ActivityManager activityManager
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfoList
                    = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(100);
            for (Iterator<ActivityManager.RunningServiceInfo> iterator = runningServiceInfoList.iterator(); iterator.hasNext();) {
                ActivityManager.RunningServiceInfo runningServiceInfo = (ActivityManager.RunningServiceInfo) iterator.next();
                if (serviceName.equals(runningServiceInfo.service.getClassName().toString())) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private void getAppList() {
        PackageManager pm = getPackageManager();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){ // 非系统應用

            } else {
                // 系统应用　　　　　　　　
            }
            arrayList.add(new AppList(
                    packageInfo.applicationInfo.loadIcon(pm),
                    packageInfo.applicationInfo.loadLabel(pm)
                            .toString(),
                    packageInfo.packageName,
                    ""
            ));

        }
    }

    public void setusetime(View view) {
        Intent intent = new Intent(MainActivity.this,SetUseTimeActivity.class);
        startActivity(intent);
        finish();
    }
}



