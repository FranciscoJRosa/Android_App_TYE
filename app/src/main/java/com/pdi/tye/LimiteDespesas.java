package com.pdi.tye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LimiteDespesas extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    public static final String EXTRA_TEXT1 = "com.pdi.tye.EXTRA_TEXT1";

    public static final String TEXT_DESP = "com.pdi.tye.TEXT_DESP";
    public static final String TEXT_DESP_ID = "com.pdi.tye.TEXT_DESP";

    DataBaseHelper db;

    private EditText et_designacaoLimite;
    private EditText et_valorLimite;
    private TextView txt_data_Inicio;
    private TextView txt_dataLimite;
    private Spinner spn_categoriasLimite;
    private Button btn_adicionarLimite;
    private Button btn_verLimitesAtivos;
    private DatePickerDialog.OnDateSetListener dateSetListenerInicio;
    private DatePickerDialog.OnDateSetListener dateSetListenerFim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limite_despesas);

        db = new DataBaseHelper(this);

        getSupportActionBar().setElevation(0);

        Intent limiteDesp_intent = getIntent();
        final String contaID = limiteDesp_intent.getStringExtra(EXTRA_TEXT1);

        final String desp = "despesa";

        et_designacaoLimite = findViewById(R.id.et_designacaoLimite);
        et_valorLimite = findViewById(R.id.et_valorLimite);
        txt_data_Inicio = findViewById(R.id.txt_data_Inicio);
        txt_dataLimite = findViewById(R.id.txt_dataLimite);
        spn_categoriasLimite = findViewById(R.id.spn_categoriasLimite);
        btn_adicionarLimite = findViewById(R.id.btn_adicionarLimite);
        btn_verLimitesAtivos = findViewById(R.id.btn_verLimitesAtivos);

        Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
        while (utilizadorID_cursor.moveToNext()){
            String utilizadorID = utilizadorID_cursor.getString(0);
            List<String> listaCat = new ArrayList<>();
            listaCat.add("Selecione a Categoria...");
            Cursor categorias = db.getDataCatDespesa(utilizadorID);

            if(categorias.getCount()==0){
                Toast.makeText(LimiteDespesas.this, "Não existem categorias definidas", Toast.LENGTH_SHORT).show();
            }else {
                while (categorias.moveToNext()){
                    listaCat.add(categorias.getString(1));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, listaCat) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position==0)
                            {
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView textView = (TextView) view;
                            if (position==0){
                                textView.setTextColor(Color.GRAY);
                            } else {
                                textView.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_categoriasLimite.setAdapter(adapter);
                }
            }
        }


        txt_data_Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogInicio = new DatePickerDialog(LimiteDespesas.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListenerInicio,
                        year, month, day);
                dialogInicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInicio.show();
            }
        });
        dateSetListenerInicio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month < 10 && dayOfMonth < 10){
                    String date = year + "/0" + month + "/0" + dayOfMonth;
                    txt_data_Inicio.setText(date);
                } else if (dayOfMonth < 10){
                    String date = year + "/" + month + "/0" + dayOfMonth;
                    txt_data_Inicio.setText(date);
                } else if (month <10){
                    String date = year + "/0" + month + "/" + dayOfMonth;
                    txt_data_Inicio.setText(date);
                } else {
                    String date = year + "/" + month + "/" + dayOfMonth;
                    txt_data_Inicio.setText(date);
                }
            }
        };

        txt_dataLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogFim = new DatePickerDialog(LimiteDespesas.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListenerFim,
                        year, month, day);
                dialogFim.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogFim.show();
            }
        });
        dateSetListenerFim = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month < 10 && dayOfMonth < 10){
                    String date = year + "/0" + month + "/0" + dayOfMonth;
                    txt_dataLimite.setText(date);
                } else if (dayOfMonth < 10){
                    String date = year + "/" + month + "/0" + dayOfMonth;
                    txt_dataLimite.setText(date);
                } else if (month <10){
                    String date = year + "/0" + month + "/" + dayOfMonth;
                    txt_dataLimite.setText(date);
                } else {
                    String date = year + "/" + month + "/" + dayOfMonth;
                    txt_dataLimite.setText(date);
                }
            }
        };


        btn_adicionarLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String designacaoLimite = et_designacaoLimite.getText().toString();
                String valorLimite = et_valorLimite.getText().toString();
                String data_Inicio = txt_data_Inicio.getText().toString();
                String dataLimite = txt_dataLimite.getText().toString();
                String categoria = spn_categoriasLimite.getSelectedItem().toString();
                if (designacaoLimite.isEmpty() || valorLimite.isEmpty() || data_Inicio.isEmpty() || dataLimite.isEmpty()){
                    Toast.makeText(LimiteDespesas.this, "Campos por preencher!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
                    while (utilizadorID_cursor.moveToNext()){
                        String utilizadorID = utilizadorID_cursor.getString(0);
                        Cursor cursor_CatID = db.getCatID(categoria, utilizadorID);
                        while (cursor_CatID.moveToNext()){
                            String catID = cursor_CatID.getString(0);
                            Boolean exists = db.checkLimitesCriados(contaID,catID,data_Inicio,dataLimite);
                            if (exists==true){
                                Toast.makeText(LimiteDespesas.this, "Já existe um limite criado na categoria e intervalo de tempo selecionado!", Toast.LENGTH_LONG).show();
                            } else {
                                long res = db.addLimiteDespesas(designacaoLimite, valorLimite, data_Inicio, dataLimite, catID, contaID);
                                if (res>0){
                                    Toast.makeText(LimiteDespesas.this, "Limite de Despesas definido com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intentLimiteDesp = new Intent(LimiteDespesas.this, MenuFuncionalidades.class);
                                    intentLimiteDesp.putExtra(TEXT_DESP, desp);
                                    intentLimiteDesp.putExtra(TEXT_DESP_ID, contaID);
                                    startActivity(intentLimiteDesp);
                                    finish();
                                } else {
                                    Toast.makeText(LimiteDespesas.this, "Limite de Despesas não definido!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });

        btn_verLimitesAtivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_verLimites = new Intent(LimiteDespesas.this, ShowLimitesDesp.class);
                intent_verLimites.putExtra(EXTRA_TEXT1, contaID);
                startActivity(intent_verLimites);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent limiteDesp_intent = getIntent();
        final String contaID = limiteDesp_intent.getStringExtra(EXTRA_TEXT1);
        Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
        while (utilizadorID_cursor.moveToNext()){
            String utilizadorID = utilizadorID_cursor.getString(0);
            Cursor username_cursor = db.getUsername(utilizadorID);
            while (username_cursor.moveToNext()){
                String username = username_cursor.getString(0);
                switch (item.getItemId()){
                    case R.id.logoutItemOption:
                        Shared.logout(this);
                        Intent logout_intent = new Intent(LimiteDespesas.this, MainActivity.class);
                        startActivity(logout_intent);
                        finish();
                        return true;
                    case R.id.carteiraContasItem:
                        Intent carteira_intent = new Intent(LimiteDespesas.this, ContaFinanceira.class);
                        carteira_intent.putExtra(EXTRA_TEXT, username);
                        startActivity(carteira_intent);
                        finish();
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent res_intent = getIntent();
        final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);
        final String desp = "despesa";

        Intent intentResumos = new Intent(LimiteDespesas.this, MenuFuncionalidades.class);
        intentResumos.putExtra(TEXT_DESP, desp);
        intentResumos.putExtra(TEXT_DESP_ID, contaID);
        startActivity(intentResumos);
        finish();
    }
}
