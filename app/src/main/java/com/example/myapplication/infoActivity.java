package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class infoActivity extends AppCompatActivity {

    private EditText etInfo;
    private ImageView back3;
    private Button bc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        etInfo=findViewById(R.id.etInfo);
        back3=findViewById(R.id.back3);
        bc2=findViewById(R.id.bc2);

        bc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info=etInfo.getText().toString();
                Intent data=new Intent();
                data.putExtra("info",info);
                setResult(RESULT_OK,data);
//                startActivity(intent);
                finish();
            }
        });
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}