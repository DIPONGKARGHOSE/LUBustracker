package com.example.lubustracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class DriverRegisterActivity extends AppCompatActivity implements
        View.OnClickListener
{
    private static final String TAG = "RegisterActivity";

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView textView;
    //vars
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        this.setTitle("Sign Up");

        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.driverSigninid).setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();


        //adding back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:{
                Log.d(TAG, "onClick: attempting to register.");
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(DriverRegisterActivity.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(DriverRegisterActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                }
               else  if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(DriverRegisterActivity.this,"Please Confirm Password",Toast.LENGTH_SHORT).show();
                }

               else  if(password.length()<6){
                    Toast.makeText(DriverRegisterActivity.this,"Password Too Short",Toast.LENGTH_SHORT).show();
                }


              else if(password.equals(confirmPassword)){
                    mProgressBar.setVisibility(View.GONE);

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DriverRegisterActivity.this, "Successfully Registered,plasec Verification Email", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(DriverRegisterActivity.this, DriverLoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    return;
                                                } else {
                                                    Toast.makeText(DriverRegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(DriverRegisterActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            }
            case R.id.driverSigninid: {
                Intent intent = new Intent(DriverRegisterActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            }


        }
    }

