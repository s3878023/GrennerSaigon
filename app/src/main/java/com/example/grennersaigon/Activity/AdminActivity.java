package com.example.grennersaigon.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.grennersaigon.Model.PinModel;
import com.example.grennersaigon.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PinAdapter pinAdapter;
    private List<PinModel> pinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pinList = new ArrayList<>();
        pinAdapter = new PinAdapter(pinList);
        recyclerView.setAdapter(pinAdapter);

        fetchPinsFromFirestore();
    }

    private void fetchPinsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pinList.clear();
                        for (PinModel pin : task.getResult().toObjects(PinModel.class)) {
                            pinList.add(pin);
                        }
                        pinAdapter.notifyDataSetChanged();
                    } else {
                        // Handle error
                    }
                });
    }
}
