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

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.ArrayList;

public class EditarConsultActivity extends AppCompatActivity {
    Spinner spPaciente;
    Spinner spMedico;
    EditText tInicio;
    EditText tFim;
    EditText tData;
    EditText tObervacoes;
    String[] aPacientes;
    String[] aMedicos;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_editar_consulta);


        tInicio = findViewById(R.id.etIniCOnEdit);
        tFim = findViewById(R.id.etFimConEdit);
        tObervacoes = findViewById(R.id.etObsEdit);
        spPaciente = findViewById(R.id.spPacEdit);
        spMedico = findViewById(R.id.spMedEdit);
        aMedicos = getAllMedic();
        aPacientes = getAllPacientes();
        tData  = findViewById(R.id.etDateConEdit);

        //FAz mascara de data
        //TInicio
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtw = new MaskTextWatcher(tInicio, smf);
        tInicio.addTextChangedListener(mtw);
        //tFIm
        mtw = new MaskTextWatcher(tFim, smf);
        tFim.addTextChangedListener(mtw);
        //tDAta
        smf = new SimpleMaskFormatter("NN/NN/NNNN");
        mtw = new MaskTextWatcher(tData, smf);
        tData.addTextChangedListener(mtw);


        ArrayAdapter<String> spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aPacientes);
        spPaciente.setAdapter(spArrayAdapter);
        spArrayAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aMedicos);
        spMedico.setAdapter(spArrayAdapter);

        Intent valores = getIntent();
        tData.setText(valores.getStringExtra("data_hora_inicio").trim().substring(0,10));
        tInicio.setText(valores.getStringExtra("data_hora_inicio").trim().substring(11,16));
        tFim.setText(valores.getStringExtra("data_hora_fim").trim().substring(11,16));
        tObervacoes.setText(valores.getStringExtra("observacoes"));
        String pacienteId = valores.getStringExtra("paciente_nome");
        String medicoId = valores.getStringExtra("medico_nome");
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
   /* private String getMedicoById(String nId){
        String sNameMedico;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM medico");
        sql.append(" WHERE _id = '" + nId + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        sNameMedico = dados.getString(dados.getColumnIndex("nome"));
        db.close();

        return sNameMedico;
    }
    private String getPacientById(String nId){
        String sNamePaciente;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM paciente");
        sql.append(" WHERE _id = '" + nId + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        sNamePaciente = dados.getString(dados.getColumnIndex("nome"));
        db.close();

        return sNamePaciente;
    }*/
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
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM medico");
        sql.append(" WHERE nome = '" + sNome + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
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
        dados.moveToFirst();
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
        sql.append("WHERE _id = '" + id + "';");
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
