package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class forgetPassword extends AppCompatActivity {

    EditText fAccount,fPassWord,fAgainPassWord,fMail,fCode;
    Button fGetCode,fChangePassWord;
    String fAccountRegister,fPasswordRegister,fAgainPasswordRegister,fMailRegister,fCodeRegister;
    ImageView back6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        fAccount=findViewById(R.id.fAccount);
        fPassWord=findViewById(R.id.fPassword);
        fAgainPassWord=findViewById(R.id.fAgainPassword);
        fMail=findViewById(R.id.fMail);
        fCode=findViewById(R.id.fCode);
        fGetCode=findViewById(R.id.fGetCode);
        back6=findViewById(R.id.back6);
        fChangePassWord=findViewById(R.id.fChangePassword);
        back6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fMailRegister=fMail.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fMailRegister=fMail.getText().toString();
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \""+fMailRegister+"\",\n    \"usage\": 2\n}");
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
        fChangePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fPasswordRegister=fPassWord.getText().toString();
                fAgainPasswordRegister=fAgainPassWord.getText().toString();
                if (fPasswordRegister.length()<6){
                    Toast.makeText(forgetPassword.this,"密码不符合规则",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (fPasswordRegister.equals(fAgainPasswordRegister)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                fAccountRegister=fAccount.getText().toString();
                                fPasswordRegister=fPassWord.getText().toString();
                                fMailRegister=fMail.getText().toString();
                                fCodeRegister=fCode.getText().toString();
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();
                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"email\": \""+fMailRegister+"\",\r\n    \"verify\": \""+fCodeRegister+"\",\r\n    \"password\": \""+fPasswordRegister+"\"\r\n}");
                                Request request = new Request.Builder()
                                        .url("http://39.106.195.109/itnews/api/reglog/pwd-recall")
                                        .method("POST", body)
                                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                        .addHeader("Content-Type", "application/json")
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseData=response.body().string();
                                    if (response.isSuccessful()){
                                        JSONObject jsonObject=new JSONObject(responseData);
                                        int code=jsonObject.getInt("code");
                                        if (code==1000){
                                            Intent intent=new Intent(forgetPassword.this,logIn.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else
                        Toast.makeText(forgetPassword.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}