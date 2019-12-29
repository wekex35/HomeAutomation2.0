package com.digiads.akshhomeautomation.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.database.AppDatabase;
import com.digiads.akshhomeautomation.helperActivity.SmartConfig;
import com.digiads.akshhomeautomation.model.DeviceModel;
import com.digiads.akshhomeautomation.utils.DbUtils;

import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_INFO;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_MAC;
import static com.digiads.akshhomeautomation.utils.finalConstants.TITLE;

public class AllDevices extends BaseActivity {
    String TAG = " AllDevices";
    private static final int ADD_DEVICES = 1;
    TextView mac,room,ip,type;
    LinearLayout linearLayout,parent;
    ImageView delete;
    String title;
    boolean addroom = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_devices);
        linearLayout = findViewById(R.id.deviceHolder);
        title = getIntent().getStringExtra(TITLE);
        if (title != null){
            setTitle(title);
            addroom = true;
        }

        showDevices();
    }

    public void smartConfig(View view) {
        Intent intent = new Intent(this, SmartConfig.class);
        startActivityForResult(intent,ADD_DEVICES);
        startActivity(new Intent(this, SmartConfig.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == ADD_DEVICES){
                showDevices();
            }
        }
    }

    private void showDevices() {
        linearLayout.removeAllViews();
        AppDatabase appDatabase = new AppDatabase(this);
        Cursor cursor = appDatabase.getAllDevices();
        if (cursor.moveToFirst()){
            do {
                View view = LayoutInflater.from(this).inflate(R.layout.items_all_devices,null,false);
                parent = view.findViewById(R.id.parent);
                delete = view.findViewById(R.id.delete);

                mac = view.findViewById(R.id.device_id);
                room = view.findViewById(R.id.room_name);
                ip = view.findViewById(R.id.device_ip);
                type = view.findViewById(R.id.device_type);

                final DeviceModel dM = new DeviceModel(cursor);
                mac.setText(dM.getDevice_mac());
                room.setText(dM.getDevice_room());
                ip.setText(dM.getDevice_ip());
                type.setText(dM.getDevice_type());
                Log.d(TAG, "showDevices: "+dM.getDevice_type());
                parent.setOnClickListener(new MyListener(dM.getAllData().toString()));
                delete.setOnClickListener(new MyListener(dM.getDevice_mac()));

                linearLayout.addView(view);
            }while(cursor.moveToNext());
        }
    }


    private class MyListener implements View.OnClickListener {
        String mac;
        public MyListener(String device_mac) {
            this.mac = device_mac;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.parent:
                    if (!addroom) {
                        Intent intent = new Intent(AllDevices.this, ViewDevicesActivity.class);
                        intent.putExtra(DEVICE_INFO, mac);
                        startActivity(intent);

                    }else/* if (addroom)*/ {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(DEVICE_INFO,mac);
                        Log.d(TAG, "updateUI: "+mac);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }

                    break;
                case R.id.delete:
                    new DbUtils(AllDevices.this).removeDevice(mac);
                    showDevices();
                    break;
            }
        }
    }
}
