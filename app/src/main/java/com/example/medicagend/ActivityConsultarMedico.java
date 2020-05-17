package com.example.medicagend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityConsultarMedico extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityConsultarMedico.this, MainActivity.class);
        startActivity(it);
    }
}
