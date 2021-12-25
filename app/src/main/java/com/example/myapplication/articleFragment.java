package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class articleFragment extends Fragment {
    private RecyclerView recyArticle;
    private ImageView addArticle;
    private String token;
    private TextView textView;
    private List<Map<String,Object>> list =new ArrayList();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyArticle=getActivity().findViewById(R.id.recyArticle);
        addArticle=getActivity().findViewById(R.id.addArticle);
        textView=getActivity().findViewById(R.id.non);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");

        addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),articleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/news-ids")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonData= jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonData.getJSONArray("news");
                        if (jsonArray.length()>0)
                        {
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String title=jsonObject1.getString("title");
                                JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                                String id=jsonObject1.getString("id");
                                String news_pics_set=null ;
                                if (jsonArray1.length()>0)
                                    news_pics_set=jsonArray1.getString(0);
                                JSONObject jsonObject2=jsonObject1.getJSONObject("author");
                                String username=jsonObject2.getString("username");
                                String avatar=jsonObject2.getString("avatar");
                                Map<String,Object> map=new HashMap<>();
                                map.put("id",id);
                                map.put("title",title);
                                map.put("news_pics_set",news_pics_set);
                                map.put("username",username);
                                map.put("avatar",avatar);
                                list.add(map);

                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                                    recyArticle=getActivity().findViewById(R.id.recyArticle);
                                    recyArticle.setLayoutManager(linearLayoutManager);
                                    articleAdapter adapter=new articleAdapter(articleFragment.this,list);
                                    recyArticle.setAdapter(adapter);

                                }
                            });
                        }
                        else textView.setText("还没有发布的新闻");

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}