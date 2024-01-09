package com.eleco.view.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.eleco.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "channel_id";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (remoteMessage.getNotification() != null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);
            String idNotif = databaseReference.push().getKey();
            Map<String, Object> notifMap = new HashMap<>();

            Long timestamp = System.currentTimeMillis();

            notifMap.put("id",idNotif);
            notifMap.put("teksNotifikasi", remoteMessage.getNotification().getTitle()+" "+remoteMessage.getNotification().getBody());
            notifMap.put("timestamp", timestamp);
            saveNotificationToDatabase(notifMap);
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createNotificationChannel();
            displayNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel Name";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void displayNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build());
    }

    private void saveNotificationToDatabase(Map<String, Object> notificationData) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifikasi").child(userId);
        String idNotif = databaseReference.push().getKey();
        if (idNotif != null) {
            databaseReference.child(idNotif).setValue(notificationData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Data notifikasi berhasil disimpan ke database"))
                    .addOnFailureListener(e -> Log.e(TAG, "Gagal menyimpan data notifikasi ke database: " + e.getMessage()));
        }
    }
}
