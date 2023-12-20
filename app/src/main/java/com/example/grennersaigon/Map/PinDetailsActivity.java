//package com.example.grennersaigon.Map;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.grennersaigon.R;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.widget.EditText;
//import android.widget.Toast;
//public class PinDetailsActivity extends AppCompatActivity {
//    private String documentId;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pin_details);
//        documentId = getIntent().getStringExtra("documentId");
//        TextView pinNameTextView = findViewById(R.id.pinNameTextView);
//        TextView siteAddressTextView = findViewById(R.id.siteAddressTextView);
//        TextView siteDescriptionTextView = findViewById(R.id.siteDescriptionTextView);
//        TextView dateTimeTextView = findViewById(R.id.dateTimeTextView);
//        TextView siteOwnerTextView = findViewById(R.id.siteOwnerTextView);
//        TextView siteReportTextView = findViewById(R.id.siteReportTextView);
//        ListView siteMembersListView = findViewById(R.id.siteMembersListView);
//        Button editPinButton = findViewById(R.id.editPinButton);
//        Button deletePinButton = findViewById(R.id.deletePinButton);
//        Button joinSiteButton = findViewById(R.id.joinSite);
//
////        Button joinSiteButton = findViewById(R.id.buttonJoinSite);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("pins").document(documentId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document != null && document.exists()) {
//                            String siteName = document.getString("siteName");
//                            String siteAddress = document.getString("siteAddress");
//                            String siteDescription = document.getString("siteDescription");
//                            Date dateTime = document.getDate("dateTime");
//                            String siteOwner = document.getString("siteOwner");
//                            ArrayList<String> siteMembers = (ArrayList<String>) document.get("siteMembers");
//                            String siteReport = document.getString("siteReport");
//                            pinNameTextView.setText(siteName);
//                            siteAddressTextView.setText("Address: " + siteAddress);
//                            siteDescriptionTextView.setText("Description: " + siteDescription);
//                            siteReportTextView.setText("Report: " + siteReport);
//
//                            fetchOwnerName(siteOwner, siteOwnerTextView);
//                            if (dateTime != null) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//                                dateTimeTextView.setText("Date & Time: " + dateFormat.format(dateTime));
//                            }
//                            fetchUserNames(siteMembers, siteMembersListView);
//
//
//                            FirebaseAuth auth = FirebaseAuth.getInstance();
//                            FirebaseUser currentUser = auth.getCurrentUser();
//
//                            if (currentUser != null) {
//                                String currentUserId = currentUser.getUid();
//                                if (!currentUserId.equals(siteOwner)) {
//                                    joinSiteButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    joinSiteButton.setVisibility(View.GONE);
//                                }
//                                if (currentUserId.equals(siteOwner)) {
//                                    editPinButton.setVisibility(View.VISIBLE);
//                                    deletePinButton.setVisibility(View.VISIBLE);
//                                } else {
//                                    editPinButton.setVisibility(View.GONE);
//                                    deletePinButton.setVisibility(View.GONE);
//                                }
//                            }
//                            joinSiteButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    joinSite();
//                                }
//                            });
//
//                            editPinButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    showEditDialog();
//                                }
//                            });
//                            deletePinButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    showDeleteConfirmationDialog();
//                                }
//                            });
//
//                            siteOwnerTextView.setText("Owner: " + siteOwner);
//
//                        } else {
//                            Log.d("PinDetailsActivity", "Document does not exist");
//                        }
//                    } else {
//                        Log.w("PinDetailsActivity", "Error getting document", task.getException());
//                    }
//                });
//    }
//    private void fetchOwnerName(String ownerId, TextView ownerTextView) {
//        // Fetch site owner name based on site owner ID
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("User").document(ownerId)
//                .get()
//                .addOnCompleteListener(ownerTask -> {
//                    if (ownerTask.isSuccessful()) {
//                        DocumentSnapshot ownerDocument = ownerTask.getResult();
//                        if (ownerDocument != null && ownerDocument.exists()) {
//                            String ownerName = ownerDocument.getString("name");
//                            ownerTextView.setText("Owner: " + ownerName);
//                        } else {
//                            Log.d("PinDetailsActivity", "Owner document does not exist");
//                        }
//                    } else {
//                        Log.w("PinDetailsActivity", "Error getting owner document", ownerTask.getException());
//                    }
//                });
//    }
//    private void fetchUserNames(ArrayList<String> userIds, ListView siteMembersListView) {
//        // Fetch user names based on user IDs in the siteMembers array
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        ArrayList<String> userNames = new ArrayList<>();
//
//        // Iterate through user IDs and fetch user names
//        for (String userId : userIds) {
//            db.collection("User").document(userId)
//                    .get()
//                    .addOnCompleteListener(userTask -> {
//                        if (userTask.isSuccessful()) {
//                            DocumentSnapshot userDocument = userTask.getResult();
//                            if (userDocument != null && userDocument.exists()) {
//                                String userName = userDocument.getString("name");
//                                userNames.add(userName);
//
//                                // Update the ArrayAdapter with the new user names
//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
//                                siteMembersListView.setAdapter(arrayAdapter);
//                            } else {
//                                Log.d("PinDetailsActivity", "User document does not exist");
//                            }
//                        } else {
//                            Log.w("PinDetailsActivity", "Error getting user document", userTask.getException());
//                        }
//                    });
//        }
//    }
//
//    private void joinSite() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            String currentUserId = currentUser.getUid();
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("pins").document(documentId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                ArrayList<String> siteMembers = (ArrayList<String>) document.get("siteMembers");
//
//                                // Check if the current user is already a member
//                                if (siteMembers != null && siteMembers.contains(currentUserId)) {
//                                    showToast("Already a member");
//                                } else {
//                                    updateSiteMembers(currentUserId);
//                                }
//                            }
//                        } else {
//                            Log.w("PinDetailsActivity", "Error getting document", task.getException());
//                        }
//                    });
//        }
//    }
//
//    private void updateSiteMembers(String currentUserId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("pins").document(documentId)
//                .update("siteMembers", FieldValue.arrayUnion(currentUserId))
//                .addOnSuccessListener(aVoid -> {
//                    showToast("Joined Successfully!");
//                })
//                .addOnFailureListener(e -> showToast("Failed to join the site: " + e.getMessage()));
//    }
//
//
//    private void showEditDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_edit_pin, null);
//        dialogBuilder.setView(dialogView);
//        EditText editSiteNameEditText = dialogView.findViewById(R.id.editSiteNameEditText);
//        EditText editSiteDescriptionEditText = dialogView.findViewById(R.id.editSiteDescriptionEditText);
//        EditText editSiteReportEditText = dialogView.findViewById(R.id.editSiteReportEditText);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("pins").document(documentId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document != null && document.exists()) {
//                            String siteName = document.getString("siteName");
//                            String siteDescription = document.getString("siteDescription");
//                            String siteReport = document.getString("siteReport");
//
//                            editSiteNameEditText.setText(siteName);
//                            editSiteDescriptionEditText.setText(siteDescription);
//                            editSiteReportEditText.setText(siteReport);
//                        }
//                    }
//                });
//        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String updatedSiteName = editSiteNameEditText.getText().toString().trim();
//                String updatedSiteDescription = editSiteDescriptionEditText.getText().toString().trim();
//                String updatedSiteReport = editSiteReportEditText.getText().toString().trim();
//                updatePinInformation(updatedSiteName, updatedSiteDescription, updatedSiteReport);
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }
//
//    private void updatePinInformation(String updatedSiteName, String updatedSiteDescription, String updatedSiteReport) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("pins").document(documentId)
//                .update("siteName", updatedSiteName, "siteDescription", updatedSiteDescription, "siteReport", updatedSiteReport)
//                .addOnSuccessListener(aVoid -> {
//                    showToast("Pin information updated successfully");
//                })
//                .addOnFailureListener(e -> showToast("Failed to update pin information: " + e.getMessage()));
//    }
//
//
//    private void showDeleteConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Delete Site");
//        builder.setMessage("Are you sure you want to delete this site?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                deletePin();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    private void deletePin() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("pins").document(documentId)
//                .delete()
//                .addOnSuccessListener(aVoid -> {
//                    showToast("Pin deleted successfully");
//                    finish();
//                })
//                .addOnFailureListener(e -> showToast("Failed to delete pin: " + e.getMessage()));
//    }
//
//    private void showToast(String message) {
//        Context context = getApplicationContext();
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//}
package com.example.grennersaigon.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grennersaigon.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PinDetailsActivity extends AppCompatActivity {

    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_details);

        documentId = getIntent().getStringExtra("documentId");

        TextView pinNameTextView = findViewById(R.id.pinNameTextView);
        TextView siteAddressTextView = findViewById(R.id.siteAddressTextView);
        TextView siteDescriptionTextView = findViewById(R.id.siteDescriptionTextView);
        TextView dateTimeTextView = findViewById(R.id.dateTimeTextView);
        TextView siteOwnerTextView = findViewById(R.id.siteOwnerTextView);
        TextView siteReportTextView = findViewById(R.id.siteReportTextView);
        RecyclerView siteMembersRecyclerView = findViewById(R.id.siteMembersRecyclerView);
        Button editPinButton = findViewById(R.id.editPinButton);
        Button deletePinButton = findViewById(R.id.deletePinButton);
        Button joinSiteButton = findViewById(R.id.joinSite);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins").document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String siteName = document.getString("siteName");
                            String siteAddress = document.getString("siteAddress");
                            String siteDescription = document.getString("siteDescription");
                            Date dateTime = document.getDate("dateTime");
                            String siteOwner = document.getString("siteOwner");
                            ArrayList<String> siteMembers = (ArrayList<String>) document.get("siteMembers");
                            String siteReport = document.getString("siteReport");
                            pinNameTextView.setText(siteName);
                            siteAddressTextView.setText("Address: " + siteAddress);
                            siteDescriptionTextView.setText("Description: " + siteDescription);
                            siteReportTextView.setText("Report: " + siteReport);

                            fetchOwnerName(siteOwner, siteOwnerTextView);
                            if (dateTime != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                dateTimeTextView.setText("Date & Time: " + dateFormat.format(dateTime));
                            }
                            fetchUserNames(siteMembers, siteMembersRecyclerView);

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = auth.getCurrentUser();

                            if (currentUser != null) {
                                String currentUserId = currentUser.getUid();
                                if (!currentUserId.equals(siteOwner)) {
                                    joinSiteButton.setVisibility(View.VISIBLE);
                                } else {
                                    joinSiteButton.setVisibility(View.GONE);
                                }
                                if (currentUserId.equals(siteOwner)) {
                                    editPinButton.setVisibility(View.VISIBLE);
                                    deletePinButton.setVisibility(View.VISIBLE);
                                } else {
                                    editPinButton.setVisibility(View.GONE);
                                    deletePinButton.setVisibility(View.GONE);
                                }
                            }
                            joinSiteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    joinSite();
                                }
                            });

                            editPinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showEditDialog();
                                }
                            });
                            deletePinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showDeleteConfirmationDialog();
                                }
                            });

                            siteOwnerTextView.setText("Owner: " + siteOwner);

                        } else {
                            Log.d("PinDetailsActivity", "Document does not exist");
                        }
                    } else {
                        Log.w("PinDetailsActivity", "Error getting document", task.getException());
                    }
                });
    }

    private void fetchOwnerName(String ownerId, TextView ownerTextView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(ownerId)
                .get()
                .addOnCompleteListener(ownerTask -> {
                    if (ownerTask.isSuccessful()) {
                        DocumentSnapshot ownerDocument = ownerTask.getResult();
                        if (ownerDocument != null && ownerDocument.exists()) {
                            String ownerName = ownerDocument.getString("name");
                            ownerTextView.setText("Owner: " + ownerName);
                        } else {
                            Log.d("PinDetailsActivity", "Owner document does not exist");
                        }
                    } else {
                        Log.w("PinDetailsActivity", "Error getting owner document", ownerTask.getException());
                    }
                });
    }

    private void fetchUserNames(ArrayList<String> userIds, RecyclerView siteMembersRecyclerView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> userNames = new ArrayList<>();

        for (String userId : userIds) {
            db.collection("User").document(userId)
                    .get()
                    .addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful()) {
                            DocumentSnapshot userDocument = userTask.getResult();
                            if (userDocument != null && userDocument.exists()) {
                                String userName = userDocument.getString("name");
                                userNames.add(userName);

                                MembersAdapter membersAdapter = new MembersAdapter(userNames);
                                siteMembersRecyclerView.setAdapter(membersAdapter);
                                siteMembersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                            } else {
                                Log.d("PinDetailsActivity", "User document does not exist");
                            }
                        } else {
                            Log.w("PinDetailsActivity", "Error getting user document", userTask.getException());
                        }
                    });
        }
    }

    private void joinSite() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("pins").document(documentId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                ArrayList<String> siteMembers = (ArrayList<String>) document.get("siteMembers");

                                if (siteMembers != null && siteMembers.contains(currentUserId)) {
                                    showToast("Already a member");
                                } else {
                                    updateSiteMembers(currentUserId);
                                }
                            }
                        } else {
                            Log.w("PinDetailsActivity", "Error getting document", task.getException());
                        }
                    });
        }
    }

    private void updateSiteMembers(String currentUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins").document(documentId)
                .update("siteMembers", FieldValue.arrayUnion(currentUserId))
                .addOnSuccessListener(aVoid -> {
                    showToast("Joined Successfully!");
                })
                .addOnFailureListener(e -> showToast("Failed to join the site: " + e.getMessage()));
    }

    private void showEditDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_pin, null);
        dialogBuilder.setView(dialogView);

        EditText editSiteNameEditText = dialogView.findViewById(R.id.editSiteNameEditText);
        EditText editSiteDescriptionEditText = dialogView.findViewById(R.id.editSiteDescriptionEditText);
        EditText editSiteReportEditText = dialogView.findViewById(R.id.editSiteReportEditText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins").document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String siteName = document.getString("siteName");
                            String siteDescription = document.getString("siteDescription");
                            String siteReport = document.getString("siteReport");

                            editSiteNameEditText.setText(siteName);
                            editSiteDescriptionEditText.setText(siteDescription);
                            editSiteReportEditText.setText(siteReport);
                        }
                    }
                });

        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedSiteName = editSiteNameEditText.getText().toString().trim();
                String updatedSiteDescription = editSiteDescriptionEditText.getText().toString().trim();
                String updatedSiteReport = editSiteReportEditText.getText().toString().trim();
                updatePinInformation(updatedSiteName, updatedSiteDescription, updatedSiteReport);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void updatePinInformation(String updatedSiteName, String updatedSiteDescription, String updatedSiteReport) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins").document(documentId)
                .update("siteName", updatedSiteName, "siteDescription", updatedSiteDescription, "siteReport", updatedSiteReport)
                .addOnSuccessListener(aVoid -> {
                    showToast("Pin information updated successfully");
                })
                .addOnFailureListener(e -> showToast("Failed to update pin information: " + e.getMessage()));
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Site");
        builder.setMessage("Are you sure you want to delete this site?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePin();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deletePin() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pins").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    showToast("Pin deleted successfully");
                    finish();
                })
                .addOnFailureListener(e -> showToast("Failed to delete pin: " + e.getMessage()));
    }

    private void showToast(String message) {
        Context context = getApplicationContext();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
