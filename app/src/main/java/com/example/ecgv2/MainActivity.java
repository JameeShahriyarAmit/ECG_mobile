package com.example.ecgv2;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.annotation.NonNull;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    String TAG = "Ecg Monitor";
    public Integer last_entry = 2;
    public ArrayList<Double> ecg_data_continious = new ArrayList<Double>();
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series;
    public Integer lastXpoint = 0;
    public Double lastYpoint = 0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[]{
        });
        graph.addSeries(series);
// set manual X bounds

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(20);
        graph.getViewport().setMaxX(200);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.setTitle("Ecg Signal");
        graph.setBackgroundColor(Color.rgb(3, 15, 5));
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
// styling series
        series.setTitle("Random Curve 1");
        series.setColor(Color.GREEN);
        series.setDataPointsRadius(4);
        series.setThickness(3);
        Log.e(TAG, "Sereis Created");
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Channel1/name");
        DatabaseReference last_entry_ref =
                database.getReference("Channel2/filtered_info/last_filtered_id");
        final TextView mLview = (TextView) findViewById(R.id.textView);
        mLview.setText("--");
        mLview.setTextSize(20);
        mLview.setTextColor(Color.RED);
        mLview.setText("Last Entry: " + last_entry);
        mLview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserDetails.class));
            }
        });
        // Read last entry from the database
        last_entry_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
/* This method is called once with the initial value and again whenever data at
this location is updated.*/
                last_entry = dataSnapshot.getValue(Integer.class);
                Log.e(TAG, "Last Entry ID : " + last_entry);
                mLview.setText("Last Entry: " + last_entry);
//Last entry Data read
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference field_value_ref =
                        db.getReference("Channel2/filtered_data/feed" + last_entry);
                field_value_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer HeartRate = dataSnapshot.child("heart_rate").getValue(Integer.class);
                        TextView hrView = (TextView) findViewById(R.id.hr);
                        hrView.setText(HeartRate.toString());
                        hrView.setTextSize(20);
                        hrView.setTextColor(Color.RED);
                        Log.e(TAG, "Heart Rate: " + HeartRate);
//Individual field values to Arraylist
                        String f1 = dataSnapshot.child("feed" +
                                last_entry.toString()).getValue(String.class);
                        Log.e(TAG, "f1 : " + f1);
                        String[] parts = f1.split(",");
                        for (int i = 0; i < parts.length; i++) {
                            ecg_data_continious.add(Double.parseDouble(parts[i]));
                        }
                        Log.e(TAG, "Get Y 0 = " + ecg_data_continious.get(1));
                        Log.e(TAG, "Size : " + ecg_data_continious.size() + " List: " +   ecg_data_continious);

                        addRandomDataPoint();
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
        Log.e(TAG, "f : " + ecg_data_continious.size());
        Button lBttn = (Button) findViewById(R.id.profile_btn);
        lBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserDetails.class));
            }
        });
    }
    private void addRandomDataPoint() {
        mHandler.postDelayed(new Runnable() {@Override
        public void run() {
            Log.e(TAG, "Size = " + ecg_data_continious.size());
            if (ecg_data_continious.size() > 0 && lastXpoint < ecg_data_continious.size() - 1)
            {

                lastYpoint = ecg_data_continious.get(lastXpoint);
                lastXpoint++;
                series.appendData(new DataPoint(lastXpoint, lastYpoint), true, 1000);
                Log.e(TAG, "X=" + lastXpoint + " Y= " + lastYpoint);
                addRandomDataPoint();
            } else if (ecg_data_continious.size() == 0) {
                Log.e(TAG, "X=" + lastXpoint);
                lastXpoint = 0;
            }
        }
        }, 0);
    }
}

