package com.example.learningproject.entireactivity.riderapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityTripHistoryBinding;
import com.example.learningproject.entireactivity.riderapp.adapter.ClickListener;
import com.example.learningproject.entireactivity.riderapp.adapter.recyclerViewHistory.HistoryAdapter;
import com.example.learningproject.entireactivity.riderapp.common.Common;
import com.example.learningproject.entireactivity.riderapp.model.firebase.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripHistoryActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference riderHistory;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvHistory;
    HistoryAdapter adapter;
    FirebaseAuth mAuth;
    ArrayList<History> listData;
    ActivityTripHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  binding= DataBindingUtil.setContentView(this,R.layout.activity_trip_history);
        initToolbar();
        initRecyclerView();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        riderHistory = database.getReference(Common.history_rider);
        listData = new ArrayList<>();
        adapter = new HistoryAdapter(this, (ArrayList<History>) listData, new ClickListener() {
            @Override
            public void onClick(View view, int index) {

            }
        });
        rvHistory.setAdapter(adapter);
        getHistory();
    }
    private void getHistory(){
        riderHistory.child(Common.userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    History history = postSnapshot.getValue(History.class);
                    listData.add(history);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initRecyclerView(){

        binding.rvHistory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.rvHistory.setLayoutManager(layoutManager);
        binding.rvHistory.setItemAnimator(new DefaultItemAnimator());
        binding.rvHistory.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Register");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}