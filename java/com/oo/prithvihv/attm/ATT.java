package com.oo.prithvihv.attm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class ATT extends AppCompatActivity {

    private static final String TAG = "MyActivity2";

    //firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    //DS
    ArrayList<Attmodel> data=new ArrayList<Attmodel>();

    //View
    private ListView AttListV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att);
        String Subject=this.getIntent().getExtras().getString("Subject");
        AttListV=(ListView)findViewById(R.id.Att);

        //FIRST GET DATA
        myRef = database.getReference("DSATM/2016/CSE/"+Subject+"/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:before loop " + dataSnapshot);
                for( DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+ snapshot);
                    String rollnumber=snapshot.getKey();
                    Log.d(TAG, rollnumber);
                    data.add(new Attmodel(rollnumber,true));
                }
                DisplayLIST();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
        //onCLICK
//        AttListV.setOnItemClickListener(
//
//                new AdapterView.OnItemClickListener(){
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        Attmodel dataModel= data.get(position);
//                        dataModel.checked = !dataModel.checked;
//                        listdata.notifyDataSetChanged();
//                        Log.d(TAG, "onItemClick: "+ dataModel.checked + "selected item is " + dataModel.roll);
//                    }
//                }
//        );

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

}
