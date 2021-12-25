package com.example.myapplication;

import android.content.Context;
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

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

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


public class newsFragment extends Fragment {
    private RecyclerView recyclerView;
    private String token;
    private List<Map<String,Object>> list =new ArrayList();
    public int page=1;
    private SmartRefreshLayout refreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView= getActivity().findViewById(R.id.recy);
        refreshLayout=  getActivity().findViewById(R.id.refreshLayout);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");
        String finalToken = token;

        new Thread(new Runnable() {
            @Override
            public void run() {
                page=1;
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+page+"&size=8")
                        .method("GET", null)
                        .addHeader("Authorization", finalToken)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonData= jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonData.getJSONArray("news");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String title=jsonObject1.getString("title");
                            JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
//                            Log.i("lcx",String.valueOf(i));
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
                                recyclerView= (RecyclerView) getActivity().findViewById(R.id.recy);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                Adapter adapter=new Adapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);

                            }
                        });
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshALL();
                refreshLayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadRefresh();
                refreshLayout.finishLoadMore(2000);
            }
        });
    }

    private void refreshALL(){
        list.clear();
        page=1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+page+"&size=8")
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
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String title=jsonObject1.getString("title");
                            JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                            String id=jsonObject1.getString("id");
                            String news_pics_set = null;
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
                                recyclerView= (RecyclerView) getActivity().findViewById(R.id.recy);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                Adapter adapter=new Adapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);

                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+page+"&size=8")
                        .method("GET", null)
                        .addHeader("Authorization",token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonData= jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonData.getJSONArray("news");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String title=jsonObject1.getString("title");
                            JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                            String id=jsonObject1.getString("id");
                            String news_pics_set = null;
                            int j=0;
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
                                recyclerView=  getActivity().findViewById(R.id.recy);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                Adapter adapter=new Adapter(getActivity(),list);
                                recyclerView.setAdapter(adapter);

                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        page++;
    }

}