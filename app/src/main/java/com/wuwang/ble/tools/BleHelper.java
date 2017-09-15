package com.wuwang.ble.tools;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by aiya on 2017/9/15.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleHelper {

    private BluetoothAdapter mBleAdapter;
    private BluetoothLeScanner mLeScanner;
    private Callback mCallback;
    private Context mContext;
    private boolean isSupportBle=false;
    private ArrayList<BluetoothDevice> mSearchArray;

    public BleHelper(Context context){
        this.mContext=context.getApplicationContext();
        BluetoothManager mManager= (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBleAdapter=mManager.getAdapter();
        isSupportBle=check();
        mSearchArray=new ArrayList<>();
    }

    public boolean check(){
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public int getState(){
        if(isSupportBle){
            return mBleAdapter.getState();
        }
        return -1;
    }

    public boolean open(){
        return isSupportBle&&mBleAdapter.enable();
    }

    public void search(){
        if(getState()==BluetoothAdapter.STATE_ON){
            mBleAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    boolean has=false;
                    for (BluetoothDevice d:mSearchArray){
                        if(d.equals(device)){
                            has=true;
                            break;
                        }
                    }
                    if(!has){
                        mSearchArray.add(device);
                        Log.e("wuwang","start getDevice:"+device.toString()+"/"+rssi+"/"+device.getName());
                    }
                }
            });
        }
    }

    public void stopSearch(){
        mBleAdapter.stopLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if(device.getName()!=null){
                    Log.e("wuwang","stop getDevice:"+device.toString()+"/"+rssi+"/"+device.getName());
                }
            }
        });
    }

    public void connect(){

    }

    public boolean close(){
        return isSupportBle&&mBleAdapter.disable();
    }

    public interface Callback{
        void onCall(int type,int ret);
    }

}
