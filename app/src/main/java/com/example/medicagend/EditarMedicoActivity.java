package com.example.medicagend;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarMedicoActivity extends AppCompatActivity {
    EditText tNome;
    EditText tCRM;
    EditText tEndereco;
    EditText tNumero;
    EditText tCidade;
    Spinner spUF;
    EditText tNumTel;
    EditText tNumCel;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_editar_medico);

        tNome = findViewById(R.id.etNome);
        tCRM = findViewById(R.id.etCRM);
        tEndereco = findViewById(R.id.etEndereco);
        tNumero = findViewById(R.id.etNum);
        tCidade = findViewById(R.id.etCidade);
        spUF = findViewById(R.id.spUfMedEdit);
        tNumTel = findViewById(R.id.etTel);
        tNumCel = findViewById(R.id.etCell);

        Intent valores = getIntent();
        tNome.setText(valores.getStringExtra("nome"));
        tCRM.setText(valores.getStringExtra("crm"));
        tEndereco.setText(valores.getStringExtra("logradouro"));
        tNumero.setText(valores.getStringExtra("numero"));
        tCidade.setText(valores.getStringExtra("cidade"));
        tNumTel.setText(valores.getStringExtra("celular"));
        tNumCel.setText(valores.getStringExtra("fixo"));
        String sUf = valores.getStringExtra("uf");

        String[] aUf = new String[] {"AC","AL", "AM", "AP", "BA","CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        ArrayAdapter<String> spArrayAdapterH =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aUf);
        spUF.setAdapter(spArrayAdapterH);

        int aux = 0 ;
        for (String c : aUf) {
            if (c.equals(sUf)) {
                break;
            }
            aux ++;
        }
        spUF.setSelection(aux);

        final String id = valores.getStringExtra("id");


        Button btn_save = findViewById(R.id.btn_save_edit_medic);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarMedico();
            }
        });
        Button btn_exc = findViewById(R.id.btn_exc_edit_medic);
        btn_exc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excMedico(id);
            }
        });
    }

    private void alterarMedico () {
        String sNome = tNome.getText().toString().trim();
        String sCRM = tCRM.getText().toString().trim();
        String sEndereco = tEndereco.getText().toString().trim();
        String sNumero = tNumero.getText().toString().trim();
        String sCidade = tCidade.getText().toString().trim();
        String sUF = spUF.getSelectedItem().toString().trim();
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
            sql.append("UPDATE  medico SET ");
            sql.append("nome = '" + sNome + "'" + ", ");
            sql.append("crm = '" + sCRM + "'" + ", ");
            sql.append("logradouro = '" + sEndereco + "'" + ", ");
            sql.append("numero = '" + sNumero + "'" + ", ");
            sql.append("cidade = '" + sCidade + "'" + ", ");
            sql.append("uf = '" + sUF + "'" + ", ");
            sql.append(" celular = '" + sNumCel + "'" + ", ");
            sql.append(" fixo = '" + sNumTel + "'");
            sql.append(");");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Médico editado", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            db.close();
            Intent i = new Intent(getApplicationContext(), ActivityConsultarMedico.class);
            startActivity(i);


        }
    }
    private void excMedico(String id) {
            db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM medico ");
            sql.append("WHERE _id = " + id + ";");
            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Medico excluído", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            db.close();
            Intent i = new Intent(getApplicationContext(), ActivityConsultarMedico.class);
            startActivity(i);

    }
}
