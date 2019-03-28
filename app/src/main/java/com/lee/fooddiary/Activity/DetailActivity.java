package com.lee.fooddiary.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lee.fooddiary.Model.FoodStore;
import com.lee.fooddiary.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private FoodStore foodStore;

    private GoogleMap mMap;
    private TextView moneyMin;
    private TextView moneyMax;
    private TextView rateText;
    private ImageView image1Detail;
    private ImageView image2Detail;
    private ImageView image3Detail;
    private TextView addressText;
    private ConstraintLayout detailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getFoodStoreFromIntent();
        findViews();


    }

    private void findViews() {
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setTitle(foodStore.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        detailActivity = findViewById(R.id.detailActivity);
        moneyMin = findViewById(R.id.moneyMinText);
        moneyMax = findViewById(R.id.moneyMaxText);
        rateText = findViewById(R.id.rateText);
        image1Detail = findViewById(R.id.image1Detail);
        image2Detail = findViewById(R.id.image2Detail);
        image3Detail = findViewById(R.id.image3Detail);
        addressText = findViewById(R.id.addressText);
        moneyMin.setText(String.valueOf(foodStore.getMoneyMin()));
        moneyMax.setText(String.valueOf(foodStore.getMoneyMax()));
        rateText.setText(String.valueOf(foodStore.getRate()));
        addressText.setText(foodStore.getAddress());
        int[] imgIDs ={R.id.image1Detail,R.id.image2Detail,R.id.image3Detail};
        final List<String> imgs = foodStore.getImageUrls();
        if (imgs.size()!=0){
            for (int i = 0 ; i <imgs.size() ; i++){
                ImageView tempImageView = findViewById(imgIDs[i]);
                final String url = imgs.get(i);
                Glide.with(this).load(url).into(tempImageView);
                tempImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailActivity.this,FullscreenActivity.class);
                        intent.putExtra("FullscreenURL",url);
                        startActivity(intent);
                    }
                });
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myDetailMap);
        mapFragment.getMapAsync(this);
    }


    private void getFoodStoreFromIntent() {
        Bundle bundle = getIntent().getBundleExtra("FOOD");
        foodStore = (FoodStore) bundle.get("FOOD");
        Log.d(TAG, "onCreate: "+ foodStore.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!foodStore.isShared()){
            getMenuInflater().inflate(R.menu.menu_detail,menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId()==R.id.upload){
            Log.d(TAG, "onOptionsItemSelected: upload");
            //TODO:UpLoad FoodStore

            foodStore.setShared(true);
            FirebaseDatabase.getInstance().getReference("FoodStores")
                    .push().setValue(foodStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        String uid = FirebaseAuth.getInstance().getUid();
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(uid).child("FoodStores").child(foodStore.getKey()).setValue(foodStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(detailActivity, "上傳成功", Snackbar.LENGTH_SHORT)
                                        .show();
                                item.setVisible(false);
                            }
                        });
                    }else {
                        Snackbar.make(detailActivity, "上傳失敗", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            });


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double latitude = foodStore.getLatitude();
        double longitude = foodStore.getLongitude();
        Log.d(TAG, "onMapReady: "+latitude+","+longitude);
        LatLng latLng = new LatLng(latitude, longitude);
        moveCamera(latLng);
    }

    private void moveCamera(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        mMap.addMarker(new MarkerOptions().position(latLng).title(foodStore.getName()));
    }



}
