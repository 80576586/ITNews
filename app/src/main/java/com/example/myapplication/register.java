package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class register extends AppCompatActivity {

    private EditText nAccount,nPassword,rnPassword,nMail,registerCode;
    private Button registerButton,getCodeButton;
    private String userAccountRegister,userPassWordRegister,againUserPasswordRegister,codeMailRegister,userMailAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nAccount=findViewById(R.id.nAccount);
        nPassword=findViewById(R.id.nPassword);
        rnPassword=findViewById(R.id.rnPassword);
        nMail=findViewById(R.id.nMail);
        registerCode=findViewById(R.id.registerCode);
        registerButton=findViewById(R.id.registerButton);
        getCodeButton=findViewById(R.id.getCodeButton);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userMailAccount=nMail.getText().toString();
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \""+userMailAccount+"\",\n    \"usage\": 1\n}");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/reglog/code-reg")
                                .method("POST", body)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassWordRegister=nPassword.getText().toString();
                againUserPasswordRegister=rnPassword.getText().toString();
                if (userPassWordRegister.length()<6){
                    Toast.makeText(register.this,"密码不符合规则",Toast.LENGTH_SHORT).show();
                }
                else{
                    if (againUserPasswordRegister.equals(userPassWordRegister)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                userAccountRegister=nAccount.getText().toString();
                                userPassWordRegister=nPassword.getText().toString();
                                againUserPasswordRegister=rnPassword.getText().toString();
                                codeMailRegister=registerCode.getText().toString();
                                userMailAccount=nMail.getText().toString();
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();
                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType, "{\n    \"username\": \""+userAccountRegister+"\",\n    \"password\": \""+userPassWordRegister+"\",\n    \"email\": \""+userMailAccount+"\",\n    \"verify\": \""+codeMailRegister+"\"\n}");
                                Request request = new Request.Builder()
                                        .url("http://39.106.195.109/itnews/api/reglog/all-reg")
                                        .method("POST", body)
                                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                        .addHeader("Content-Type", "application/json")
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseData=response.body().string();
                                    if(response.isSuccessful()) {
                                        JSONObject jsonObject=new JSONObject(responseData);
                                        int code=jsonObject.getInt("code");
                                        if (code==1000) {
                                            Intent intent = new Intent(register.this, logIn.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(register.this,"出现错误", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    }
                                }  catch (IOException | JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else Toast.makeText(register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//    private void saveRegisterInfo(String userName, String userPassWord) {
//        String md5Psw = MD5Utils.md5(userPassWord);//把密码用MD5加密
//        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
//        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        //key,value,如键值对，editor.putString(用户名，密码）;
//        editor.putString(userName, md5Psw);
//        //editor.putBoolean("isLogin",true);
//        editor.commit();
//
//
//    }

//    private boolean isExistUserName(String userName) {
//        boolean has_userName=false;
//        SharedPreferences sharedPreferences=getSharedPreferences("logInInfo",MODE_PRIVATE);
//        String spPsw=sharedPreferences.getString(userName, "");//传入用户名获取密码
//        if(!TextUtils.isEmpty(spPsw)) {
//            has_userName=true;
//        }
//        return has_userName;
//
//    }


}