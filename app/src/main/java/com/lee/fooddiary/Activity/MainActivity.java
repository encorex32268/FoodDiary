package com.lee.fooddiary.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lee.fooddiary.Fragment.HomeFragment;
import com.lee.fooddiary.Fragment.MyFoodFragment;
import com.lee.fooddiary.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView ;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.fragment_main);
        initViews();
        position = getSharedPreferences("FragmentState",MODE_PRIVATE)
                .getInt("POS",0);
        if (position!=0) bottomNavigationView.setSelectedItemId(position);

    }

    private void replaceMyFoodFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, MyFoodFragment.getInstance())
                .commit();
    }

    private void replaceHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HomeFragment.getInstance())
                .commit();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.main_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG, "onNavigationItemSelected: "+menuItem.getItemId());
                switch (menuItem.getItemId())
                {
                    case R.id.nav_homepage:
                        getSharedPreferences("FragmentState",MODE_PRIVATE)
                                .edit().putInt("POS",menuItem.getItemId()).apply();
                        replaceHomeFragment();
                        break;
                    case R.id.nav_myfood:
                        getSharedPreferences("FragmentState",MODE_PRIVATE)
                                .edit().putInt("POS",menuItem.getItemId()).apply();
                        replaceMyFoodFragment();
                        break;
                }
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab");
                startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signout) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            new AlertDialog.Builder(this)
                    .setTitle("登出")
                    .setMessage("確定要登出嗎？")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.signOut();

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }


}
