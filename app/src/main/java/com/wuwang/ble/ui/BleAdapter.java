package com.wuwang.ble.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wuwang.ble.R;
import com.wuwang.ble.tools.ClickUtils;

import java.util.ArrayList;

public class BleAdapter extends RecyclerView.Adapter<BleAdapter.BleHolder> {

    public ArrayList<BluetoothDevice> data;
    private Context context;
    private View.OnClickListener listener;

    public BleAdapter(Context context){
        this.context=context;
        data=new ArrayList<>();
    }

    @Override
    public BleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BleHolder(LayoutInflater.from(context).inflate(R.layout.item_ble,parent,false));
    }

    @Override
    public void onBindViewHolder(BleHolder holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    private View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onClick(v);
            }
        }
    };

    class BleHolder extends RecyclerView.ViewHolder{

        private TextView mTvName;

        public BleHolder(View itemView) {
            super(itemView);
            ClickUtils.addClickTo(itemView,mListener);
            mTvName= (TextView) itemView.findViewById(R.id.mName);
        }

        public void setPosition(int pos){
            ClickUtils.setPos(itemView,pos);
            BluetoothDevice device=data.get(pos);
            mTvName.setText(device.getName()==null?device.getAddress():device.getName());
        }

    }

}
