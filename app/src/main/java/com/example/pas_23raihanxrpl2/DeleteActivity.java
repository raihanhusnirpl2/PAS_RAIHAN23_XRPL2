package com.example.pas_23raihanxrpl2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteActivity extends AppCompatActivity {
    private String name, email, phone, key;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        TextInputEditText txt_name = findViewById(R.id.txt_name_edit);
        TextInputEditText txt_email = findViewById(R.id.txt_email_edit);
        TextInputEditText txt_phone = findViewById(R.id.txt_phone_edit);
        Button btn_delete = findViewById(R.id.btn_delete);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            email = bundle.getString("email");
            phone = bundle.getString("phone");
            key = bundle.getString("key");

            txt_name.setText(name);
            txt_email.setText(email);
            txt_phone.setText(phone);
        }


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child("contacts").child(key);

                ProgressDialog progressDialog = new ProgressDialog(DeleteActivity.this);
                progressDialog.setMessage("Menghapus data anda...");
                progressDialog.show();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue();
                        }
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Data has been deleted !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }

                });
            }
        });
    }
}
