package com.pdi.tye;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShowLimitesDesp extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";
    public static final String EXTRA_TEXT1 = "com.pdi.tye.EXTRA_TEXT1";

    RecyclerView rv_Limites;
    ArrayList<ModelLimites> modelLimitesArrayList;
    AdapterLimites adapterLimites;

    DataBaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_limites_desp);

        getSupportActionBar().setElevation(0);

        rv_Limites = findViewById(R.id.rv_limites);
        modelLimitesArrayList = new ArrayList<>();

        Intent limiteDesp_intent = getIntent();
        final String contaID = limiteDesp_intent.getStringExtra(EXTRA_TEXT1);
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM excedenciaDespesas WHERE contaID=?", new String[] {contaID});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não Existe Limites Definidos", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor categoria_cursor = dataBaseHelper.getDesignacaoCat(cursor.getString(5));
                        while (categoria_cursor.moveToNext()){
                            modelLimitesArrayList.add(new ModelLimites(cursor.getString(0),
                                    "Designação: "+cursor.getString(1),
                                    "Valor Limite: "+cursor.getString(2)+"€",
                                    "Data de Inicio: "+cursor.getString(3),
                                    "Data de Fim: "+cursor.getString(4),
                                    "Categoria: "+categoria_cursor.getString(0),
                                    cursor.getString(6)));
                        }
                    }
                    adapterLimites = new AdapterLimites(modelLimitesArrayList);
                    rv_Limites.hasFixedSize();
                    rv_Limites.setLayoutManager(new LinearLayoutManager(this));
                    rv_Limites.setAdapter(adapterLimites);
                }
            }else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_LONG).show();
            }
            dataBaseHelper.close();
        } catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_Limites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        db = new DataBaseHelper(this);
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
                        Intent logout_intent = new Intent(ShowLimitesDesp.this, MainActivity.class);
                        startActivity(logout_intent);
                        return true;
                    case R.id.carteiraContasItem:
                        Intent carteira_intent = new Intent(ShowLimitesDesp.this, ContaFinanceira.class);
                        carteira_intent.putExtra(EXTRA_TEXT, username);
                        startActivity(carteira_intent);
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    ModelLimites contaApagada = null;

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
                contaApagada = modelLimitesArrayList.get(position);
                modelLimitesArrayList.remove(position);
                adapterLimites.notifyItemRemoved(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ShowLimitesDesp.this);
                builder.setTitle("AVISO");
                builder.setMessage("Tem a certeza que pretende eliminar permanentemente o limite com "+contaApagada.getDesignacaoLimite()+"?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int res = db.deleteLimiteDespesas(contaApagada.getLimiteID());
                        if (res > 0){
                            Toast.makeText(ShowLimitesDesp.this, "Limite eliminado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelLimitesArrayList.add(position, contaApagada);
                        adapterLimites.notifyItemInserted(position);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ShowLimitesDesp.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ShowLimitesDesp.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onBackPressed() {
        Intent limiteDesp_intent = getIntent();
        final String contaID = limiteDesp_intent.getStringExtra(EXTRA_TEXT1);

        Intent limiteDesp_intentback = new Intent(ShowLimitesDesp.this, LimiteDespesas.class);
        limiteDesp_intentback.putExtra(EXTRA_TEXT1, contaID);
        startActivity(limiteDesp_intentback);
        finish();
    }
}
