package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class megActivity extends AppCompatActivity {
    Button btn_inp;
    EditText ed_inp;
    TextView text_meg;
    String shard = " file";
    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meg);

        btn_inp = (Button) findViewById(R.id.btn_inp);
        ed_inp = (EditText) findViewById(R.id.ed_inp);
        text_meg = (TextView) findViewById(R.id.text_meg);
        SharedPreferences sharedPreferences = getSharedPreferences(shard, 0);
        String value = sharedPreferences.getString("meg", "");
        text_meg.setText(value);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    SharedPreferences sharedPreferences = getSharedPreferences(shard, 0);
    SharedPreferences.Editor editor =sharedPreferences.edit();
    String value = text_meg.getText().toString();
    editor.putString("meg",value);
    editor.commit();

    }

    public void onBtninp(View view) {

        str = ed_inp.getText().toString();
        text_meg.setText(str);
        ed_inp.setText("");
    }
}