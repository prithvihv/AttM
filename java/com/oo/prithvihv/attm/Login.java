package com.oo.prithvihv.attm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    //try variables
    final Context context = this;


    //variables for page2
    String Attsubject=null;
        //DS
        ArrayList<Attmodel> data=new ArrayList<Attmodel>();


    //Andriod studio elements
    private Button signIn;
    private Button studentButton;
    private static final String TAG = "MyActivity";
    private TextView statusUpdate;
    private EditText userNameF;
    private EditText passwordF;


    //after login
    private ListView SubjectsListV;
    List<String> subjects=new ArrayList<String>();
    String[] subjectsArr=null;


    //database accessing
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("DSATM/2016/Course/CSE/MATH3/TotalClass");

    //authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is running ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //andriod studio elements selectors
        signIn = (Button) findViewById(R.id.clickfirebase);
        studentButton=(Button) findViewById(R.id.studentButton);
        statusUpdate = (TextView) findViewById(R.id.statusView);
        SubjectsListV=(ListView)findViewById(R.id.subjects);
        userNameF = (EditText) findViewById(R.id.userName);
        passwordF = (EditText) findViewById(R.id.password);
        SubjectsListV.setVisibility(View.INVISIBLE);

        //signIn button
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signInFunction(userNameF.getText().toString(), passwordF.getText().toString());
            }
        });

        //Student button
        studentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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

    private void updateUI(final FirebaseUser user){
        //UI changesV
        signIn.setVisibility(View.GONE);
        studentButton.setVisibility(View.GONE);
        statusUpdate.setVisibility(View.GONE);
        userNameF.setVisibility(View.GONE);
        passwordF.setVisibility(View.GONE);
        SubjectsListV.setVisibility(View.VISIBLE);

        myRef = database.getReference("DSATM/AccessID");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot snapshot:dataSnapshot.getChildren()){
                   if((snapshot.getKey()).equals(user.getUid()))
                   {
                       Log.d(TAG, "onDataChange: if"+ snapshot);
                       for(DataSnapshot snappshot:snapshot.getChildren())
                       {
                           subjects.add(snappshot.getValue().toString());
                       }
                   }
                }
                subjectsArr = new String[subjects.size()];
                subjectsArr = subjects.toArray(subjectsArr);
                gListview();
                SubjectsListV.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Attsubject=String.valueOf(adapterView.getItemAtPosition(i));
                                Log.d(TAG, "onItemClick: " + Attsubject);
                                Intent detailIntent = new Intent(context, ATT.class);
                                detailIntent.putExtra("Subject", Attsubject);
                                startActivity(detailIntent);
                                signIn.setVisibility(View.VISIBLE);
                                studentButton.setVisibility(View.VISIBLE);
                                statusUpdate.setVisibility(View.VISIBLE);
                                userNameF.setVisibility(View.VISIBLE);
                                passwordF.setVisibility(View.VISIBLE);
                                SubjectsListV.setVisibility(View.GONE);
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
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
                            updateUI(user);
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

    private void gListview(){
        ListAdapter Attapt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subjectsArr);
        SubjectsListV.setAdapter(Attapt);
    }

}
