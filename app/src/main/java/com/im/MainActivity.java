package com.im;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    public static TextView tv_message;
    EditText et_connect_id;
    EditText et_receiver_id;
    EditText et_message;
    Button btn_connect;
    Button btn_send;
    boolean isConnect = false;
    boolean isOpenConversation = false;

    BmobIMConversation mBmobIMConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message = (TextView) findViewById(R.id.tv_message);
        et_connect_id = (EditText) findViewById(R.id.et_connect_id);
        et_receiver_id = (EditText) findViewById(R.id.et_receiver_id);
        et_message = (EditText) findViewById(R.id.et_message);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_connect){
            if (TextUtils.isEmpty(et_connect_id.getText().toString())){
                Toast.makeText(this, "请填写连接id", Toast.LENGTH_SHORT).show();
                return;
            }
            btn_connect.setClickable(false);
            BmobIM.connect(et_connect_id.getText().toString(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        isConnect = true;
                        Log.i("TAG","服务器连接成功");
                    }else {
                        Log.i("TAG",e.getMessage()+"  "+e.getErrorCode());
                    }
                }
            });
        }else if (v.getId() == R.id.btn_send){
            if (!isConnect){
                Toast.makeText(this, "未连接状态不能打开会话", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isOpenConversation){
                BmobIMUserInfo info =new BmobIMUserInfo();
                info.setAvatar("填写接收者的头像");
                info.setUserId(et_receiver_id.getText().toString());
                info.setName("填写接收者的名字");
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation c, BmobException e) {
                        if(e==null){
                            isOpenConversation = true;
                            //在此跳转到聊天页面或者直接转化
                            mBmobIMConversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
                            tv_message.append("发送者："+et_message.getText().toString()+"\n");
                            BmobIMTextMessage msg =new BmobIMTextMessage();
                            msg.setContent(et_message.getText().toString());
                            mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                                @Override
                                public void done(BmobIMMessage msg, BmobException e) {
                                    if (e != null) {
                                        et_message.setText("");
                                    }else{
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(MainActivity.this, "开启会话出错", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                BmobIMTextMessage msg =new BmobIMTextMessage();
                msg.setContent(et_message.getText().toString());
                tv_message.append("发送者："+et_message.getText().toString()+"\n");
                mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e != null) {
                            et_message.setText("");
                        }else{
                        }
                    }
                });
            }

            
        }
    }
}
