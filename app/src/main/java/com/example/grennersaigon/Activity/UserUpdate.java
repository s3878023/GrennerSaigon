//package com.example.grennersaigon;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.grennersaigon.Model.User;
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
//    private Button buttonLogout, buttonSubmit, buttonShowNames;
//    private EditText editTextName, editTextAge, editTextAddress;
//    private TextView textViewUserUid, textViewShowNames;
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
//        buttonLogout = findViewById(R.id.logout);
//        buttonSubmit = findViewById(R.id.buttonSubmit);
//        buttonShowNames = findViewById(R.id.buttonShowNames);
//        textViewUserUid = findViewById(R.id.textViewUserUid);
//        textViewShowNames = findViewById(R.id.textViewShowNames);
//
//        if (user != null) {
//            String userUid = "User UID: " + user.getUid();
//            textViewUserUid.setText(userUid);
//        }
//
//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });
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
//                    User user = new User(name, age, address, userId, ownSite, joinSite);
//                    saveInformationToFirestore(user);
//                }
//            }
//        });
//
//        buttonShowNames.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Show names from ownSite where userId matches the current user ID
//                showNamesFromFirestore();
//            }
//        });
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
//                    // Handle failure
//                });
//    }
//
//    private void showNamesFromFirestore() {
//        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("User")
//                .document(userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        User user = task.getResult().toObject(User.class);
//                        if (user != null) {
//                            // Use user.getOwnSite() to access the list of ownSite
//                            StringBuilder namesBuilder = new StringBuilder();
//                            for (String site : user.getOwnSite()) {
//                                namesBuilder.append(site).append("\n");
//                            }
//                            textViewShowNames.setText(namesBuilder.toString());
//                        }
//                    } else {
//                        // Handle errors
//                    }
//                });
//    }
//}
package com.example.grennersaigon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grennersaigon.Model.User;
import com.example.grennersaigon.R;
import com.example.grennersaigon.authenticate.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class UserUpdate extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button buttonSubmit;
    private EditText editTextName, editTextAge, editTextAddress;
    private FirebaseUser user;

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
                    ArrayList<String> ownSite = new ArrayList<>();
                    ArrayList<String> joinSite = new ArrayList<>();
                    User user = new User(name, age, address, userId);
                    saveInformationToFirestore(user);

                    Intent intent = new Intent(UserUpdate.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    }
                });
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
                })
                .addOnFailureListener(e -> {
                });
    }
}
