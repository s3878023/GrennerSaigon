//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import org.w3c.dom.Text;
//
//public class MainActivity extends AppCompatActivity {
//
//    FirebaseAuth auth;
//    Button button;
//    TextView textView;
//    FirebaseUser user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
//        user = auth.getCurrentUser();
//
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
//            textView.setText(user.getEmail());
//        }
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//            }
//        });
//    }
//}
//--------------------------TEST CODE------------------------////////
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class MainActivity extends AppCompatActivity {
//
//    FirebaseAuth auth;
//    Button buttonLogout, buttonSubmit;
//    EditText editTextName, editTextAge, editTextAddress;
//    FirebaseUser user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
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
//
//        if (user != null) {
//            // Assuming you have a TextView with id textView in your layout
//            // textView.setText(userEmail);
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
//
//                if (!name.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
//                    // Create a data object
//                    Information information = new Information(name, age, address);
//
//                    // Save data to Firestore
//                    saveInformationToFirestore(information);
//                } else {
//                    // Handle empty fields
//                    // You may want to display an error message
//                }
//            }
//        });
//    }
//
////    private void saveInformationToFirestore(Information information) {
////        // Get the current user's ID
////        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////
////        // Add the user ID to the Information object
////        information.setUserId(userId);
////        FirebaseFirestore db = FirebaseFirestore.getInstance();
////        db.collection("informationList")
////                .add(information)
////                .addOnSuccessListener(documentReference -> {
////                    // Document added successfully
////                    editTextName.setText("");
////                    editTextAge.setText("");
////                    editTextAddress.setText("");
////                })
////                .addOnFailureListener(e -> {
////
////                });
////    }
//    private void saveInformationToFirestore(Information information) {
//    // Get the current user's ID
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Set the user ID in the Information object
//        information.setUserId(userId);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("informationList")
//                .add(information)
//                .addOnSuccessListener(documentReference -> {
//                    // Document added successfully
//                    editTextName.setText("");
//                    editTextAge.setText("");
//                    editTextAddress.setText("");
//                })
//                .addOnFailureListener(e -> {
//                    // Handle errors
//                    // You may want to show an error message
//                });
//    }
//}
