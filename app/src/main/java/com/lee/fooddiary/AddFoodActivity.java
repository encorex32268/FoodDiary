package com.lee.fooddiary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, OnCompleteListener<Location>, View.OnClickListener {

    private static final String TAG = AddFoodActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_CODE = 100;
    private static final int REQUEST_READ_IMG = 200;

    private GoogleMap mMap;
    private EditText edStoreName;
    private RatingBar ratingBar;
    private EditText edNote;
    private TextView addressText;
    private double mLatitude;
    private double mLongtitude;

    private FusedLocationProviderClient client;

    private ImageView foodImage1;
    private ImageView foodImage2;
    private ImageView foodImage3;
    private int nowImage ;
    private List<Uri> uriList = new ArrayList<>();
    private List<String> imageDownloadURL;
    private ConstraintLayout progressbarConstraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        findViews();
        setGoogleMap();


    }


    private void findViews() {
        edStoreName = findViewById(R.id.edStoreName);
        ratingBar = findViewById(R.id.ratingBar);
        edNote = findViewById(R.id.edNote);
        addressText = findViewById(R.id.address);
        foodImage1 = findViewById(R.id.foodImage1);
        foodImage2 = findViewById(R.id.foodImage2);
        foodImage3 = findViewById(R.id.foodImage3);
        progressbarConstraintLayout = findViewById(R.id.progressbarConstraintLayout);
        foodImage1.setOnClickListener(this);
        foodImage2.setOnClickListener(this);
        foodImage3.setOnClickListener(this);
    }

    private void setGoogleMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        // 101 - 25.0339687 ,121.5622835
        LatLng taipei101 = new LatLng(25.0339687, 121.5622835);
        moveCamera(taipei101);

        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            return;
        }else{
            setUpLocation();
        }
    }

    private void moveCamera(LatLng latLng) {
//        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        // get location
        mLatitude = latLng.latitude;
        mLongtitude = latLng.longitude;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_LOCATION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            setUpLocation();
        }else if (requestCode == REQUEST_READ_IMG && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getPicture();
        }
    }

    @SuppressLint("MissingPermission")
    private void setUpLocation() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnCompleteListener(this);

    }

    private void getAddress(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.TAIWAN);
        try {
            List<Address> addressList =
                    geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);
//            for (Address temp : addressList)
//            {
//                Log.d(TAG, "getAddress: "+ temp.toString());
//            }
            Address addressFirst = addressList.get(0);
            String addressString = addressFirst.getAddressLine(0);
            addressText.setText(addressString);

        } catch (IOException e) {
            e.printStackTrace();
            addressText.setText("無法顯示位置");

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onMyLocationButtonClick() {
//        Log.d(TAG, "onMyLocationButtonClick: ");
        client.getLastLocation().addOnCompleteListener(this);
        return true;
    }

    @Override
    public void onComplete(@NonNull Task<Location> task) {
        if (task.isSuccessful()){
            Location location = task.getResult();
//            Log.d(TAG, "onComplete: " +
//                    location.getLatitude() +"," + location.getLongitude());
            getAddress(location);
            moveCamera(new LatLng(location.getLatitude(),location.getLongitude()));

        }
    }

    public void add(final View view)
    {
        progressbarConstraintLayout.setVisibility(View.VISIBLE);
        view.setClickable(false);
        final String uid = FirebaseAuth.getInstance().getUid();
        final FirebaseStorage storage = FirebaseStorage.getInstance("gs://fooddiary-43342.appspot.com");
        imageDownloadURL = new ArrayList<>();
        if (uriList.size()>0){
            for (Uri uri : uriList){
                final StorageReference filePath = storage.getReference().child("images/users/"+uid+"/"+getDate()+".jpg");
                final UploadTask uploadTask =filePath.putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();

                                }
                                // Continue with the task to get the download URL
                                return filePath.getDownloadUrl();

                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "onComplete: "+task.getResult().toString());
                                    String result = task.getResult().toString() ;
                                    imageDownloadURL.add(result);
                                    if (imageDownloadURL.size() == uriList.size())
                                    {
                                        FoodStore foodStore = setFoodStoreObject();
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(uid).child("FoodStores").push().setValue(foodStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    progressbarConstraintLayout.setVisibility(View.GONE);
                                                    Toast.makeText(AddFoodActivity.this,"儲存成功",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }else{
                                                    progressbarConstraintLayout.setVisibility(View.GONE);
                                                    view.setClickable(true);
                                                    Toast.makeText(AddFoodActivity.this,"儲存失敗",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressbarConstraintLayout.setVisibility(View.GONE);
                        view.setClickable(true);
                    }
                });

            }
        }else
        {
            FoodStore foodStore = setFoodStoreObject();
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(uid).child("FoodStores").push().setValue(foodStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressbarConstraintLayout.setVisibility(View.GONE);
                        Toast.makeText(AddFoodActivity.this,"儲存成功",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        progressbarConstraintLayout.setVisibility(View.GONE);
                        view.setClickable(true);
                        Toast.makeText(AddFoodActivity.this,"儲存失敗",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }






    }



    private String getDate() {
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss:SSS");
        return sdFormat.format(date);
    }

    private FoodStore setFoodStoreObject() {
        FoodStore foodStore = new FoodStore();
        String name = edStoreName.getText().toString();
        float rate = ratingBar.getRating();
        String note = edNote.getText().toString();
        String address = addressText.getText().toString();
        Date date = new Date();
        String pattern = "yyyy/MM/dd HH:mm:SS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        foodStore.setDate(simpleDateFormat.format(date));
        foodStore.setName(name);
        foodStore.setRate(rate);
        foodStore.setNote(note);
        foodStore.setAddress(address);
        foodStore.setLatitude(mLatitude);
        foodStore.setLongtiude(mLongtitude);
        foodStore.setImageUrls(imageDownloadURL);
        return foodStore ;
    }

    @Override
    public void onClick(View view) {


        int pickImagePermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if (pickImagePermission == PackageManager.PERMISSION_GRANTED){
            nowImage = view.getId();
            Log.d(TAG, "onClick: "+nowImage);
            getPicture();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_IMG);
        }

    }

    private void getPicture() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,REQUEST_READ_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == REQUEST_READ_IMG && resultCode==RESULT_OK){
            Uri uri = data.getData();
            switch (nowImage)
            {
                case R.id.foodImage1:
                    uriList.add(uri);
                    foodImage1.setImageURI(uri);
                    break;
                case R.id.foodImage2:
                    uriList.add(uri);
                    foodImage2.setImageURI(uri);
                    break;
                case R.id.foodImage3:
                    uriList.add(uri);
                    foodImage3.setImageURI(uri);
                    break;
            }
        }
    }
}
