package com.digiads.akshhomeautomation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.digiads.akshhomeautomation.activities.AllDevices;
import com.digiads.akshhomeautomation.activities.BaseActivity;
import com.digiads.akshhomeautomation.utils.DbUtils;
import com.digiads.akshhomeautomation.utils.finalConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digiads.akshhomeautomation.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements finalConstants {
    private String TAG = "HomeActivity";
    private  final int ADDROOM = 1;
    Dialog dialog;
    DbUtils dbUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbUtils = new DbUtils(this);
        ArrayList arrayList = dbUtils.getAllRooms();
        if (arrayList.size()==0){
            addrooms();
        }else {
            addRoomstoTab();
        }
            FloatingActionButton fab = findViewById(R.id.fab);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);



    }

    private void addRoomstoTab() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void addrooms() {
        Intent intent =new Intent(HomeActivity.this, AllDevices.class);
        intent.putExtra("title","Select Device");
        startActivityForResult(intent,ADDROOM);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void remoteCLicked(View view) //using rgbremote Clicked
    {
        Log.d(TAG, "remoteCLicked: "+rgbcodes[Integer.parseInt(view.getTag().toString())]);
        //publishcontrol(2,rgbcodes[Integer.parseInt(view.getTag().toString())]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Log.d(TAG, requestCode+" onActivityResult: "+data.getStringExtra(DEVICE_INFO));
            if (requestCode == ADDROOM) {
                String deviceInfo = data.getStringExtra(DEVICE_INFO);
                if (deviceInfo != null){
                    Log.d(TAG, "onActivityResult: ");
                dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                View view = LayoutInflater.from(this).inflate(R.layout.dia_add_room, null, false);
                final EditText roomName = view.findViewById(R.id.EDRoomName);
                Button done = view.findViewById(R.id.done);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String roomname = roomName.getText().toString();
                        if (roomName.length() > 3) {
                          long val = dbUtils.addRoom(data.getStringExtra(DEVICE_INFO), roomname);
                          if (val > 0){
                              addRoomstoTab();
                              dialog.dismiss();
                          }else {
                              showToast("Room Not Added");
                                dialog.dismiss();
                          }
                        } else {
                            showToast(getString(R.string.nar));
                        }
                    }

                });


                dialog.setContentView(view);
                dialog.show();
            }
             //   ArrayList arrayList = new DbUtils(this).getAllRooms();
              //  Log.d(TAG, "onActivityResult: "+arrayList.si);
            }
        }
    }


    public void addrooms(View view) {
        addrooms();
    }
}