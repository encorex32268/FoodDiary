package com.lee.fooddiary.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.lee.fooddiary.Activity.DetailActivity;
import com.lee.fooddiary.Model.FoodStore;
import com.lee.fooddiary.R;
import com.lee.fooddiary.Model.User;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyFoodFragment extends Fragment implements FirebaseAuth.AuthStateListener, ValueEventListener, ChildEventListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private FirebaseAuth mAuth;
    private TextView userText;
    private FirebaseRecyclerAdapter<FoodStore, FoodStoreViewHolder> adapter;
    private RecyclerView recyclerFoodStore;

    private static MyFoodFragment instance;
    public static MyFoodFragment getInstance() {
        if (instance == null) {
            instance = new MyFoodFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myfood, container, false);
        recyclerFoodStore = rootView.findViewById(R.id.recyclerFoodStore);
        userText = rootView.findViewById(R.id.userText);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getUid() !=null){
            setUpRecyclerView();
        }
        return rootView;
    }


    private void setUpRecyclerView() {
        recyclerFoodStore.setHasFixedSize(true);
        recyclerFoodStore.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Query query = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(mAuth.getUid())
                .child("FoodStores")
                .orderByChild("rate");
        query.addChildEventListener(this);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<FoodStore>()
                .setQuery(query, FoodStore.class)
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
                View view = getLayoutInflater().inflate(R.layout.item_foodstore, viewGroup, false);
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
            if (model.getImageUrls().size() != 0) {
                Glide.with(getActivity())
                        .load(model.getImageUrls().get(0))
                        .into(foodImg);
                foodImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("FOOD", model);
                    intent.putExtra("FOOD", bundle);
                    startActivity(intent);


                }
            });
        }
    }




    /**
     * Override void
     */


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        mAuth.removeAuthStateListener(this);
        if (adapter != null) {
            adapter.stopListening();
        }

    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).child("Info")
                    .addValueEventListener(this);
            setUpRecyclerView();
            adapter.startListening();

        }


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        userText.setText("User : " + user.getName());
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

}
