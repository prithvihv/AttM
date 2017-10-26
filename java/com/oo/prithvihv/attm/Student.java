package com.oo.prithvihv.attm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private Button find;
    private EditText rollNo;

    private ListView Attlist;
    private ArrayAdapter<String> listAdapter;


    List<String> Att=new ArrayList<String>();
    String[] Attar;
    int counter=0;
    //database accessing
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("DSATM/2016/CSE/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Attlist=(ListView)findViewById(R.id.Attlist);
        find =(Button)findViewById(R.id.find);
        rollNo =(EditText)findViewById(R.id.rollNo);


        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String roll=rollNo.getText().toString();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange:before loop " + dataSnapshot);
                        for( DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if(snapshot.child(roll)!=null) {
                                Log.d(TAG, "onDataChange: " + snapshot);
                                long attClasses = (long) snapshot.child(roll).getValue();
                                long totalClasses = (long) snapshot.child("TotalClass").getValue();
                                double percentage = ((double) attClasses / totalClasses) * 100;
                                String sample = snapshot.getKey() + " AttendedClasses : " + snapshot.child(roll).getValue().toString() + "       " + " " + percentage + "% ";
                                Log.d(TAG, sample);
                                Att.add(sample);
                            }
                        }
                        Attar = new String[Att.size()];
                        Attar = Att.toArray(Attar);
                        gListview();
//                      Log.d(TAG, "Valueof  snapshot in loop is is:      " + snapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }

                });
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


    }
    private void gListview(){

        ListAdapter Attapt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Attar);
        Attlist.setAdapter(Attapt);
    }


}
