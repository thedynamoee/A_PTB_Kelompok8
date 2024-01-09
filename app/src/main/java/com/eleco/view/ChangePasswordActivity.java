package com.eleco.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.eleco.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText editTextOldPassword, editTextNewPassword, editTextConfirmPassword;
    private AppCompatButton btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextName);

        btUpdate = findViewById(R.id.btUpdate);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            Toast.makeText(this, "Masukkan Password lama terlebih dahulu", Toast.LENGTH_LONG).show();
        } else if (newPassword.equals(confirmPassword)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

                user.reauthenticate(credential)
                        .addOnCompleteListener(reAuthTask -> {
                            if (reAuthTask.isSuccessful()) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Gagal mengubah password", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(this, "Gagal otorisasi, password lama salah", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                // User belum login atau session kadaluarsa
                // Lakukan tindakan untuk meminta login kembali atau sesuai dengan kebutuhan aplikasi kamu
            }
        } else {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_LONG).show();
        }
    }

}
