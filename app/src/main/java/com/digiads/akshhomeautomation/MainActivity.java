package com.digiads.akshhomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.digiads.akshhomeautomation.activities.AllDevices;
import com.digiads.akshhomeautomation.activities.BaseActivity;
import com.digiads.akshhomeautomation.database.AppDatabase;
import com.digiads.akshhomeautomation.helperActivity.SmartConfig;
import com.digiads.akshhomeautomation.services.MqttMessageService;

import static com.digiads.akshhomeautomation.utils.Constants.PASSWORD;
import static com.digiads.akshhomeautomation.utils.Constants.USER_ID;
import static com.digiads.akshhomeautomation.utils.Utils.savetoShared;

public class MainActivity extends BaseActivity {

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savetoShared(this).edit().putString(USER_ID,"wekex").apply();
        savetoShared(this).edit().putString(PASSWORD,"Wekex@mqtt").apply();
        startService(new Intent(this, MqttMessageService.class));
      //  startActivity(new Intent(this,HomeActivity.class));
//        appDatabase = new AppDatabase(this);

      //  appDatabase.insertData("he","hee","heee","heeee",1);

    }

    public void smartConfig(View view) {
        startActivity(new Intent(this, SmartConfig.class));
    }

    public void showDevices(View view) {
        startActivity(new Intent(this, AllDevices.class));
    }

    public void showRooms(View view) {
        startActivity(new Intent(this,HomeActivity.class));
    }
}
