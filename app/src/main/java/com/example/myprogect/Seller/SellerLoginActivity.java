package com.example.myprogect.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class SellerLoginActivity extends AppCompatActivity {

    private Button regestersellerButton;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.seller_login__email);
        passwordInput  = findViewById(R.id.seller_login__password);

        regestersellerButton = findViewById(R.id.seller_login_btn);
        regestersellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginseler();
            }
        });

    }

    private void loginseler() {
          String email = emailInput.getText().toString();
         String password = passwordInput.getText().toString();

        if (!email.equals("") && !password.equals("")) {

            loadingBar.setTitle("تسجيل الدخول إلى حساب البائع");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();

                                Intent intent = new Intent(SellerLoginActivity.this, SellersHomeMainActivity.class);
                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });


        }else {
            Toast.makeText(this, "استمارة تسجيل الدخول كاملة", Toast.LENGTH_SHORT).show();
        }

    }

}