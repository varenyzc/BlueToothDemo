package com.dgut.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceListAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    private int[] header = {
            R.mipmap.header0,R.mipmap.header1,R.mipmap.header2,
            R.mipmap.header3,R.mipmap.header4,R.mipmap.header5,
            R.mipmap.header6,R.mipmap.header7,R.mipmap.header8,
            R.mipmap.header9,R.mipmap.header10,R.mipmap.header11,
            R.mipmap.header12,R.mipmap.header13,R.mipmap.header14,
            R.mipmap.header15,R.mipmap.header16,R.mipmap.header17,
            R.mipmap.header18,R.mipmap.header19,R.mipmap.header20,
    };

    public DeviceListAdapter(int layoutResId, @Nullable List<BluetoothDevice> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.tvDisplayName,item.getName());
        helper.setText(R.id.tvContent, item.getAddress());
        Random random = new Random();
        int headerCount = random.nextInt(header.length);
        helper.setImageResource(R.id.ivHeader, header[headerCount]);
    }
}
