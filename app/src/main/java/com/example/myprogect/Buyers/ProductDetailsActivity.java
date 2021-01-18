package com.example.myprogect.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myprogect.R;
import com.example.myprogect.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCarBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productprice, productDiscribtion, productNaem, productsalarynametv;
    private String productId = "" , state = "Normal" ;

    public static String productsalarynametxt;

    public static int NUM_CART;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");
        productsalarynametxt = getIntent().getStringExtra("hhh");

        Toast.makeText(this, "sale is" + productsalarynametxt, Toast.LENGTH_SHORT).show();
        numberButton = findViewById(R.id.number_btn);
        addToCarBtn = findViewById(R.id.pd_add_to_cart_button);

        productImage = findViewById(R.id.product_image_detailes);
        productNaem = findViewById(R.id.product_name_detailes);
        productDiscribtion = findViewById(R.id.product_discribtion_detailes);
        productprice = findViewById(R.id.product_price_detailes);

        productsalarynametv = findViewById(R.id.product_price_salary_name_detailess);


        addToCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Order placed") || state.equals("Order shipped")) {

                    Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products , once your order is shapped or configration ??????", Toast.LENGTH_LONG).show();
                } else {

                    if (!state.equals("Order placed") || !state.equals("Order shipped")) {
                        addToCartList();
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getProductDetailes();// wad in on create
        ckeckOrserState();
    }

    private void addToCartList() {

        String saveCurrentTiem, saveCurrentDate;

        Calendar calForData = Calendar.getInstance();

        SimpleDateFormat currentData = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentData.format(calForData.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTiem = currentTime.format(calForData.getTime());

        final DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cart list");
        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("pid", productId);
        cartMap.put("pname", productNaem.getText().toString());
        cartMap.put("price", productprice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTiem);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        cartMap.put("salaryName", productsalarynametxt);


        cartlistRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartlistRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productId)
                                    .updateChildren(cartMap)

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "تمت الإضافة إلى قائمة عربة التسوق", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }

                    }
                });

    }


    private void getProductDetailes() {
////////////////////////////////////////////////////////////////////////////////////////////////////Products
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //        ????????????????????????????????????
                    Products products = snapshot.getValue(Products.class);


                    productsalarynametv.setText("اسم البائع : " + productsalarynametxt);
                    productNaem.setText(products.getPname());
                    productprice.setText(products.getPrice());
                    productDiscribtion.setText(products.getDescription());


                    Picasso.get().load(products.getImage()).into(productImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ckeckOrserState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        state = "order shipped";
                    } else if (shippingState.equals("not shipped")) {
                        state = "order placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}