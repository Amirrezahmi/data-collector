package com.example.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        Button contactUsButton = findViewById(R.id.contactUsButton);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "amirrezahmi2002@gmail.com";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}



//package com.example.helloworld;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//
//public class Activity2 extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity2);
//
//        Button backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(Activity2.this, MainActivity.class);
//            startActivity(intent);
//        });
//    }
//}