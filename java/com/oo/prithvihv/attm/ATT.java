package com.oo.prithvihv.attm;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class ATT extends AppCompatActivity {

    private static final String TAG = "MyActivity3";

    //firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    //DS
    ArrayList<Attmodel> data=new ArrayList<Attmodel>();
    String Subject="";
    long Att=0;

    //View
    private ListView AttListV;
    private Button commit;

    //authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att);
        Subject=this.getIntent().getExtras().getString("Subject");
        AttListV=(ListView)findViewById(R.id.Att);
        commit=(Button)findViewById(R.id.commit);
        commit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i=0;i<data.size();i++)
                {
                    if(data.get(i).checked)
                    myRef.child(data.get(i).roll).setValue(data.get(i).classes+1);
                }
                FirebaseAuth.getInstance().signOut();
                finish();

            }
        });
        //FIRST GET DATA
        myRef = database.getReference("DSATM/2016/CSE/"+Subject+"/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String rollnumber=snapshot.getKey();
                    long attClasses = (long) snapshot.getValue();
                    data.add(new Attmodel(rollnumber,true,attClasses));
                }
                DisplayLIST();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });

    }
    private void DisplayLIST(){
        //DISPLAY
        final AdapterClass listdata=new AdapterClass(data,getApplicationContext());
        AttListV.setAdapter(listdata);
        //onCLICK
        AttListV.setOnItemClickListener(

                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Attmodel dataModel= data.get(position);
                        dataModel.checked = !dataModel.checked;
                        listdata.notifyDataSetChanged();
                        Log.d(TAG, "onItemClick: "+ dataModel.checked + "selected item is " + dataModel.roll);
                    }
                }
        );
    }
    private void setvalue(long num){

    }
//    private long getNUM(){
//        final long[] num=null;
//        myRef.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        long roll = (long) dataSnapshot.getValue();
//                        Log.d(TAG, "onDataChange: " +roll);
//                        Att=roll;
//                        return;
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        return Att;
//    }


}
