package com.example.lubustracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class driver_Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton route1,route2,route3,route4;
    Spinner spinner;
    public   int num;
    int ar[]={0,1,2,3,4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        spinner = findViewById(R.id.spinner2);

        this.setTitle("Driver Profile");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.routes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        Button button = findViewById(R.id.haha);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(driver_Profile.this,String.valueOf(num),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(driver_Profile.this, Driver_MapsActivity.class);
                intent.putExtra("Route",num);
                startActivity(intent);

                finish();

            }
        });
        // Toast.makeText(this,String.valueOf(num),Toast.LENGTH_SHORT).show();

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        num=ar[position];
       // Toast.makeText(parent.getContext(), String.valueOf(num), Toast.LENGTH_SHORT).show();
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }public int ponit(){
        return num;
    }






}
