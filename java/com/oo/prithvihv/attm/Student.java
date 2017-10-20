package com.oo.prithvihv.attm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private Button find;
    private EditText rollNo;
    private TextView DS;
    private TextView MATH3;

    private ListView Attlist;
    private ArrayAdapter<String> listAdapter;
    int counter=0;
    //database accessing
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CSE/2016/A");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Attlist=(ListView)findViewById(R.id.Attlist);
        find =(Button)findViewById(R.id.find);
        rollNo =(EditText)findViewById(R.id.rollNo);
        DS=(TextView) findViewById(R.id.DS);
        MATH3=(TextView) findViewById(R.id.MATH3);
        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Read from the database
                counter=0;
                final String roll=rollNo.getText().toString();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for( DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Log.d(TAG, "Valueof  snapshot in loop is is:      " + snapshot.child(roll));
                            switch (counter)
                            {
                                case 0:DS.setText( snapshot.getKey() + " : "+snapshot.child(roll).getValue().toString()); break;
                                case 1:MATH3.setText(snapshot.getKey() + " : "+snapshot.child(roll).getValue().toString());break;
                            }
                            counter++;
                        }
//                            Log.d(TAG, "Valueof  snapshot in loop is is:      " + snapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });


    }
}
