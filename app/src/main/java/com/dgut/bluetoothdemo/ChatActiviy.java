package com.dgut.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lqr.recyclerview.LQRRecyclerView;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActiviy extends AppCompatActivity {

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
    @Bind(R.id.ibSearch)
    ImageButton ibSearch;
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
    @Bind(R.id.rvMsg)
    LQRRecyclerView rvMsg;
    @Bind(R.id.ivAudio)
    ImageView ivAudio;
    @Bind(R.id.etContent)
    EditText etContent;
    @Bind(R.id.btnAudio)
    Button btnAudio;
    @Bind(R.id.ivEmo)
    ImageView ivEmo;
    @Bind(R.id.ivMore)
    ImageView ivMore;
    @Bind(R.id.btnSend)
    Button btnSend;
    @Bind(R.id.llContent)
    AutoLinearLayout llContent;
    @Bind(R.id.flEmotionView)
    AutoFrameLayout flEmotionView;
    @Bind(R.id.llRoot)
    AutoLinearLayout llRoot;


    private static final int REQUEST_ENABLE_BT = 1;
    private static final int MSG_CLIENT_READ = 1;
    private static final int CONNECT_SUCCESS = 2;

    private BluetoothAdapter mBluetoothAdapter;
    private BlueTooth mBlueTooth;
    private BluetoothDevice bluetoothDevice;

    private List<ChatMessage> list = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    private long exitTime =0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_CLIENT_READ:
                    Bundle data =msg.getData();
                    String str = data.getString("BTdata");
                    //receiveText.append(str);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setType(ChatAdapter.RECEIVE_TEXT);
                    chatMessage.setContent(str);
                    receiveNewMessage(chatMessage);
                    break;
                case CONNECT_SUCCESS:
                    Bundle dName = msg.getData();
                    String deviceName = dName.getString("device_name");
                    //Toast.makeText(getApplicationContext(), "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activiy);
        ButterKnife.bind(this);
        initEvent();
        initView();
    }

    private void initView() {
        setAdapter();
        tvToolbarTitle.setText(bluetoothDevice.getName());
        ibSearch.setVisibility(View.GONE);
        ibToolbarMore.setVisibility(View.VISIBLE);
        ivToolbarNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etContent.getText().toString().trim().length() > 0) {
                    btnSend.setVisibility(View.VISIBLE);
                    ivMore.setVisibility(View.GONE);
                } else {
                    btnSend.setVisibility(View.GONE);
                    ivMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvMoveToBottom();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContent.getText().toString().trim().length() > 0) {
                    String str = etContent.getText().toString();
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setType(ChatAdapter.SEND_TEXT);
                    chatMessage.setContent(str);
                    sendNewMessage(chatMessage);
                    etContent.setText(null);
                    try {
                        mBlueTooth.connectedThread.write(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void setAdapter() {
        if (mChatAdapter == null) {
            mChatAdapter = new ChatAdapter(this, list);
            rvMsg.setAdapter(mChatAdapter);
        } else {
            mChatAdapter.notifyDataSetChangedWrapper();
            if (rvMsg != null) {
                rvMoveToBottom();
            }
        }
    }

    private void rvMoveToBottom() {
        rvMsg.smoothMoveToPosition(list.size() - 1);
    }

    private void initEvent() {
        mBlueTooth = new BlueTooth(this,mHandler);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(ChatActiviy.this,"该设备不支持蓝牙功能",Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        bluetoothDevice = getIntent().getParcelableExtra("device");
        mBlueTooth.connect(bluetoothDevice);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(ChatActiviy.this, "再按一下返回",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void receiveNewMessage(ChatMessage chatMessage) {
        list.add(chatMessage);
        setAdapter();
    }

    private void sendNewMessage(ChatMessage chatMessage) {
        list.add(chatMessage);
        setAdapter();
    }
}
