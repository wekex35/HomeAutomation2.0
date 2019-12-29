package com.digiads.akshhomeautomation.mqtt;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.digiads.akshhomeautomation.utils.Constants;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import static com.digiads.akshhomeautomation.utils.Constants.PASSWORD;
import static com.digiads.akshhomeautomation.utils.Constants.USER_ID;
import static com.digiads.akshhomeautomation.utils.Constants.mytopic;
import static com.digiads.akshhomeautomation.utils.Utils.getstatus;
import static com.digiads.akshhomeautomation.utils.Utils.savetoShared;


/**
 * Created by brijesh on 20/4/17.
 */

public class PahoMqttClient {

    private static final String TAG = "PahoMqttClient";

    private MqttAndroidClient mqttAndroidClient;
    private String username;
    private String password;

    /*public MqttAndroidClient getMqttClient(Context context, String brokerUrl, String clientId) {

        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "Success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttAndroidClient;
    }*/

    public MqttAndroidClient getMqttClientAuthenticate(final Context context, String brokerUrl, String UserId, String pass) {
        username = UserId;
        password = pass;
        String clientId = Constants.clientId;
        Log.d(TAG, "getMqttClientAuthenticate: "+clientId);
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            final IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOptionAthenticate());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "Success "+token.toString());
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
//                    setLog("Success "+token.toString());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            Log.d(TAG, "Failure1 " + e.toString());
            e.printStackTrace();
        }

        return mqttAndroidClient;
    }
    public MqttAndroidClient getHomeMqttClientAuthenticate(Context context, String brokerUrl) {
        username = savetoShared(context).getString(USER_ID,"0");
        password= savetoShared(context).getString(PASSWORD,"0");
        String clientId = Constants.clientId;
        Log.d(TAG, "getMqttClientAuthenticate: "+clientId);
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            final IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOptionAthenticate());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "Success "+token.toString());
                    try {
                       subscribe(Constants.GeneralpahoMqttClient,  mytopic, 0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onSuccess: "+e.getMessage());
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            Log.d(TAG, "Failure1 " + e.toString());
            e.printStackTrace();
        }

        return mqttAndroidClient;
    }
    public void disconnect(@NonNull MqttAndroidClient client) throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }

    @NonNull
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    @NonNull
    private MqttConnectOptions getMqttConnectionOptionAthenticate() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        //mqttConnectOptions.setWill(Constants.PUBLISH_TOPIC, "I am going offline".getBytes(), 1, true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        return mqttConnectOptions;
    }

    @NonNull
    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        //mqttConnectOptions.setWill(Constants.PUBLISH_TOPIC, "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("user");
        //mqttConnectOptions.setPassword("WtjhZKl3OPoK".toCharArray());
        return mqttConnectOptions;
    }


    public void publishMessage(@NonNull MqttAndroidClient client, @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(320);
        message.setRetained(false);
        message.setQos(qos);
        client.publish(topic, message);
        Log.d(TAG, "publishMessage: "+topic+" "+message);
    }

    public void subscribe(@NonNull MqttAndroidClient client, @NonNull final String topic, int qos) throws MqttException {
        final String[] ret = new String[1];
        IMqttToken token = client.subscribe(topic, qos);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Subscribe Successfully " + topic);
                getstatus(topic,5);
//                setLog("Subscribe Successfully " + topic);

            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                setLog("Subscribe Successfully " + topic);
            }
        });

    }

    public void unSubscribe(@NonNull MqttAndroidClient client, @NonNull final String topic) throws MqttException {

        IMqttToken token = client.unsubscribe(topic);

        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "UnSubscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "UnSubscribe Failed " + topic);
            }
        });
    }
    public void setLog(String msg){

    }

}