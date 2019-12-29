package com.digiads.akshhomeautomation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.util.DBUtil;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.digiads.akshhomeautomation.Fragement_DT1;
import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.model.DeviceModel;
import com.digiads.akshhomeautomation.utils.DbUtils;

import static com.digiads.akshhomeautomation.utils.Utils.publishcontrol;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_INFO;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_MAC;
import static com.digiads.akshhomeautomation.utils.finalConstants.rgbcodes;

public class ViewDevicesActivity extends BaseActivity implements Fragement_DT1.OnFragmentInteractionListener {
    LinearLayout buttonHolder;
    String did;
    DeviceModel deviceModel;
    private String TAG = "ViewDevicesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_devices);
        buttonHolder = findViewById(R.id.buttonHolder);
        did = getIntent().getStringExtra(DEVICE_INFO);
        Log.d(TAG, "onCreate: ");
        deviceModel = new DeviceModel(did);
        setDevice(deviceModel);
    }

    private void setDevice(DeviceModel dM) {
        Fragment fragment;
        switch (dM.getDevice_type())
        {
            case "1":
                fragment = Fragement_DT1.newInstance(dM.getAllData().toString());
                loadFragment(fragment);
                break;
        }

    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.buttonHolder, fragment).commit();
        }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void remoteCLicked(View view) //using rgbremote Clicked
    {
        Log.d(TAG, "remoteCLicked: "+rgbcodes[Integer.parseInt(view.getTag().toString())]);
        publishcontrol(2,rgbcodes[Integer.parseInt(view.getTag().toString())],deviceModel.getDevice_mac());
    }
}
