package com.lee.fooddiary.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lee.fooddiary.Model.FoodStore;
import com.lee.fooddiary.R;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private FoodStore foodStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getFoodStoreFromIntent();

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
                        Toast.makeText(DetailActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        item.setVisible(false);
                    }
                }
            });

            String uid = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(uid).child("FoodStores").child(foodStore.getKey()).setValue(foodStore);

        }
        return super.onOptionsItemSelected(item);
    }

}
