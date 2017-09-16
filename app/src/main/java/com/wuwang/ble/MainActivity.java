package com.wuwang.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.wuwang.ble.tools.BleHelper;
import com.wuwang.ble.tools.ClickUtils;
import com.wuwang.ble.ui.BleAdapter;

public class MainActivity extends AppCompatActivity implements BleHelper.Callback {

    private BleHelper mBle;
    private RecyclerView mList;
    private BleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBle=new BleHelper(getApplicationContext());
        mBle.setCallback(this);
        mList= (RecyclerView) findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mList.setAdapter(mAdapter=new BleAdapter(getApplicationContext()));
        mAdapter.setOnClickListener(mClickListener);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mBtnOpen:
                mBle.open();
                break;
            case R.id.mBtnSearch:
                mAdapter.data.clear();
                mAdapter.notifyDataSetChanged();
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

    @Override
    public void onCall(int type, int ret, Object object) {
        if(type==BleHelper.TYPE_GET_DEVICE&&ret==1){
            mAdapter.data.add((BluetoothDevice) object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private ClickUtils.OnClickListener mClickListener=new ClickUtils.OnClickListener() {
        @Override
        public void onClick(View v, int type, int pos, int child) {
            mBle.connect(mAdapter.data.get(pos).getAddress(),true);
        }
    };

}
