package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenActivity extends AppCompatActivity {

    TextView openText;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        lottieAnimationView=findViewById(R.id.lottie);
        openText=findViewById(R.id.openText);
        openText.animate().translationY(-1500).setDuration(3500).setStartDelay(450);
        lottieAnimationView.animate().translationX(2000).setDuration(2000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
                String token=sharedPreferences.getString("token","");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/self/info")
                                .method("GET", null)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData=response.body().string();
                            if (response.isSuccessful())
                            {
                                JSONObject jsonObject=new JSONObject(responseData);
                                int code=jsonObject.getInt("code");
                                Intent intent;
                                if (code==1000){
                                    intent = new Intent(OpenActivity.this, MainActivity.class);

                                }
                                else {
                                    intent = new Intent(OpenActivity.this, logIn.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Intent intent=new Intent(OpenActivity.this,logIn.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).start();


            }
        },4000);

    }
}