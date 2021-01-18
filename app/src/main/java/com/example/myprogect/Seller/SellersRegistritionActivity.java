package com.example.myprogect.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprogect.R;
import com.example.myprogect.Sellers.SellersHomeMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SellersRegistritionActivity extends AppCompatActivity {
String ex ;
    private Button sellerLoginButton;
    private EditText nameInput , phoneInput , emailInput , passwordInput , addressInput ;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    String sid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_registrition);

        sellerLoginButton = findViewById(R.id.seller_Allredy_have_account_btn);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        nameInput = findViewById(R.id.seller_name_item_sall222);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);


        registerButton = findViewById(R.id.seller_login_register_btn);


        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellersRegistritionActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();

            }
        });
    }

    private void registerSeller() {

        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();


//        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("") ){
        if (name.isEmpty()) {
            nameInput.setError("ادخل الاسم ");
            nameInput.requestFocus();
        }
        if(phone.isEmpty() || phone.length() < 10) {
            phoneInput.setError("رقم الهاتف غير صحيح");
            phoneInput.requestFocus();
            return;
        }
        else if (email.isEmpty()) {
            emailInput.setError("ادخل البريد الالكنروني ");
            emailInput.requestFocus();
        }  else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.setError("  البريد الالكنروني غير صحيح ");
            emailInput.requestFocus();
        }
        else if (password.isEmpty() ||password.length()< 6) {
            passwordInput.setError(" كلمه المرور لا تقل عن 6 ارقام");
            passwordInput.requestFocus();
        }else if (address.isEmpty()) {
            addressInput.setError("ادخل العنوان");
            addressInput.requestFocus();

        }else  {


            loadingBar.setTitle("إنشاء حساب بائع ");
            loadingBar.setMessage("يُرجى الانتظار ، بينما نتحقق من بياناتك");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        final DatabaseReference rootRef ;
                        rootRef = FirebaseDatabase.getInstance().getReference();

       sid = mAuth.getCurrentUser().getUid();

                        HashMap<String , Object> sellerMap = new HashMap<>();

                        sellerMap.put("uid" , sid);
                        sellerMap.put("phone" , phone);
                        sellerMap.put("email" , email);
                        sellerMap.put("address" , address);
                        sellerMap.put("name" , name);

                        rootRef.child("selers").child(sid).updateChildren(sellerMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingBar.dismiss();
              //   Toast.makeText(SellersRegistritionActivity.this, "onComplete", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SellersRegistritionActivity.this, SellersHomeMainActivity.class);
                                        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                    else   {
                        Toast.makeText(SellersRegistritionActivity.this, " خطا في البيانات", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });



        }
            }
         }
