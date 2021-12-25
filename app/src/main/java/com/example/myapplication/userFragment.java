package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class userFragment extends Fragment {
    private Uri imageUri;
    private TextView tvSignOut,editUser;
    private TextView changePassword;
    private TextView sex;
    private RoundedImageView avatar1;
    private ImageView changeIcon;
    private TextView star_num1,nickname1,gender1,follow_num1,info1,username1,news_num;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO = 2;
    private String imagePath;
    private int img_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState);
        tvSignOut=getActivity().findViewById(R.id.signOut);
        editUser=getActivity().findViewById(R.id.editUser);
        changePassword=getActivity().findViewById(R.id.changePassword);
        star_num1=getActivity().findViewById(R.id.star_num);
        nickname1=getActivity().findViewById(R.id.nickname);
        gender1=getActivity().findViewById(R.id.gender);
        info1=getActivity().findViewById(R.id.info1);
        avatar1=getActivity().findViewById(R.id.avatar);
        news_num=getActivity().findViewById(R.id.news_num);
        username1=getActivity().findViewById(R.id.username);
        follow_num1=getActivity().findViewById(R.id.follow_num);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info", MODE_PRIVATE);
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
                    if (response.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseData);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String nickname=jsonObject1.getString("nickname");
                        String info=jsonObject1.getString("info");
                        String gender=jsonObject1.getString("gender");
                        String username=jsonObject1.getString("username");
                        int like_num=jsonObject1.getInt("like_num");
                        int star_num=jsonObject1.getInt("star_num");
                        int follow_num=jsonObject1.getInt("follow_num");
                        String star_num2=Integer.toString(star_num);
                        String follow_num2=Integer.toString(follow_num);
                        String avatar=jsonObject1.getString("avatar");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nickname1.setText(nickname);
                                gender1.setText(gender);
                                star_num1.setText(star_num2);
                                username1.setText(username);
                                follow_num1.setText(follow_num2);
                                info1.setText(info);
                                Glide.with(getActivity())
                                        .load(avatar)
                                        .placeholder(R.drawable.loading)
                                        .into(avatar1);
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
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonObject1.getJSONArray("news");
                        int news_number=jsonArray.length();
                        String news_number1=Integer.toString(news_number);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                news_num.setText(news_number1);
                            }
                        });

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),logIn.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),editActivity.class);
                startActivity(intent);

            }
        });
        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String choice[]=new String[]{"相机","相册"};
                AlertDialog.Builder builder1 =new AlertDialog.Builder(getActivity());
                builder1.setIcon(R.drawable.ic_launcher_foreground);
                builder1.setTitle("选择上传方式:");
                builder1.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            File outputImage = new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                            try{
                                if(outputImage.exists())
                                    outputImage.delete();
                                outputImage.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            if(Build.VERSION.SDK_INT >=24){
                                imageUri = FileProvider.getUriForFile(getActivity(),
                                        "com.example.myapplication.fileprovider",outputImage);
                            }else{
                                imageUri = Uri.fromFile(outputImage);
                            }
                            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info", MODE_PRIVATE);
                            String token=sharedPreferences.getString("token","");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                            .build();
                                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("img",imagePath,
                                                    RequestBody.create(MediaType.parse("application/octet-stream"),
                                                            new File(imagePath)))
                                            .addFormDataPart("type","1")
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("http://39.106.195.109/itnews/api/img-upload")
                                            .method("POST", body)
                                            .addHeader("Authorization", token)
                                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                            .build();
                                    try {
                                        Response response = client.newCall(request).execute();
                                        String responseData=response.body().string();
                                        if (response.isSuccessful()){
                                            JSONObject jsonObject=new JSONObject(responseData);
                                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                            img_id = jsonObject1.getInt("img_id");
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("imd_id", img_id);
                                            editor.commit();
                                            int a=img_id;
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
                                    MediaType mediaType = MediaType.parse("application/json");
                                    RequestBody body = RequestBody.create(mediaType, "{\n    \"img_id\": "+img_id+"\n}");
                                    Request request = new Request.Builder()
                                            .url("http://39.106.195.109/itnews/api/self/avatar-upload")
                                            .method("POST", body)
                                            .addHeader("Authorization", token)
                                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    try {
                                        Response response = client.newCall(request).execute();
                                        String responsedata=response.body().string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            startActivityForResult(intent,TAKE_PHOTO);

                        }
                        else
                        {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }else {
                                openAlbum();

                            }

                        }
                    }
                });
                builder1.create().show();
            }

            private void openAlbum() {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,CHOOSE_PHOTO);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),forgetPassword.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        avatar1.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    //因为sdk19以后返回的数据不同，所以要根据手机系统版本进行不同的操作
                    //判断手机系统版本
                    if(Build.VERSION.SDK_INT >= 19) {
                        imagePath=handleImageOnKiKai(data);
                    }else {
                        imagePath=handleImageBeforeKiKai(data);
                    }
                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("info", MODE_PRIVATE);
                    String token=sharedPreferences.getString("token","");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("img",imagePath,
                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                    new File(imagePath)))
                                    .addFormDataPart("type","1")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/img-upload")
                                    .method("POST", body)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String responseData=response.body().string();
                                if (response.isSuccessful()){
                                    JSONObject jsonObject=new JSONObject(responseData);
                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                    img_id = jsonObject1.getInt("img_id");
                                    OkHttpClient client1 = new OkHttpClient().newBuilder()
                                            .build();
                                    MediaType mediaType1 = MediaType.parse("application/json");
                                    RequestBody body1 = RequestBody.create(mediaType1, "{\n    \"img_id\": "+img_id+"\n}");
                                    Request request1 = new Request.Builder()
                                            .url("http://39.106.195.109/itnews/api/self/avatar-upload")
                                            .method("POST", body1)
                                            .addHeader("Authorization", token)
                                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    try {
                                        Response response1 = client.newCall(request1).execute();
                                        String responsedata=response1.body().string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    int a=img_id;

                }
                break;
            default:
                break;
        }
    }
    //>=19的操作
    @TargetApi(19)
    private String handleImageOnKiKai(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是Document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }else if("content".equalsIgnoreCase(uri.getScheme())) {
                //不是document类型的Uri，普通方法处理
                imagePath = getImagePath(uri, null);
            }
            displayImage(imagePath);

        }
        return imagePath;
    }

    //<19的操作
    private String handleImageBeforeKiKai(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        return imagePath;
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri 和selection获取真正的图片路径
        Cursor cursor = getActivity().getContentResolver().query(
                uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String path) {
        if(path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            avatar1.setImageBitmap(bitmap);
        }else {
            Toast.makeText(getActivity(), "Load Failed", Toast.LENGTH_LONG).show();
        }
    }
}