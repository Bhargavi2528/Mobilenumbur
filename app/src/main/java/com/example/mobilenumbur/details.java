package com.example.mobilenumbur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class details extends AppCompatActivity {

    ImageView profileImage;
    EditText nickname;
    EditText email;
    EditText mobilenumber;
    Button btnNext;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String sNickname;
    String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        profileImage = (ImageView) findViewById(R.id.details_profile_image);
        profileImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        nickname = (EditText) findViewById(R.id.edt_nickname);
        btnNext = (Button) findViewById(R.id.btn_next);
        email = (EditText) findViewById(R.id.email);
        mobilenumber = (EditText) findViewById(R.id.mobilenumber);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nickname != null) {
                    storeDetails(sNickname, phoneNo);
                } else {
                    Toast.makeText(details.this, "Please Enter Nick name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void storeDetails(String username, String phoneNumber) {
        String stringemail = email.getText().toString();
        String stringmobilenumber = mobilenumber.getText().toString();
        String userId = firebaseUser.getUid();
        phoneNo = firebaseUser.getPhoneNumber();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", userId);
        hashMap.put("username", username);
        hashMap.put("email",stringemail);
        hashMap.put("mobilenumber",stringmobilenumber);
        hashMap.put("imageURL", "default");

        reference.setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(details.this, "Finish", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
