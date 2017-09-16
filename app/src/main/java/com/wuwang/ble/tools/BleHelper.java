package com.wuwang.ble.tools;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.wuwang.ble.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

/**
 * Created by aiya on 2017/9/15.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleHelper {

    public static final int TYPE_GET_DEVICE=0x0010;

    private BluetoothAdapter mBleAdapter;
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

    public void setCallback(Callback callback){
        this.mCallback=callback;
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
            mSearchArray.clear();
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
                        onCall(TYPE_GET_DEVICE,1,device);
                    }else{
                        onCall(TYPE_GET_DEVICE,0,device);
                    }
                }
            });
        }
    }

    public void onCall(int type,int ret,Object object){
        if(mCallback!=null){
            mCallback.onCall(type, ret, object);
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

    public void connect(String address,boolean auto){
        BluetoothDevice conn=null;
        for (BluetoothDevice d:mSearchArray){
            if(d.getAddress().equals(address)){
                conn=d;
                break;
            }
        }
        if(conn!=null){
            conn.connectGatt(mContext,auto,mBluetoothGattCallback);
        }
    }

    private BluetoothGattCallback mBluetoothGattCallback=new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState==STATE_CONNECTED){
                getGattService(gatt.getServices());
            }
        }
    };

    public boolean close(){
        return isSupportBle&&mBleAdapter.disable();
    }

    public interface Callback{
        void onCall(int type,int ret,Object object);
    }

    public void getGattService(List<BluetoothGattService> gattServices){
        if(gattServices==null)return;
        Log.i("wuwang", "获得服务列表");
//        serviceList.clear();
//        characteristicList.clear();
        String uuid;
        String default_service="unknown service";
        String default_characteristic="unknow_characteric";
        Log.d("wuwang","服务个数："+gattServices.size());
        for(BluetoothGattService gService:gattServices){
            Log.i("wuwang", "发现一个服务");
            HashMap<String, String> sData=new HashMap<String, String>();
            uuid=gService.getUuid().toString();
            sData.put("name",SampleGattAttributes.lookup(uuid,default_service));
            sData.put("uuid", uuid);
//            serviceList.add(sData);
            Log.e("wuwang", SampleGattAttributes.lookup(uuid,default_service)+":"+uuid);
            List<BluetoothGattCharacteristic> charaList=gService.getCharacteristics();
            String cuuid;
            for(BluetoothGattCharacteristic chara:charaList){
                HashMap<String, String> cData=new HashMap<String, String>();
                cuuid=chara.getUuid().toString();
                cData.put("cname", SampleGattAttributes.lookup(cuuid, default_characteristic));
                cData.put("cuuid", cuuid);
                cData.put("puuid", uuid);
//                characteristicList.add(cData);
                Log.d("wuwang", "   "+SampleGattAttributes.lookup(cuuid, default_characteristic)+":"+cuuid);
            }
        }

    }

}
