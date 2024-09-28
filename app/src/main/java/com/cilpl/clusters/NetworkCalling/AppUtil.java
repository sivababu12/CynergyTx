package com.cilpl.clusters.NetworkCalling;

import android.app.ActivityManager;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.List;

public class AppUtil {

    /**
     * networkState: This is global variable contain network state.
     */
    public static final String GCM_REPEAT_TAG = "repeat|[7200,1800]";
    public static final String BROADCAST_NOTIFICATION = "broadcast_notification";
    private static NetworkConnectionState networkState;

    public static void setNetworkState(NetworkConnectionState networkConnectionState) {
        networkState = networkConnectionState;
    }

    public static NetworkConnectionState getNetworkState() {
        return networkState;
    }

    public static boolean checkApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }

    public static boolean isFragmentUIActive(Fragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static String getLocationURL(double latitude, double longitude, String placeName, int zoom) {
        return "https://maps.google.com/?q=" + latitude + "," + longitude + " (" + placeName + ")" + "&z=" + zoom + "&ll=" + latitude + "," +
                longitude;
    }
}
