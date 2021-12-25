package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class articleActivity extends AppCompatActivity {
    private EditText tv11,editText;
    private Button submit_btn;
    private ImageView addPicture,addPicture1;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO = 2;
    private String imagePath,imagePath1;
    private int img_id=0,img_id1=0;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        tv11=findViewById(R.id.tv01);
        editText=findViewById(R.id.editText01);
        submit_btn=findViewById(R.id.submit_btn);
        addPicture=findViewById(R.id.addPicture);
        addPicture1=findViewById(R.id.addPicture1);
        SharedPreferences sharedPreferences=getSharedPreferences("info", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String choice[]=new String[]{"相机","相册"};
                AlertDialog.Builder builder1 =new AlertDialog.Builder(articleActivity.this);
                builder1.setIcon(R.drawable.ic_launcher_foreground);
                builder1.setTitle("选择上传方式:");
                builder1.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                            try{
                                if(outputImage.exists())
                                    outputImage.delete();
                                outputImage.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            if(Build.VERSION.SDK_INT >=24){
                                imageUri = FileProvider.getUriForFile(articleActivity.this,
                                        "com.example.myapplication.fileprovider",outputImage);
                            }else{
                                imageUri = Uri.fromFile(outputImage);
                            }
                            String z= String.valueOf(imageUri);
                            SharedPreferences sharedPreferences=articleActivity.this.getSharedPreferences("info", MODE_PRIVATE);
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
                                            .addFormDataPart("type","2")
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
                                            SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
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
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            startActivityForResult(intent,TAKE_PHOTO);
                        }
                        else
                        {
                            if (ContextCompat.checkSelfPermission(articleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                                ActivityCompat.requestPermissions(articleActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }else {
                                openAlbum();

                            }

                        }
                    }
                });
                builder1.create().show();
                        }
        });
        addPicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String choice[]=new String[]{"相机","相册"};
                AlertDialog.Builder builder1 =new AlertDialog.Builder(articleActivity.this);
                builder1.setIcon(R.drawable.ic_launcher_foreground);
                builder1.setTitle("选择上传方式:");
                builder1.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                            try{
                                if(outputImage.exists())
                                    outputImage.delete();
                                outputImage.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            if(Build.VERSION.SDK_INT >=24){
                                imageUri = FileProvider.getUriForFile(articleActivity.this,
                                        "com.example.myapplication.fileprovider",outputImage);
                            }else{
                                imageUri = Uri.fromFile(outputImage);
                            }
                            SharedPreferences sharedPreferences=articleActivity.this.getSharedPreferences("info", MODE_PRIVATE);
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
                                            .addFormDataPart("type","2")
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
                                            img_id1 = jsonObject1.getInt("img_id");
                                            SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("imd_id", img_id1);
                                            editor.commit();
                                            int a=img_id1;
                                        }
                                    } catch (IOException | JSONException e) {
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
                            if (ContextCompat.checkSelfPermission(articleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                                ActivityCompat.requestPermissions(articleActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }else {
                                openAlbum();

                            }

                        }
                    }
                });
                builder1.create().show();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=tv11.getText().toString();
                String context=editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        if (img_id==0)
                            Toast.makeText(articleActivity.this,"请上传图片",Toast.LENGTH_SHORT).show();
                        else {
                            RequestBody body = RequestBody.create(mediaType, "title="+title+"&content="+context+"&tag=2&img_ids="+img_id);
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/news/release")
                                    .method("POST", body)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String string=response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
                finish();
            }
        });

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        addPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //因为sdk19以后返回的数据不同，所以要根据手机系统版本进行不同的操作
                    //判断手机系统版本
                    if (Build.VERSION.SDK_INT >= 19) {
                        imagePath = handleImageOnKiKai(data);
                    } else {
                        imagePath = handleImageBeforeKiKai(data);
                    }
                    SharedPreferences sharedPreferences =getSharedPreferences("info", MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", "");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("img", imagePath,
                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                    new File(imagePath)))
                                    .addFormDataPart("type", "1")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/img-upload")
                                    .method("POST", body)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                if (response.isSuccessful()) {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    img_id = jsonObject1.getInt("img_id");

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    int a = img_id;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, "{\n    \"img_id\": " + img_id + "\n}");
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/self/avatar-upload")
                                    .method("POST", body)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String responsedata = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private String handleImageOnKiKai(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(articleActivity.this, uri)) {
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
        Cursor cursor = getContentResolver().query(
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
            addPicture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(articleActivity.this, "Load Failed", Toast.LENGTH_LONG).show();
        }
    }
}