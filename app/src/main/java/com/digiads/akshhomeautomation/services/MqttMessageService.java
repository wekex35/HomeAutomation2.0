package com.digiads.akshhomeautomation.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.utils.DbUtils;
import com.digiads.akshhomeautomation.utils.dbhelper;
import com.digiads.akshhomeautomation.mqtt.PahoMqttClient;
import com.digiads.akshhomeautomation.utils.Constants;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MqttMessageService extends Service {
   public static final String BROADCAST_ACTION = "com.demo.apps.cricinformation.displayevent";
    private static final String TAG = "MqttMessageService";
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    Intent intent;

    public MqttMessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        Log.d(TAG, "onCreate");

        Log.d(TAG, "onCreate: service started");
        intent = new Intent(BROADCAST_ACTION);
       // mytopic = getTopic(getBaseContext());
        Constants.GeneralpahoMqttClient = new PahoMqttClient().getHomeMqttClientAuthenticate(this, Constants.MQTT_BROKER_URL);
        Constants.GeneralpahoMqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }


            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //setMessageNotification(s, new String(mqttMessage.getPayload()));
                String msg =new String(mqttMessage.getPayload());
               // if (!msg.contains("power"))
                Log.d(TAG, "messageArrived: "+msg);
                DisplayLoggingInfo(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void setMessageNotification(@NonNull String topic, @NonNull String msg) {
        DisplayLoggingInfo(msg);
       // Intent intent = new Intent(this, MainActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fd634dgdft5";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(topic)
                .setContentText(msg)
                // .setLargeIcon(emailObject.getSenderAvatar())

                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void DisplayLoggingInfo(String msg) {
        //  savetoShared(this).edit().putString(SCORE,msg).apply();
      /*  String type = "Message Recieved";
        Log.d(TAG, "MessageEncryption recieved: encrypted "+msg);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject.has("today")){
            savetoShared(this).edit().putString(Constants.SCORE, msg).apply();
            type = Constants.SCORE;
        }else if(jsonObject.has("live_line")) {
            savetoShared(this).edit().putString(Constants.LIVELINE, msg).apply();
            type = Constants.LIVELINE;
        }else{
            savetoShared(this).edit().putString(Constants.SCORECARD, msg).apply();
            type = Constants.SCORECARD;
        }*/

        if (!msg.contains("hello world")){
            new DbUtils(this).upDateDatabase(msg);
            intent.putExtra("datafromService", msg);
        sendBroadcast(intent);   }
    }




        private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

}