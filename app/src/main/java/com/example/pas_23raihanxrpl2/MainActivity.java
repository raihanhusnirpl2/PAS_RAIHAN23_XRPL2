package com.example.pas_23raihanxrpl2;

import  androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String name, phone, email;
    private ArrayList<ContactData> List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //buat arraylist find view by id dan ngekonek firebase
        List = new  ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  simpanan = database.getReference("contacts");
        TextInputEditText txt_email = findViewById(R.id.txt_email);
        TextInputEditText txt_name = findViewById(R.id.txt_name);
        TextInputEditText txt_phone = findViewById(R.id.txt_phone);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_intent = findViewById(R.id.btn_intent);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txt_name.getText().toString();
                email = txt_email.getText().toString();
                phone = txt_phone.getText().toString();


                if (name.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill the empty bar !", Toast.LENGTH_SHORT).show();
                } else {

                    String key = simpanan.push().getKey();
                    ContactData profile = new ContactData(name, phone,email);
                  simpanan.child(key).setValue(profile);


                    txt_name.setText("");
                    txt_email.setText("");
                    txt_phone.setText("");


                    Toast.makeText(getApplicationContext(), "Data has been added !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                startActivity(intent);
            }
        });
    }
}