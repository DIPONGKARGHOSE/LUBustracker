package com.example.lubustracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginActivity  extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private EditText mEmail, mPassword, mroute,nbus;
            TextView fpass;
    private Button mLogin;



    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.axtivity_driver_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mroute = (EditText) findViewById(R.id.route);

        this.setTitle("Sign In");

        fpass =  findViewById(R.id.forgorPasswordId);
        //adding back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mLogin = (Button) findViewById(R.id.login);
        TextView textView = findViewById(R.id.driverSignupid);
        firebaseAuth = FirebaseAuth.getInstance();


        try {
            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    final String rout = mroute.getText().toString();
//                    int result = Integer.parseInt(rout);
                    if (!rout.equals( "leadinguniversity")) {
                        Toast.makeText(DriverLoginActivity.this, "Please Enter Vaild Code", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(email)) {
                        Toast.makeText(DriverLoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(DriverLoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                    Intent intent = new Intent(DriverLoginActivity.this, driver_Profile.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(DriverLoginActivity.this, "plasce Verification Email ", Toast.LENGTH_LONG).show();
                                                }
                                                //Intent intent = new Intent(DriverLoginActivity.this, Driver_MapsActivity.class);

                                            } else {
                                                Toast.makeText(DriverLoginActivity.this, "Login UnSuccessFull", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        } catch (Exception e) {
                            Toast.makeText(DriverLoginActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
           textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DriverLoginActivity.this,DriverRegisterActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            });

        } catch (Exception e) {
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show();
        }
        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLoginActivity.this, Forget_Password.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

    }

}