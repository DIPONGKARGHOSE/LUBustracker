package com.example.lubustracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    Spinner spinner;
    static   public int la;
    double la1;
    RadioButton Routes0,Routes1,Routes2,Routes3,Routes4;
    public double []ar={24.896629,24.903049,24.905953,24.906239,24.905342,24.905106,24.905954,24.907102,24.909814,24.911129,24.910277,24.910982,24.912859,24.879817,24.895242,24.894768,24.892687,24.890468,24.890493,24.896150,24.898506,24.903907,24.923963,24.917791,24.891711,24.913651,24.885892,24.877789,24.867591,24.860972};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        spinner = findViewById(R.id.spinner1);
        this.setTitle("Student Profile");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Button button = findViewById(R.id.BUT);

        Routes0 = (RadioButton) findViewById(R.id.Routes0);
        Routes1 = (RadioButton) findViewById(R.id.Routes1);
        Routes2 = (RadioButton) findViewById(R.id.Routes2);
        Routes3 = (RadioButton) findViewById(R.id.Routes3);
        Routes4 = (RadioButton) findViewById(R.id.Routes4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Routes0.isChecked()) {
                    la=0;
                } else if (Routes1.isChecked()) {
                    la=1;
                } else if (Routes2.isChecked()) {
                    la=2;
                } else if (Routes3.isChecked()) {
                    la=3;
                } else if (Routes4.isChecked()) {
                    la=4;
                }
                // Toast.makeText(getApplicationContext(), String.valueOf(la), Toast.LENGTH_LONG).show(); // print the value of selected super star

                Intent intent = new Intent(Profile.this, Student_MapsActivity.class);
                intent.putExtra("lat",la1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        la1=ar[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }public static int  ponit(){
        return la;
    }
}
