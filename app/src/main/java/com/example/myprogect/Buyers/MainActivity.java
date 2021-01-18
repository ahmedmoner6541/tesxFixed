package com.example.myprogect.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprogect.R;
import com.example.myprogect.Seller.SellersRegistritionActivity;
import com.example.myprogect.Sellers.SellersHomeMainActivity;
import com.example.myprogect.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {



    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerBigin ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNowButton =   findViewById(R.id.main_join_now_btn);
        loginButton =  findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        sellerBigin=  findViewById(R.id.seller_begain);

      Paper.init(this);


        sellerBigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, SellersRegistritionActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }
    @Override///////// BECAUSE EXEPTION
    protected void onDestroy() {
        try {
            if (loadingBar != null)
                loadingBar.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){

            Intent intent =new Intent(MainActivity.this , SellersHomeMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "الرجاء الانتظار ، لقد قمت بتسجيل الدخول بالفعل ", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "هذا الجساب" + phone + " غبر موجود", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
  }
}