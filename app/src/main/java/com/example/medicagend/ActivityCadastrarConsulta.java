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
        setContentView(R.layout.tela_cadastrar_consulta);

        consultMed = new ActivityConsultarMedico();
        consultPac = new ActivityConsultarPaciente();

        tInicio = findViewById(R.id.etIniCOn);
        tFim = findViewById(R.id.etFimCon);
        tObervacoes = findViewById(R.id.etObs);
        spPaciente = findViewById(R.id.spPac);
        spMedico = findViewById(R.id.spMed);
        aMedicos = getAllMedic();
        aPacientes = getAllPacientes();

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
    private String[] getAllMedic(){
        String[] aMed;
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
        return aMed;
    }
    private String[] getAllPacientes(){
        String[] aPac;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM paciente;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!dados.isAfterLast()) {
            names.add(dados.getString(dados.getColumnIndex("nome")));
            dados.moveToNext();
        }
        dados.close();
        aPac = names.toArray(new String[names.size()]);
        db.close();
        return aPac;
    }
    private String getIdMedByNome(String sNome){
        String nIdMed;
        ArrayList<String> names = new ArrayList<String>();
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM medico");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        nIdMed = dados.getString(dados.getColumnIndex("_id"));
        dados.close();

        db.close();

        return nIdMed;
    }
    private String getIdPacByNome(String sNome){
        String nIdPac;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM paciente");
        sql.append(" WHERE nome = '" + sNome + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        nIdPac = dados.getString(0);
        db.close();

        return nIdPac;
    }
    private void insereConsulta () {
        String sInicio = tInicio.getText().toString().trim();
        String sFim = tFim.getText().toString().trim();
        String sObs = tObervacoes.getText().toString().trim();
        String sMedico = spMedico.getSelectedItem().toString();
        String sPaciente = spPaciente.getSelectedItem().toString();
        String sPacCOd = "0";
        String sMedCod = "0";
        sMedCod = getIdMedByNome(sMedico);
        sPacCOd = getIdPacByNome(sPaciente);

        if(sInicio.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de início não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sFim.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de fim não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sObs.equals("")) {
            Toast.makeText(getApplicationContext(), "A data de fim não pode estar vazia!", Toast.LENGTH_LONG).show();
        } else if (sMedCod.equals("0")) {
            Toast.makeText(getApplicationContext(), "Selecao com problema!", Toast.LENGTH_LONG).show();
        }else if (sPacCOd.equals("0")) {
            Toast.makeText(getApplicationContext(), "Selecao com problema!", Toast.LENGTH_LONG).show();
        }else {
            db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
            StringBuilder sql = new StringBuilder();

            sql.append("INSERT INTO consulta(paciente_id,medico_id,data_hora_inicio, data_hora_fim, observacao) VALUES (");
            sql.append( "'"+sPacCOd +"'");
            sql.append(",'" + sMedCod + "', ");
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
            tObervacoes.setText("");
            spMedico.setSelection(0);
            spPaciente.setSelection(0);
            db.close();

        }
    }
}
