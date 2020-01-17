package com.twwm.trafficviolationreport;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity implements LocationListener {

    private String photoLink;
    private String typeStr;

    public static String Longi;
    public static String Lati;
    LocationManager locationManager;

    private Button photo;
    private Button submit;
    private EditText number;
    private Spinner spinner;

    private ArrayList<String> types;

    private Map<String, Object> data;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        CheckPermission();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        photo = findViewById(R.id.photo);
        submit = findViewById(R.id.submit);
        number = findViewById(R.id.number);
        spinner = findViewById(R.id.spinner);

        types = new ArrayList<>();

        db.collection("Crimes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {

                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        types.add(document.getString("Name"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_spinner_dropdown_item, types);
                    spinner.setAdapter(adapter);

                }

            }
        });

        data = new HashMap<>();

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(photoLink != null) {
                    if (check(number.getText().toString())) {
                        Integer indexValue = spinner.getSelectedItemPosition();
                        typeStr = types.get(indexValue);

                        data.put("Latitude", Lati);
                        data.put("Longitude", Longi);
                        data.put("Vehicle Number", number.getText().toString());
                        data.put("emailId", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        data.put("TimeStamp", getCurrentTimeStamp());
                        data.put("Link", photoLink);
                        data.put("Type", typeStr);
                        db.collection("Events").document(FirebaseAuth.getInstance().getCurrentUser().getEmail() + number.getText().toString() + getCurrentTimeStamp()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(InfoActivity.this, "Complaint Taken", Toast.LENGTH_LONG).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(InfoActivity.this, Main2Activity.class));
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(InfoActivity.this, "Complaint Not Taken due to " + e, Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {

                        Toast.makeText(InfoActivity.this, "Please Enter Valid Number", Toast.LENGTH_LONG).show();

                    }
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                    builder.setMessage("Do you want to upload without a picture?");
                    builder.setTitle("Alert !");
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                                    "Yes",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {

                                            if (check(number.getText().toString())) {
                                                Integer indexValue = spinner.getSelectedItemPosition();
                                                typeStr = types.get(indexValue);

                                                data.put("Latitude", Lati);
                                                data.put("Longitude", Longi);
                                                data.put("Vehicle Number", number.getText().toString());
                                                data.put("emailId", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                data.put("TimeStamp", getCurrentTimeStamp());
                                                data.put("Link", photoLink);
                                                data.put("Type", typeStr);
                                                db.collection("Events").document(FirebaseAuth.getInstance().getCurrentUser().getEmail() + number.getText().toString() + getCurrentTimeStamp()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(InfoActivity.this, "Complaint Taken", Toast.LENGTH_LONG).show();
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startActivity(new Intent(InfoActivity.this, Main2Activity.class));
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                Toast.makeText(InfoActivity.this, "Complaint Not Taken due to " + e, Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                            } else {

                                                Toast.makeText(InfoActivity.this, "Please Enter Valid Number", Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    });
                    builder.setNegativeButton(
                                    "No",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

            }
        });

    }

    static boolean check(String num)
    {
        boolean numeric = true;
        boolean string = false;
        try {
            int number = Integer.parseInt(num.substring(2,4));
        }
        catch (NumberFormatException e) {
            numeric = false;
        }
        try {
            int number = Integer.parseInt(num.substring(0,2).toUpperCase());
        }
        catch (NumberFormatException e)
        {
            string = true;
        }
        if(numeric==true && string==true && (num.charAt(0)>64 && num.charAt(0)<91) && (num.charAt(1)>64 && num.charAt(1)<91))
        {
            return true;
        }
        else
        {
            return false;
        }
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
        Toast.makeText(InfoActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            final StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(number.getText().toString() + "/" + getCurrentTimeStamp() +".jpg");

            Bitmap bitmapNo = (Bitmap) data.getExtras().get("data");
            Bitmap bitmap = scaleBitmapAndKeepRation(bitmapNo, 40,40);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(datas);

            final ProgressDialog dialog = ProgressDialog.show(InfoActivity.this, "Uploading", "Image", true);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.create();
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoLink = uri.toString();
                        }
                    })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    dialog.dismiss();

                                }
                            });
                }
            });

        }
    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap targetBmp,int reqHeightInPixels,int reqWidthInPixels)
    {
        Matrix matrix = new Matrix();
        matrix .setRectToRect(new RectF(0, 0, targetBmp.getWidth(), targetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmp.getWidth(), targetBmp.getHeight(), matrix, true);
        return scaledBitmap;
    }

    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
