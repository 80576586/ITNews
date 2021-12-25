package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class editActivity extends AppCompatActivity {
    //    private static final int TAKE_PHOTO=1;
//    private static final int RESULT_REQUEST_CODE = 2;
//    private static final int GET_BACKGROUND_FROM_CAPTURE_RESULT = 0;
//    private static final int MY_PERMISSION_REQUEST_CODE =-1 ;
    private ImageView picture, back4;
    private Uri photoUri;
    private TextView editSex, preNickname, editInfo, bc3;
    private RelativeLayout editSexLN, rlNickname;
    private AlertDialog alertDialog = null;
    private LinearLayout editInfoLN;
    private AlertDialog.Builder builder = null;
    private Context context;
    public String nickname, info, sex1, path;
    public int gender, img_id;
    private File outputImage;

    private String[] Permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA
    };

//    private void selectCamera() {
//
//        //创建file对象，用于存储拍照后的图片，这也是拍照成功后的照片路径
//        outputImage = new File(this.getExternalCacheDir(), "camera_photos.jpg");
//        try {
//            //判断文件是否存在，存在删除，不存在创建
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        photoUri = Uri.fromFile(outputImage);
//        photoUri= FileProvider.getUriForFile(editActivity.this,"com.example.myapplication.fileprovider",outputImage);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        startActivityForResult(intent,TAKE_PHOTO);
//    }

//
//    public static final String STR_IMAGE = "image/*";
//    //选择相册
//    private void selectPhoto(){
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STR_IMAGE);
//        startActivityForResult(intent,GET_BACKGROUND_FROM_CAPTURE_RESULT);
//    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if (resultCode == RESULT_OK) {
                    info = data.getStringExtra("info");
                    editInfo.setText(info);

                }
                break;

            case 3:
                if (resultCode == RESULT_OK) {
                    nickname = data.getStringExtra("nickname");
                    preNickname.setText(nickname);
                }
                break;

//            case TAKE_PHOTO:
//                if (resultCode == RESULT_OK)
//                    cropRawPhoto(photoUri);
//                break;
//            case RESULT_REQUEST_CODE:
//                if (resultCode==RESULT_OK){
//                    final String selectPhoto = getRealPathFromUri(this,cropImgUri);
//                    SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
//                    String token=sharedPreferences.getString("token","");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String a=path;
//                            OkHttpClient client = new OkHttpClient().newBuilder()
//                                    .build();
//                            MediaType mediaType = MediaType.parse("text/plain");
//                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                    .addFormDataPart("img",selectPhoto,
//                                            RequestBody.create(MediaType.parse("application/octet-stream"),
//                                                    new File(selectPhoto)))
//                                    .addFormDataPart("type","1")
//                                    .build();
//                            Request request = new Request.Builder()
//                                    .url("http://39.106.195.109/itnews/api/img-upload")
//                                    .method("POST", body)
//                                    .addHeader("Authorization", token)
//                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
//                                    .build();
//                            try {
//                                Response response = client.newCall(request).execute();
//                                String responseData=response.body().string();
//                                if (response.isSuccessful()){
//                                    JSONObject jsonObject=new JSONObject(responseData);
//                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                                    img_id=jsonObject1.getInt("img_id");
//                                }
//                            } catch (IOException | JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            OkHttpClient client = new OkHttpClient().newBuilder()
//                                    .build();
//                            MediaType mediaType = MediaType.parse("application/json");
//                            RequestBody body = RequestBody.create(mediaType, "{\n    \"img_id\": "+img_id+"\n}");
//                            Request request = new Request.Builder()
//                                    .url("http://39.106.195.109/itnews/api/self/avatar-upload")
//                                    .method("POST", body)
//                                    .addHeader("Authorization", token)
//                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
//                                    .addHeader("Content-Type", "application/json")
//                                    .build();
//                            try {
//                                Response response = client.newCall(request).execute();
//                                String responseData=response.body().string();
//                                if (response.isSuccessful()){
//                                    JSONObject jsonObject=new JSONObject(responseData);
//                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                                    String avatar=jsonObject1.getString("avatar");
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Glide.with(editActivity.this)
//                                                    .load(avatar)
//                                                    .into(picture);
//                                        }
//                                    });
//                                }
//                            } catch (IOException | JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//
//
//                    break;
//
//                }
//            case GET_BACKGROUND_FROM_CAPTURE_RESULT :
//                photoUri=data.getData();
//                cropRawPhoto(photoUri);
            default:
                break;
        }
    }

//    private Uri cropImgUri;
//    public void cropRawPhoto(Uri uri) {
//        //创建file文件，用于存储剪裁后的照片
//        File cropImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "crop_image.jpg");
//        String path = cropImage.getAbsolutePath();
//        try {
//            if (cropImage.exists()) {
//                cropImage.delete();
//            }
//            cropImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        cropImgUri = Uri.fromFile(cropImage);
//        cropImgUri= FileProvider.getUriForFile(editActivity.this,"com.example.myapplication.fileprovider",cropImage);
//        Intent intent = new Intent("com.android.camera.action.CROP");
////设置源地址uri
//        intent.setDataAndType(photoUri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("scale", true);
////设置目的地址uri
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
////设置图片格式
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("return-data", false);
//        intent.putExtra("noFaceDetection", true); // no face detection
//        setResult(RESULT_REQUEST_CODE,intent);
//    }
//    public static String getRealPathFromUri(Context context, Uri uri) {
//        if(context == null || uri == null) {
//            return null;
//        }
//        if("file".equalsIgnoreCase(uri.getScheme())) {
//            return getRealPathFromUri_Byfile(context,uri);
//        } else if("content".equalsIgnoreCase(uri.getScheme())) {
//            return getRealPathFromUri_Api11To18(context,uri);
//        }
//
//        return getRealPathFromUri_AboveApi19(context, uri);//没用到
//    }
//    private static String getRealPathFromUri_Byfile(Context context,Uri uri){
//        String uri2Str = uri.toString();
//        String filePath = uri2Str.substring(uri2Str.indexOf(":") + 3);
//        Log.d("1233",filePath);
//        return filePath;
//    }
//    @SuppressLint("NewApi")
//    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
//        String filePath = null;
//        String wholeID = null;
//
//        wholeID = DocumentsContract.getDocumentId(uri);
//
//        // 使用':'分割
//        String id = wholeID.split(":")[1];
//
//        String[] projection = { MediaStore.Images.Media.DATA };
//        String selection = MediaStore.Images.Media._ID + "=?";
//        String[] selectionArgs = { id };
//
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
//                selection, selectionArgs, null);
//        int columnIndex = cursor.getColumnIndex(projection[0]);
//
//        if (cursor.moveToFirst()) {
//            filePath = cursor.getString(columnIndex);
//        }
//        cursor.close();
//        return filePath;
//    }
//
//    /**
//     * //适配api11-api18,根据uri获取图片的绝对路径。
//     * 针对图片URI格式为Uri:: content://media/external/images/media/1028
//     */
//    @SuppressLint("Range")
//    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
//        String filePath = null;
//        String[] projection = { MediaStore.Images.Media.DATA };
//
//        CursorLoader loader = new CursorLoader(context, uri, projection, null,
//                null, null);
//        Cursor cursor = loader.loadInBackground();
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
//            cursor.close();
//        }
//        return filePath;
//    }
//
//    /**
//     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
//     */
//    @SuppressLint("Range")
//    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
//        String filePath = null;
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.getContentResolver().query(uri, projection,
//                null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
//            cursor.close();
//        }
//        return filePath;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        context = editActivity.this;
//        picture = findViewById(R.id.cIcon);
        editInfoLN = findViewById(R.id.editInfoLN);
        editInfo = findViewById(R.id.editInfo);
        bc3 = findViewById(R.id.bc3);
        back4 = findViewById(R.id.back4);
        editSexLN = findViewById(R.id.editSexLN);
        rlNickname = findViewById(R.id.rlNickname);
        preNickname = findViewById(R.id.preNickname);
        editSex = findViewById(R.id.editSex);
        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        editInfoLN.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(editActivity.this, infoActivity.class);
                startActivityForResult(intent2, 4);

//                startActivity(intent2);
            }
        });
        editSexLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] sex = new String[]{"男", "女"};
                alertDialog = null;
                builder = new AlertDialog.Builder(context);
                alertDialog = builder.setTitle("性别")
                        .setItems(sex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editSex.setText(sex[i]);
                                sex1 = sex[i];
                                if (sex1.equals("男"))
                                    gender = 1;
                                if (sex1.equals("女"))
                                    gender = 0;
                            }
                        }).create();
                alertDialog.show();
            }
        });


        bc3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"info\": \"" + info + "\",\n    \"nickname\": \"" + nickname + "\",\n    \"gender\": " + gender + "\n}");
                        //性别还未修改
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/self/info-refresh")
                                .method("POST", body)
                                .addHeader("Authorization", token)
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


                finish();
            }
        });

        back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editActivity.this, nicknameActivity.class);
//                setResult(3,intent);
                startActivityForResult(intent, 3);
            }
        });
    }
}


//        picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                boolean allGranted = checkPermissionAllGranted(Permission);
////                if (allGranted) {
////                    //所有权限已经授权！
////                    doBackup();
////                }
////                // 请求Permission数组中的所有权限，如果已经有了则会忽略。
////                else  ActivityCompat.requestPermissions(editActivity.this,
////                        Permission, 1);
//
//
//            }
////
////            private boolean checkPermissionAllGranted(String[] permissions) {
////                for (String permission : permissions) {
////                    if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
////                        // 只要有一个权限没有被授予, 则直接返回 false
////                        return false;
////                    }
////                }
////                return true;
////            }
////
////
////        });
////
////    }
//
////    private void doBackup() {
////        final String sex[]=new String[]{"相机","相册"};
////        AlertDialog.Builder builder1 =new AlertDialog.Builder(editActivity.this);
////        builder1.setIcon(R.drawable.ic_launcher_foreground);
////        builder1.setTitle("选择上传方式:");
////        builder1.setItems(sex, new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                if(which==0)
////                {
////                    selectCamera();
////                }
////                else
////                {
////                    selectPhoto();
////                }
////            }
////        });
////        builder1.create().show();
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if (requestCode == 1) {
////            boolean allGranted = true;
////            // 判断是否所有的权限都已经授予了
////            for (int grant : grantResults) {
////                if (grant != PackageManager.PERMISSION_GRANTED) {
////                    allGranted = false;
////                    break;
////                }
////            }
////            if (allGranted) {
////                doBackup();
////            } else {
////
////            }
////        }
//
//
//}
