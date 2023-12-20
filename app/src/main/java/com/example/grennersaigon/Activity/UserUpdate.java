//package com.example.grennersaigon.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.grennersaigon.Model.User;
//import com.example.grennersaigon.R;
//import com.example.grennersaigon.authenticate.Login;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class UserUpdate extends AppCompatActivity {
//
//    private FirebaseAuth auth;
//    private Button buttonSubmit;
//    private EditText editTextName, editTextAge, editTextAddress;
//    private FirebaseUser user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_update);
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }
//
//        editTextName = findViewById(R.id.editTextName);
//        editTextAge = findViewById(R.id.editTextAge);
//        editTextAddress = findViewById(R.id.editTextAddress);
//        buttonSubmit = findViewById(R.id.buttonSubmit);
//
//
//
//        // Fetch existing user data from Firestore and populate EditText fields
//        fetchExistingUserData();
//
//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = editTextName.getText().toString().trim();
//                String age = editTextAge.getText().toString().trim();
//                String address = editTextAddress.getText().toString().trim();
//                String userId = user.getUid();
//
//                if (!name.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
//                    ArrayList<String> ownSite = new ArrayList<>();
//                    ArrayList<String> joinSite = new ArrayList<>();
//                    User user = new User(name, age, address, userId);
//                    saveInformationToFirestore(user);
//
//                    Intent intent = new Intent(UserUpdate.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//
//
//        });
//
//    }
//
//    private void fetchExistingUserData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        db.collection("User")
//                .document(userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        User existingUser = task.getResult().toObject(User.class);
//                        if (existingUser != null) {
//                            // Populate EditText fields with existing user data
//                            editTextName.setText(existingUser.getName());
//                            editTextAge.setText(existingUser.getAge());
//                            editTextAddress.setText(existingUser.getAddress());
//                        }
//                    } else {
//                        // Handle errors
//                    }
//                });
//    }
//
//    private void saveInformationToFirestore(User user) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("User")
//                .document(user.getUserId())  // Use userId as the document ID
//                .set(user)
//                .addOnSuccessListener(documentReference -> {
//                    editTextName.setText("");
//                    editTextAge.setText("");
//                    editTextAddress.setText("");
//                })
//                .addOnFailureListener(e -> {
//                });
//    }
//}

package com.example.grennersaigon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grennersaigon.Model.User;
import com.example.grennersaigon.R;
import com.example.grennersaigon.authenticate.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class UserUpdate extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button buttonSubmit;
    private EditText editTextName, editTextAge, editTextAddress;
    private FirebaseUser user;
    private RecyclerView recyclerViewOwnedSites, recyclerViewJoinedSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        recyclerViewOwnedSites = findViewById(R.id.ReViewOwnedSites);
        recyclerViewJoinedSites = findViewById(R.id.ReViewJoinedSites);

        // Set up RecyclerView
        recyclerViewOwnedSites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJoinedSites.setLayoutManager(new LinearLayoutManager(this));

        // Fetch existing user data from Firestore and populate EditText fields
        fetchExistingUserData();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String userId = user.getUid();

                if (!name.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
                    User user = new User(name, age, address, userId);
                    saveInformationToFirestore(user);

                    Intent intent = new Intent(UserUpdate.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserUpdate.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchExistingUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        db.collection("User")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User existingUser = task.getResult().toObject(User.class);
                        if (existingUser != null) {
                            // Populate EditText fields with existing user data
                            editTextName.setText(existingUser.getName());
                            editTextAge.setText(existingUser.getAge());
                            editTextAddress.setText(existingUser.getAddress());
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(UserUpdate.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                });

        fetchOwnedSites(userId);
        fetchJoinedSites(userId);
    }

    private void saveInformationToFirestore(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(user.getUserId())  // Use userId as the document ID
                .set(user)
                .addOnSuccessListener(documentReference -> {
                    editTextName.setText("");
                    editTextAge.setText("");
                    editTextAddress.setText("");
                    Toast.makeText(UserUpdate.this, "Information saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(UserUpdate.this, "Failed to save information", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchOwnedSites(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins")
                .whereEqualTo("siteOwner", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> ownedSites = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            ownedSites.add(document.getString("siteName"));
                        }
                        OwnedSitesAdapter ownedSitesAdapter = new OwnedSitesAdapter(ownedSites);
                        recyclerViewOwnedSites.setAdapter(ownedSitesAdapter);
                    } else {
                        // Handle errors
                        Toast.makeText(UserUpdate.this, "Error fetching owned sites", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchJoinedSites(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins")
                .whereArrayContains("siteMembers", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> joinedSites = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            joinedSites.add(document.getString("siteName"));
                        }
                        JoinedSitesAdapter joinedSitesAdapter = new JoinedSitesAdapter(joinedSites);
                        recyclerViewJoinedSites.setAdapter(joinedSitesAdapter);
                    } else {
                        // Handle errors
                        Toast.makeText(UserUpdate.this, "Error fetching joined sites", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Create a custom adapter for owned sites
    private static class OwnedSitesAdapter extends RecyclerView.Adapter<OwnedSitesAdapter.ViewHolder> {
        private final ArrayList<String> ownedSites;

        public OwnedSitesAdapter(ArrayList<String> ownedSites) {
            this.ownedSites = ownedSites;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(ownedSites.get(position));
        }

        @Override
        public int getItemCount() {
            return ownedSites.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    // Create a custom adapter for joined sites
    private static class JoinedSitesAdapter extends RecyclerView.Adapter<JoinedSitesAdapter.ViewHolder> {
        private final ArrayList<String> joinedSites;

        public JoinedSitesAdapter(ArrayList<String> joinedSites) {
            this.joinedSites = joinedSites;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(joinedSites.get(position));
        }

        @Override
        public int getItemCount() {
            return joinedSites.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}

