package com.example.ecgv2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Date;

public class Profile extends AppCompatActivity{
    String TAG = "Profile";
    public Integer last_entry = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Channel1/name");
        DatabaseReference last_entry_ref =
                database.getReference("Channel2/filtered_info/last_filtered_id");
// Read last entry from the database
        last_entry_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again whenever data at this location is updated.
                last_entry = dataSnapshot.getValue(Integer.class);
                Log.e(TAG, "Last Entry ID : " + last_entry);
//Last entry Data read
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference
                        field_value_ref = db.getReference("Channel2/filtered_data/feed" + last_entry);
                field_value_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer HeartRate = dataSnapshot.child("heart_rate").getValue(Integer.class);
                        TextView hrView = (TextView) findViewById(R.id.h_r);
                        hrView.setText("Heart Rate: " +String.format("%d",HeartRate));
                        hrView.setTextSize(20);
                        hrView.setTextColor(Color.RED);
                        Log.e(TAG, "Heart Rate: " + HeartRate);
                        Double qq = dataSnapshot.child("QQ").getValue(Double.class);
                        TextView qqview = (TextView) findViewById(R.id.qq);
                        qqview.setText("QQ: "+String.format("%.2f",qq)+ "s");
                        qqview.setTextSize(20);
                        qqview.setTextColor(Color.BLUE);
                        Double rr = dataSnapshot.child("RR").getValue(Double.class);
                        TextView rrview = (TextView) findViewById(R.id.rr);
                        rrview.setText("RR:" +String.format("%.2f",rr)+ "s");
                        rrview.setTextSize(20);
                        rrview.setTextColor(Color.BLUE);
                        Double ss = dataSnapshot.child("SS").getValue(Double.class);
                        TextView ssview = (TextView) findViewById(R.id.ss);
                        ssview.setText("SS: "+String.format("%.2f",ss)+ "s");
                        ssview.setTextSize(20);
                        ssview.setTextColor(Color.BLUE);
                        Double tt = dataSnapshot.child("TT").getValue(Double.class);
                        TextView ttview = (TextView) findViewById(R.id.tt);
                        ttview.setText("TT: "+String.format("%.2f",tt)+ "s");
                        ttview.setTextSize(20);
                        ttview.setTextColor(Color.BLUE);
                        String timeStamp = dataSnapshot.child("TimeStamp").getValue(String.class);
                        TextView timeView = (TextView) findViewById(R.id.timeStamp);
                        timeView.setText("Time: "+timeStamp);
                        timeView.setTextSize(20);
                        timeView.setTextColor(Color.RED);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Button lBttn = (Button) findViewById(R.id.ECG_BTN);
        lBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, MainActivity.class));
            }
        });
        ImageView i2 = (ImageView) findViewById(R.id.imageView2);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, profile.class));
            }
        });
    }
}
