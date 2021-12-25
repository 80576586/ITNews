package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class commentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Map<String,Object>> list2=new ArrayList<>();
    private ImageView back5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.recyclerView);
        back5=findViewById(R.id.back5);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String id=bundle.getString("id");
        SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/info/"+id+"/comment?page=1&size=")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonObject1.getJSONArray("comments");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            String nickname=jsonObject2.getString("nickname");
                            String content=jsonObject2.getString("content");
                            String avatar=jsonObject2.getString("avatar");
                            Map<String,Object> map=new HashMap<>();
                            map.put("nickname",nickname);
                            map.put("content",content);
                            map.put("avatar",avatar);
                            list2.add(map);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(commentActivity.this);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    commentAdapter commentAdapter =new commentAdapter(commentActivity.this,list2);
                                    recyclerView.setAdapter(commentAdapter);
                                }
                            });
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}