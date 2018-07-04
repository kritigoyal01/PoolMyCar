package com.example.anew.poolmycar.part1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.anew.poolmycar.Part2.EnterInformation;
import com.example.anew.poolmycar.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import static com.example.anew.poolmycar.Helper.FirebaseUrl.firebase;


public class signUp extends AppCompatActivity {

    private EditText inputEmail, inputPassword,confirminputpassword;

     String username;
    private FirebaseAuth auth;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();


        inputEmail = (EditText) findViewById(R.id.emailsignup);
        inputPassword = (EditText) findViewById(R.id.passwordsignup);
        confirminputpassword = (EditText) findViewById(R.id.confirmpasswordsignup);




    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    public void onBackPressed() {
        Intent a = new Intent(new Intent(this,login.class));

        startActivity(a);
        finish();

    }


    //////////
    //// onclick button start
    ////////////


    public void Alreadyaccountonclick(View view){
        startActivity(new Intent(this,login.class));
    }

    public void forgotpasswordonclick(View view){
        //startActivity(new Intent(this,forgotpassword.class));
    }

    public void signup_button(View view){

        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        String confirmpassword = confirminputpassword.getText().toString().trim();

        String [] em=email.split("@");
        username=em[0];

        if(checkEmail(email)){

        }else {
            Toast.makeText(getApplicationContext(), "Enter Valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmpassword)) {

            inputPassword.setError("Enter same password ");
            return;
        }

        mprogress=new ProgressDialog(signUp.this);
        mprogress.setMessage("Loading..");
        mprogress.setIndeterminate(false);
        mprogress.setCancelable(false);
        mprogress.show();
        //progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Toast.makeText(signUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                       // mprogress.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(signUp.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                            inputEmail.setError("User exist !!");

                            mprogress.dismiss();
                        } else {

                            ///
                            /// enter single data
                            /////

                            userInformationClass userInformation=new userInformationClass();
                            userInformation.setUsername(username);

                            firebase.child("userdetails").child(username).setValue(userInformation, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if(firebaseError!=null){
                                        mprogress.dismiss();
                                    }else {
                                        mprogress.dismiss();
                                        Toast.makeText(signUp.this, "hello "+username, Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(),EnterInformation.class));
                                        finish();


                                    }
                                }
                            });

                        }

                        ///
                        ///
                        ////
                    }
                });
    }





    //////////
    //// onclick button end
    ////////////
}
