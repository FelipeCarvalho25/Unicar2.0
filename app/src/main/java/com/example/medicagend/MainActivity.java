package com.example.medicagend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_cadastros = (Button)findViewById(R.id.btn_cadastro);
        btn_cadastros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, ActivityCadastros.class);
                startActivity(it);
            }
        });

        Button btn_consultas = (Button)findViewById(R.id.btn_listar);
        btn_consultas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, ActivityConsultas.class);
                startActivity(it);
            }
        });
    }
}
