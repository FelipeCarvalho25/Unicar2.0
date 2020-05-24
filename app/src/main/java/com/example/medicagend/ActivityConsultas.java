package com.example.medicagend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ActivityConsultas extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_conMed = (Button)findViewById(R.id.btn_listMed);
        btn_conMed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityConsultas.this, ActivityConsultarMedico.class);
                startActivity(it);
            }
        });

        Button btn_conPac = (Button)findViewById(R.id.btn_listPac);
        btn_conPac.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityConsultas.this, ActivityConsultarPaciente.class);
                startActivity(it);
            }
        });

        Button btn_conCons = (Button)findViewById(R.id.btn_listCon);
        btn_conCons.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(ActivityConsultas.this, ActivityConsultarConsultas.class);
                startActivity(it);
            }
        });
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityConsultas.this, MainActivity.class);
        startActivity(it);
    }
}
