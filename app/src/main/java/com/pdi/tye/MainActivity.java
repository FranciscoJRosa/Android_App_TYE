package com.pdi.tye;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper db;

    ActionBar actionBar;

    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHelper(this);

        actionBar = getSupportActionBar();
        actionBar.hide();

        final EditText et_username = findViewById(R.id.et_username);
        final EditText et_password = findViewById(R.id.et_password);
        Button btn_entrar = findViewById(R.id.btn_entrar);
        TextView txt_novoUtilizador = findViewById(R.id.txt_novoUtilizador);

        if (Shared.getUsername(this) != null){
            Intent contaFinanceiraIntent = new Intent(MainActivity.this, ContaFinanceira.class);
            startActivity(contaFinanceiraIntent);
            finish();
        } else {

        }

        txt_novoUtilizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registoIntent = new Intent(MainActivity.this, Registo.class);
                startActivity(registoIntent);
                finish();
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                Boolean res = db.checkUser(user, password);
                if (res==true){
                    Shared.saveUsername(user, MainActivity.this);
                    Intent contaFinanceiraIntent = new Intent(MainActivity.this, ContaFinanceira.class);
                    contaFinanceiraIntent.putExtra(EXTRA_TEXT, user);
                    startActivity(contaFinanceiraIntent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Username ou password incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
