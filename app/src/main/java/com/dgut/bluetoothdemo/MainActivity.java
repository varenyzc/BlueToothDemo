package com.dgut.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqr.recyclerview.LQRRecyclerView;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "MainActivity";
    @Bind(R.id.ivToolbarNavigation)
    ImageView ivToolbarNavigation;
    @Bind(R.id.vToolbarDivision)
    View vToolbarDivision;
    @Bind(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @Bind(R.id.tvToolbarSubTitle)
    TextView tvToolbarSubTitle;
    @Bind(R.id.llToolbarTitle)
    AutoLinearLayout llToolbarTitle;
    @Bind(R.id.ibAddMenu)
    ImageButton ibAddMenu;
    @Bind(R.id.tvToolbarAddFriend)
    TextView tvToolbarAddFriend;
    @Bind(R.id.llToolbarAddFriend)
    AutoLinearLayout llToolbarAddFriend;
    @Bind(R.id.etSearchContent)
    EditText etSearchContent;
    @Bind(R.id.llToolbarSearch)
    AutoLinearLayout llToolbarSearch;
    @Bind(R.id.btnToolbarSend)
    Button btnToolbarSend;
    @Bind(R.id.ibToolbarMore)
    ImageButton ibToolbarMore;
    @Bind(R.id.flToolbar)
    AutoFrameLayout flToolbar;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.tvMessageNormal)
    TextView tvMessageNormal;
    @Bind(R.id.tvMessagePress)
    TextView tvMessagePress;
    @Bind(R.id.rlMessage)
    AutoRelativeLayout rlMessage;
    @Bind(R.id.tvMessageCount)
    TextView tvMessageCount;
    @Bind(R.id.tvMessageTextNormal)
    TextView tvMessageTextNormal;
    @Bind(R.id.tvMessageTextPress)
    TextView tvMessageTextPress;
    @Bind(R.id.llMessage)
    AutoLinearLayout llMessage;
    @Bind(R.id.tvContactsNormal)
    TextView tvContactsNormal;
    @Bind(R.id.tvContactsPress)
    TextView tvContactsPress;
    @Bind(R.id.rlContacts)
    AutoRelativeLayout rlContacts;
    @Bind(R.id.tvContactCount)
    TextView tvContactCount;
    @Bind(R.id.tvContactRedDot)
    TextView tvContactRedDot;
    @Bind(R.id.tvContactsTextNormal)
    TextView tvContactsTextNormal;
    @Bind(R.id.tvContactsTextPress)
    TextView tvContactsTextPress;
    @Bind(R.id.llContacts)
    AutoLinearLayout llContacts;
    @Bind(R.id.tvDiscoveryNormal)
    TextView tvDiscoveryNormal;
    @Bind(R.id.tvDiscoveryPress)
    TextView tvDiscoveryPress;
    @Bind(R.id.rlDiscovery)
    AutoRelativeLayout rlDiscovery;
    @Bind(R.id.tvDiscoveryCount)
    TextView tvDiscoveryCount;
    @Bind(R.id.tvDiscoveryTextNormal)
    TextView tvDiscoveryTextNormal;
    @Bind(R.id.tvDiscoveryTextPress)
    TextView tvDiscoveryTextPress;
    @Bind(R.id.llDiscovery)
    AutoLinearLayout llDiscovery;
    @Bind(R.id.tvMeNormal)
    TextView tvMeNormal;
    @Bind(R.id.tvMePress)
    TextView tvMePress;
    @Bind(R.id.rlMe)
    AutoRelativeLayout rlMe;
    @Bind(R.id.tvMeCount)
    TextView tvMeCount;
    @Bind(R.id.tvMeTextNormal)
    TextView tvMeTextNormal;
    @Bind(R.id.tvMeTextPress)
    TextView tvMeTextPress;
    @Bind(R.id.llMe)
    AutoLinearLayout llMe;
    @Bind(R.id.llButtom)
    AutoLinearLayout llButtom;
    @Bind(R.id.activity_main)
    AutoLinearLayout activityMain;
    @Bind(R.id.rv_device)
    RecyclerView rvDevice;

    private BluetoothAdapter mBluetoothAdapter;

    private BroadcastReceiver mDiscoveryReceiver;
    private List<BluetoothDevice> datasource;

    private DeviceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        datasource = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        addDiscoveryReceiver();
        discovery();
        datasource = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                datasource.add(device);
            }
        }
        mAdapter = new DeviceListAdapter(R.layout.item_device, datasource);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BluetoothDevice bluetoothDevice = datasource.get(position);
                Intent intent = new Intent(MainActivity.this, ChatActiviy.class);
                intent.putExtra("device", bluetoothDevice);
                startActivity(intent);
            }
        });
        rvDevice.setLayoutManager(linearLayoutManager);
        rvDevice.setAdapter(mAdapter);
    }

    private void initView() {
        tvMessagePress.getBackground().setAlpha(255);
        tvMessageTextPress.setTextColor(Color.argb(255, 69, 192, 26));
        tvMessageNormal.getBackground().setAlpha(255);
        tvContactsNormal.getBackground().setAlpha(255);
        tvDiscoveryNormal.getBackground().setAlpha(255);
        tvMeNormal.getBackground().setAlpha(255);
        tvContactsPress.getBackground().setAlpha(1);
        tvDiscoveryPress.getBackground().setAlpha(1);
        tvMePress.getBackground().setAlpha(1);
        tvMessageTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        tvContactsTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        tvDiscoveryTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        tvMeTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        tvContactsTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        tvDiscoveryTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        tvMeTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        ibAddMenu.setVisibility(View.VISIBLE);
        tvToolbarTitle.setPadding(40, 0, 0, 0);
        vToolbarDivision.setVisibility(View.GONE);
        ivToolbarNavigation.setVisibility(View.GONE);

    }

    private void discovery() {
        datasource.clear();
        findBoundDevices();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeDiscoveryReceiver();
    }

    private void addDiscoveryReceiver() {
        // Create a BroadcastReceiver for ACTION_FOUND
        mDiscoveryReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    datasource.add(device);
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mDiscoveryReceiver, filter); // Don't forget to unregister during onDestroy

    }

    private void removeDiscoveryReceiver() {
        if (mDiscoveryReceiver != null) {
            unregisterReceiver(mDiscoveryReceiver);
            mDiscoveryReceiver = null;
        }
    }

    private void findBoundDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                datasource.add(device);
            }
        }
    }

}
