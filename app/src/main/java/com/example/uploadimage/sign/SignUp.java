package com.example.uploadimage.sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uploadimage.R;
import com.example.uploadimage.chats.ChatsPepole;
import com.example.uploadimage.modual.DataUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText userSign , PassSign ;
    Button bbtnSignUp ;
    String nameSignUp , passSignUp ;
    FirebaseAuth auth ;
    DatabaseReference ref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        IntiView();

    }
    protected void IntiView()
    {
        userSign = findViewById(R.id.username_sign_up);
        PassSign = findViewById(R.id.pass_sign_up);
        bbtnSignUp = findViewById(R.id.sign_up_btn);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
    }
    protected  void getTextSignUp()
    {
        if(userSign.getText().toString().equals("")||PassSign.getText().toString().equals("")){
            return; }
        nameSignUp = userSign.getText().toString().trim();
        passSignUp = PassSign.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        if (!userSign.getText().toString().equals("")||!PassSign.getText().toString().equals(""))
        {
            getTextSignUp();
            auth.signInWithEmailAndPassword(nameSignUp , passSignUp ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    final String id = auth.getCurrentUser().getUid() ;
                    String userName = getData(id);
                    Toast.makeText(SignUp.this, "SUCCESS "+ userName  , Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, "FAIL TO GET BY YOUR USER NAME ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this , ChatsPepole.class));
                    finish();
                }
            });
        }
    }
    protected String getData(final String id)
    {
        final List<DataUsers> list = new ArrayList<>();
        ref.child(MainActivity.REF_CHILD_MAIN_FIREBASE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add(snapshot.child(id).getValue(DataUsers.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       return list.get(0).getName() ;
    }
}
