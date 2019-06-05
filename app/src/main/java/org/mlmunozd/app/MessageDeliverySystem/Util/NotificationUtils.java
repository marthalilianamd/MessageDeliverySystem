package org.mlmunozd.app.MessageDeliverySystem.Util;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import org.mlmunozd.app.MessageDeliverySystem.R;
import java.util.List;


public class NotificationUtils {

    private static final int NOTIFICATION_ID = 100;
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Muestra la notificación
     */
    public void showNotificationMessage(final String title, final String message, final String phone,Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Compruebe si hay un mensaje vacío
        if (TextUtils.isEmpty(message))
            return;
        // icono de notificación
        final int icon = R.mipmap.applogo;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setContentInfo(phone)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify (NOTIFICATION_ID, notification);
    }

    /**
     * Método comprueba si la aplicación está en segundo plano o no
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Borra los mensajes de la bandeja de notificaciones.
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}