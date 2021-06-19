package com.example.pas_23raihanxrpl2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DataActivity extends AppCompatActivity {

    private ArrayList<ContactData> List;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    EditText search;
    CharSequence searchresult = "";
    private final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        recyclerView = findViewById(R.id.rv_main);
        addData();
    }

    private void addData() {
        TextView tv_item = findViewById(R.id.tv_item);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ContactData profile = dataSnapshot.getValue(ContactData.class);
                    List.add(profile);
                }
                if (List != null) {
                    if (List.isEmpty()) tv_item.setVisibility(View.VISIBLE);
                    else tv_item.setVisibility(View.INVISIBLE);
                } else tv_item.setVisibility(View.INVISIBLE);

                adapter = new RecyclerViewAdapter(getApplicationContext(), List);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        myRef.orderByChild("phone").equalTo(List.get(position).getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String key = "";
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    key = childSnapshot.getKey();
                                    Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
                                }


                                String name, phone, email;

                                name = List.get(position).getName();
                                email = List.get(position).getEmail();
                                phone = List.get(position).getPhone();

                                Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                intent.putExtra("phone", phone);
                                intent.putExtra("key", key);
                                startActivityForResult(intent, REQUEST_CODE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                addData();
        }
    }
}
