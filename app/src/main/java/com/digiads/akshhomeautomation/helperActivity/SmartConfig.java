package com.digiads.akshhomeautomation.helperActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.activities.BaseActivity;
import com.digiads.akshhomeautomation.model.DeviceModel;
import com.digiads.akshhomeautomation.services.MqttMessageService;
import com.digiads.akshhomeautomation.utils.Constants;
import com.digiads.akshhomeautomation.utils.DbUtils;
import com.digiads.akshhomeautomation.utils.EspUtils;
import com.digiads.akshhomeautomation.views.CircleProgressBar;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspNetUtil;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static com.digiads.akshhomeautomation.utils.Utils.getstatus;
import static com.digiads.akshhomeautomation.utils.Utils.savetoShared;
import static com.digiads.akshhomeautomation.utils.Utils.subscribe;
import static com.digiads.akshhomeautomation.utils.finalConstants.SMARTCONFIGPASSWORD;

public class SmartConfig extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "SmartConfig";

    private static final int REQUEST_PERMISSION = 0x01;

    private EditText mApSsidTV;
    private TextView mApBssidTV;
    private ShowHidePasswordEditText mApPasswordET;
    private EditText mDeviceCountET;
    private RadioGroup mPackageModeGroup;
    private TextView mMessageTV;
    private Button mConfirmBtn;
    private String GdeviceId = ".....";
    String RoomId;

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private EsptouchAsyncTask4 mTask;

    private boolean mReceiverRegistered = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    WifiInfo wifiInfo;
                    if (intent.hasExtra(WifiManager.EXTRA_WIFI_INFO)) {
                        wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    } else {
                        wifiInfo = wifiManager.getConnectionInfo();
                    }
                    onWifiChanged(wifiInfo);
                    break;
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    onLocationChanged();
                    break;
            }
        }
    };

    private boolean mDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_config);
        setTitle("Add Device");
        mApSsidTV = findViewById(R.id.ap_ssid_text);
        mApBssidTV = findViewById(R.id.ap_bssid_text);
        mApPasswordET = findViewById(R.id.ap_password_edit);
        mApPasswordET.setText(savetoShared(SmartConfig.this).getString(SMARTCONFIGPASSWORD,""));
        mDeviceCountET = findViewById(R.id.device_count_edit);
        mDeviceCountET.setText("1");
        mPackageModeGroup = findViewById(R.id.package_mode_group);
        mMessageTV = findViewById(R.id.message);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setOnClickListener(this);


        RoomId = getIntent().getStringExtra("RoomId");
       // TextView versionTV = findViewById(R.id.version_tv);
        //versionTV.setText(IEsptouchTask.ESPTOUCH_VERSION);

        if (isSDKAtLeastP()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
            } else {
                registerBroadcastReceiver();
            }

        } else {
            registerBroadcastReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!mDestroyed) {
                        registerBroadcastReceiver();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDestroyed = true;
        if (mReceiverRegistered) {
            unregisterReceiver(mReceiver);
        }
    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        registerReceiver(mReceiver, filter);
        mReceiverRegistered = true;
    }

    private void onWifiChanged(WifiInfo info) {
        if (info == null) {
            mApSsidTV.setText("");
            mApSsidTV.setTag(null);
            mApBssidTV.setTag("");
            mMessageTV.setText("");
            mConfirmBtn.setEnabled(false);

            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
                new AlertDialog.Builder(SmartConfig.this)
                        .setMessage("Wifi disconnected or changed")
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            mApSsidTV.setText(ssid);
            mApSsidTV.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = EspUtils.getOriginalSsidBytes(info);
            mApSsidTV.setTag(ssidOriginalData);

            String bssid = info.getBSSID();
            mApBssidTV.setText(bssid);

            mConfirmBtn.setEnabled(true);
            mMessageTV.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequence = info.getFrequency();
                if (frequence > 4900 && frequence < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    mMessageTV.setText(R.string.wifi_5g_message);
                }
            }
        }
    }

    private void onLocationChanged() {
        boolean enable;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            enable = false;
        } else {
            boolean locationGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean locationNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            enable = locationGPS || locationNetwork;
        }

        if (!enable) {
            mMessageTV.setText(R.string.location_disable_message);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmBtn) {
            byte[] ssid = mApSsidTV.getTag() == null ? ByteUtil.getBytesByString(mApSsidTV.getText().toString())
                    : (byte[]) mApSsidTV.getTag();
            byte[] password = ByteUtil.getBytesByString(mApPasswordET.getText().toString());
            savetoShared(SmartConfig.this).edit().putString(SMARTCONFIGPASSWORD,mApPasswordET.getText().toString()).apply();
            byte [] bssid = EspNetUtil.parseBssid2bytes(mApBssidTV.getText().toString());
            byte[] deviceCount = mDeviceCountET.getText().toString().getBytes();
            byte[] broadcast = {(byte) (mPackageModeGroup.getCheckedRadioButtonId() == R.id.package_broadcast
                    ? 1 : 0)};

            if(mTask != null) {
                mTask.cancelEsptouch();
            }
            mTask = new EsptouchAsyncTask4(this);
            mTask.execute(ssid, bssid, password, deviceCount, broadcast);
            MyshowDialog();

        }
    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid().toUpperCase() + " is connected to the wifi";
                Toast.makeText(SmartConfig.this, text, Toast.LENGTH_LONG).show();
            }

        });

    }

    private class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
        private WeakReference<SmartConfig> mActivity;

        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running

        private final Object mLock = new Object();
      //  private ProgressDialog mProgressDialog;
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;


        EsptouchAsyncTask4(SmartConfig activity) {
            mActivity = new WeakReference<>(activity);
        }

        public void cancelEsptouch() {
            cancel(true);
           /* if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }*/
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            Activity activity = mActivity.get();
           /* mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Esptouch is configuring, please wait for a moment...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog back pressed canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getText(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (mLock) {
                                if (__IEsptouchTask.DEBUG) {
                                    Log.i(TAG, "progress dialog cancel button canceled");
                                }
                                if (mEsptouchTask != null) {
                                    mEsptouchTask.interrupt();
                                }
                            }
                        }
                    });
            mProgressDialog.show();*/
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            SmartConfig activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(activity.myListener);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            SmartConfig activity = mActivity.get();
            //mProgressDialog.dismiss();
            mResultDialog = new AlertDialog.Builder(activity)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            mResultDialog.setCanceledOnTouchOutside(true);
            if (result == null) {
                mResultDialog.setMessage("Create Esptouch task failed, the esptouch port could be used by other thread");
                mResultDialog.show();
                return;
            }

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    String deviceId = "";
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, \nBssid = ")
                                .append(resultInList.getBssid().toUpperCase())
                                .append(", \nInetAddress = ")
                                .append(resultInList.getInetAddress().getHostAddress())
                                .append("\n");
                        deviceId = resultInList.getBssid().toUpperCase();
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's ")
                                .append(result.size() - count)
                                .append(" more result(s) without showing\n");
                    }
                    isShowDefault = false;
                    GdeviceId = deviceId;
                    //#################### #########################
                    subscribe(GdeviceId);
                  //  getstatus(deviceId,5);
                    dialog.cancel();
                    mResultDialog.dismiss();

                } else {
                    mResultDialog.setMessage("Esptouch fail");
                }
                if (isShowDefault)
                mResultDialog.show();
            }

            activity.mTask = null;
        }
    }
    private boolean isShowDefault = true;
    Dialog dialog;
    public void MyshowDialog(){

         dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = LayoutInflater.from(this).inflate(R.layout.smart_config_progress,null,false);
        dialog.setContentView(view);

        final TextView progressText = view.findViewById(R.id.smart_config_progress);
        TextView ssid = view.findViewById(R.id.connecting_ssid);
        final LinearLayout buttonHoldeer = view.findViewById(R.id.buttonHoldeer);
        final CircleProgressBar circleProgressBar = view.findViewById(R.id.progressBar);
        progressText.setText(R.string.smart_congig_progress+"in progress");

        Button manual = view.findViewById(R.id.manual);
        Button retry = view.findViewById(R.id.retry);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        manual.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(SmartConfig.this, device, Toast.LENGTH_SHORT).show();
                // DtypeViews.addDevicetoRoom(SmartConfig.this,device,RoomId);
            }
        });

        new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {
                circleProgressBar.setText(String.valueOf((100000-millisUntilFinished)/1000));
                circleProgressBar.setProgress(millisUntilFinished/1000);
            }

            public void onFinish() {
                buttonHoldeer.setVisibility(View.VISIBLE);
                progressText.setText(R.string.smart_congig_progress+"in progress");
            //    mTask.cancelEsptouch();
            }
        }.start();



        dialog.show();


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MqttMessageService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void updateUI(Intent intent) {
        String Rdata = intent.getStringExtra("datafromService");
        Log.d(TAG, GdeviceId+" updateUI: "+Rdata);
       // DeviceModel deviceModel = new DeviceModel(Rdata);

        if (Rdata.contains(GdeviceId)){
            new DbUtils(this).addDeviceNewDevice(Rdata);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","Added");
            setResult(Activity.RESULT_OK,returnIntent);
            Log.d(TAG, "updateUI: "+GdeviceId);
            SmartConfig.this.finish();
        }


    }
}
