package com.example.medicagend;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;

public class EditarConsultActivity extends AppCompatActivity {
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
        setContentView(R.layout.tela_editar_consulta);

        consultMed = new ActivityConsultarMedico();
        consultPac = new ActivityConsultarPaciente();

        tInicio = findViewById(R.id.etIniCOnEdit);
        tFim = findViewById(R.id.etFimConEdit);
        tObervacoes = findViewById(R.id.etObsEdit);
        spPaciente = findViewById(R.id.spPacEdit);
        spMedico = findViewById(R.id.spMedEdit);
        getAllMedic(aMedicos);
        getAllPacientes(aPacientes);

        ArrayAdapter<String> spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aPacientes);
        spPaciente.setAdapter(spArrayAdapter);
        spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aMedicos);
        spMedico.setAdapter(spArrayAdapter);

        Intent valores = getIntent();
        tInicio.setText(valores.getStringExtra("data_hora_inicio"));
        tFim.setText(valores.getStringExtra("data_hora_fim"));
        tObervacoes.setText(valores.getStringExtra("observacoes"));
        String pacienteId = valores.getStringExtra("paciente");
        String medicoId = valores.getStringExtra("medico");
        pacienteId = getPacientById(Integer.parseInt(pacienteId));
        medicoId = getMedicoById(Integer.parseInt(medicoId));

        final String id = valores.getStringExtra("id");

        int aux = 0 ;
        for (String c : aPacientes) {
            if (c.equals(pacienteId)) {
                break;
            }
            aux ++;
        }
        spPaciente.setSelection(aux);
        aux = 0;
        for (String c : aMedicos) {
            if (c.equals(medicoId)) {
                break;
            }
            aux ++;
        }
        spMedico.setSelection(aux);

        Button btn_save = findViewById(R.id.btn_save_edit_consult);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarConsulta();
            }
        });
        Button btn_exc = findViewById(R.id.btn_exc_edit_consult);
        btn_exc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excConsulta(id);
            }
        });
    }
    private String getMedicoById(int nId){
        String sNameMedico;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM medico");
        sql.append(" WHERE _id = " + nId + ";");
        Cursor dados = db.rawQuery(sql.toString(), null);
        sNameMedico = dados.getString(dados.getColumnIndex("nome"));
        db.close();

        return sNameMedico;
    }
    private String getPacientById(int nId){
        String sNamePaciente;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM paciente");
        sql.append(" WHERE _id = " + nId + ";");
        Cursor dados = db.rawQuery(sql.toString(), null);
        sNamePaciente = dados.getString(dados.getColumnIndex("nome"));
        db.close();

        return sNamePaciente;
    }
    private void getAllPacientes(String[] aPac){
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM paciente;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!dados.isAfterLast()) {
            names.add(dados.getString(dados.getColumnIndex("name")));
            dados.moveToNext();
        }
        dados.close();
        aPac = names.toArray(new String[names.size()]);
        db.close();
    }
    private void getAllMedic(String[] aMed){
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM medico;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!dados.isAfterLast()) {
            names.add(dados.getString(dados.getColumnIndex("nome")));
            dados.moveToNext();
        }
        dados.close();
        aMed = names.toArray(new String[names.size()]);

        db.close();
    }
    private String getIdMedByNome(String sNome){
        String nIdMed;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM medico");
        sql.append(" WHERE nome = '" + sNome + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        nIdMed = dados.getString(dados.getColumnIndex("_id"));
        db.close();

        return nIdMed;
    }
    public String getIdPacByNome(String sNome){
        String nIdPac;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM paciente");
        sql.append(" WHERE nome = '" + sNome + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        nIdPac = dados.getString(dados.getColumnIndex("_id"));
        db.close();

        return nIdPac;
    }
    private void editarConsulta () {
        String sInicio = tInicio.getText().toString().trim();
        String sFim = tFim.getText().toString().trim();
        String sObs = tObervacoes.getText().toString().trim();

        String nPacCOd = "0";
        String nMedCod = "0";
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
            nMedCod = getIdMedByNome(sMedico);
            nPacCOd = getIdPacByNome(sPaciente);
            sql.append("UPDATE consulta SET ");
            sql.append(" paciente_id ='" + nPacCOd +"'");
            sql.append(", medico_id ='" + nMedCod + "', ");
            sql.append("data_hora_inicio = '" + sInicio + "'"+ ", ");
            sql.append("data_hora_fim = '" + sFim + "'"+ ", ");
            sql.append("observacao = '" + sObs + "'");
            sql.append(";");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Consulta cadastrada", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            db.close();
            Intent i = new Intent(getApplicationContext(), ActivityConsultarConsultas.class);
            startActivity(i);

        }
    }
    private void excConsulta(String id) {
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM consulta ");
        sql.append("WHERE _id = " + id + ";");
        try {
            db.execSQL(sql.toString());
            Toast.makeText(getApplicationContext(), "Consulta excluído", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        db.close();
        Intent i = new Intent(getApplicationContext(), ActivityConsultarConsultas.class);
        startActivity(i);
    }
}
