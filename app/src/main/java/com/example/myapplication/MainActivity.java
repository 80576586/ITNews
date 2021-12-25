package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout fNews,fArticle,fPerson;
    private FrameLayout frameLayout;
    private TextView sex,tvSingOut;
    private Fragment currentFragment=null;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent2=getIntent();
        token=intent2.getStringExtra("token");
        setContentView(R.layout.activity_main);
        fNews=findViewById(R.id.fNews);
        fArticle=findViewById(R.id.fArticle);
        fPerson=findViewById(R.id.fPerson);
        tvSingOut=findViewById(R.id.signOut);

        show("news");
        fNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show( "news" );
            }
        });
        fPerson.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                show("user");
            }
        });
        fArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show( "article");
            }
        });

    }


    private void show(String tag) {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null) {
            switch (tag) {
                case "news":
                    currentFragment = new newsFragment();
                    break;
                case "article":
                    currentFragment = new articleFragment();
                    break;
                case "user":
                    currentFragment = new userFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, currentFragment, tag).commit();
        }
        getSupportFragmentManager().beginTransaction().show(currentFragment).commit();

    }

}