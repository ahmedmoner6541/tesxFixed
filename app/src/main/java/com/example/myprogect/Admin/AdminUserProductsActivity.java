package com.example.myprogect.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Buyers.CartActivity;
import com.example.myprogect.Buyers.Prevalent;
import com.example.myprogect.R;
import com.example.myprogect.ViewHolder.CartViewHoleder;
import com.example.myprogect.model.Cart;
import com.example.myprogect.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList ;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference carttRef ;
    private String userID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        userID = getIntent().getStringExtra("uid");
        productsList = findViewById(R.id.product_List);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
    // the  products of user id
carttRef = FirebaseDatabase.getInstance().getReference().child("cart list").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart > options =
                new  FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(carttRef , Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart , CartViewHoleder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHoleder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHoleder holder, int position, @NonNull Cart model) {

                        holder.textproducQuantity.setText("الكميه ="+model.getQuantity());
                        holder.textproducprice.setText("السعر ="+model.getPrice());
                        holder.textproductName.setText("اسم المنتج ="+model.getPname());

                     //   holder.textproductNameSalery.setText(model.get);


                    }

                    @NonNull
                    @Override
                    public CartViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHoleder holeder = new CartViewHoleder(view);
                        return holeder;
                    }
                };
        productsList.setAdapter(adapter);
        adapter.startListening();

    }
}
























///////////////////////////////////////////////////////////////////////////////////
//package com.example.myprogect.Admin;
//
//        import android.os.Bundle;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Toast;
//
//        import androidx.annotation.NonNull;
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.example.myprogect.Buyers.CartActivity;
//        import com.example.myprogect.Buyers.Prevalent;
//        import com.example.myprogect.R;
//        import com.example.myprogect.ViewHolder.CartViewHoleder;
//        import com.example.myprogect.model.Cart;
//        import com.example.myprogect.model.Products;
//        import com.firebase.ui.database.FirebaseRecyclerAdapter;
//        import com.firebase.ui.database.FirebaseRecyclerOptions;
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//        import com.google.firebase.database.DatabaseReference;
//        import com.google.firebase.database.FirebaseDatabase;
//        import com.google.firebase.database.ValueEventListener;
//
//public class  AdminUserProductsActivity extends AppCompatActivity {
//
//    private RecyclerView productsList ;
//    private RecyclerView.LayoutManager layoutManager;
//    private DatabaseReference carttRef ;
//    private String userID = "";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_user_products);
//
//        userID = getIntent().getStringExtra("uid");
//
//        productsList = findViewById(R.id.product_List);
//        productsList.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//
//        productsList.setLayoutManager(layoutManager);
//
///////////////////////////////
//
//
//        DatabaseReference orderRef2;
//        orderRef2 = FirebaseDatabase.getInstance().getReference().child("orders")
//                .child("ordersNotApproved")
//                .child(userID)
//                .child("Products");;
//
//
//
//
//
///////////////////////////
//        carttRef = FirebaseDatabase.getInstance().getReference()
//                .child("cart list")
//                .child("Admin View")
//                .child(userID)
//                .child("Products");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<Cart > options =
//                new  FirebaseRecyclerOptions.Builder<Cart>()
//                        .setQuery(carttRef , Cart.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Cart , CartViewHoleder> adapter =
//                new FirebaseRecyclerAdapter<Cart, CartViewHoleder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull CartViewHoleder holder, int position, @NonNull Cart model) {
//
//                        holder.textproducQuantity.setText("الكميه ="+model.getQuantity());
//                        holder.textproducprice.setText("السعر ="+model.getPrice());
//                        holder.textproductName.setText("اسم المنتج ="+model.getPname());
//
//                        holder.textproductNameSalery.setText(model.get);
//
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public CartViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
//                        CartViewHoleder holeder = new CartViewHoleder(view);
//                        return holeder;
//                    }
//                };
//        productsList.setAdapter(adapter);
//        adapter.startListening();
//
//    }
//}
//
//
