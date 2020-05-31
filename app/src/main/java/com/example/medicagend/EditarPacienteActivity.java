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

public class EditarPacienteActivity extends AppCompatActivity {
    EditText tNome;
    Spinner spGrpSan;
    EditText tEndereco;
    EditText tNumero;
    EditText tCidade;
    Spinner spUf;
    EditText tNumTel;
    EditText tNumCel;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_editar_paciente);

        tNome = findViewById(R.id.etNomePac);
        tEndereco = findViewById(R.id.etEnderecoPAc);
        tNumero = findViewById(R.id.etNumPAc);
        tCidade = findViewById(R.id.etCidadPac);
        spUf = findViewById(R.id.spUfPacEdit);
        tNumTel = findViewById(R.id.etTelPac);
        tNumCel = findViewById(R.id.etCellPac);
        spGrpSan = findViewById(R.id.spGrpSan);

        Intent valores = getIntent();
        tNome.setText(valores.getStringExtra("nome"));
        String sGrpSan = valores.getStringExtra("grp_sanguineo");
        tEndereco.setText(valores.getStringExtra("logradouro"));
        tNumero.setText(valores.getStringExtra("numero"));
        tCidade.setText(valores.getStringExtra("cidade"));
        String sUf = valores.getStringExtra("uf");
        tNumTel.setText(valores.getStringExtra("celular"));
        tNumCel.setText(valores.getStringExtra("fixo"));

        String[] aGrpSan = new String[] {
                "A+",
                "A-",
                "B+",
                "AB+",
                "AB-",
                "O+",
                "O-"
        };

        ArrayAdapter<String> spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aGrpSan);
        spGrpSan.setAdapter(spArrayAdapter);
        String[] aUf = new String[] {"AC","AL", "AM", "AP", "BA","CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        ArrayAdapter<String> spArrayAdapterH =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aUf);
        spUf.setAdapter(spArrayAdapterH);

        int aux = 0 ;
        for (String c : aGrpSan) {
            if (c.equals(sGrpSan)) {
                break;
            }
            aux ++;
        }
        spGrpSan.setSelection(aux);


        aux = 0 ;
        for (String c : aUf) {
            if (c.equals(sUf)) {
                break;
            }
            aux ++;
        }
        spUf.setSelection(aux);

        final String id = valores.getStringExtra("id");

        Button btn_save = findViewById(R.id.btn_save_edit_pacient);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraPaciente();
            }
        });
        Button btn_exc = findViewById(R.id.btn_exc_edit_pacient);
        btn_exc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excPaciente(id);
            }
        });
    }

    private void alteraPaciente () {
        String sNome = tNome.getText().toString().trim();
        String sEndereco = tEndereco.getText().toString().trim();
        String sNumero = tNumero.getText().toString().trim();
        String sCidade = tCidade.getText().toString().trim();
        String sUF = spUf.getSelectedItem().toString().trim();
        String sNumTel = tNumTel.getText().toString().trim();
        String sNumCel = tNumCel.getText().toString().trim();
        String sGrpSan = spGrpSan.getSelectedItem().toString();
        if (sNome.equals("")) {
            Toast.makeText(getApplicationContext(), "O nome não pode estar vazio!", Toast.LENGTH_LONG).show();
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
            sql.append("UPDATE medico SET ");
            sql.append("nome = '" + sNome + "'" + ", ");
            sql.append("grp_sanguineo = '" + sGrpSan + "'" + ", ");
            sql.append("logradouro = '" + sEndereco + "'" + ", ");
            sql.append("numero = '" + sNumero + "'" + ", ");
            sql.append("cidade = '" + sCidade + "'" + ", ");
            sql.append("uf = '" + sUF + "'" + ", ");
            sql.append("celular = '" + sNumCel + "'" + ", ");
            sql.append("fixo = '" + sNumTel + "'");
            sql.append(");");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Paciente alterado", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            db.close();
            Intent i = new Intent(getApplicationContext(), ActivityConsultarPaciente.class);
            startActivity(i);

        }
    }

    private void excPaciente(String id) {
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM paciente ");
        sql.append("WHERE _id = " + id + ";");
        try {
            db.execSQL(sql.toString());
            Toast.makeText(getApplicationContext(), "Paciente excluído", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        db.close();
        Intent i = new Intent(getApplicationContext(), ActivityConsultarPaciente.class);
        startActivity(i);

    }

}
