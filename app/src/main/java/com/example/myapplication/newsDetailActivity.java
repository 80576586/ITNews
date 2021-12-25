package com.example.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class newsDetailActivity extends AppCompatActivity {
    private TextView titleDetail,authorDetail,follow;
    private RoundedImageView iconDetail;
    private RecyclerView newsDetail,newsPic;
    private ImageView back,like,collect,comment;
    private List<Map<String,Object>> list=new ArrayList<>();
    private List<Map<String,Object>> list1=new ArrayList<>();

    private String author_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        titleDetail=findViewById(R.id.titleDetail);
        newsDetail=findViewById(R.id.newsDetail);
        newsPic=findViewById(R.id.newsPic);
        collect=findViewById(R.id.collect);
        authorDetail=findViewById(R.id.authorDetail);
        back=findViewById(R.id.back1);
        follow=findViewById(R.id.follow);
        comment=findViewById(R.id.comment);
        iconDetail=findViewById(R.id.iconDetail);
        like=findViewById(R.id.like);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String id=bundle.getString("id");
        String avatar=bundle.getString("avatar");
        SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        Glide.with(newsDetailActivity.this).load(avatar).into(iconDetail);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/info/"+id+"/info-full")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonData=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonData.getJSONArray("pics");
                        Map<String,Object> map=new HashMap<>();
                        for (int j=0;j<jsonArray.length();j++)
                        {
                            String pics=jsonArray.getString(j);
                            map.put("pics",pics);
                            list.add(map);
                        }

                        author_id=jsonData.getString("author_id");
                        String title=jsonData.getString("title");
                        String content=jsonData.getString("content");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleDetail.setText(title);
                                authorDetail.setText(author_id);
                                Map<String,Object> map1=new HashMap<>();
                                map1.put("content",content);
                                list1.add(map1);
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(newsDetailActivity.this);
                                newsDetail.setLayoutManager(linearLayoutManager);
                                newsAdapter adapter1=new newsAdapter(newsDetailActivity.this,list1);
                                newsDetail.setAdapter(adapter1);


                                newsPic.setLayoutManager(new GridLayoutManager(newsDetailActivity.this,2,GridLayoutManager.VERTICAL,false));
                                pictureAdapter pictureAdapter=new pictureAdapter(newsDetailActivity.this,list);
                                newsPic.setAdapter(pictureAdapter);
                            }
                        });

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/like")
                        .method("POST", body)
                        .addHeader("Authorization",token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        int code=jsonObject.getInt("code");
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        int isLike=jsonObject1.getInt("isLike");
                        if (code==1000){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                            .build();
                                    MediaType mediaType = MediaType.parse("text/plain");
                                    RequestBody body = RequestBody.create(mediaType, "");
                                    Request request = new Request.Builder()
                                            .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/like")
                                            .method("POST", body)
                                            .addHeader("Authorization",token)
                                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                            .build();
                                    try {
                                        Response response = client.newCall(request).execute();
                                        String responseData=response.body().string();
                                        if (response.isSuccessful()){
                                            JSONObject jsonObject=new JSONObject(responseData);
                                            int code=jsonObject.getInt("code");
                                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                            int isLike=jsonObject1.getInt("isLike");
                                            if (code==1000){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (isLike==1)
                                                            like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.good1));
                                                        else
                                                            like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.good));
                                                    }
                                                });
                                            }
                                        }
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/users/"+author_id+"/operator/follow")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData=response.body().string();
                            if (response.isSuccessful()){
                                JSONObject jsonObject=new JSONObject(responseData);
                                String msg=jsonObject.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(newsDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/star")
                        .method("POST", body)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        int code=jsonObject.getInt("code");
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        int isStar=jsonObject1.getInt("isStar");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();
                                MediaType mediaType = MediaType.parse("text/plain");
                                RequestBody body = RequestBody.create(mediaType, "");
                                Request request = new Request.Builder()
                                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/star")
                                        .method("POST", body)
                                        .addHeader("Authorization", token)
                                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseData=response.body().string();
                                    if (response.isSuccessful()){
                                        JSONObject jsonObject=new JSONObject(responseData);
                                        int code=jsonObject.getInt("code");
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        int isStar=jsonObject1.getInt("isStar");
                                        if (code==1000){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (isStar==1)
                                                        collect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite1));
                                                    else
                                                        collect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite));
                                                }
                                            });
                                        }


                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/like")
                                .method("POST", body)
                                .addHeader("Authorization",token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData=response.body().string();
                            if (response.isSuccessful()){
                                JSONObject jsonObject=new JSONObject(responseData);
                                int code=jsonObject.getInt("code");
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                int isLike=jsonObject1.getInt("isLike");
                                if (code==1000){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isLike==1)
                                                like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.good1));
                                            else
                                                like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.good));
                                        }
                                    });
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/star")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String responseData=response.body().string();
                            if (response.isSuccessful()){
                                JSONObject jsonObject=new JSONObject(responseData);
                                int code=jsonObject.getInt("code");
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                int isStar=jsonObject1.getInt("isStar");
                                if (code==1000){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isStar==1)
                                                collect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite1));
                                            else
                                                collect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.favorite));
                                        }
                                    });
                                }


                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(newsDetailActivity.this,commentActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("id",id);
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}