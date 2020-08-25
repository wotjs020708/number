package com.example.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ToggleButton tb_on_off;
    int mAutio;
    String shard = " file";
    private static final String TAG = "MainActivity";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb_on_off = (ToggleButton) findViewById(R.id.tb_on_off);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button bt_meg = (Button) findViewById(R.id.bt_meg) ;
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Toast.makeText(getApplicationContext(), "권한을 허용해주세요", Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        }

        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("권한필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(
                        Manifest.permission.READ_PHONE_STATE
                        ,Manifest.permission.READ_CALL_LOG
                        ,Manifest.permission.SEND_SMS

                )
                .check();

        tb_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    imageView.setImageResource(R.drawable.ic_baseline_blur_off_24);
                    AudioManager audioManager;

                    audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);

                    if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {//벨소림 모드일때
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//무음 모드
                        mAutio = 1 ;
                    }
                    else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) { //진동모드 일때
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//무음 모드
                        mAutio = 2;
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences(shard, 0);
                    String value = sharedPreferences.getString("meg", "");
                    Intent intent= new Intent(MainActivity.this, CallService.class);
                    intent.putExtra("m",value);
                    Log.d(TAG, "m" + value);
                    intent.putExtra("r",true);
                    startService(intent);

                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_blur_on_24);
                    AudioManager audioManager;

                    audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
                    Intent intent = new Intent(MainActivity.this, CallService.class);
                    stopService(intent);
                    if(mAutio == 1 ) {// 처음 어플을 키기 전 상태 표시

                        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {//무음 모드일때
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); // 벨소림모드

                        }
                    }
                    else if(mAutio == 2 ) {// 처음 어플을 키기 전 상태 표시

                        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {//무음 모드일때
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);// 진동

                        }
                    }


                }
            }
        });




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


    public void onMegove(View view) {
        Intent intent = new Intent(this,megActivity.class);
        startActivity(intent);
    }
}