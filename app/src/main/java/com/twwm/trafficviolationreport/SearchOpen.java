package com.twwm.trafficviolationreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SearchOpen extends AppCompatActivity {

    private String passInfo;

    private ListAdapter arrayAdapter;
    private HashMap<Integer, String> threadMap;
    private ArrayList<String> threadNames;
    private ArrayList<String> threadLocation;
    private ArrayList<String> threadLink;

    private ArrayList<String> threadId;
    private ArrayList<String> threadLat;
    private ArrayList<String> threadLong;

    private ArrayList<String> threadTime;

    private EditText inputSearch;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_open);
        inputSearch = findViewById(R.id.searchOpen);

        threadNames = new ArrayList<>();
        threadLocation = new ArrayList<>();
        threadLink = new ArrayList<>();
        threadTime = new ArrayList<>();
        threadId = new ArrayList<>();
        threadLat = new ArrayList<>();
        threadLong = new ArrayList<>();
        threadMap = new HashMap<>();
        arrayAdapter = new ListAdapter(SearchOpen.this, R.layout.list_item, threadMap, threadNames, threadTime, threadLink);

        final ListView mThreadListView = findViewById(R.id.EventList);
        mThreadListView.setAdapter(arrayAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mThreadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Integer pos = (Integer)getKeyFromValue(threadMap, arrayAdapter.arrayList.get(i));

                passInfo = threadId.get(pos) + threadNames.get(pos) + threadTime.get(pos);

                Intent intent = new Intent(SearchOpen.this, ViewInfo.class);
                intent.putExtra("Info", passInfo);
                startActivity(intent);

            }
        });

        db.collection("Events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        threadMap.put(count, document.getString("Vehicle Number"));
                        threadNames.add(threadMap.get(count));
                        threadLocation.add("Latitude: \n" + document.getString("Latitude") + " " + "\nLongitude: \n" + document.getString("Longitude"));
                        threadLink.add(document.getString("Link"));
                        threadTime.add(document.getString("TimeStamp"));
                        threadId.add(document.getString("emailId"));
                        threadLat.add(document.getString("Latitude"));
                        threadLong.add(document.getString("Longitude"));
                        arrayAdapter.notifyDataSetChanged();
                        count++;

                    }

                } else {
                    Toast.makeText(SearchOpen.this, "Failed to load list.", Toast.LENGTH_LONG).show();
                }

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                arrayAdapter.getFilter().filter(s.toString());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after)
            {
                arrayAdapter.getFilter().filter(s.toString());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                arrayAdapter.getFilter().filter(s.toString());
                arrayAdapter.notifyDataSetChanged();
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