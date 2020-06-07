package com.pdi.tye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CriarContaFin extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    DataBaseHelper db;

    ActionBar actionBar;

    private ImageView image_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_fin);

        Intent intent = getIntent();
        final String username = intent.getStringExtra(ContaFinanceira.EXTRA_TEXT);

        db = new DataBaseHelper(this);

        populateSampleDataDespesas(username);
        populateSampleDataRendimentos(username);

        actionBar = getSupportActionBar();
        actionBar.hide();


        final EditText txt_designacao = findViewById(R.id.txt_designacao);
        final  EditText txt_saldo = findViewById(R.id.txt_saldo);
        final Button btn_adicionar = findViewById(R.id.btn_adicionar);
        image_logout = findViewById(R.id.img_logoutCriarContaFin);


        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String designacao = txt_designacao.getText().toString().trim();
                String saldo = txt_saldo.getText().toString().trim();
                Cursor cursor = db.getID(username);
                while (cursor.moveToNext()){
                    String utilizadorID = cursor.getString(0);
                    Boolean designacaoValida = db.checkDesignacaoConta(designacao, utilizadorID);
                    if (designacao.isEmpty() || saldo.isEmpty()){
                        Toast.makeText(CriarContaFin.this, "Campos por preencher!",Toast.LENGTH_SHORT).show();
                    }else if (designacaoValida==true){
                        long val = db.criarConta(designacao, saldo, utilizadorID);
                        if(val>0){
                            Toast.makeText(CriarContaFin.this, "Conta criada com sucesso!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CriarContaFin.this, ContaFinanceira.class);
                            intent.putExtra(EXTRA_TEXT, username);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CriarContaFin.this, "Conta não adicionada",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CriarContaFin.this, "Já existe uma conta registada com a designação "+designacao, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.logout(CriarContaFin.this);
                Intent logout_intent = new Intent(CriarContaFin.this, MainActivity.class);
                startActivity(logout_intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        final String username = intent.getStringExtra(ContaFinanceira.EXTRA_TEXT);
        Intent intentback = new Intent(CriarContaFin.this, ContaFinanceira.class);
        intentback.putExtra(EXTRA_TEXT, username);
        startActivity(intentback);
        finish();
    }

    private void populateSampleDataRendimentos(String username) {
        Cursor id_cursor = db.getID(username);
        while (id_cursor.moveToNext()){
            String utilizadorID = id_cursor.getString(0);
            Boolean isEmpty = db.checkDataCatRendimento(utilizadorID);
            if (isEmpty==true){
                db.addCatRendimento("Salário", utilizadorID);
                db.addCatRendimento("Investimentos", utilizadorID);
                db.addCatRendimento("Prémios Monetários", utilizadorID);
                db.addCatRendimento("Vendas", utilizadorID);
                db.addCatRendimento("Mesada", utilizadorID);
                db.addCatRendimento("Semanada", utilizadorID);
                db.addCatRendimento("Horas Extra", utilizadorID);
            }
        }
    }

    private void populateSampleDataDespesas(String username) {
        Cursor id_cursor = db.getID(username);
        while (id_cursor.moveToNext()) {
            String utilizadorID = id_cursor.getString(0);
            Boolean isEmpty = db.checkDataCatDespesa(utilizadorID);
            if (isEmpty==true){
                db.AddCatDespesa("Todas", utilizadorID);
                db.AddCatDespesa("Mercearia", utilizadorID);
                db.AddCatDespesa("Entertenimento", utilizadorID);
                db.AddCatDespesa("Vestuário e Calçado", utilizadorID);
                db.AddCatDespesa("Saúde", utilizadorID);
                db.AddCatDespesa("Carro e Transporte", utilizadorID);
                db.AddCatDespesa("Comer Fora", utilizadorID);
                db.AddCatDespesa("Fitness", utilizadorID);
                db.AddCatDespesa("Subscrições", utilizadorID);
                db.AddCatDespesa("Despesa de Alojamento", utilizadorID);
                db.AddCatDespesa("Cuidados Pessoais", utilizadorID);
            }
        }
    }

}
