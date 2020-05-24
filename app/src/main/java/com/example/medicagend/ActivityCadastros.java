package com.example.medicagend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCadastros extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_cadMed = (Button)findViewById(R.id.btn_cadMed);
        btn_cadMed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityCadastros.this, ActivityCadastrarMedico.class);
                startActivity(it);
            }
        });

        Button btn_cadPac = (Button)findViewById(R.id.btn_CadPac);
        btn_cadPac.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityCadastros.this, ActivityCadastrarPaciente.class);
                startActivity(it);
            }
        });

        Button btn_cadCons = (Button)findViewById(R.id.btn_CadCons);
        btn_cadCons.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityCadastros.this, ActivityCadastrarConsulta.class);
                startActivity(it);
            }
        });
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityCadastros.this, MainActivity.class);
        startActivity(it);
    }
}
