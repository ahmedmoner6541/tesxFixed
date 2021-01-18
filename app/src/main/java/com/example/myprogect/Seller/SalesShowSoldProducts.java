package com.example.myprogect.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myprogect.R;
import com.example.myprogect.ViewHolder.CartViewHoleder;
import com.example.myprogect.model.Cart;
import com.example.myprogect.model.Products;
import com.example.myprogect.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SalesShowSoldProducts extends AppCompatActivity {

    private RecyclerView productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference carttRef;
    private String userID = "";
    String name;


    private DatabaseReference alPproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_show_sold_products);


        userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.product_List_show_sales);
//        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        productsList.setLayoutManager(layoutManager);


        FirebaseDatabase.getInstance().getReference().child("selers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.child("name").getValue().toString();


                        Toast.makeText(SalesShowSoldProducts.this, "البائع" + name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    protected void onStart() {
        super.onStart();

        carttRef = FirebaseDatabase.getInstance().getReference().child("cart list")
                .child("Admin View")
                .child(userID)
                .child("Products");

      //  alPproduct = FirebaseDatabase.getInstance().getReference().child("Products");


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(

   carttRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Cart.class)

                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHoleder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHoleder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHoleder holder, int position, @NonNull Cart model) {

                        holder.textproducQuantity.setText("الكميه =" + model.getQuantity());
                        holder.textproducprice.setText("السعر =" + model.getPrice());
                        holder.textproductName.setText("اسم المنتج =" + model.getPname());

                        //   holder.textproductNameSalery.setText(model.get);


                    }

                    @NonNull
                    @Override
                    public CartViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHoleder holeder = new CartViewHoleder(view);
                        return holeder;
                    }
                };
        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}





















