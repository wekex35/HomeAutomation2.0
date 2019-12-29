package com.digiads.akshhomeautomation.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.digiads.akshhomeautomation.database.AppDatabase;
import com.digiads.akshhomeautomation.model.DeviceModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_ROOM;

public class DbUtils {
    AppDatabase appDatabase;
    public DbUtils(Context context) {
        this.appDatabase = new AppDatabase(context);
    }

    public void addDeviceNewDevice(String s){
        appDatabase.insertData(new DeviceModel(s));
    }

    public DeviceModel getDeviceByID(String did) {
        Cursor cursor = appDatabase.getDeviceById(did);
        if (cursor.moveToNext())
        return new DeviceModel(cursor);
        else
            return null;
    }

    public void removeDevice(String did) {
        appDatabase.removeDevices(did);
    }

    public ArrayList<String> getAllRooms() {
        ArrayList<String> arrayList = new ArrayList<>();
       Cursor cursor = appDatabase.getAllRooms();
       if (cursor.moveToFirst()){
           do {
               arrayList.add(cursor.getString(cursor.getColumnIndex(DEVICE_ROOM)));
           }while (cursor.moveToNext());
       }

       return arrayList;
    }

    public long addRoom(String data, String roomname) {

     return appDatabase.addRoom(new DeviceModel(data).getDevice_mac(),roomname);

    }

    public void upDateDatabase(String msg) {
        DeviceModel dM = new DeviceModel(msg);
       // updateIt.myUpdate(dM);
        if (!dM.getStatus()){
             appDatabase.setStatus(dM.getDevice_mac());
        }else if(!dM.getCdata().equals("")){
             updateDataByCurrentData(dM);
        }else{
            dM.setStatus(true);
            appDatabase.insertData(dM);
        }
    }
  /*  public interface updateIt{
        public static void myUpdate(DeviceModel deviceModel);
    }
*/
    private void updateDataByCurrentData(DeviceModel dM) {
        DeviceModel DM = new DeviceModel(appDatabase.getDeviceById(dM.getDevice_mac()));
        try {
            JSONArray jsonArray = DM.getDevicedataJSONArray();
            jsonArray.put(Integer.parseInt(dM.getId()),dM.getCdata());
            appDatabase.upData(dM.getDevice_mac(),jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
