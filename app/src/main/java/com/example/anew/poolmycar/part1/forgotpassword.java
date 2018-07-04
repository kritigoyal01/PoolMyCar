package com.example.anew.poolmycar.part1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.anew.poolmycar.R;


public class forgotpassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_forgotpassword);
    }

    public void btn_back(View view){
        startActivity(new Intent(this,login.class));
        finish();
    }
}
