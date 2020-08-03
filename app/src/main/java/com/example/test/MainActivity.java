package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.nfc.Tag;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("권한필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(
                        Manifest.permission.READ_PHONE_STATE
                        ,Manifest.permission.READ_CALL_LOG

                )
                .check();

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener(){

            public void onCallStateChanged(int state, String incommingNumber){

                switch (state){
                    case  TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG,"CALL_STATE_RINGING.:" + incommingNumber);
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG,"CAll_STATE_IDE");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG,"CALL_STATE_OFFHOOK");
                        break;

                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);




    }
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };
}