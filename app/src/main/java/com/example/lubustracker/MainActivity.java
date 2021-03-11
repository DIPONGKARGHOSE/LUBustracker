package com.example.lubustracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity{
    private Button mDriver, mStudent;
    FirebaseUser firebaseAuth;
    ViewFlipper v_flipper;
    //int c = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDriver = findViewById(R.id.driver);
        mStudent = findViewById(R.id.student);

        this.setTitle("Home");



        v_flipper = findViewById(R.id.viewFlipper);
        int images[] = {R.drawable.v1
                ,R.drawable.v7,R.drawable.v10};
        for(int image : images){
            flipperImages(image);
        }



        mDriver.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
               // c = 1;
               // System.out.println("c="+c);
                Intent intent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);
             //   finish();
            }
        });


        mStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // c = 2;
               // System.out.println("c="+c);
                Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
                startActivity(intent);

               // finish();

            }
        });
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

   /* public void autologin(){
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseAuth != null && c == 1)
        {

            Intent intent = new Intent(MainActivity.this,driver_Profile.class);
            startActivity(intent);
            finish();

        }
        else if(firebaseAuth != null && c == 2)
        {

            Intent intent = new Intent(MainActivity.this,Profile.class);
            startActivity(intent);
            finish();

        }
    }*/
    @Override
    protected void onStart() {
        super.onStart();
       // autologin();


    }

}
