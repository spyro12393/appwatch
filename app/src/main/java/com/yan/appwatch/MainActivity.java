package com.yan.appwatch;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
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
    ApplicationInfo appinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        packageManager = this.getPackageManager();
        openPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getHistoryApps();
        adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.list_layout, arrayList
        );
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Intent intent = getPackageManager().getLaunchIntentForPackage("com.yan.appwatch");
       // startActivity(intent);

    }

    public void openPermission() {
        if (!checkPermission()) {
            Toast.makeText(this, "請開啟APPWATCH權限", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, 100);
        }
    }

    public void openService(View view) {
        startService(new Intent(MainActivity.this, MonitorService.class));
        Toast.makeText(this, "服務已開啟！", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (!checkPermission()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, 100);
            } else {
                Toast.makeText(this, "權限已開啟！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode;
        mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void getHistoryApps() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            HashSet<String> set = new HashSet<>();
            for (UsageStats usageStats : usageStatsList) {
                set.add(usageStats.getPackageName());
                try {
                    appinfo = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
                    arrayList.add(new AppList(
                            appinfo.loadIcon(packageManager),
                            appinfo.loadLabel(packageManager).toString(),
                            usageStats.getPackageName(),
                            String.valueOf(usageStats.getTotalTimeInForeground() / 1000+"秒")
                    ));

                } catch (Exception e) {}
            }
            if (!set.isEmpty()) {
                Log.e("size", set.size() + "");
            }
        }

    }

}
