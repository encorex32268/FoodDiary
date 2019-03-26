package com.lee.fooddiary.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lee.fooddiary.DetailActivity;
import com.lee.fooddiary.FoodStore;
import com.lee.fooddiary.R;
import com.lee.fooddiary.User;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements FirebaseAuth.AuthStateListener, ValueEventListener, ChildEventListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private TextView userText;
    private FirebaseRecyclerAdapter<FoodStore, FoodStoreViewHolder> adapter;
    private RecyclerView recyclerFoodStore;

    private static HomeFragment instance  ;

    public static HomeFragment getInstance() {
        Log.d(TAG, "getInstance: ");
        if (instance == null)
        {
            instance = new HomeFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerFoodStore = rootView.findViewById(R.id.recyclerFoodStore);
        userText = rootView.findViewById(R.id.userText);
        mAuth = FirebaseAuth.getInstance();
        setUpRecyclerView();

        Log.d(TAG, "onCreateView: ");
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
    }



    private void setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView: ");
        recyclerFoodStore.setHasFixedSize(true);
        recyclerFoodStore.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Query query = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(mAuth.getUid())
                .child("FoodStores")
                .orderByChild("rate")
                ;
        query.addChildEventListener(this);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<FoodStore>()
                .setQuery(query,FoodStore.class)

                .build();
        adapter = new FirebaseRecyclerAdapter<FoodStore, FoodStoreViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodStoreViewHolder holder, int position, @NonNull final FoodStore model) {
                holder.toBind(model);
                holder.itemView.setTag(model);
            }
            @NonNull
            @Override
            public FoodStoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view =getLayoutInflater().inflate(R.layout.item_foodstore,viewGroup,false);
                return new FoodStoreViewHolder(view);
            }

            @NonNull
            @Override
            public FoodStore getItem(int position) {
                //be reverse
                return super.getItem(this.getItemCount() - position - 1);

            }
        };

        recyclerFoodStore.setAdapter(adapter);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user =  dataSnapshot.getValue(User.class);
        userText.setText("User : "+user.getName());
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private class FoodStoreViewHolder extends RecyclerView.ViewHolder {

        TextView foodName;
        TextView foodStar;
        ImageView foodImg;
        public FoodStoreViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_name);
            foodStar = itemView.findViewById(R.id.item_star);
            foodImg = itemView.findViewById(R.id.item_foodImg);

        }

        public void toBind(final FoodStore model) {
            foodName.setText(model.getName());
            foodStar.setText(String.valueOf(model.getRate()));
            if (model.getImageUrls().size() !=0)
            {
                Glide.with(HomeFragment.this)
                        .load(model.getImageUrls().get(0))
                        .into(foodImg);
                foodImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("FOOD",model);
                    intent.putExtra("FOOD",bundle);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mAuth.addAuthStateListener(this);
        //loadRecycler;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        mAuth.removeAuthStateListener(this);
        if (adapter!=null)
        {
            adapter.stopListening();
        }

    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }else if (id == R.id.action_signout){
//            new AlertDialog.Builder(this)
//                    .setTitle("登出")
//                    .setMessage("確定要登出嗎？")
//                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            mAuth.signOut();
//                            recyclerFoodStore.setAdapter(null);
//
//                        }
//                    })
//                    .setNegativeButton("取消",null)
//                    .show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.mipmap.ic_foodstore)
                            .build(),
                    RC_SIGN_IN);


        }else {
            FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).child("Info")
                    .addValueEventListener(this);
            setUpRecyclerView();
            adapter.startListening();

        }



    }


}