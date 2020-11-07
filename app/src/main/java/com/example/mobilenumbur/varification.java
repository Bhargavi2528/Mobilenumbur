package com.example.mobilenumbur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

public class varification extends AppCompatActivity {

    EditText verificationCode;
    Button verify;

    TextView phoneNo;
    TextView wrongNo;

    String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);

        verificationCode = (EditText) findViewById(R.id.edt_verification_code);
        verify = (Button) findViewById(R.id.btn_verify);

        phoneNo = (TextView) findViewById(R.id.phone_number);
        wrongNo = (TextView) findViewById(R.id.wrong_number);

        String phoneNumber = getIntent().getStringExtra("mobileno");
        phoneNo.setText(phoneNumber);
        String otp = verificationCode.getText().toString();
        sendVerificationCodeToUSer(phoneNumber);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = verificationCode.getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    verificationCode.setError("Wrong OTP...");
                    verificationCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        wrongNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(varification.this, MainActivity.class));
            }
        });
    }

    private void sendVerificationCodeToUSer(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(varification.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String verificationCodeByUser) {
        if (verificationCodeBySystem == null){
            Toast.makeText(varification.this,"Please Enter valid OTP",Toast.LENGTH_SHORT).show();
        }else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, verificationCodeByUser);
            signInTheUserByCredential(credential);
        }
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(varification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(varification.this, details.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(varification.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
