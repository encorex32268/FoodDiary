package com.lee.fooddiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lee.fooddiary.Fragment.HomeFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
//        implements FirebaseAuth.AuthStateListener,ValueEventListener, ChildEventListener
{

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private TextView userText;
   // private FirebaseRecyclerAdapter<FoodStore, FoodStoreViewHolder> adapter;
    private RecyclerView recyclerFoodStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.fragment_main);



       // userText = findViewById(R.id.userText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,HomeFragment.getInstance())
                .commit()
        ;
//        getSupportFragmentManager()
//                .beginTransaction()
             //   .replace(R.id.container, HomeFragment.)


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddFoodActivity.class));
            }
        });
//
//        mAuth = FirebaseAuth.getInstance();




    }

//    private void setUpRecyclerView() {
//        recyclerFoodStore = findViewById(R.id.recyclerFoodStore);
//        recyclerFoodStore.setHasFixedSize(true);
//        recyclerFoodStore.setLayoutManager(new LinearLayoutManager(this));
//        final Query query = FirebaseDatabase.getInstance()
//                .getReference("Users")
//                .child(mAuth.getUid())
//                .child("FoodStores")
//                .orderByChild("rate")
//                ;
//        query.addChildEventListener(this);
//
//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<FoodStore>()
//                .setQuery(query,FoodStore.class)
//
//                .build();
//        adapter = new FirebaseRecyclerAdapter<FoodStore, FoodStoreViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull FoodStoreViewHolder holder, int position, @NonNull final FoodStore model) {
//                holder.toBind(model);
//                holder.itemView.setTag(model);
//            }
//            @NonNull
//            @Override
//            public FoodStoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view =getLayoutInflater().inflate(R.layout.item_foodstore,viewGroup,false);
//                return new FoodStoreViewHolder(view);
//            }
//
//            @NonNull
//            @Override
//            public FoodStore getItem(int position) {
//                //be reverse
//               return super.getItem(this.getItemCount() - position - 1);
//
//            }
//        };
//
//        recyclerFoodStore.setAdapter(adapter);
//
//    }

//    public class FoodStoreViewHolder extends RecyclerView.ViewHolder {
//
//        TextView foodName;
//        TextView foodStar;
//        ImageView foodImg;
//        public FoodStoreViewHolder(@NonNull View itemView) {
//            super(itemView);
//            foodName = itemView.findViewById(R.id.item_name);
//            foodStar = itemView.findViewById(R.id.item_star);
//            foodImg = itemView.findViewById(R.id.item_foodImg);
//
//        }
//
//        public void toBind(final FoodStore model) {
//            foodName.setText(model.getName());
//            foodStar.setText(String.valueOf(model.getRate()));
//            if (model.getImageUrls().size() !=0)
//            {
//                Glide.with(MainActivity.this)
//                        .load(model.getImageUrls().get(0))
//                        .into(foodImg);
//                foodImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            }
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("FOOD",model);
//                        intent.putExtra("FOOD",bundle);
//                        startActivity(intent);
//
//                }
//            });
//        }
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart: ");
//        mAuth.addAuthStateListener(this);
//        //loadRecycler;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop: ");
//        mAuth.removeAuthStateListener(this);
//        if (adapter!=null)
//        {
//            adapter.stopListening();
//        }
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
//        {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            final User user = new User();
//            user.setUid(currentUser.getUid());
//            user.setEmail(currentUser.getEmail());
//            user.setName(currentUser.getDisplayName());
//
//            FirebaseDatabase.getInstance().getReference("Users")
//                    .child(mAuth.getUid())
//                    .child("Info")
//                    .setValue(user);
//        }
//    }

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
        }else if (id == R.id.action_signout){
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            new AlertDialog.Builder(this)
                    .setTitle("登出")
                    .setMessage("確定要登出嗎？")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.signOut();
                            recyclerFoodStore.setAdapter(null);

                        }
                    })
                    .setNegativeButton("取消",null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null)
//        {
//            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                    new AuthUI.IdpConfig.GoogleBuilder().build());
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(providers)
//                            .setIsSmartLockEnabled(false)
//                            .setLogo(R.mipmap.ic_foodstore)
//                            .build(),
//                    RC_SIGN_IN);
//
//
//        }else {
//            FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).child("Info")
//                    .addValueEventListener(this);
//            setUpRecyclerView();
//            adapter.startListening();
//
//        }
//
//
//
//    }
//
//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        User user =  dataSnapshot.getValue(User.class);
//        userText.setText("User : "+user.getName());
//    }
//
//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//    }
//
//    @Override
//    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//    }
//
//    @Override
//    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//    }
//
//    @Override
//    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//    }
}
