package com.example.anew.poolmycar.Part4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.anew.poolmycar.R;

import java.util.Random;

public class SMSConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsconfirmation);

        try {
            Random rand = new Random();
            int n = rand.nextInt(99999) + 100000;
            String messageto = Integer.toString(n);
            String recmob = "9871834229";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(recmob, null, messageto, null, null);
            Toast.makeText(getApplicationContext(), "SMS SENT!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed,please try again later !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


}
