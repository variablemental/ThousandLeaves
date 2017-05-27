package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by coder-z on 17-5-26.
 */

public class LoginActivity extends Activity {
    private static final String font_path="fonts/nicai.TTF";

    private static final String PARAM_MODE="mode";
    private static final String PARAM_LOGIN="login";
    private static final String PARAM_REGISTER="register";
    private static final String PARAM_USERNAME="username";
    private static final String PARAM_PASSWORD="password";

    private static final String URL="http://119.23.210.134:8080/wb/member";



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
                         StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                             @Override
                             public void onResponse(String s) {
                                if(s.equals("登录成功！")){
                                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                                }
                             }
                         }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError volleyError) {
                                 Toast.makeText(LoginActivity.this,"网络异常，请稍后再试试(都是服务器的锅！)", Toast.LENGTH_LONG).show();
                             }
                         }){
                           @Override
                           protected Map<String,String> getParams(){
                               Map<String,String> params=new HashMap<String, String>();
                               params.put(PARAM_MODE,PARAM_LOGIN);
                               params.put(PARAM_USERNAME,mUsername.getText().toString());
                               params.put(PARAM_PASSWORD,mPassword.getText().toString());
                               return params;
                           }

                         };
                         RequestQueue queue=LocationApplication.getRequestQueue();
                         queue.add(request);
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
                    StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this,"网络异常，请稍后再试试(都是服务器的锅！)", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params=new HashMap<String, String>();
                            params.put(PARAM_MODE,PARAM_REGISTER);
                            params.put(PARAM_USERNAME,mUsername.getText().toString());
                            params.put(PARAM_PASSWORD,mPassword.getText().toString());
                            return params;
                        }

                    };
                    RequestQueue queue=LocationApplication.getRequestQueue();
                    queue.add(request);

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
