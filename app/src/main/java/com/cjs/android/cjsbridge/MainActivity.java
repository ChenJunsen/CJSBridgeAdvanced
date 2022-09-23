package com.cjs.android.cjsbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjs.android.cjsbridge.core.CJSBridge;
import com.cjs.android.cjsbridge.plugin.CJSBUIPlugin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_bridge_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化桥并注册插件
        CJSBridge.getInstance().addJSInterface(new CJSBUIPlugin());

        btn_bridge_test = findViewById(R.id.btn_bridge_test);
        btn_bridge_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btn_bridge_test == v) {
            Intent i = new Intent(MainActivity.this, CJSWebActivityAdvanced.class);
            i.putExtra("title", "测试的网页Advanced");
            i.putExtra("url", "file:///android_asset/html/bridgeTest.html");
//            i.putExtra("url", "http://172.22.206.168:8080/src/html/func3.html");
            startActivity(i);
        }
    }
}
