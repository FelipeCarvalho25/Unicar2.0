package com.example.medicagend;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCadastrarMedico extends AppCompatActivity {
    EditText tNome;
    EditText tCRM;
    EditText tEndereco;
    EditText tNumero;
    EditText tCidade;
    EditText tUF;
    EditText tNumTel;
    EditText tNumCel;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastrar_medico);

        tNome = findViewById(R.id.etNome);
        tCRM = findViewById(R.id.etCRM);
        tEndereco = findViewById(R.id.etEndereco);
        tNumero = findViewById(R.id.etNum);
        tCidade = findViewById(R.id.etCidade);
        tUF = findViewById(R.id.etUF);
        tNumTel = findViewById(R.id.etTel);
        tNumCel = findViewById(R.id.etCell);

        Button btn_cadastrar = findViewById(R.id.btn_cadastrarMedi);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereMedico();
            }
        });
    }

    private void insereMedico () {
        String sNome = tNome.getText().toString().trim();
        String sCRM = tCRM.getText().toString().trim();
        String sEndereco = tEndereco.getText().toString().trim();
        String sNumero = tNumero.getText().toString().trim();
        String sCidade = tCidade.getText().toString().trim();
        String sUF = tUF.getText().toString().trim();
        String sNumTel = tNumTel.getText().toString().trim();
        String sNumCel = tNumCel.getText().toString().trim();

        if (sNome.equals("")) {
            Toast.makeText(getApplicationContext(), "O nome não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else if (sCRM.equals("")) {
            Toast.makeText(getApplicationContext(), "O CRM não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else if (sEndereco.equals("")) {
            Toast.makeText(getApplicationContext(), "O CRM não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else if (sNumero.equals("")) {
            Toast.makeText(getApplicationContext(), "O numero não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else if (sCidade.equals("")) {
            Toast.makeText(getApplicationContext(), "A Cidade não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sUF.equals("")) {
            Toast.makeText(getApplicationContext(), "A UF não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sNumTel.equals("")) {
            Toast.makeText(getApplicationContext(), "O numero de telefone não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else if (sNumCel.equals("")) {
            Toast.makeText(getApplicationContext(), "O numero de celular não pode estar vazio!", Toast.LENGTH_LONG).show();
        } else {
            db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO medico(nome,crm,logradouro, numero, cidade, uf, celular,fixo) VALUES (");
            sql.append("'" + sNome + "'" + ", ");
            sql.append("'" + sCRM + "'" + ", ");
            sql.append("'" + sEndereco + "'" + ", ");
            sql.append("'" + sNumero + "'" + ", ");
            sql.append("'" + sCidade + "'" + ", ");
            sql.append("'" + sUF + "'" + ", ");
            sql.append("'" + sNumCel + "'" + ", ");
            sql.append("'" + sNumTel + "'");
            sql.append(");");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Médico cadastrado", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            tNome.setText("");
            tCRM.setText("");
            tEndereco.setText("");
            tNumero.setText("");
            tCidade.setText("");
            tUF.setText("");
            tNumTel.setText("");
            tNumCel.setText("");
            db.close();

        }
    }

}