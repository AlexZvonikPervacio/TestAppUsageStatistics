package com.android.internal.app;

import android.content.ComponentName;
import com.android.internal.os.PkgUsageStats;

interface IUsageStats {
    void noteResumeComponent(in ComponentName componentName);
    void notePauseComponent(in ComponentName componentName);
    void noteLaunchTime(in ComponentName componentName, int millis);
    PkgUsageStats getPkgUsageStats(in ComponentName componentName);
    PkgUsageStats[] getAllPkgUsageStats();
}