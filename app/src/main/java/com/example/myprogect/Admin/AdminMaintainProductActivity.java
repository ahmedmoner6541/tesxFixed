package com.example.myprogect.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprogect.R;
import com.example.myprogect.Seller.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {

    private Button applychangesBtn , deletbTtn;
    private EditText name, price, describtion;
    private ImageView imageView;

    private String productId = "";


    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        applychangesBtn = findViewById(R.id.aplly_changes_btn);
        deletbTtn = findViewById(R.id.delet_product_btn);

        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        describtion = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();

        applychangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applayChanges();
            }
        });
        deletbTtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletThisProduct();
            }
        });

    }

    private void deletThisProduct() {

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductActivity.this, "تم جذف المنتج", Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applayChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = describtion.getText().toString();

        if (pName.equals("")){
            Toast.makeText(this, "\n" +"اكتب اسم المنتج", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals("")){
            Toast.makeText(this, "\n" +"اكتب سعر المنتج", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals("")){
            Toast.makeText(this, "\n" + "اكتب وصف  المنتج ", Toast.LENGTH_SHORT).show();     }
        else {

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("description",pDescription);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);
        ////////////    import updateChildren
            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductActivity.this, "\n" +                                "تم تغيير التطبيق بنجاح ..", Toast.LENGTH_SHORT).show();

                        Intent intent =new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });




        }
    }

    private void displaySpecificProductInfo() {
     //add
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();


                    name.setText(pName);
                    price.setText(pPrice);
                    describtion.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
