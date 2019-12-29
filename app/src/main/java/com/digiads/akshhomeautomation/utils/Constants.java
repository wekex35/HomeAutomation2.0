package com.digiads.akshhomeautomation.utils;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.UUID;

public class Constants {
    public static MqttAndroidClient GeneralpahoMqttClient;
    public static final String MQTT_BROKER_URL = "tcp://206.189.135.46:1883";
    public static final String PASSWORD = "pass";
    public static final String USER_ID = "UID";
    static final String SHARED_PREFERENCES_NAME = "automation";
    public static final String  clientId   = UUID.randomUUID().toString();
    public static String mytopic = "outTopic";
    public static String publisTopic = "inTopic";
}
