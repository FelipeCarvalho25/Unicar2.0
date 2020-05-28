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

public class ActivityConsultarConsultas extends AppCompatActivity {
    SQLiteDatabase db;
    ListView lvConsultas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_list);

        lvConsultas = findViewById(R.id.lvConsultas);
        listarConsultas();
        lvConsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = lvConsultas.getChildAt(position);
                TextView tvListId = v.findViewById(R.id.tvListIdCon);
                TextView tvListPacient = v.findViewById(R.id.tvListPacient);
                TextView tvListMedico = v.findViewById(R.id.tvListIMedico);
                TextView tvListDtHrIni = v.findViewById(R.id.tvListDtHrIni);
                TextView tvListDtHrFim = v.findViewById(R.id.tvListDtHrFim);
                TextView tvListObs= v.findViewById(R.id.tvListObs);

                Intent i = new Intent(getApplicationContext(), EditarConsultActivity.class);
                i.putExtra("id", tvListId.getText().toString());
                i.putExtra("paciente_id", tvListPacient.getText().toString());
                i.putExtra("medico_id", tvListMedico.getText().toString());
                i.putExtra("data_hora_inicio", tvListDtHrIni.getText().toString());
                i.putExtra("data_hora_fim", tvListDtHrFim.getText().toString());
                i.putExtra("observacoes", tvListObs.getText().toString());
                startActivity(i);
            }
        });

    }

    private void listarConsultas() {
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM consulta;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        String[] from = {"_id", "paciente_id", "medico_id", "data_hora_inicio", "data_hora_fim", "observacao"};
        int[] to = {R.id.tvListIdCon, R.id.tvListPacient, R.id.tvListIMedico, R.id.tvListDtHrIni, R.id.tvListDtHrFim, R.id.tvListObs};

        SimpleCursorAdapter scAdapter =
                new SimpleCursorAdapter(getApplicationContext(), R.layout.dados_consulta, dados, from, to, 0);

        lvConsultas.setAdapter(scAdapter);
        db.close();
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityConsultarConsultas.this, MainActivity.class);
        startActivity(it);
    }
}
