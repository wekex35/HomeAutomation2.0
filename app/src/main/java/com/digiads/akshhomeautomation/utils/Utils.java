package com.digiads.akshhomeautomation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.digiads.akshhomeautomation.mqtt.PahoMqttClient;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.digiads.akshhomeautomation.utils.Constants.SHARED_PREFERENCES_NAME;
import static com.digiads.akshhomeautomation.utils.Constants.publisTopic;

public class Utils {
    static String TAG = "Utils";

    public static SharedPreferences savetoShared(Context baseContext) {
        return baseContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void publishcontrol(int pin,int state,String deviceID){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("p", pin);
            jsonObject.put("s",state);

            new PahoMqttClient().publishMessage(Constants.GeneralpahoMqttClient,
                    jsonObject.toString(),
                    1,
                    "D-"+deviceID
            );
        } catch (JSONException | UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public static void getstatus(String deviceID,int state){
        Log.d(TAG, "getstatus: "+deviceID);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("status", state);

            new PahoMqttClient().publishMessage(Constants.GeneralpahoMqttClient,
                    jsonObject.toString(),
                    1,
                    "D-"+deviceID
            );
        } catch (JSONException | UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public static void subscribe(String deviceID){
        try {
            new PahoMqttClient().subscribe(
                    Constants.GeneralpahoMqttClient,
                    "P-"+deviceID,
                    0
            );
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
