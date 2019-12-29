package com.digiads.akshhomeautomation;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

import com.digiads.akshhomeautomation.activities.AllDevices;
import com.digiads.akshhomeautomation.model.DeviceModel;

import org.json.JSONArray;
import org.json.JSONException;

import static com.digiads.akshhomeautomation.utils.Utils.publishcontrol;

public class Fragement_DT1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragement_DT1() {
    }
    public static Fragement_DT1 newInstance(String param1) {
        Fragement_DT1 fragment = new Fragement_DT1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    DeviceModel dM ;
    String mac;
    private String TAG = "Fragement_DT1";
    private static final int ADD_DEVICES = 1;
    TextView mactv,roomtv,iptv,typetv;
    LinearLayout linearLayout,parent;
    ImageView delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dM = new DeviceModel(getArguments().getString(ARG_PARAM1));
            Log.d(TAG, "onCreate: "+dM.getDevice_mac());
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    LinearLayout deviceHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragement__dt1, container, false);
        deviceHolder = view.findViewById(R.id.deviceHolder);
        mac = dM.getDevice_mac();
        try {
            addDevice();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        deviceInfo(view);
        return view;
    }

    private void deviceInfo(View view) {
        parent = view.findViewById(R.id.parent);
        delete = view.findViewById(R.id.delete);

        mactv = view.findViewById(R.id.device_id);
        roomtv = view.findViewById(R.id.room_name);
        iptv = view.findViewById(R.id.device_ip);
        typetv = view.findViewById(R.id.device_type);


        mactv.setText(dM.getDevice_mac());
        roomtv.setText(dM.getDevice_room());
        iptv.setText(dM.getDevice_ip());
        typetv.setText(dM.getDevice_type());
        Log.d(TAG, "showDevices: "+dM.getDevice_type());
        delete.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addDevice() throws JSONException {
        deviceHolder.removeAllViews();
        JSONArray jsonArray = dM.getDevicedataJSONArray();
        Log.d(TAG, "addDevice: "+jsonArray.toString());
        for (int i=0;i<=jsonArray.length();i++){
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
                        publishcontrol(pin,seekBar.getProgress(),mac);
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
                    publishcontrol(pin,state,mac);

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
    }
}
