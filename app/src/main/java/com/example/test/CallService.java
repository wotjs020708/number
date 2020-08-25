package com.example.test;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CallService extends Service {

    private static final String TAG = "CallService";
    String call_number;
    String meg;
    boolean r;
    ArrayList<String> callnumber = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyMgr.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String incommingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "number " + incommingNumber);
                        if(r) {
                            call_number = incommingNumber;
                            if (call_number != null) {
                                if (callnumber.contains(call_number)) {
                                    audioManager();
                                } else {
                                    HiSendMessage(call_number, meg);
                                    Log.d(TAG, "메세지 전송됨" + meg);
                                    arrycallnumber();
                                }
                            }
                            Log.d(TAG, "callnumber = " + call_number);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if(r) {
                            AudioManager audioManager;

                            audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
                            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) { //진동모드 일때
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//무음 모드

                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                }
            }

        }, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override

    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            r = intent.getBooleanExtra("r",false);
            meg = intent.getStringExtra("m");
            Log.d(TAG, "r" + r);
        }


        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    private  void audioManager( ){
        AudioManager audioManager;

        audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);

        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            Toast.makeText(getApplicationContext(), "진동", Toast.LENGTH_LONG).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//진동
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callnumber.isEmpty() == false) {
            for (int i = 0; i < callnumber.size(); i++) {
                String number;
                number = callnumber.get(i);
                HiSendMessage(number, "현재 전화가 가능한 상태입니다.");
                Log.d(TAG, "전화 왔던 전화번호" + number);
            }
            callnumber.clear();
        }else {
            Toast.makeText(getApplicationContext(), "전화온 사람이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        r = false;
    }

    private void arrycallnumber(){
        if(callnumber.indexOf(call_number) == -1){
            Log.d(TAG, "index" + callnumber.indexOf(call_number));
            callnumber.add(call_number);
        }
    }


    private void HiSendMessage(String phoneNo, String sms )
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
    }


}



