package com.example.uploadimage.sign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uploadimage.R;
import com.example.uploadimage.chats.ChatsPepole;
import com.example.uploadimage.modual.DataUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img ;
    EditText userName , passWord , realName ;
    ProgressBar upload ;
    Button btnUpload ;
    TextView textSignUp ;
    FirebaseAuth auth  ;
    DatabaseReference realTime  ;
    StorageReference storage  ;
    String Name , pass , realNameAdd ;
    final int REQUIST_PICK_IMAGE = 100 ;
    Uri image  ;

    final static String REF_CHILD_MAIN_FIREBASE = "users" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        btnUpload.setOnClickListener(this);
        img.setOnClickListener(this);





    }

    protected  void InitView()
    {
        img = findViewById(R.id.img);
        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.pass);
        btnUpload = findViewById(R.id.upload);
        upload = findViewById(R.id.pro);
        auth = FirebaseAuth.getInstance();
        realTime = FirebaseDatabase.getInstance().getReference() ;
        storage = FirebaseStorage.getInstance().getReference() ;
        textSignUp = findViewById(R.id.long_press_signup);
        realName = findViewById(R.id.name);
        registerForContextMenu(textSignUp);


    }
    protected  void getText()
    {
        if(userName.getText().toString().equals("")||passWord.getText().toString().equals("")||realName.getText().toString().equals("")){
            return; }
        Name = userName.getText().toString().trim();
        pass = passWord.getText().toString().trim();
        realNameAdd = realName.getText().toString().trim() ;
    }

    public static void BuildPopUpContext(String title , Context con)
    {
        AlertDialog.Builder alert ;
        alert  = new  AlertDialog.Builder(con) ;
        alert.setTitle("Alert!");
        alert.setMessage(title);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();;
            }
        });
        alert.setCancelable(false);
        alert.show();
    }
    protected  void PickImage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , REQUIST_PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUIST_PICK_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                image = data.getData();
                img.setImageURI(image);
            }
        }
    }
    protected void UploadImage(Uri imageUri)
    {
        upload.setVisibility(View.VISIBLE);

        final StorageReference realUpload = storage.child("image"+ System.currentTimeMillis()+getResolution(image));
        realUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                upload.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "upload photo", Toast.LENGTH_SHORT).show();
                realUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String uriFireBase =  uri.toString() ;
                        Toast.makeText(MainActivity.this, "download photo", Toast.LENGTH_SHORT).show();
                        auth.createUserWithEmailAndPassword(Name , pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this, "auth success", Toast.LENGTH_SHORT).show();
                                realTime.child(REF_CHILD_MAIN_FIREBASE).child(auth.getCurrentUser().getUid()).setValue(new DataUsers(uriFireBase , realNameAdd , auth.getCurrentUser().getUid()));
                                Toast.makeText(MainActivity.this, "upload all data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this , ChatsPepole.class));

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                upload.setVisibility(View.GONE);
                BuildPopUpContext("Please Try Again Later " , MainActivity.this);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_sign_up , menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.sign_up)
        {
            startActivity(new Intent(MainActivity.this , SignUp.class));
            return true ;
        }
        return super.onContextItemSelected(item);


    }

    private String getResolution(Uri image)
    {
        ContentResolver resolve = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return  typeMap.getExtensionFromMimeType(resolve.getType(image));
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpload)
        {
            if(!userName.getText().toString().equals("")||
                    !passWord.getText().toString().equals("")||
                    !realName.getText().toString().equals("")||
                    image != null)
            {
                getText();
                UploadImage(image);
            }
            else
            {
                BuildPopUpContext("CHECK DATA THAT YOU ENTERD." , MainActivity.this);
            }
        }
        else if (v == img)
        {
            PickImage();
        }
    }
}
