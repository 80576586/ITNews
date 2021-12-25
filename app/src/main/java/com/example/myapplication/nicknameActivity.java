package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class nicknameActivity extends AppCompatActivity {

    private ImageView back2;
    private Button bc1;
    private EditText etNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        back2=findViewById(R.id.back2);
        bc1=findViewById(R.id.bc1);
        etNickname=findViewById(R.id.etNickname);
        bc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname=etNickname.getText().toString();
                Intent data =new Intent();
                data.putExtra("nickname",nickname);
                setResult(RESULT_OK,data);
//                startActivity(intent);
                finish();
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}