package com.example.anew.poolmycar.Part2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anew.poolmycar.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import android.app.AlertDialog;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

import static com.example.anew.poolmycar.Helper.FirebaseUrl.firebase;


public class EnterInformation extends AppCompatActivity {

    EditText schoolinstituteinformation;
    Uri downloadUrl;
    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference storageRef;
    FirebaseStorage storage;
    private static final String TAG = "Storage#MainActivity";
    Bitmap bitmap;
    ProgressDialog mprogress,mprogress1;
    String username;

    String Email;
    private Uri filePath;
    private StorageReference storageReference;
    ImageView imageView;
    Spinner spinner;
    String[] array;
    RelativeLayout relativeLayout_ok;
    EditText editText_name,editText_city,editText_contactnumber,editText_age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterinformation);
        Firebase.setAndroidContext(this);
        SetText();

        storageReference = FirebaseStorage.getInstance().getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

            String tempemail=user.getEmail();
            Email=user.getEmail();
            Toast.makeText(getApplicationContext(),user.getEmail(),Toast.LENGTH_LONG).show();
            StringTokenizer stringTokenizer=new StringTokenizer(tempemail,"@");

                username=stringTokenizer.nextToken();

        } else {
            // No user is signed in

        }

        if(Build.VERSION.SDK_INT>=21){
            Window window=this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

    }

    public void SetText(){
        editText_age=(EditText)findViewById(R.id.enter_information_age);
        editText_name=(EditText)findViewById(R.id.enter_information_name);
        editText_city=(EditText)findViewById(R.id.enter_information_city);
        editText_contactnumber=(EditText)findViewById(R.id.enter_information_contactnumber);
        relativeLayout_ok=(RelativeLayout)findViewById(R.id.enter_information_saveok);

         schoolinstituteinformation=(EditText)findViewById(R.id.enter_information_school);

        spinner=(Spinner) findViewById(R.id.enterinformationspinner);

        array=getResources().getStringArray(R.array.mode);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,array);
        spinner.setAdapter(arrayAdapter);

        imageView=(ImageView)findViewById(R.id.enter_information_profile_picture);
        relativeLayout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Insterdata();
            }
        });


    }


    public void Insterdata(){

        final String name = editText_name.getText().toString();
        final String age = editText_age.getText().toString();
        final String city = editText_city.getText().toString();
        final String contactnumber = editText_contactnumber.getText().toString();
        final String schoolinformation = schoolinstituteinformation.getText().toString();
       final String type=spinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter name !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(age)) {
            Toast.makeText(getApplicationContext(), "Enter age!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getApplicationContext(), "Enter city!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contactnumber)) {
            Toast.makeText(getApplicationContext(), "Enter contact number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(schoolinformation)) {
            Toast.makeText(getApplicationContext(), "Enter School/club/Institute information!", Toast.LENGTH_SHORT).show();
            return;
        }

       /* userInformationClass userInformation =new userInformationClass();
        userInformation.setName(name);
        userInformation.setAge(age);
        userInformation.setCity(city);
        userInformation.setContactnumber(contactnumber);
        userInformation.setEmail(Email);*/
        //userInformation.setProfilelink(contactnumber);


        mprogress=new ProgressDialog(EnterInformation.this);
        mprogress.setMessage("Loading..");
        mprogress.setIndeterminate(false);
        mprogress.show();


        ////
        // update
        ////
        Firebase usersRef  = firebase.child("userdetails").child(username);
        Map<String,Object> nickname=new HashMap<String,Object>();

        nickname.put("username",username);
        nickname.put("name",name);
        nickname.put("age",age);
        nickname.put("city",city);
        nickname.put("email",Email);
        nickname.put("contactnumber",contactnumber);
        nickname.put("schoolcollegeinformation",schoolinformation);
        nickname.put("type",type);


        usersRef.updateChildren(nickname, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Toast.makeText(EnterInformation.this, "error", Toast.LENGTH_LONG).show();
                    mprogress.dismiss();
                } else {
                    // Profileimagesession.createprofileSession(stringUri);
                    Toast.makeText(EnterInformation.this, "done", Toast.LENGTH_LONG).show();
                  //  sharedPreference.createDetailsSession(username,name,age,contactnumber,city,Email,schoolinformation,type);

                    mprogress.dismiss();
                   // startActivity(new Intent(getApplicationContext(),Home.class));
                    //finish();

                }

            }
        });

        /////
        //
        /////
    }



    public void singleprofile123(View view) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EnterInformation.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), username+".jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals(username + ".jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    storageRef = storage.getReferenceFromUrl("gs://poolmycar-6395e.appspot.com");
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true);
                    imageView.setImageBitmap(bitmap);

                    StorageReference mountainsRef = storageRef.child(username + ".jpg");


                    StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");


                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data1 = baos.toByteArray();

                    mprogress1 = new ProgressDialog(EnterInformation.this);
                    mprogress1.setMessage("Loading..");
                    mprogress1.setCancelable(false);
                    mprogress1.setIndeterminate(false);
                    mprogress1.show();

                    UploadTask uploadTask = mountainsRef.putBytes(data1);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(EnterInformation.this, "completed", Toast.LENGTH_LONG).show();
                            updatedetails();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                    uploadFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child(username+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            downloadUrl = taskSnapshot.getDownloadUrl();
                          //  Toast.makeText(EnterInformation.this, "completed"+downloadUrl, Toast.LENGTH_LONG).show();
                            updatedetails();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    @Override
    public void onBackPressed () {
       // Intent setIntent = new Intent(this, scroll.class);
        //startActivity(setIntent);
        //finish();
    }



    public void updatedetails(){
       // Firebase myFirebaseRef = new Firebase("https://showursport-b6e37.firebaseio.com/userdetails");
        Firebase usersRef  = firebase.child("userdetails").child(username);
        Map<String,Object> nickname=new HashMap<String,Object>();

        final String stringUri;
        stringUri = downloadUrl.toString();
        nickname.put("image",stringUri);

        usersRef.updateChildren(nickname, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    //mprogress1.dismiss();
                    Toast.makeText(EnterInformation.this, "error", Toast.LENGTH_LONG).show();
                } else {
                   // mprogress1.dismiss();
                    // Profileimagesession.createprofileSession(stringUri);
                    Toast.makeText(EnterInformation.this, "done", Toast.LENGTH_LONG).show();
                    /*Picasso.with(singleprofile.this)
                            .load(stringUri)
                            //.placeholder(R.drawable.profile)   // optional
                            //.error(R.drawable.profile)
                            .fit()

                            .networkPolicy(NetworkPolicy.NO_CACHE)// optional
                            //.resize(400, 300)                        // optional
                            // optional
                            .into(imageView);*/
                  //  setprofile();


                }
            }
        });
    }


    ////
    // set image
    /////
    public void setprofile(){
       // Firebase ref = new Firebase("https://showursport-b6e37.firebaseio.com/userdetails/");
        firebase.child("userdetails").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value = (String) snapshot.child("image").getValue();
                Toast.makeText(EnterInformation.this,value,Toast.LENGTH_SHORT).show();
                Picasso.with(EnterInformation.this)
                        .load(value)
                        .fit()
                        //.memoryPolicy(MemoryPolicy.NO_CACHE)
                        //.networkPolicy(NetworkPolicy.NO_CACHE)// optional
                        .into(imageView);
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

    }

    ////
    ////
    ////





}
