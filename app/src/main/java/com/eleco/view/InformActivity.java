package com.eleco.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.eleco.R;

import java.util.ArrayList;

public class InformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        ImageView btBack = findViewById(R.id.btBack);
        AppCompatButton btnextChat = findViewById(R.id.btnextChat);


        String pesan = "Hi, tell me more about Eleco!";
        String nomorTelepon = "+6282173926690";

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnextChat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + nomorTelepon + "&text=" + pesan);

                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(whatsappIntent);
            }
        });

    }

}