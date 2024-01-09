package com.eleco.view.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.eleco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText editTextName, editTextEmail, editTextPassword;
    private AppCompatButton buttonRegister;
    private TextView buttonToLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonToLogin = findViewById(R.id.buttontoMasuk);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Objects.requireNonNull(editTextName.getText()).toString().trim();
                String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
                String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
                registerUser(name, email, password);
            }
        });
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });
    }

    private void registerUser(final String name, String email, String password) {
        if (isValidName(name)) {
            textInputLayoutName.setError(null);
            if (isValidEmail(email)) {
                textInputLayoutEmail.setError(null);
                if (isValidPassword(password)) {
                    textInputLayoutPassword.setError(null);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String uid = currentUser.getUid();

                                        mDatabase.child(uid).child("name").setValue(name);
                                        mDatabase.child(uid).child("email").setValue(currentUser.getEmail());

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                        Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Pendaftaran gagal, coba lagi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    textInputLayoutPassword.setError("Password minimal 8 karakter");
                }
            } else {
                textInputLayoutEmail.setError("Email tidak valid");
            }
        } else {
            textInputLayoutName.setError("Masukkan nama");
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
    private boolean isValidName(String name) {
        return !name.isEmpty();
    }
}
