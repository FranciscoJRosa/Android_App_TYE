package com.pdi.tye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ContaFinanceira extends AppCompatActivity implements EditContaDialog.EditContaDialogListener{
    DataBaseHelper db;

    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";

    private long backPressedTime;

    ActionBar actionBar;
    RecyclerView rv_contas;
    String username;

    ArrayList<Model> modelArrayList;

    Adapter adapter;

    private ImageView image_logout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_financeira);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TEXT)){
            username = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        } else {
            username = Shared.getUsername(this);
            Toast.makeText(ContaFinanceira.this, "Bem-vindo "+username, Toast.LENGTH_LONG).show();

        }



        actionBar = getSupportActionBar();
        actionBar.hide();

        image_logout = findViewById(R.id.image_logout);


        rv_contas = findViewById(R.id.rv_contas);

        modelArrayList = new ArrayList<>();

        showContas(null);


        FloatingActionButton fab = findViewById(R.id.fbtn_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nova = new Intent(ContaFinanceira.this, CriarContaFin.class);
                nova.putExtra(EXTRA_TEXT, username);
                startActivity(nova);
                finish();
            }
        });

        if(modelArrayList.size()!=0){
            adapter.setOnItemClickedListener(new Adapter.OnItemClickedListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(ContaFinanceira.this, MenuFuncionalidades.class);
                    intent.putExtra("ID", modelArrayList.get(position));
                    startActivity(intent);
                    finish();
                }
            });

        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_contas);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper2.attachToRecyclerView(rv_contas);

        image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.logout(ContaFinanceira.this);
                Intent logout_intent = new Intent(ContaFinanceira.this, MainActivity.class);
                startActivity(logout_intent);
                finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showContas(View view)
    {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursorID = dataBaseHelper.getID(username);
                while (cursorID.moveToNext()){
                    String utilizadorID = cursorID.getString(0);
                    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM conta WHERE utilizadorID=?", new String[] {utilizadorID});
                    if (cursor.getCount()==0) {
                        Toast.makeText(this, "Não existem contas criadas", Toast.LENGTH_SHORT).show();
                    }else {
                        while (cursor.moveToNext()) {
                            modelArrayList.add(new Model(cursor.getString(0),
                                    cursor.getString(1),
                                    cursor.getString(2)));
                        }
                        adapter = new Adapter(this, modelArrayList);
                        rv_contas.hasFixedSize();
                        rv_contas.setLayoutManager(new LinearLayoutManager(this));
                        rv_contas.setAdapter(adapter);
                    }
                }
            }else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
            dataBaseHelper.close();
        } catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    Model contaApagada = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            db = new DataBaseHelper(getApplicationContext());
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT){
                contaApagada = modelArrayList.get(position);
                modelArrayList.remove(position);
                adapter.notifyItemRemoved(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ContaFinanceira.this);
                builder.setTitle("AVISO");
                builder.setMessage("Tem a certeza que pretende eliminar permanentemente a conta: "+contaApagada.getDesignacao()+"?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int res = db.deleteContaFin(contaApagada.getId());
                        int res1 = db.deleteDespesaConta(contaApagada.getId());
                        int res2 = db.deleteRendimentoConta(contaApagada.getId());
                        int res3 = db.deleteExcDespesaConta(contaApagada.getId());
                        int res4 = db.deleteTransferenciaConta(contaApagada.getId());
                        if (res > 0){
                            Toast.makeText(ContaFinanceira.this, "Conta eliminada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelArrayList.add(position, contaApagada);
                        adapter.notifyItemInserted(position);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ContaFinanceira.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ContaFinanceira.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    Model contaEdit = null;

    ItemTouchHelper.SimpleCallback simpleCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            db = new DataBaseHelper(getApplicationContext());
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.RIGHT){
                contaEdit = modelArrayList.get(position);
                openDialog(contaEdit.getId(), contaEdit.getDesignacao(), contaEdit.getSaldo());

            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ContaFinanceira.this, R.color.colorAccent))
                    .addSwipeRightActionIcon(R.drawable.ic_mode_edit_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ContaFinanceira.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void openDialog(String contaID, String designacaoConta, String saldoConta) {
        EditContaDialog editContaDialog = new EditContaDialog();
        editContaDialog = EditContaDialog.newInstance(contaID, designacaoConta, saldoConta);
        editContaDialog.show(getSupportFragmentManager(), "Dialog");
    }

    public void applyTexts(String contaID, String designacaoConta, String saldoConta){
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TEXT)){
            username = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        } else {
            username = Shared.getUsername(this);
            Toast.makeText(ContaFinanceira.this, "Bem-vindo "+username, Toast.LENGTH_LONG).show();
        }
        if (contaID.equals("Cancelar")){
            Intent intentback = new Intent(ContaFinanceira.this, ContaFinanceira.class);
            intentback.putExtra(EXTRA_TEXT, username);
            startActivity(intentback);
            finish();
        } else {
            long res = db.updateInfoConta(contaID, designacaoConta, saldoConta);
            if (res > 0){

                Toast.makeText(ContaFinanceira.this, "Informação da conta alterada com sucesso!",Toast.LENGTH_SHORT).show();
                Intent intentback = new Intent(ContaFinanceira.this, ContaFinanceira.class);
                intentback.putExtra(EXTRA_TEXT, username);
                startActivity(intentback);
                finish();
            } else {
                Toast.makeText(ContaFinanceira.this, "Informação da conta não alterada!",Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        } else {
            Toast.makeText(ContaFinanceira.this, "Pressione Voltar novamente para sair", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
