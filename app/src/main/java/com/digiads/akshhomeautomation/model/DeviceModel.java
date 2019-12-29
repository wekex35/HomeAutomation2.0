package com.digiads.akshhomeautomation.model;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.digiads.akshhomeautomation.utils.finalConstants.CDATA;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_DATA;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_IP;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_MAC;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_ROOM;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_TYPE;
import static com.digiads.akshhomeautomation.utils.finalConstants.DID;
import static com.digiads.akshhomeautomation.utils.finalConstants.ID;
import static com.digiads.akshhomeautomation.utils.finalConstants.STATUS;
import static com.digiads.akshhomeautomation.utils.finalConstants.TIME;
import static com.digiads.akshhomeautomation.utils.finalConstants.VERSION;

public class DeviceModel {
    private String id = "";
    private String cdata = "";
    private boolean status = false;
    private String  device_room = "";
    private String  device_mac = "";
    private String  device_ip = "";
    private String  device_data = "";
    private String  device_type = "";
    private String  version = "";
    private String  time ="";

    public DeviceModel(String data) {
        try {
            JSONObject jo = new JSONObject(data);
            if(jo.has(DEVICE_ROOM))
                this.device_room = jo.getString(DEVICE_ROOM);
            if(jo.has(DEVICE_MAC))
                this.device_mac = jo.getString(DEVICE_MAC);
            if(jo.has(DEVICE_IP))
                this.device_ip = jo.getString(DEVICE_IP);
            if(jo.has(DEVICE_DATA))
                this.device_data = jo.getString(DEVICE_DATA);
            if(jo.has(DEVICE_TYPE))
                this.device_type = jo.getString(DEVICE_TYPE);
            if(jo.has(VERSION))
                this.version = jo.getString(VERSION);
            if(jo.has(TIME))
                this.time = jo.getString(TIME);
            if(jo.has(ID))
                this.id = jo.getString(ID);
            if(jo.has(STATUS))
                this.status = jo.getBoolean(STATUS);
            if(jo.has(CDATA))
                this.cdata = jo.getString(CDATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DeviceModel(Cursor cursor) {
        this.device_room =  cursor.getString(cursor.getColumnIndex(DEVICE_ROOM));
        this.device_mac = cursor.getString(cursor.getColumnIndex(DEVICE_MAC));
        this.device_type = cursor.getString(cursor.getColumnIndex(DEVICE_TYPE));
        this.device_ip = cursor.getString(cursor.getColumnIndex(DEVICE_IP));
        this.device_data = cursor.getString(cursor.getColumnIndex(DEVICE_DATA));
        this.version = cursor.getString(cursor.getColumnIndex(VERSION));
        this.time = cursor.getString(cursor.getColumnIndex(TIME));
        this.status = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(STATUS)));
    }

    public JSONObject getAllData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DEVICE_ROOM, this.device_room);
            jsonObject.put(DEVICE_MAC, this.device_mac);
            jsonObject.put(DEVICE_TYPE, this.device_type);
            jsonObject.put(DEVICE_IP, this.device_ip);
            jsonObject.put(DEVICE_DATA, this.device_data);
            jsonObject.put(VERSION, this.version);
            jsonObject.put(TIME, this.time);
            jsonObject.put(STATUS, this.status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCdata() {
        return cdata;
    }

    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDevice_data() {
        return device_data;
    }

    public JSONArray getDevicedataJSONArray() throws JSONException {
        return new JSONArray(device_data);
    }

    public void setDevice_data(String device_data) {
        this.device_data = device_data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDevice_room() {
        return device_room == null ?"Room Not Set":device_room;
    }

    public void setDevice_room(String device_room) {
        this.device_room = device_room;
    }

    public String getDevice_mac() {
        return device_mac;
    }

    public void setDevice_mac(String device_mac) {
        this.device_mac = device_mac;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
