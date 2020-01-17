package com.twwm.trafficviolationreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApprovedSearch extends AppCompatActivity {

    private ListAdapter arrayAdapter;
    private HashMap<Integer,String> threadMap;
    private ArrayList<String> threadNames;
    private ArrayList<String> threadLocation;
    private ArrayList<String> threadLink;
    private ArrayList<String> threadTime;
    private ArrayList<String> threadId;

    private String docId;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_search);

        threadNames = new ArrayList<>();
        threadLocation = new ArrayList<>();
        threadLink = new ArrayList<>();
        threadTime = new ArrayList<>();
        threadId = new ArrayList<>();
        threadMap = new HashMap<>();
        arrayAdapter = new ListAdapter(ApprovedSearch.this, R.layout.list_item, threadMap, threadNames, threadTime, threadLink);

        FirebaseFirestore.getInstance().collection("Approved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {

                    for(QueryDocumentSnapshot document: task.getResult())
                    {

                        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(document.getString("emailId"))) {

                            threadMap.put(count,document.getString("Vehicle Number"));
                            threadNames.add(threadMap.get(count));
                            threadTime.add(document.getString("TimeStamp"));
                            threadLocation.add("Latitude: \n" + document.getString("Latitude") + " " + "\nLongitude: \n" + document.getString("Longitude"));
                            threadLink.add(document.getString("Link"));
                            threadId.add(document.getString("emailId"));
                            arrayAdapter.notifyDataSetChanged();
                            count++;

                        }

                    }

                }
                else
                {
                    Toast.makeText(ApprovedSearch.this, "Failed to load list.", Toast.LENGTH_LONG).show();
                }

            }
        });

        ListView mThreadListView = findViewById(R.id.EventList);
        mThreadListView.setAdapter(arrayAdapter);

        mThreadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Integer pos = (Integer)getKeyFromValue(threadMap, arrayAdapter.arrayList.get(i));

                docId = threadId.get(pos) + threadNames.get(pos) + threadTime.get(pos);

                Intent intent = new Intent(ApprovedSearch.this, ViewApprovals.class);
                intent.putExtra("Info", docId);
                startActivity(intent);

            }
        });

    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
