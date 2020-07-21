package com.example.day11hx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class RegitActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 请输入账号
     */
    private EditText mEtName;
    /**
     * 请输入密码
     */
    private EditText mEtPwd;
    /**
     * 注册
     */
    private Button mBtnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regit);
        initView();
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnRegist = (Button) findViewById(R.id.btn_regist);
        mBtnRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_regist:
                regist();
                break;
        }
    }

    //环信注册账号
    private void regist() {
        //得到用户名和密码
        final String name = mEtName.getText().toString().trim();
        final String pwd = mEtPwd.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)){//非空进行注册
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        EMClient.getInstance().createAccount(name,pwd);//注册
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegitActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                RegitActivity.this.finish();//关闭注册页面，回到登录页面
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegitActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }.start();

        }else {
            Toast.makeText(this, "账号密码不可为空", Toast.LENGTH_SHORT).show();
        }
    }
}
