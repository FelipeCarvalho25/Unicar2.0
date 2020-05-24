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

public class ActivityCadastrarConsulta extends AppCompatActivity {
    Spinner spPaciente;
    Spinner spMedico;
    EditText tInicio;
    EditText tFim;
    EditText tObervacoes;
    String[] aPacientes;
    String[] aMedicos;
    ActivityConsultarMedico consultMed;
    ActivityConsultarPaciente consultPac;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consultMed = new ActivityConsultarMedico();
        consultPac = new ActivityConsultarPaciente();

        tInicio = findViewById(R.id.etIniCOn);
        tFim = findViewById(R.id.etFimCon);
        tObervacoes = findViewById(R.id.etObs);
        consultMed.getAllMedic(aMedicos);
        consultPac.getAllPacientes(aPacientes);

        ArrayAdapter<String> spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aPacientes);
        spPaciente.setAdapter(spArrayAdapter);
        spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aMedicos);
        spMedico.setAdapter(spArrayAdapter);

        Button btn_cadastrar = findViewById(R.id.btn_cadastrarCon);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereConsulta();
            }
        });
    }

    private void insereConsulta () {
        String sInicio = tInicio.getText().toString().trim();
        String sFim = tFim.getText().toString().trim();
        String sObs = tObervacoes.getText().toString().trim();

        int nPacCOd = 0;
        int nMedCod = 0;
        if(sInicio.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de início não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sFim.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de fim não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sObs.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de fim não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else {
            db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
            StringBuilder sql = new StringBuilder();
            String sMedico = spMedico.getSelectedItem().toString();
            String sPaciente = spPaciente.getSelectedItem().toString();
            nMedCod = consultMed.getIdMedByNome(sMedico);
            nPacCOd = consultPac.getIdPacByNome(sPaciente);
            sql.append("INSERT INTO consulta(paciente_id,medico_id,data_hora_inicio, data_hora_fim, observacao) VALUES (");
            sql.append( nPacCOd );
            sql.append("," + nMedCod + ", ");
            sql.append("'" + sInicio + "'"+ ", ");
            sql.append("'" + sFim + "'"+ ", ");
            sql.append("'" + sObs + "'");
            sql.append(");");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Consulta cadastrada", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            tInicio.setText("");
            tFim.setText("");
            tObervacoes.setSelection(0);
            db.close();

        }
    }
}
