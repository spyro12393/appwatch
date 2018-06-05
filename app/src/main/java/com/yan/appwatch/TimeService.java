package com.yan.appwatch;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class TimeService extends Service {
    private Context mContext;
    private Timer mTimer;
    private long useTime =0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getTotalUseTime();
                if(useTime>=AppConfig.allowuseTime){
                    boolean isRunning = isServiceRunning(getApplicationContext(),"com.yan.appwatch.MonitorService");
                    if (isRunning) {

                    } else {
                        startService(new Intent(TimeService.this, MonitorService.class));
                    }

                }
            }
        };

        mTimer.schedule(task, 0, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private void getTotalUseTime() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            //HashSet<String> set = new HashSet<>();
            for (UsageStats usageStats : usageStatsList) {
                //set.add(usageStats.getPackageName());
                try {
                    useTime+= usageStats.getTotalTimeInForeground();//加總今日使用時間
                } catch (Exception e) {}
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

}
