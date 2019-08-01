package com.dgut.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int MSG_CLIENT_READ = 1;
    private static final int CONNECT_SUCCESS = 2;

    private EditText editText;
    private Button sendButton;
    private Button clearButton;
    private TextView receiveText;

    private BluetoothAdapter mBluetoothAdapter;
    private BlueTooth mBlueTooth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        editText = (EditText)findViewById(R.id.edit_text);
        sendButton = (Button)findViewById(R.id.send_button);
        clearButton = (Button)findViewById(R.id.clear_button);
        receiveText = (TextView) findViewById(R.id.textView1);

        mBlueTooth = new BlueTooth(this,mHandler);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(BluetoothActivity.this,"该设备不支持蓝牙功能",Toast.LENGTH_SHORT).show();
        }


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        BluetoothDevice bluetoothDevice = getIntent().getParcelableExtra("device");
        mBlueTooth.connect(bluetoothDevice);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                mBlueTooth.connectedThread.write(inputText);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveText.setText(null);
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_CLIENT_READ:
                    Bundle data =msg.getData();
                    String str = data.getString("BTdata");
                    receiveText.append(str);
                    break;
                case CONNECT_SUCCESS:
                    Bundle dName = msg.getData();
                    String deviceName = dName.getString("device_name");
                    Toast.makeText(getApplicationContext(), "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
