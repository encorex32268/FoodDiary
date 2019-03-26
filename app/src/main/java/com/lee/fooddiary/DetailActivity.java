package com.lee.fooddiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getBundleExtra("FOOD");
        FoodStore foodStore = (FoodStore) bundle.get("FOOD");
        Log.d(TAG, "onCreate: "+foodStore.toString());


    }
}