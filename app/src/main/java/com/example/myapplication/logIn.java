package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class logIn extends AppCompatActivity {
    private EditText account, password;
    private Button logInButton;
    private TextView newRegister, forget;
    private String userName, sPassword, spPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        logInButton = findViewById(R.id.logInButton);
        newRegister = findViewById(R.id.newRegister);
        forget = findViewById(R.id.forget);
        String token = "token";
        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = account.getText().toString();
                sPassword = password.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \"" + userName + "\",\r\n    \"password\": \"" + sPassword + "\"\r\n}");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/reglog/all-log")
                                .method("POST", body)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData = Objects.requireNonNull(response.body()).string();
                            if (response.isSuccessful()) {
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject data = jsonObject.getJSONObject("data");
                                String token = data.getString("token");

                                int code = jsonObject.getInt("code");
                                if (code == 1000) {
                                    Intent intent = new Intent(logIn.this, MainActivity.class);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                    finish();
                                    editor.putString("token", token);
//                                    editor.putString("")
                                    editor.apply();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(logIn.this, "登陆失败", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(logIn.this, "无网络连接", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                });
                thread.start();


            }
        });

        newRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, register.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, forgetPassword.class);
                startActivity(intent);
            }
        });
    }

}