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

public class ActivityConsultarPaciente extends AppCompatActivity {
    SQLiteDatabase db;
    ListView lvPacientes;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pacientes_list);

        lvPacientes = findViewById(R.id.lvPacientes);
        listarPacientes();
        lvPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = lvPacientes.getChildAt(position);
                TextView tvListId =     v.findViewById(R.id.tvListIdPAc);
                TextView tvListNomPac = v.findViewById(R.id.tvListNomePac);
                TextView tvListGrpSan =    v.findViewById(R.id.tvListGrpSan);
                TextView tvListEndPAc = v.findViewById(R.id.tvListEndPAc);
                TextView tvListNumPac = v.findViewById(R.id.tvListNumPac);
                TextView tvListCidPac = v.findViewById(R.id.tvListCidPAc);
                TextView tvListUfPac =  v.findViewById(R.id.tvListUfPac);
                TextView tvListCelPac = v.findViewById(R.id.tvListCelPAc);
                TextView tvListTelPac = v.findViewById(R.id.tvListTelPac);
                tvListEndPAc.setVisibility(View.INVISIBLE);
                tvListTelPac.setVisibility(View.INVISIBLE);
                tvListCelPac.setVisibility(View.INVISIBLE);
                tvListUfPac.setVisibility(View.INVISIBLE);
                tvListCidPac.setVisibility(View.INVISIBLE);
                tvListNumPac.setVisibility(View.INVISIBLE);

                Intent i = new Intent(getApplicationContext(), EditarPacienteActivity.class);
                i.putExtra("id", tvListId.getText().toString());
                i.putExtra("nome", tvListNomPac.getText().toString());
                i.putExtra("grp_sanguineo", tvListGrpSan.getText().toString());
                i.putExtra("logradouro", tvListEndPAc.getText().toString());
                i.putExtra("numero", tvListNumPac.getText().toString());
                i.putExtra("cidade", tvListCidPac.getText().toString());
                i.putExtra("uf", tvListUfPac.getText().toString());
                i.putExtra("celular", tvListCelPac.getText().toString());
                i.putExtra("fixo", tvListTelPac.getText().toString());
                startActivity(i);
            }
        });

    }



    private void listarPacientes() {
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM paciente;");
        Cursor dados = db.rawQuery(sql.toString(), null);
        String[] from = {"_id", "nome", "grp_sanguineo", "logradouro", "numero", "cidade", "uf", "celular", "fixo"};
        int[] to = {R.id.tvListIdPAc, R.id.tvListNomePac, R.id.tvListGrpSan, R.id.tvListEndPAc, R.id.tvListNumPac, R.id.tvListCidPAc, R.id.tvListUfPac, R.id.tvListCelPAc, R.id.tvListTelPac};

        SimpleCursorAdapter scAdapter =
                new SimpleCursorAdapter(getApplicationContext(), R.layout.dados_paciente, dados, from, to, 0);

        lvPacientes.setAdapter(scAdapter);
        db.close();
    }
    public void jumpHome(View v) {
        Intent it = new Intent(ActivityConsultarPaciente.this, MainActivity.class);
        startActivity(it);
    }
    public void jumptoAdd(View v) {
        Intent it = new Intent(ActivityConsultarPaciente.this, ActivityCadastrarPaciente.class);
        startActivity(it);
    }
}
