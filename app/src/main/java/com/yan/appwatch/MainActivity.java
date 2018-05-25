package com.yan.appwatch;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private PackageManager packageManager = null;
    private ListView lv;
    private CustomListAdapter adapter;
    ArrayList<AppList> arrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getApps();
        adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.list_layout, arrayList
        );
        //lv.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.yan.appwatch");
        startActivity(intent);

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

    private void getApps() {
        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
        if (apps != null && !apps.isEmpty()) {
            for (ApplicationInfo applicationInfo : apps) {
                try {

                    arrayList.add(new AppList(
                            applicationInfo.loadIcon(packageManager),
                            applicationInfo.loadLabel(packageManager).toString(),
                            applicationInfo.packageName,
                            ""

                    ));

                } catch (Exception e) {

                }
            }
        }

    }


}
