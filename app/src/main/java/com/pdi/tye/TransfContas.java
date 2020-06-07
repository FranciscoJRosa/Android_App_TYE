package com.pdi.tye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

public class TransfContas extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    public static final String EXTRA_TEXT1 = "com.pdi.tye.EXTRA_TEXT1";
    public static final String EXTRA_TEXT2 = "com.pdi.tye.EXTRA_TEXT2";

    public static final String TEXT_DESP = "com.pdi.tye.TEXT_DESP";
    public static final String TEXT_DESP_ID = "com.pdi.tye.TEXT_DESP";

    private Spinner spn_contaDestino;
    private EditText et_valorTransf;
    private TextView txt_dataTransf;
    private Button btn_transferir;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText et_designacaoTransf;

    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transf_contas);
        db = new DataBaseHelper(this);

        getSupportActionBar().setElevation(0);

        Intent transf_intent = getIntent();
        final String contaID = transf_intent.getStringExtra(EXTRA_TEXT1);
        final String saldo = transf_intent.getStringExtra(EXTRA_TEXT2);

        final String desp = "despesa";

        spn_contaDestino = findViewById(R.id.spn_contaDestino);
        et_valorTransf = findViewById(R.id.et_valorTransf);
        txt_dataTransf = findViewById(R.id.txt_dataTransf);
        btn_transferir = findViewById(R.id.btn_transferir);
        et_designacaoTransf = findViewById(R.id.et_designacaoTransf);


        List<String> listaContas = new ArrayList<>();
        listaContas.add("Selecione a Conta Destino...");
        Cursor cursorUserID = db.getUtilizadorID(contaID);
        while (cursorUserID.moveToNext()){
            String utilizadorID = cursorUserID.getString(0);
            Cursor contas = db.getContasTransf(contaID, utilizadorID);
            if(contas.getCount()==0){
                Toast.makeText(TransfContas.this, "Não existem contas definidas", Toast.LENGTH_SHORT).show();
            }else {
                while (contas.moveToNext()){
                    listaContas.add(contas.getString(0));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, listaContas){
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
                    spn_contaDestino.setAdapter(adapter);
                }
            }

        }

        txt_dataTransf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TransfContas.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month < 10 && dayOfMonth < 10){
                    String date = year + "/0" + month + "/0" + dayOfMonth;
                    txt_dataTransf.setText(date);
                } else if (dayOfMonth < 10){
                    String date = year + "/" + month + "/0" + dayOfMonth;
                    txt_dataTransf.setText(date);
                } else if (month <10){
                    String date = year + "/0" + month + "/" + dayOfMonth;
                    txt_dataTransf.setText(date);
                } else {
                    String date = year + "/" + month + "/" + dayOfMonth;
                    txt_dataTransf.setText(date);
                }
            }
        };

        btn_transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorTransf = et_valorTransf.getText().toString().trim();
                String contaDestino =spn_contaDestino.getSelectedItem().toString().trim();
                String designacaoTransf = et_designacaoTransf.getText().toString().trim();
                String dataTransf = txt_dataTransf.getText().toString().trim();
                if (valorTransf.isEmpty() || designacaoTransf.isEmpty() || dataTransf.isEmpty()){
                    Toast.makeText(TransfContas.this, "Campos por preencher!", Toast.LENGTH_SHORT).show();
                } else{
                    Boolean debito = db.transfContaOrigem(contaID,valorTransf,saldo);
                    Cursor cur_utilizadorID = db.getUtilizadorID(contaID);
                    while (cur_utilizadorID.moveToNext()){
                        String utilizadorID = cur_utilizadorID.getString(0);
                        Cursor cur_contaDestinoID = db.getContaID(contaDestino,utilizadorID);
                        while (cur_contaDestinoID.moveToNext()){
                            String contaDestinoID = cur_contaDestinoID.getString(0);
                            long res = db.addTransferencia(designacaoTransf,contaDestinoID,valorTransf,dataTransf,contaID);
                            Cursor cur_contaDestinoSaldo = db.getSaldoConta(contaDestino, utilizadorID);
                            while(cur_contaDestinoSaldo.moveToNext()){
                                String contaDestinoSaldo = cur_contaDestinoSaldo.getString(0);
                                Boolean credito = db.transfContaDestino(contaDestinoID,valorTransf,contaDestinoSaldo);
                            }
                            if(res>0){
                                Toast.makeText(TransfContas.this, "Transferência efetuada com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intentTransf = new Intent(TransfContas.this, MenuFuncionalidades.class);
                                intentTransf.putExtra(TEXT_DESP, desp);
                                intentTransf.putExtra(TEXT_DESP_ID, contaID);
                                startActivity(intentTransf);
                                finish();

                            }else {
                                Toast.makeText(TransfContas.this, "Transferência não efetuada!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
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
                        Intent logout_intent = new Intent(TransfContas.this, MainActivity.class);
                        startActivity(logout_intent);
                        finish();
                        return true;
                    case R.id.carteiraContasItem:
                        Intent carteira_intent = new Intent(TransfContas.this, ContaFinanceira.class);
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

        Intent intentResumos = new Intent(TransfContas.this, MenuFuncionalidades.class);
        intentResumos.putExtra(TEXT_DESP, desp);
        intentResumos.putExtra(TEXT_DESP_ID, contaID);
        startActivity(intentResumos);
        finish();
    }
}
