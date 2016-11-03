package com.example.lenovo.testappusagestatistics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.internal.app.IUsageStats;
import com.android.internal.os.PkgUsageStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String permission = Manifest.permission.PACKAGE_USAGE_STATS;
        boolean granted = getPackageManager().checkPermission(permission, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        Log.w(TAG, "granted " + granted);
        granted = this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        Log.w(TAG, "granted " + granted);

        // The was no such activity in api before 21
//        if (!granted) {
//            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));//Settings.ACTION_USAGE_ACCESS_SETTINGS));
//        }

        IUsageStats mUsageStatsService = IUsageStats.Stub.asInterface(getService());
        if (mUsageStatsService == null) {
            Log.e(TAG, "Failed to retrieve usagestats service");
        } else {
            Log.w(TAG, "usagestats retrieved");
            try {
                // will throw SecurityException because android.permission.PACKAGE_USAGE_STATS was not granted
                PkgUsageStats[] allPkgUsageStats = mUsageStatsService.getAllPkgUsageStats();
                Log.w(TAG, "allPkgUsageStats length = " + allPkgUsageStats.length);
                for (PkgUsageStats stats : allPkgUsageStats) {
                    Log.w(TAG, "stats : " + stats.toString());
                }
            } catch (RemoteException e) {
                Log.w(TAG, e.getMessage());
            }
        }
    }

    private IBinder getService() {
        try {
            Class clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            return (IBinder) method.invoke(null, "usagestats");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
