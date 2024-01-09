package com.eleco.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatButton;

import com.eleco.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ImageView btBack = findViewById(R.id.btBack);
        AppCompatButton faqButton = findViewById(R.id.faq);
        AppCompatButton informButton = findViewById(R.id.inform);


        btBack.setOnClickListener(v -> finish());

        faqButton.setOnClickListener(v -> {
            Intent intent = new Intent(HelpActivity.this, FaqActivity.class);
            startActivity(intent);
        });

        informButton.setOnClickListener(v -> {
            Intent intent = new Intent(HelpActivity.this, InformActivity.class);
            startActivity(intent);
        });
    }
}
