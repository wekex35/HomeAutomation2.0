package com.digiads.akshhomeautomation.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.activities.AllDevices;
import com.digiads.akshhomeautomation.activities.ViewDevicesActivity;
import com.digiads.akshhomeautomation.database.AppDatabase;
import com.digiads.akshhomeautomation.model.DeviceModel;

import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_INFO;
import static com.digiads.akshhomeautomation.utils.finalConstants.DEVICE_ROOM;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    int index;
    public static PlaceholderFragment newInstance(int index, String roomName) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(DEVICE_ROOM, roomName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }
    private static final int ADD_DEVICES = 1;
    TextView mac,room,ip,type;
    LinearLayout parent;
    ImageView delete,status;
    String title;

     LinearLayout deviceHolder;
    String TAG = "PlaceholderFragment";
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.roomaname);
         ImageView roomImage = root.findViewById(R.id.roomImage);
        Log.d(TAG, "onCreateView: "+getArguments().getString(DEVICE_ROOM));
         roomImage.setImageResource(R.drawable.bed_room);

        deviceHolder = root.findViewById(R.id.deviceholder);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        showDevices(getArguments().getString(DEVICE_ROOM));
        return root;
    }
    private void showDevices(String roomname) {
        AppDatabase appDatabase = new AppDatabase(getContext());
        Cursor cursor = appDatabase.getDevicesByRooms(roomname);
        if (cursor.moveToFirst()){
            do {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.items_all_devices,null,false);
                parent = view.findViewById(R.id.parent);
                delete = view.findViewById(R.id.delete);

                mac = view.findViewById(R.id.device_id);
                room = view.findViewById(R.id.room_name);
                ip = view.findViewById(R.id.device_ip);
                type = view.findViewById(R.id.device_type);
                status = view.findViewById(R.id.status);

                final DeviceModel dM = new DeviceModel(cursor);
                mac.setText(dM.getDevice_mac());
                room.setText(dM.getDevice_room());
                ip.setText(dM.getDevice_ip());
                type.setText(dM.getDevice_type());
                Log.d(TAG, "showDevices: "+dM.getDevice_type());

                if (dM.getStatus()){
                    status.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else {
                    status.setBackgroundColor(getResources().getColor(R.color.offwhite));
                }

                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewDevicesActivity.class);
                        intent.putExtra(DEVICE_INFO, dM.getAllData().toString());
                        startActivity(intent);
                    }
                });

                delete.setVisibility(View.GONE);

                deviceHolder.addView(view);
            }while(cursor.moveToNext());
        }
    }

  /*  private void addDevice(int d) {
        deviceHolder.removeAllViews();
        for (int i=0;i<=d;i++){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.buttons,null,false);
            final int pin = i;
            if (i == 2) {
                 view = LayoutInflater.from(getContext()).inflate(R.layout.button_remote,null,false);
                CardView button_card = view.findViewById(R.id.button_card);
                button_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRGBRemote();
                    }
                });

            }
            if (i==8) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.button_dimmer, null, false);
                SeekBar seekBar = view.findViewById(R.id.control);
                final TextView dimpercent = view.findViewById(R.id.dimpercent);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        dimpercent.setText(String.valueOf((int)((progress*100)/255))+"%");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        control(seekBar.getProgress(),pin);
                    }
                });
            }


            TextView textView = view.findViewById(R.id.button_name);
            textView.setText("Button No. "+i);
            Switch button_switch = view.findViewById(R.id.button_switch);

            button_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int state = isChecked?1:0;
                    Log.d("PPPPPP", "onCheckedChanged: "+ state);
//                    control(pin,state);

                }
            });
            deviceHolder.addView(view);
        }
    }

    private void showRGBRemote() {
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rgb_remote,null,false);
        dialog.setContentView(view);
        dialog.show();
    }*/

    

    @Override
    public void onClick(View v) {

    }
}