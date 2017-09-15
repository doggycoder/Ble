package com.wuwang.ble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wuwang.ble.tools.BleHelper;

public class MainActivity extends AppCompatActivity {

    public BleHelper mBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBle=new BleHelper(getApplicationContext());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mBtnOpen:
                mBle.open();
                break;
            case R.id.mBtnSearch:
                mBle.search();
                break;
            case R.id.mBtnStopSearch:
                mBle.stopSearch();
                break;
            case R.id.mBtnClose:
                mBle.close();
                break;
        }
    }

}
