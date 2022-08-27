package com.vise.bledemo.tcp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public EditText editText;
    public TextView textView_send;
    public TextView textView_receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_main);
        editText = (EditText) findViewById(R.id.send_editText);
        textView_send = (TextView) findViewById(R.id.send_textView);
        textView_receive = (TextView) findViewById(R.id.receive_textView);
        textView_send.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_receive.setMovementMethod(ScrollingMovementMethod.getInstance());
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                textView_receive.setText(textView_receive.getText().toString() + "断开连接" + "\n");
            }
        });
        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                textView_receive.setText(textView_receive.getText().toString() + "连接成功" + "\n");
            }
        });
        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
                textView_receive.setText(textView_receive.getText().toString() +"返回数据"+ receicedMessage + "\n");
            }
        });
    }

    public void sendMessage(View view) {
        String msg = editText.getText().toString();
        textView_send.setText(textView_send.getText().toString() + msg + "\n");
        //00 00 00 00 00 06 01 03 0B 3E 00 01
        String mm="00000000000601030B3E0001";
        textView_send.setText(mm);
        //todo 将十六进制字符数组转换为字节数组
        TaskCenter.sharedCenter().send(HexUtil.decodeHex(mm.toCharArray()));
    }

    public void connect(View view) {
        TaskCenter.sharedCenter().connect("192.168.1.181",503);
    }

    public void disconnect(View view) {
        TaskCenter.sharedCenter().disconnect();
    }

    public void clear1(View view) {
        textView_send.setText("");
    }

    public void clear2(View view) {
        textView_receive.setText("");
    }

}
