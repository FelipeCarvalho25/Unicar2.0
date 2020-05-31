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

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class ActivityCadastrarPaciente extends AppCompatActivity {
    EditText tNome;
    Spinner spGrpSan;
    EditText tEndereco;
    EditText tNumero;
    EditText tCidade;
    Spinner spUF;
    EditText tNumTel;
    EditText tNumCel;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastrar_paciente);

        tNome = findViewById(R.id.etNomePac);
        tEndereco = findViewById(R.id.etEnderecoPAc);
        tNumero = findViewById(R.id.etNumPAc);
        tCidade = findViewById(R.id.etCidadPac);
        spUF = findViewById(R.id.spUfPac);
        tNumTel = findViewById(R.id.etTelPac);
        tNumCel = findViewById(R.id.etCellPac);
        spGrpSan = findViewById(R.id.spGrpSan);

        //FAz mascara de numero
        //cell
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(tNumCel, smf);
        tNumCel.addTextChangedListener(mtw);
        //ttel
        smf = new SimpleMaskFormatter("(NN)NNNN-NNNN");
        mtw = new MaskTextWatcher(tNumTel, smf);
        tNumTel.addTextChangedListener(mtw);

        String[] aGrpSan = new String[] {
                "A+",
                "A-",
                "B+",
                "AB+",
                "AB-",
                "O+",
                "O-"
        };
        ArrayAdapter<String> spArrayAdapterG =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aGrpSan);
        spGrpSan.setAdapter(spArrayAdapterG);

        String[] aUf = new String[] {"AC","AL", "AM", "AP", "BA","CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        ArrayAdapter<String> spArrayAdapterH =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, aUf);
        spUF.setAdapter(spArrayAdapterH);

        Button btn_cadastrar = findViewById(R.id.btn_cadastrarPac);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserePaciente();
            }
        });
    }

    private void inserePaciente () {
        String sNome = tNome.getText().toString().trim();
        String sEndereco = tEndereco.getText().toString().trim();
        String sNumero = tNumero.getText().toString().trim();
        String sCidade = tCidade.getText().toString().trim();
        String sUF = spUF.getSelectedItem().toString().trim();
        String sNumTel = tNumTel.getText().toString().trim();
        String sNumCel = tNumCel.getText().toString().trim();
        String sGrpSan = spGrpSan.getSelectedItem().toString().trim();
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
            sql.append("INSERT INTO paciente(nome,grp_sanguineo,logradouro, numero, cidade, uf, celular,fixo) VALUES (");
            sql.append("'" + sNome + "'" + ", ");
            sql.append("'" + sGrpSan + "'" + ", ");
            sql.append("'" + sEndereco + "'" + ", ");
            sql.append("'" + sNumero + "'" + ", ");
            sql.append("'" + sCidade + "'" + ", ");
            sql.append("'" + sUF + "'" + ", ");
            sql.append("'" + sNumCel + "'" + ", ");
            sql.append("'" + sNumTel + "'");
            sql.append(");");

            try {
                db.execSQL(sql.toString());
                Toast.makeText(getApplicationContext(), "Paciente cadastrado", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            tNome.setText("");
            spGrpSan.setSelection(0);
            tEndereco.setText("");
            tNumero.setText("");
            tCidade.setText("");
            spUF.setSelection(0);
            tNumTel.setText("");
            tNumCel.setText("");
            db.close();

        }
    }

}
