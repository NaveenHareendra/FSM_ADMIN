package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WelcomePage extends AppCompatActivity {
    private EditText username, password;
    private TextView signup;
    Button btnLogin;
    String User_username, User_password;
    DatabaseReference obj;

    /*SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        //References to the XML Layout

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signupText);
        btnLogin = findViewById(R.id.btnLogin);

        /*VideoView videoView = findViewById(R.id.videoView);

        //Set the video file as the source
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.tractor;
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);

        // Start the video playback
        videoView.start();*/

        //Set On Click Listener on Login Button

        btnLogin.setOnClickListener(view -> {

            if (!validateUsername() || !validatePassword()) {
                return;
                // Handle validation errors
            }

            checkUserIsValid();
        });

        signup.setOnClickListener(view ->{
            Intent i = new Intent(this, RegisterFO.class);
            startActivity(i);
        });
    }

    //METHODS COMES IN HERE .......

    public Boolean validateUsername(){
        String val = username.getText().toString();
        if (val.isEmpty()){
            username.setError("Username cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = password.getText().toString();
        if (val.isEmpty()){
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
    public void checkUserIsValid() {
        User_username = username.getText().toString().trim();
        User_password = password.getText().toString().trim();

        obj = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabase = obj.orderByChild("user_name_of_user").equalTo(User_username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);

                    // Iterate through matching users (there should be only one, assuming usernames are unique)
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String roleFromDB = userSnapshot.child("selectedRole").getValue(String.class);
                        String fullnameFromDB = userSnapshot.child("full_name_of_user").getValue(String.class);
                        String passwordFromDB = userSnapshot.child("user_password").getValue(String.class);
                        String DivisionFromDB = userSnapshot.child("selectedDivision").getValue(String.class);
                        String VSDomainFromDB = userSnapshot.child("selectedVSDomain").getValue(String.class);
                        String userIDFromDB = userSnapshot.getKey();


                        if ( roleFromDB != null && roleFromDB.equals("Admin") && passwordFromDB != null && passwordFromDB.equals(User_password)) {

                            // Password matches, start the new activity
                            Intent i = new Intent(WelcomePage.this, NavigationDrawer.class);

                            i.putExtra("full_name_of_user", fullnameFromDB);
                            i.putExtra("selectedVSDomain", VSDomainFromDB);
                            i.putExtra("selectedDivision", DivisionFromDB);
                            i.putExtra("userID", userIDFromDB);

                            Toast.makeText(WelcomePage.this, "Selected Division : " +DivisionFromDB, Toast.LENGTH_SHORT).show();

                            startActivity(i);

                            return;  // Exit the loop if a match is found

                        } else if (roleFromDB != null && !roleFromDB.equals("Admin")) {
                            password.setError("Invalid Credentials: Not a Admin");
                            password.requestFocus();
                            return;  // Exit the loop if role is not a Field Officer
                        }
                    }

                    // If we reach here, the password did not match any user

                    password.setError("Invalid Credentials: Password does not match");
                    password.requestFocus();

                } else {
                    username.setError("User does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}