package com.example.anew.poolmycar.part1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anew.poolmycar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.anew.poolmycar.Helper.FirebaseUrl.firebase;


public class login extends AppCompatActivity {

   private FirebaseAuth auth;
    Firebase ref;
    ProgressDialog mprogress;
String username;
    TextView emailtext,passwordtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);

        emailtext=(TextView)findViewById(R.id.loginemail);
        passwordtext=(TextView)findViewById(R.id.loginpassword);
        // ref=new Firebase("https://showursport-b6e37.firebaseio.com/userdetails");


    }


    //////////
    //// onclick button start
    ////////////
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    public void donthaveaccountonclick(View view){
        startActivity(new Intent(this,signUp.class));
    }

    public void forgotpasswordonclick(View view){
       // startActivity(new Intent(this,forgotpassword.class));
    }

    public void login_button(View view){
        final String email = emailtext.getText().toString();
        final String password = passwordtext.getText().toString();

        String [] em=email.split("@");
        username=em[0];
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        mprogress=new ProgressDialog(login.this);
        mprogress.setMessage("Loading..");
        mprogress.setIndeterminate(false);
        mprogress.setCancelable(false);
        mprogress.show();
        // progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        // progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                mprogress.dismiss();
                                passwordtext.setError("minimum 6 digits");
                            } else {
                                mprogress.dismiss();
                                Toast.makeText(login.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        } else {


                            ////
                            //  fetch single data
                            /////
                          firebase.child("userdetails").child(username).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //Getting the data from snapshot


                                    String name=(String) dataSnapshot.child("name").getValue();
                                    String age=(String) dataSnapshot.child("age").getValue();
                                    String city=(String) dataSnapshot.child("city").getValue();
                                    String contactnumber=(String) dataSnapshot.child("contactnumber").getValue();
                                    String Email=(String) dataSnapshot.child("email").getValue();
                                    String schoolinformation=(String) dataSnapshot.child("schoolcollegeinformation").getValue();
                                    String type=(String) dataSnapshot.child("type").getValue();

                                    mprogress.dismiss();

                                    Toast.makeText(getApplicationContext(),username+name,Toast.LENGTH_LONG).show();
                                   // startActivity(new Intent(getApplicationContext(),Home.class));
                                    //finish();

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }



                        /////
                        ///

                        ///

                    }


                });

    }



    //////////
    //// onclick button end
    ////////////
}

