package com.example.medicagend;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityConsultarMedico extends AppCompatActivity {
    SQLiteDatabase db;
    ListView lvMedicos;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicos_list);

        lvMedicos = findViewById(R.id.lvPacientes);
        listarMedicos();
        lvMedicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = lvMedicos.getChildAt(position);
                TextView tvListId =     v.findViewById(R.id.tvListIdMed);
                TextView tvListNomMed = v.findViewById(R.id.tvListNomeMed);
                TextView tvListCRM =    v.findViewById(R.id.tvListCRM);
                TextView tvListEndMed = v.findViewById(R.id.tvListEndMed);
                TextView tvListNumMed = v.findViewById(R.id.tvListNumMed);
                TextView tvListCidMed = v.findViewById(R.id.tvListCidPMed);
                TextView tvListUfMed =  v.findViewById(R.id.tvListUfMed);
                TextView tvListCelMed = v.findViewById(R.id.tvListCelMed);
                TextView tvListTelMed = v.findViewById(R.id.tvListTelMed);

                Intent i = new Intent(getApplicationContext(), EditarConsultActivity.class);
                i.putExtra("id", tvListId.getText().toString());
                i.putExtra("nome", tvListNomMed.getText().toString());
                i.putExtra("crm", tvListCRM.getText().toString());
                i.putExtra("logradouro", tvListEndMed.getText().toString());
                i.putExtra("numero", tvListNumMed.getText().toString());
                i.putExtra("cidade", tvListCidMed.getText().toString());
                i.putExtra("uf", tvListUfMed.getText().toString());
                i.putExtra("celular", tvListCelMed.getText().toString());
                i.putExtra("fixo", tvListTelMed.getText().toString());
                startActivity(i);
            }
        });

    }
    public String getMedicoById(int nId){
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

    public int getIdMedByNome(String sNome){
        int nIdMed;
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM medico");
        sql.append(" WHERE nome = '" + sNome + "';");
        Cursor dados = db.rawQuery(sql.toString(), null);
        nIdMed = dados.getInt(dados.getColumnIndex("_id"));
        db.close();

        return nIdMed;
    }
    public void getAllMedic(String[] aMed){
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nome FROM medico;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!dados.isAfterLast()) {
            names.add(dados.getString(dados.getColumnIndex("name")));
            dados.moveToNext();
        }
        dados.close();
        aMed = names.toArray(new String[names.size()]);
        db.close();
    }
    private void listarMedicos() {
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM medico;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        String[] from = {"_id", "nome", "crm", "logradouro", "numero", "cidade", "uf", "celular", "fixo"};
        int[] to = {R.id.tvListIdMed, R.id.tvListNomeMed, R.id.tvListCRM, R.id.tvListEndMed, R.id.tvListNumMed, R.id.tvListCidPMed, R.id.tvListUfMed, R.id.tvListCelMed, R.id.tvListTelMed};

        SimpleCursorAdapter scAdapter =
                new SimpleCursorAdapter(getApplicationContext(), R.layout.dados_medico, dados, from, to, 0);

        lvMedicos.setAdapter(scAdapter);
        db.close();
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityConsultarMedico.this, MainActivity.class);
        startActivity(it);
    }
}
