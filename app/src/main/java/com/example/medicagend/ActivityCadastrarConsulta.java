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
import java.util.Calendar;

public class ActivityCadastrarConsulta extends AppCompatActivity {
    Spinner spPaciente;
    Spinner spMedico;
    EditText tInicio;
    EditText tData;
    EditText tFim;
    EditText tObervacoes;
    String[] aPacientes;
    String[] aMedicos;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastrar_consulta);

        tInicio = findViewById(R.id.etHorIniCon);
        tFim = findViewById(R.id.etHorFimCon);
        tData  = findViewById(R.id.etDateCon);
        tObervacoes = findViewById(R.id.etObs);
        spPaciente = findViewById(R.id.spPac);
        spMedico = findViewById(R.id.spMed);
        aMedicos = getAllMedic();
        aPacientes = getAllPacientes();

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
    private int getConsultConflit(String horario_ini, String horario_fim){
        db = openOrCreateDatabase("consulta.db", Context.MODE_PRIVATE, null);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT _id FROM consulta");
        sql.append(" WHERE data_hora_inicio between '" + horario_ini+"' AND '" + horario_fim+"'");
        sql.append(" OR data_hora_fim between '" + horario_ini+"' AND '" + horario_fim+"'");
        sql.append(";");
        Cursor dados = db.rawQuery(sql.toString(), null);
        dados.moveToFirst();
        ArrayList<String> ids = new ArrayList<String>();
        while(!dados.isAfterLast()) {
            ids.add(dados.getString(dados.getColumnIndex("_id")));
            dados.moveToNext();
        }
        dados.close();
        db.close();

        return ids.size();
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

    private boolean validaInicioFim() {
        String inicio = tData.getText().toString().trim()+ " " +tInicio.getText().toString().trim();
        String fim = tData.getText().toString().trim() + " " + tFim.getText().toString().trim();
        if (inicio.length() < 16) {
            Toast.makeText(getApplicationContext(), "Inicio da consulta falta informação, favor digitar data ehora!", Toast.LENGTH_LONG).show();
            return false;
        } else if (fim.length() < 16) {
            Toast.makeText(getApplicationContext(), "Fim da consulta falta informação, favor digitar data ehora!", Toast.LENGTH_LONG).show();
            return false;
        }
        int dia_inicio = Integer.parseInt(inicio.substring(0, 2));
        int mes_inicio = Integer.parseInt(inicio.substring(3, 5));
        int ano_inicio = Integer.parseInt(inicio.substring(6, 10));
        int hora_inicio = Integer.parseInt(inicio.substring(11, 13));
        int minuto_inicio = Integer.parseInt(inicio.substring(14, 16));
        int dia_fim = Integer.parseInt(fim.substring(0, 2));
        int mes_fim = Integer.parseInt(fim.substring(3, 5));
        int ano_fim = Integer.parseInt(fim.substring(6, 10));
        int hora_fim = Integer.parseInt(fim.substring(11, 13));
        int minuto_fim = Integer.parseInt(fim.substring(14, 16));
        Calendar calendar_inicio = Calendar.getInstance();
        Calendar calendar_fim = Calendar.getInstance();
        Calendar compareDate = Calendar.getInstance();
        //valida numeros negativos e maiores que o formato ou caso sejam diferentes o ano, mes e dia
        if (dia_inicio < 0| dia_inicio > 31 || mes_inicio < 0 || mes_inicio > 12 || ano_inicio < 0|| ano_inicio > 9999 || hora_inicio < 0 || hora_inicio > 23 || minuto_inicio < 0 || minuto_inicio > 59) {
            Toast.makeText(getApplicationContext(), "Data e horário de início inválido, favor revisar!", Toast.LENGTH_LONG).show();
            return false;
        } else if (dia_fim < 0|| dia_fim > 31 || mes_fim < 0 || mes_fim > 12 || ano_fim < 0 || ano_fim > 9999 || hora_fim < 0 || hora_fim > 23 || minuto_fim < 0 || minuto_fim > 59) {
            Toast.makeText(getApplicationContext(), "Data e horário de fim inválido, favor revisar!", Toast.LENGTH_LONG).show();
            return false;
        } else if (dia_fim != dia_inicio || mes_fim != mes_inicio || ano_fim != ano_inicio) {
            Toast.makeText(getApplicationContext(), "uma consulta não pode levar mais de um dia!", Toast.LENGTH_LONG).show();
            return false;
        }
        //atribui as variaveis digitadas a um calendar para verificar o dia da semana que caiu OBS: o mes está com -1, pois a base começa em 0
        calendar_inicio.set(ano_inicio, mes_inicio - 1, dia_inicio, hora_inicio, minuto_inicio);
        calendar_inicio.setFirstDayOfWeek(calendar_inicio.SUNDAY);
        calendar_fim.set(ano_fim, mes_fim - 1, dia_fim, hora_fim, minuto_fim);
        calendar_fim.setFirstDayOfWeek(calendar_fim.SUNDAY);

        if (calendar_inicio.getTime().before(compareDate.getTime()) || calendar_fim.getTime().before( compareDate.getTime())){//verificar se a data é menor que hoje
            Toast.makeText(getApplicationContext(), "Você não pode cadastrar uma consulta num dia anterior a hoje!", Toast.LENGTH_LONG).show();
            return false;
        }else if (calendar_inicio.get(calendar_inicio.DAY_OF_WEEK) == calendar_inicio.SUNDAY || calendar_inicio.get(calendar_inicio.DAY_OF_WEEK) == calendar_inicio.SATURDAY){//verifica se é fim de semana
            Toast.makeText(getApplicationContext(), "Não há expediente nos fins de semana favor escolher outro dia!", Toast.LENGTH_LONG).show();
            return false;
        }else if(calendar_fim.get(calendar_fim.DAY_OF_WEEK) == calendar_fim.SUNDAY || calendar_fim.get(calendar_fim.DAY_OF_WEEK) == calendar_fim.SATURDAY){//verifica se é fim de semana
            Toast.makeText(getApplicationContext(), "Não há expediente nos fins de semana favor escolher outro dia!", Toast.LENGTH_LONG).show();
            return false;
        }else if((hora_inicio < 8)|| (hora_inicio >= 17 && minuto_inicio >=30) || (hora_inicio >= 12 && (hora_inicio <= 13 && minuto_inicio <= 30))){// valida se está no horario de almoço ou fora do expediente
            Toast.makeText(getApplicationContext(), "Hora de início da consulta fora do horário de expediente, favor escolher um horário entre as 8:00 e as 12:00 ou entre às 13:30 e 17:30!", Toast.LENGTH_LONG).show();
            return false;
        }else if((hora_fim<= 8 && minuto_fim == 0)|| (hora_fim >= 17 && minuto_fim >=30) || (hora_fim >= 12 && (hora_fim <= 13 && minuto_fim <= 30))){///faz a mesma verificação acima so que com o horario de fim
            Toast.makeText(getApplicationContext(), "Hora de fim da consulta fora do horário de expediente, favor escolher um horário entre as 8:00 e as 12:00 ou entre às 13:30 e 17:30!", Toast.LENGTH_LONG).show();
            return false;
        }else if ((hora_inicio + (minuto_inicio/60)) >= (hora_fim + (minuto_fim/60))){//verifica se o horario de saida é menor que o de inicio
            Toast.makeText(getApplicationContext(), "O horário de início não pode ser maior ou igual ao horário de fim", Toast.LENGTH_LONG).show();
            return false;
        }else if(getConsultConflit(inicio, fim)>0){//verifica se há consultas nesse horario
            Toast.makeText(getApplicationContext(), "Há consultas nestes horários, favor consultar os horários já reservados!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    private void insereConsulta () {
        String sInicio = tData.getText().toString().trim()+ " " +tInicio.getText().toString().trim();
        String sFim = tData.getText().toString().trim() + " " + tFim.getText().toString().trim();
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
        }else if (validaInicioFim()) {

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
