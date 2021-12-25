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
    private Fragment currentFragment;

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
        replaceFragment(new newsFragment());
        fNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new newsFragment());
            }
        });
        fPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new userFragment());
            }
        });
        fArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new articleFragment());
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        Intent intent2=getIntent();
        token=intent2.getStringExtra("token");
        Bundle bundle1=new Bundle();
        bundle1.putString("token",token);
        fragment.setArguments(bundle1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
        currentFragment=fragment;
    }
    //正确的做法
    private void switchFragment(Fragment targetFragment) {


        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frameLayout, targetFragment)
                    .commit();

        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }

}