package eaut.it.mobileappdev.notificationdemo;

import static android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private final String channelId = "test_channel";
    private final String channelName = "Demo Channel";
    private final String description = "This is a test notification";
    private final String notification_title = "Thông báo";
    private final String notification_message = "Chào bạn, đây là thông báo thử nghiệm tính năng notification trong Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        });

        createNotificationChannel();

        btn.setOnClickListener(v -> {
            addNotification();
            sendNotification();
        });

        requestPermission();
    }

    private void addNotification() {
        builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notification_title)
                .setContentText(notification_message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notification_message))
                .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //add notification tap action
        Intent tapActionIntent = new Intent(this, TapActionView.class);
        tapActionIntent.putExtra("message", notification_message);
        PendingIntent tapActionPendingIntent = PendingIntent.getActivity(this, 0, tapActionIntent,
                PendingIntent.FLAG_IMMUTABLE );
        builder.setContentIntent(tapActionPendingIntent);

        //add action button
        Intent actionButtonIntent = new Intent(this, ActionButtonView.class);
        PendingIntent actionButtonPendingIntent = PendingIntent.getActivity(this, 0, actionButtonIntent,
                PendingIntent.FLAG_IMMUTABLE );
        builder.addAction(R.drawable.view_detail, "View Detail", actionButtonPendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void sendNotification() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "You don't have permission to post notification.", Toast.LENGTH_LONG).show();
        else notificationManager.notify(123, builder.build());
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

}

