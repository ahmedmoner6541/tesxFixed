package com.example.myprogect.Buyers;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.R;
import com.example.myprogect.ViewHolder.CartViewHoleder;
import com.example.myprogect.model.Cart;
import com.example.myprogect.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;
    private TextView txtTotalAmount , txtMsg1;
    private int overTotalPrice = 0;
    int oneTypeProductPrice ;

    public static int NUM_CART ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        recyclerView = findViewById(R.id.cart_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);





        nextBtn = findViewById(R.id.next_proccess_btn);
        txtTotalAmount = findViewById(R.id.page_title);
        txtMsg1 = findViewById(R.id.msgl);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





              //  txtTotalAmount.setText("Total price = $" + overTotalPrice);/// case for crach
                txtTotalAmount.setText("المبلغ الكلي  " + (overTotalPrice));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        //////////////////

        ////////////////////////
        ckeckOrserState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                        .build();



        FirebaseRecyclerAdapter<Cart , CartViewHoleder> adapter
                 = new FirebaseRecyclerAdapter<Cart, CartViewHoleder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHoleder holder, int position, @NonNull final Cart model) {
                NUM_CART =  getItemCount();



                holder.textproducQuantity.setText("الكميه ="+model.getQuantity());
                holder.textproducprice.setText("السعر ="+model.getPrice());
                holder.textproductName.setText("الاسم ="+model.getPname());

                Toast.makeText(CartActivity.this, ""+NUM_CART, Toast.LENGTH_SHORT).show();
                oneTypeProductPrice =(( Integer.parseInt(model.getPrice()))* (Integer.parseInt(model.getQuantity())));
                overTotalPrice = overTotalPrice + oneTypeProductPrice;
                txtTotalAmount.setText("المبلغ الكلي  " + (overTotalPrice));//from comments

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options [] = new CharSequence[]
                                {
                                        "تعديل",
                                        "حذف"
                                };
                        AlertDialog .Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle(" خيارات عربة التسوق:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0 ){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);

                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (which ==1 ){
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())


                                            .removeValue()

                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "تم مسح العنصر", Toast.LENGTH_SHORT).show();
                                                        NUM_CART = position  ;

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHoleder holeder = new CartViewHoleder(view);
                return holeder;
            }
        };
        recyclerView.setAdapter(adapter);



        adapter.startListening();

    }
    private void ckeckOrserState(){
        final DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("ordersNotApproved").child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){

                        txtTotalAmount.setText("عزيزي العميل : "+userName+"\n تم شحن الطلب بنجاح");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setText("تتهنئة..... طلبك قيد التحضير سيتم التحقق منه في اقرب وقت");
                        txtMsg1.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "يكنك شراء المزيد من المنتجات بمجرد استلام طلبك الأول", Toast.LENGTH_SHORT).show();

                    }else if (shippingState.equals("not shipped")){
                        txtTotalAmount.setText( "حالة الشحن = لم يتم الشحن");

                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "يمكنك شراء المزيد من المنتجات بمجرد استلام طلبك الأول", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}