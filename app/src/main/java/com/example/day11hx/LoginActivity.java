package com.example.day11hx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.day11hx.utils.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 请输入账号
     */
    private EditText mEtName;
    /**
     * 请输入密码
     */
    private EditText mEtPwd;
    /**
     * 登录
     */
    private Button mBtnLogin;
    /**
     * 注册
     */
    private Button mBtnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否登录过，如果登录过并且设置的是自动登录，直接跳转到MainActivity
        boolean loggedInBefore = EMClient.getInstance().isLoggedInBefore();
        if(loggedInBefore){//为真表示登录过，直接跳转
            startActivity(new Intent(this,MainActivity.class));
            finish();//关闭本页面
            return;
        }

        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnRegist = (Button) findViewById(R.id.btn_regist);
        mBtnRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_regist:
                startActivity(new Intent(this,RegitActivity.class));
                break;
        }
    }

    //登录
    private void login() {
        //得到用户名和密码
        final String name = mEtName.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {//非空进行注册
            //调用环信的登录功能，EMCallBack是登录后的回调
            EMClient.getInstance().login(name, pwd, new EMCallBack() {
                @Override
                public void onSuccess() {//登录成功
                    //以下两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            //记录登录的账号，在后面的页面中显示
                            SpUtils.setParam(LoginActivity.this,"name",name);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();//关闭登录界面，无需返回到此页面
                        }
                    });


                }

                @Override
                public void onError(int i, final String s) {//登录失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录失败："+s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }


}
