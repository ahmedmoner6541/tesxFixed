package com.example.myprogect.Admin;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Buyers.ConfirmFinalOrderActivity;
import com.example.myprogect.Buyers.HomeActivity;
import com.example.myprogect.Buyers.Prevalent;
import com.example.myprogect.R;
import com.example.myprogect.model.AdminOrsers;
import com.example.myprogect.model.Cart;
import com.example.myprogect.model.Products;
import com.example.myprogect.prevalent.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private static final String TAG = "zxcAdminNewOrdersActivity";
    RecyclerView orderlist;
    DatabaseReference orderRef, AdminViewRef, soldProductsRef;

    ConfirmFinalOrderActivity confirmFinalOrderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("ordersNotApproved");
        AdminViewRef = FirebaseDatabase.getInstance().getReference().child("cart list").child("Admin View");
        soldProductsRef = FirebaseDatabase.getInstance().getReference().child("soldProducts");

        orderlist = findViewById(R.id.order_List);
        orderlist.setLayoutManager(new LinearLayoutManager(this));
//        orderlist.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrsers> options =
                new FirebaseRecyclerOptions.Builder<AdminOrsers>()
                        .setQuery(orderRef, AdminOrsers.class)

                        .build();

        FirebaseRecyclerAdapter<AdminOrsers, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrsers, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull AdminOrsers model) {

                        holder.userName.setText("الاسم : " + model.getName());
                        holder.userPhoneNumber.setText("الهاتف : " + model.getPhone());
                        holder.userTotalPrice.setText("المبلغ الإجمالي : " + model.getTotalAmount());
                        holder.userDataTime.setText("تم الطلب في : " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("عنوان الطلب : " + model.getAddress());

                        holder.showOrders.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uID = getRef(position).getKey();///**********

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence option[] = new CharSequence[]
                                        {
                                                "نعم",
                                                "لا"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("هل توافق علي اتمام الاوردر ؟");
                                builder.setItems(option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (which == 0) {
                                            String uID = getRef(position).getKey();///**********

                                            test(uID);

                                        } else {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);

                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();

    }

    private void test(String uID) {

        final String saveCurrentDate, saveCurrentTiem;
        Calendar calForData = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentData.format(calForData.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTiem = currentTime.format(calForData.getTime());


        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("orders")
                .child("ordersApproved")
                .child(uID);


        HashMap<String, Object> orderMap = new HashMap<>();

        AdminOrsers model = new AdminOrsers();

        orderMap.put("totalAmount", model.getTotalAmount());  // price
        orderMap.put("address", model.getAddress());
        orderMap.put("time", saveCurrentTiem);  ////    holder.userDataTime.setText("تم الطلب في : "+model.getDate() + " " + model.getTime());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("name", uID);
        orderMap.put("phone", model.getPhone());
        orderMap.put("city", model.getCity());
        orderMap.put("state", "shipped");


        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("orders")
                            .child("ordersNotApproved")
                            .child(uID)

                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplication(), "  تم اتمام طلبك النهائي بنحاج  hhhhhhhhhh", Toast.LENGTH_SHORT).show();
                                      //  Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     //   startActivity(intent);
                                     //   finish();

                                        addSoldProduct(uID);
                                    }
                                }
                            });




                }
            }
        });

    }

    private void addSoldProduct(String uID) {
        AdminViewRef.child(uID).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: "+ dataSnapshot.getKey());
                    // User model = dataSnapshot.getValue(User.class);
                    Products products = dataSnapshot.getValue(Products.class);
                    Log.d(TAG, "onDataChange: "+products.getSid());

                    soldProductsRef.child(products.getSid()).child(products.getPid()).setValue(dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}


