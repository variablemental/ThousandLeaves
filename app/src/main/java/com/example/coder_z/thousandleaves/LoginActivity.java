package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by coder-z on 17-5-26.
 */

public class LoginActivity extends Activity {
    private static final String font_path="fonts/nicai.TTF";
    private EditText mUsername;
    private EditText mPassword;
    private Button   mLogin;
    private Button   mRegister;
    private TextView mQian;
    private TextView mBai;
    private TextView mYe;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mUsername=(EditText)findViewById(R.id.username_text);
        mPassword=(EditText)findViewById(R.id.password_text);
        mLogin=(Button)findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUsername.getText().equals("")||mPassword.getText().equals("")){
                    Dialog dialog=new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("warning").setMessage("帐号或密码不能为空！").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return ;
                                }
                            }).create();
                    return;
                }
                else{
                     if(mUsername.getText().toString().equals("admin")&&mPassword.getText().toString().equals("admin")){
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this,"帐号或密码错误!",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
        mRegister=(Button)findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUsername.getText().toString().isEmpty()||mPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"填好帐号和密码点我直接注册就行了哦！",Toast.LENGTH_LONG).show();
                    return;
                }else{

                }
            }
        });

        mQian=(TextView)findViewById(R.id.chara_qian);
        mBai=(TextView)findViewById(R.id.chara_bai);
        mYe=(TextView)findViewById(R.id.chara_ye);
        Typeface typeface=Typeface.createFromAsset( getAssets(),font_path);
        mQian.setTypeface(typeface);
        mBai.setTypeface(typeface);
        mYe.setTypeface(typeface);
/*        setTypeface(mQian);
        setTypeface(mBai);
        setTypeface(mYe);*/

    }



}
