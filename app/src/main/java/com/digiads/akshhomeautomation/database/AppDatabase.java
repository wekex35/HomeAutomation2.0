package com.digiads.akshhomeautomation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.digiads.akshhomeautomation.model.DeviceModel;
import com.digiads.akshhomeautomation.utils.finalConstants;


public class AppDatabase extends SQLiteOpenHelper implements finalConstants {
    String TAG = "AppDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppDatabase.db";


    public AppDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + DEVICE_TABLE + "("
                + DID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEVICE_ROOM + " TEXT, "
                + DEVICE_DATA + " TEXT, "
                + DEVICE_IP + " TEXT, "
                + DEVICE_TYPE + " TEXT, "
                + VERSION + " TEXT, "
                + TIME + " TEXT, "
                + STATUS + " TEXT, "
                + DEVICE_MAC + " TEXT  )";

        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertData(DeviceModel dM){
        String mac = dM.getDevice_mac();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(DEVICE_ROOM, roomName);
        values.put(DEVICE_DATA, dM.getDevice_data());
        values.put(DEVICE_TYPE, dM.getDevice_type());
        values.put(DEVICE_IP, dM.getDevice_ip());
        values.put(DEVICE_MAC, dM.getDevice_mac());
        values.put(TIME, dM.getTime());
        values.put(VERSION, dM.getVersion());
        values.put(STATUS, dM.getStatus());

       long newRowId;
        if (existsDeviceinfo(mac)) {
             Log.d(TAG, "insertUserInfo : update");
             newRowId = db.update(DEVICE_TABLE, values, DEVICE_MAC + " =?", new String[]{mac});
        }
        else {
             newRowId = db.insert(DEVICE_TABLE,null,values);
            Log.d(TAG, "insertUserInfo : inserted");
        }

       /* if (existsDeviceinfo(did)){
            //Log.d(TAG, "insertUserInfo: "+afterInsert(did));
        }
*/
        //Insert the new row, returning the primary key value of the new row
       // long newRowId = db.insert(DEVICE_TABLE, null, values);
        Log.d(TAG, "insertData: "+newRowId);
        db.close();
    }

    public boolean existsDeviceinfo(String did){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "SELECT "+DEVICE_MAC
                        +" FROM "+DEVICE_TABLE
                        +" WHERE "+DEVICE_MAC+" =?",
                new String[]{String.valueOf(did)});
        Log.d(TAG, "existsDeviceinfo: "+res.getCount());
        if (res.getCount()>0){
            return true;
        }else
            return false;
    }

    public Cursor getAllDevices() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "SELECT * FROM "+DEVICE_TABLE,null);
        return res;
    }

    public void removeDevices(String did) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DEVICE_TABLE,DEVICE_MAC+" =?",new String[]{did});
        db.close();
        if (existsDeviceinfo(did)){
            Log.d(TAG, "removeDevices: ND");
        }else {
            Log.d(TAG, "removeDevices: D");
        }
    }

    public Cursor getDeviceById(String did) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "SELECT "+DEVICE_MAC
                        +" FROM "+DEVICE_TABLE
                        +" WHERE "+DEVICE_MAC+" =?",
                new String[]{String.valueOf(did)});
        Log.d(TAG, "getDeviceById: "+res.getCount());
        return res;
    }

    public Cursor getAllRooms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "+DEVICE_ROOM+" FROM "+DEVICE_TABLE+" WHERE "+DEVICE_ROOM+" IS NOT NULL",null);
        Log.d(TAG, "getAllRooms: "+cursor.getCount());
        return cursor;
    }

    public long addRoom(String mac, String roomname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEVICE_ROOM, roomname);

        long newRowId;
            Log.d(TAG, "insertUserInfo : update");
            newRowId = db.update(DEVICE_TABLE, values, DEVICE_MAC + " =?", new String[]{mac});



       /* if (existsDeviceinfo(did)){
            //Log.d(TAG, "insertUserInfo: "+afterInsert(did));
        }
*/
        //Insert the new row, returning the primary key value of the new row
        // long newRowId = db.insert(DEVICE_TABLE, null, values);
        Log.d(TAG, "insertData: "+newRowId);

        db.close();
        return newRowId;
    }

    public Cursor getDevicesByRooms(String roomname) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery(
                    "SELECT * FROM "+DEVICE_TABLE
                            +" WHERE "+DEVICE_ROOM+" =?",
                    new String[]{String.valueOf(roomname)});
            Log.d(TAG, "getDeviceById: "+res.getCount());
            return res;
    }

    public void setStatus(String device_mac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, true);
        long newRowId;
        if (existsDeviceinfo(device_mac)) {
            Log.d(TAG, "insertUserInfo : update");
            newRowId = db.update(DEVICE_TABLE, values, DEVICE_MAC + " =?", new String[]{device_mac});
        }
        else {
            newRowId = db.insert(DEVICE_TABLE,null,values);
            Log.d(TAG, "insertUserInfo : inserted");
        }

        Log.d(TAG, "insertData: "+newRowId);
        db.close();
    }

    public void upData(String device_mac, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEVICE_DATA, data);
        long newRowId;
        if (existsDeviceinfo(device_mac)) {
            Log.d(TAG, "insertUserInfo : update");
            newRowId = db.update(DEVICE_TABLE, values, DEVICE_MAC + " =?", new String[]{device_mac});
        }
        else {
            newRowId = db.insert(DEVICE_TABLE,null,values);
            Log.d(TAG, "insertUserInfo : inserted");
        }

        Log.d(TAG, "insertData: "+newRowId);
        db.close();
    }
}
