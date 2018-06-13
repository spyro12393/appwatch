package com.yan.appwatch;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {
    private PackageManager packageManager = null;
    private ListView lv;
    private TextView usedTime_tv,isrunning_tv,restTime_tv,avaiableTime_tv;
    private AppListAdapter adapter;
    public static long usedTime;
    private Date date;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//format sec to HH:mm:ss
    ArrayList<AppList> arrayList ;
    ApplicationInfo appinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        isrunning_tv = findViewById(R.id.isrunning_tv);
        lv = findViewById(R.id.listView);
        usedTime_tv = findViewById(R.id.tv_UsedTime);
        restTime_tv = findViewById(R.id.tv_RestTime);
        avaiableTime_tv = findViewById(R.id.tv_AvailableTime);
        arrayList = new ArrayList<>();
        packageManager = this.getPackageManager();
        openPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayList.clear();
        getHistoryApps();
        adapter = new AppListAdapter(
                getApplicationContext(), R.layout.use_list_layout, arrayList
        );
        lv.setAdapter(adapter);
        /**聽服務是否啟動*/
        boolean isRunning = isServiceRunning(getApplicationContext(),"com.yan.appwatch.MonitorService");
        if (isRunning) {
            isrunning_tv.setText("服務執行中");
            isrunning_tv.setTextColor(Color.WHITE);
            isrunning_tv.setBackgroundColor(Color.RED);
        } else {
            isrunning_tv.setText("服務已關閉");
            isrunning_tv.setTextColor(Color.WHITE);
            isrunning_tv.setBackgroundColor(Color.rgb(0,180,30));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    public void control(View view) {
        Intent i = new Intent(DetailsActivity.this,PermissionActivity.class);
        startActivity(i);
        finish();
    }

    public void openPermission() {
        if (!checkPermission()) {
            Toast.makeText(this, "請開啟APPWATCH權限", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, 100);
        }
    }

    private boolean checkPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode;
        mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
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

    private void getHistoryApps() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            usedTime=0;
            date= new Date(usedTime);
            for (UsageStats usageStats : usageStatsList) {
                date.setTime(usageStats.getTotalTimeInForeground());
                try {
                    appinfo = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
                    arrayList.add(new AppList(
                            appinfo.loadIcon(packageManager),
                            appinfo.loadLabel(packageManager).toString(),
                            usageStats.getPackageName(),
                            String.valueOf(df.format(date))
                    ));
                    usedTime += usageStats.getTotalTimeInForeground();//加總今日使用時間
                } catch (Exception e) {}
            }
            /**總時間*/
            date.setTime(usedTime);
            usedTime_tv.setText(df.format(date));
            /**可用時間*/
            date.setTime(AppConfig.allowuseTime);
            avaiableTime_tv.setText(df.format(date));
            /**剩餘時間*/
            date.setTime(AppConfig.allowuseTime-usedTime);
            restTime_tv.setText(df.format(date));

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
}
