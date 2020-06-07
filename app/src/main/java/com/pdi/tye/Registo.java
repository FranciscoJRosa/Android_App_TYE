package com.pdi.tye;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registo extends AppCompatActivity {

    DataBaseHelper db;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        db = new DataBaseHelper(this);

        actionBar = getSupportActionBar();
        actionBar.hide();

        final EditText et_username = findViewById(R.id.et_username);
        final EditText et_password = findViewById(R.id.et_password);
        final EditText et_confPassword = findViewById(R.id.et_confPassword);
        Button btn_registar = findViewById(R.id.btn_registar);
        TextView txt_login = findViewById(R.id.txt_login);

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Registo.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        btn_registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String confPassword = et_confPassword.getText().toString().trim();

                Boolean res = db.checkUsername(user);
                if (res==true){
                    if (password.equals(confPassword)){
                        long val = db.addUser(user, password);
                        if (val >0) {
                            Toast.makeText(Registo.this, "Utilizador registado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(Registo.this, MainActivity.class);
                            startActivity(login);
                            finish();
                        }else {
                            Toast.makeText(Registo.this, "Erro no registo!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Registo.this, "Password não confirmada!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Registo.this, "username já existe", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
