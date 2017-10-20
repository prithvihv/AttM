package com.oo.prithvihv.attm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //Andriod studio elements
    private Button signIn;
    private Button studentButton;
    private static final String TAG = "MyActivity";
    private TextView statusUpdate;
    private EditText userNameF;
    private EditText passwordF;

    //database accessing
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CSE/2016/A/MATH3/TotalClass");

    //authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Intent
//    Intent StudentIntent = new Intent(Login.this,Student.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is running ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //andriod studio elements selectors
        signIn = (Button) findViewById(R.id.clickfirebase);
        studentButton=(Button) findViewById(R.id.studentButton);
        statusUpdate = (TextView) findViewById(R.id.statusView);
        final EditText userNameF = (EditText) findViewById(R.id.userName);
        final EditText passwordF = (EditText) findViewById(R.id.password);


        //signIn button
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //returning username and password from field
                signInFunction(userNameF.getText().toString(), passwordF.getText().toString());
            }
        });

        //Student button
        studentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //starting activity
//                startActivity(Student);
                //couldnt make intent work without new class
                studentIntent();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    statusUpdate.setText("signed in ? O.o");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    statusUpdate.setText("not signed in ");
                }
                //..
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void signInFunction(String email, String password) {
        Log.d(TAG, "signIn: email" + email);
        Log.d(TAG, "signIn: password" + password);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }
    public void studentIntent() {
        Intent intent = new Intent(this, Student.class);
        startActivity(intent);
    }

}
