package com.twwm.trafficviolationreport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewInfo extends AppCompatActivity implements LocationListener {

    private ImageView imageView;
    private TextView Name;
    private TextView Id;
    private TextView Time;

    public static String Longi;
    public static String Lati;
    LocationManager locationManager;

    private Map<String,Object> dataHead;

    private String docId;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);
        CheckPermission();

        Intent intent = getIntent();

        docId = intent.getStringExtra("Info");

        db.collection("Events").document(docId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                dataHead = new HashMap<>();

                dataHead.put("Latitude", documentSnapshot.getString("Latitude"));
                dataHead.put("Longitude", documentSnapshot.getString("Longitude"));
                dataHead.put("Vehicle Number", documentSnapshot.getString("Vehicle Number"));
                dataHead.put("emailId", documentSnapshot.getString("emailId"));
                dataHead.put("TimeStamp", documentSnapshot.getString("TimeStamp"));
                dataHead.put("Link", documentSnapshot.getString("Link"));
                dataHead.put("Type", documentSnapshot.getString("Type"));

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewInfo.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        imageView = findViewById(R.id.ProfilePicInfo1);

                        if(dataHead.get("Link")!= null)
                        {

                            Glide.with(ViewInfo.this)
                                    .load(dataHead.get("Link").toString())
                                    /*.override(300, 200)*/
                                    .into(imageView);

                        }

                        Name = findViewById(R.id.ProfileNameInfo1);
                        Id = findViewById(R.id.RegisInfo1);
                        Time = findViewById(R.id.TimeInfo1);

                        Name.setText(dataHead.get("Vehicle Number").toString());
                        Id.setText(dataHead.get("Type").toString());
                        Time.setText(dataHead.get("TimeStamp").toString());
                    }
                });
    }

    public void getLocationed(View view)
    {

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + Lati + "," + Longi + "&daddr=" + dataHead.get("Latitude") + "," + dataHead.get("Longitude")));
        startActivity(mapIntent);

    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Longi = String.valueOf(location.getLongitude());
        Lati = String.valueOf(location.getLatitude());
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ViewInfo.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }


}
