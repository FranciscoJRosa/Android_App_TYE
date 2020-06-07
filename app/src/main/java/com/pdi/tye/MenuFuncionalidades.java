package com.pdi.tye;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MenuFuncionalidades extends AppCompatActivity {

    DataBaseHelper db;

    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    public static final String EXTRA_TEXT1 = "com.pdi.tye.EXTRA_TEXT1";
    public static final String EXTRA_TEXT2 = "com.pdi.tye.EXTRA_TEXT2";



    public static final String TEXT_DESP = "com.pdi.tye.TEXT_DESP";
    public static final String TEXT_DESP_ID = "com.pdi.tye.TEXT_DESP";

    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_funcionalidades);

        actionBar = getSupportActionBar();
        actionBar.hide();

        db = new DataBaseHelper(this);



        final Intent intent = getIntent();
        final Model model = intent.getParcelableExtra("ID");

        final Intent intentDesp = getIntent();
        final String desp = intentDesp.getStringExtra(TEXT_DESP);
        final String idConta = intent.getStringExtra(TEXT_DESP_ID);

        TextView txt_designacaoConta = findViewById(R.id.txt_designacaoConta);
        TextView txt_saldoConta = findViewById(R.id.txt_saldoConta);
        CardView btn_regDespesa = findViewById(R.id.btn_regDespesa);
        CardView btn_regRend = findViewById(R.id.btn_regRend);
        CardView btn_Transf = findViewById(R.id.btn_Transf);
        CardView btn_consRes = findViewById(R.id.btn_consResumos);
        CardView btn_limDespesas = findViewById(R.id.btn_limDespesas);

        checkExcDespesaLimite();

        if(desp == null){
            final String contaID = model.getId();
            final String designacaoConta = model.getDesignacao();
            String saldoConta = model.getSaldo();

            txt_designacaoConta.setText(designacaoConta);
            txt_saldoConta.setText("Saldo= "+saldoConta+"€");

            btn_regDespesa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent regDesp_intent = new Intent(MenuFuncionalidades.this, RegDespesa.class);
                    regDesp_intent.putExtra(EXTRA_TEXT1, model.getId());
                    regDesp_intent.putExtra(EXTRA_TEXT2, model.getSaldo());
                    startActivity(regDesp_intent);
                    finish();

                }
            });

            btn_regRend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent regRend_intent = new Intent(MenuFuncionalidades.this, RegRendimento.class);
                    regRend_intent.putExtra(EXTRA_TEXT1, model.getId());
                    regRend_intent.putExtra(EXTRA_TEXT2, model.getSaldo());
                    startActivity(regRend_intent);
                    finish();
                }
            });

            btn_Transf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transf_intent = new Intent(MenuFuncionalidades.this, TransfContas.class);
                    transf_intent.putExtra(EXTRA_TEXT1, model.getId());
                    transf_intent.putExtra(EXTRA_TEXT2, model.getSaldo());
                    startActivity(transf_intent);
                    finish();
                }
            });

            btn_consRes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent res_intent = new Intent(MenuFuncionalidades.this, Resumos.class);
                    res_intent.putExtra(EXTRA_TEXT1, model.getId());
                    startActivity(res_intent);
                    finish();
                }
            });

            btn_limDespesas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent limiteDesp_intent = new Intent(MenuFuncionalidades.this, LimiteDespesas.class);
                    limiteDesp_intent.putExtra(EXTRA_TEXT1, model.getId());
                    startActivity(limiteDesp_intent);
                    finish();
                }
            });
        } else {
            Cursor cursor = db.getInfoConta(idConta);
            while(cursor.moveToNext()){
                String designacaoConta = cursor.getString(1);
                final String saldo = cursor.getString(2);
                txt_designacaoConta.setText(designacaoConta);
                txt_saldoConta.setText("Saldo= "+saldo+"€");

                btn_regDespesa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent regDesp_intent = new Intent(MenuFuncionalidades.this, RegDespesa.class);
                        regDesp_intent.putExtra(EXTRA_TEXT1, idConta);
                        regDesp_intent.putExtra(EXTRA_TEXT2, saldo);
                        startActivity(regDesp_intent);
                        finish();
                    }
                });

                btn_regRend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent regRend_intent = new Intent(MenuFuncionalidades.this, RegRendimento.class);
                        regRend_intent.putExtra(EXTRA_TEXT1, idConta);
                        regRend_intent.putExtra(EXTRA_TEXT2, saldo);
                        startActivity(regRend_intent);
                        finish();
                    }
                });

                btn_Transf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent transf_intent = new Intent(MenuFuncionalidades.this, TransfContas.class);
                        transf_intent.putExtra(EXTRA_TEXT1, idConta);
                        transf_intent.putExtra(EXTRA_TEXT2, saldo);
                        startActivity(transf_intent);
                        finish();
                    }
                });

                btn_consRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent res_intent = new Intent(MenuFuncionalidades.this, Resumos.class);
                        res_intent.putExtra(EXTRA_TEXT1, idConta);
                        startActivity(res_intent);
                        finish();
                    }
                });

                btn_limDespesas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent limiteDesp_intent = new Intent(MenuFuncionalidades.this, LimiteDespesas.class);
                        limiteDesp_intent.putExtra(EXTRA_TEXT1, idConta);
                        startActivity(limiteDesp_intent);
                        finish();
                    }
                });

            }

        }



    }

    private void checkExcDespesaLimite() {
        final Intent intent = getIntent();
        final Model model = intent.getParcelableExtra("ID");
        final String idConta = intent.getStringExtra(TEXT_DESP_ID);
        final Intent intentDesp = getIntent();
        final String desp = intentDesp.getStringExtra(TEXT_DESP);
        if (desp == null) {
            Date data = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String dataAtual = df.format(data);
            int res = db.deleteLimiteDataFim(dataAtual, model.getId());
            if (res > 0) {
                Toast.makeText(MenuFuncionalidades.this, "Limite eliminado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Date data = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String dataAtual = df.format(data);
            int res = db.deleteLimiteDataFim(dataAtual, idConta);
            if (res > 0) {
                Toast.makeText(MenuFuncionalidades.this, "Limite eliminado", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onBackPressed() {
        final Intent intent = getIntent();
        final Model model = intent.getParcelableExtra("ID");
        final String idConta = intent.getStringExtra(TEXT_DESP_ID);
        final Intent intentDesp = getIntent();
        final String desp = intentDesp.getStringExtra(TEXT_DESP);
        if (desp == null){
            Cursor userID_cursor = db.getUtilizadorID(model.getId());
            while (userID_cursor.moveToNext()) {
                String userID = userID_cursor.getString(0);
                Cursor username_cursor = db.getUsername(userID);
                while (username_cursor.moveToNext()) {
                    String user = username_cursor.getString(0);
                    Intent contaFinanceiraIntent = new Intent(MenuFuncionalidades.this, ContaFinanceira.class);
                    contaFinanceiraIntent.putExtra(EXTRA_TEXT, user);
                    startActivity(contaFinanceiraIntent);
                    finish();
                }
            }
        } else {
            Cursor userID_cursor = db.getUtilizadorID(idConta);
            while (userID_cursor.moveToNext()){
                String userID = userID_cursor.getString(0);
                Cursor username_cursor = db.getUsername(userID);
                while (username_cursor.moveToNext()){
                    String user = username_cursor.getString(0);
                    Intent contaFinanceiraIntent = new Intent(MenuFuncionalidades.this, ContaFinanceira.class);
                    contaFinanceiraIntent.putExtra(EXTRA_TEXT, user);
                    startActivity(contaFinanceiraIntent);
                    finish();
                }
            }
        }
    }
}
