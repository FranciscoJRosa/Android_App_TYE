package com.pdi.tye;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Display;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {
    //Base de Dados
    public static final String DATABASE_NAME="tye.db";

    //Tabela utilizador
    public static final String TABLE_NAME="utilizador";
    public static final String COL_1="ID";
    public static final String COL_2="username";
    public static final String COL_3="password";

    //Tabela conta
    public static final String TABLE1_NAME="conta";
    public static final String COL1_1="ID";
    public static final String COL1_2="designacao";
    public static final String COL1_3="saldo";
    public static final String COL1_4="utilizadorID";

    //Tabela categoriaDespesa
    public static final String TABLE2_NAME="categoriaDespesa";
    public static final String COL2_1="cat_ID";
    public static final String COL2_2="designacao";
    public static final String COL2_3="utilizadorID";

    //Tabela registoDespesa
    public static final String TABLE3_NAME="registoDespesa";
    public static final String COL3_1="regDesp_ID";
    public static final String COL3_2="conta_ID";
    public static final String COL3_3="cat_ID";
    public static final String COL3_4="valor";
    public static final String COL3_5="data";
    public static final String COL3_6="designacao";

    //Tabela categoriaRendimento
    public static final String TABLE4_NAME="categoriaRendimento";
    public static final String COL4_1="cat_rend_ID";
    public static final String COL4_2="designacao";
    public static final String COL4_3="utilizadorID";

    //Tabela registoRendimento
    public static final String TABLE5_NAME="registoRendimento";
    public static final String COL5_1="regRend_ID";
    public static final String COL5_2="conta_ID";
    public static final String COL5_3="cat_rend_ID";
    public static final String COL5_4="valor";
    public static final String COL5_5="data";
    public static final String COL5_6="designacao";

    //Tabela excedenciaDespesas
    public static final String TABLE6_NAME="excedenciaDespesas";
    public static final String COL6_1="exc_desp_id";
    public static final String COL6_2="designacao";
    public static final String COL6_3="limite";
    public static final String COL6_4="data_Inicio";
    public static final String COL6_5="data_Fim";
    public static final String COL6_6="cat_ID";
    public static final String COL6_7="contaID";

    //Tabela registoTransferencias
    public static final String TABLE7_NAME="registoTransferencias";
    public static final String COL7_1="regTransf_id";
    public static final String COL7_2="designacao";
    public static final String COL7_3="contaDestino_ID";
    public static final String COL7_4="valor";
    public static final String COL7_5="dataTransf";
    public static final String COL8_6="conta_ID";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 36);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE utilizador (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE "+TABLE1_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, designacao TEXT, saldo DOUBLE, utilizadorID, FOREIGN KEY (utilizadorID) REFERENCES utilizador (ID))");
        db.execSQL("CREATE TABLE "+TABLE2_NAME+" (cat_ID INTEGER PRIMARY KEY AUTOINCREMENT, designacao TEXT, utilizadorID INTEGER, FOREIGN KEY (utilizadorID) REFERENCES utilizador (ID))");
        db.execSQL("CREATE TABLE "+TABLE3_NAME+" (regDesp_ID INTEGER PRIMARY KEY AUTOINCREMENT, conta_ID INTEGER, cat_ID INTEGER, valor DOUBLE, data DATE, designacao TEXT," +
                " FOREIGN KEY(conta_ID) REFERENCES conta(ID), FOREIGN KEY(cat_ID) REFERENCES categoriaDespesa(cat_ID))");
        db.execSQL("CREATE TABLE "+TABLE4_NAME+" (cat_rend_ID INTEGER PRIMARY KEY AUTOINCREMENT, designacao TEXT, utilizadorID INTEGER, FOREIGN KEY (utilizadorID) REFERENCES utilizador (ID))");
        db.execSQL("CREATE TABLE "+TABLE5_NAME+" (regRend_ID INTEGER PRIMARY KEY AUTOINCREMENT, conta_ID INTEGER, cat_rend_ID INTEGER, valor DOUBLE, data DATE, designacao TEXT," +
                " FOREIGN KEY(conta_ID) REFERENCES conta(ID), FOREIGN KEY(cat_rend_ID) REFERENCES categoriaRendimento(cat_rend_ID))");
        db.execSQL("CREATE TABLE "+TABLE6_NAME+" (exc_desp_id INTEGER PRIMARY KEY AUTOINCREMENT, designacao TEXT, limite DOUBLE, data_Inicio DATE, data_Fim DATE, cat_ID INTEGER, contaID INTEGER, " +
                "FOREIGN KEY(cat_ID) REFERENCES categoriaDespesa(cat_ID), FOREIGN KEY(contaID) REFERENCES conta(ID))");
        db.execSQL("CREATE TABLE "+TABLE7_NAME+" (regTransf_id INTEGER PRIMARY KEY AUTOINCREMENT, designacao TEXT, contaDestino_ID INTEGER, valor DOUBLE, dataTransf DATE, conta_ID INTEGER, " +
                "FOREIGN KEY(contaDestino_ID) REFERENCES conta(ID), FOREIGN KEY(conta_ID) REFERENCES conta(ID))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE3_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE4_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE5_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE6_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE7_NAME);
        onCreate(db);

    }


    public long addUser(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        long res = db.insert("utilizador", null, contentValues);
        db.close();
        return res;

    }

    public boolean checkUser(String username, String password){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM utilizador where username=?", new String[] {username});
        int count = cursor.getCount();

        if (count>0){
            return false;
        }else {
            return true;
        }
    }



    public Cursor getID(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM utilizador WHERE username=?", new String[] {username});
        return cursor;
    }

    public boolean checkDesignacaoConta(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM conta WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        int count = cursor.getCount();
        if (count > 0){
            return false;
        }else{
            return true;
        }
    }



    public long criarConta(String designacao, String saldo, String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("saldo", saldo);
        contentValues.put("utilizadorID", utilizadorID);
        long res = db.insert("conta", null, contentValues);
        db.close();
        return res;
    }

    public long updateInfoConta(String id, String designacao, String saldo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("saldo", saldo);
        long res = db.update("conta", contentValues, "ID=?", new String[] {id});
        db.close();
        return res;
    }

    public Cursor getSaldoContaID(String contaID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT saldo FROM conta WHERE ID=?", new String[] {contaID});
        return cursor;
    }

    public long AddCatDespesa(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("utilizadorID", utilizadorID);
        long res = db.insert("categoriaDespesa", null, contentValues);
        db.close();
        return res;
    }

    public boolean checkDataCatDespesa(String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categoriaDespesa WHERE utilizadorID=?", new String[] {utilizadorID});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return  false;
        }else {
            return  true;
        }
    }

    public boolean checkCatDespesaExiste(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categoriaDespesa WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return true;
        } else {
            return false;
        }
    }

    public Cursor getDataCatDespesa(String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categoriaDespesa WHERE utilizadorID=?", new String[] {utilizadorID});
        return cursor;
    }

    public Cursor getCatID(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cat_ID FROM categoriaDespesa WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        return cursor;
    }

    public long addDespesa(String valor, String data, String catDespID, String contaID, String designacao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conta_ID", contaID);
        contentValues.put("cat_ID", catDespID);
        contentValues.put("valor", valor);
        contentValues.put("data", data);
        contentValues.put("designacao", designacao);
        long res = db.insert("registoDespesa", null, contentValues);
        db.close();
        return res;
    }

    public boolean SubtDespesa(String contaID, String saldo, String valorDesp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Double novoSaldo = Double.valueOf(saldo) - Double.valueOf(valorDesp);
        contentValues.put("saldo", novoSaldo);
        db.update("conta", contentValues, "ID=?", new String[] {contaID});
        db.close();
        return true;
    }

    public Cursor getValorDespesa(String DespID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT valor FROM registoDespesa WHERE regDesp_ID=?", new String[] {DespID});
        return cursor;
    }

    public Cursor getInfoConta(String contaID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM conta WHERE ID=?", new String[] {contaID});
        return cursor;
    }

    public long addCatRendimento(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("utilizadorID", utilizadorID);
        long res = db.insert("categoriaRendimento", null, contentValues);
        db.close();
        return res;
    }

    public Cursor getDataCatRendimento(String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categoriaRendimento WHERE utilizadorID=?", new String[] {utilizadorID});
        return cursor;
    }

    public boolean checkDataCatRendimento(String utilizadorID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categoriaRendimento WHERE utilizadorID=?", new String[] {utilizadorID});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return  false;
        }else {
            return  true;
        }
    }

    public boolean checkCatRendimentoExiste(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM categoriaRendimento WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return true;
        } else {
            return false;
        }
    }

    public Cursor getCatRendID(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cat_rend_ID FROM categoriaRendimento WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        return cursor;
    }

    public long addRendimento(String valorRend, String data, String catRendID, String contaID, String designacao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conta_ID", contaID);
        contentValues.put("cat_rend_ID", catRendID);
        contentValues.put("valor", valorRend);
        contentValues.put("data", data);
        contentValues.put("designacao", designacao);
        long res = db.insert("registoRendimento", null, contentValues);
        db.close();
        return res;
    }

    public boolean somaRend(String contaID, String saldo, String valorRend){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Double novoSaldo = Double.valueOf(saldo) + Double.valueOf(valorRend);
        contentValues.put("saldo", novoSaldo);
        db.update("conta", contentValues, "ID=?", new String[] {contaID});
        db.close();
        return true;
    }

    public Cursor getvalorRend(String rendID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT valor FROM registoRendimento WHERE regRend_ID=?", new String[] {rendID});
        return cursor;
    }

    public Cursor getContasTransf(String contaID, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT designacao FROM conta WHERE ID!=? AND utilizadorID=?", new String[] {contaID, utilizadorID});
        return cursor;
    }

    public Cursor getUtilizadorID(String contaID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT utilizadorID FROM conta WHERE ID=?", new String[] {contaID});
        return cursor;
    }

    public Cursor getUsername(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM utilizador WHERE ID=?", new String[] {ID});
        return cursor;
    }

    public long addTransferencia(String designacao, String contaDestinoID, String valorTransferencia, String dataTransf, String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("contaDestino_ID", contaDestinoID);
        contentValues.put("valor", valorTransferencia);
        contentValues.put("dataTransf", dataTransf);
        contentValues.put("Conta_ID", contaID);
        long res = db.insert("registoTransferencias", null, contentValues);
        db.close();
        return res;
    }

    public boolean transfContaOrigem(String contaID, String valorTransf, String saldo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Double novoSaldo = Double.valueOf(saldo) - Double.valueOf(valorTransf);
        contentValues.put("saldo", novoSaldo);
        db.update("conta", contentValues, "ID=?", new String[] {contaID});
        db.close();
        return true;
    }

    public boolean transfContaDestino(String contaDestinoID, String valorTransf, String saldo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Double novoSaldo = Double.valueOf(saldo) + Double.valueOf(valorTransf);
        contentValues.put("saldo", novoSaldo);
        db.update("conta", contentValues, "ID=?", new String[] {contaDestinoID});
        db.close();
        return true;
    }

    public Cursor getValorTransf(String regTransfID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT valor FROM registoTransferencias WHERE regTransf_id=?", new String[] {regTransfID});
        return cursor;
    }

    public Cursor getContaDestinoID(String regTransfID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT contaDestino_ID FROM registoTransferencias WHERE regTransf_id=?", new String[] {regTransfID});
        return cursor;
    }

    public Cursor getContaID(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM conta WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        return cursor;
    }

    public Cursor getSaldoConta(String designacao, String utilizadorID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT saldo FROM conta WHERE designacao=? AND utilizadorID=?", new String[] {designacao, utilizadorID});
        return cursor;
    }

    public ArrayList<String> queryDespesa(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> despesas = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND data>=? AND data<=? GROUP BY cat_ID ORDER BY data", new String[] {contaID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            despesas.add(cursor.getString(0));
        }
        cursor.close();
        return despesas;

    }

    public Cursor getTotalDespesas(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND data>=? AND data <=?", new String[] {contaID,dataInicio,dataFim});
        return cursor;
    }

    public Cursor getTotalDespesasCat(String contaID, String catID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND cat_ID=? AND data>=? AND data <=?", new String[] {contaID,catID,dataInicio,dataFim});
        return cursor;
    }

    public ArrayList<String> queryDespesaCat(String contaID, String dataInicio, String dataFim, String catID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> despesasCat = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND data>=? AND data<=? AND cat_ID=? GROUP BY data ORDER BY data", new String[] {contaID, dataInicio, dataFim, catID});
        while (cursor.moveToNext()){
            despesasCat.add(cursor.getString(0));
        }
        cursor.close();
        return despesasCat;
    }

    public ArrayList<String> queryDatasDespCat(String contaID, String catID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> datas = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT data FROM registoDespesa WHERE conta_ID=? AND cat_ID=? AND data>=? AND data<=? GROUP BY data ORDER BY data", new String[] {contaID, catID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            datas.add(cursor.getString(0));
        }
        cursor.close();
        return datas;

    }

    public ArrayList<String> queryCatID(String contaID, String data_Inicio, String data_Fim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> cat_ID = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT cat_ID FROM registoDespesa WHERE conta_ID=? AND data>=? AND data<=? GROUP BY cat_ID ORDER BY data", new String[] {contaID, data_Inicio, data_Fim});
        while (cursor.moveToNext()){
            cat_ID.add(cursor.getString(0));
        }
        cursor.close();
        return cat_ID;
    }

    public Cursor getDesignacaoCat(String catID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT designacao FROM categoriaDespesa WHERE cat_ID=?", new String[] {catID});
        return cursor;
    }

    public Cursor getTotalRendimentos(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoRendimento WHERE conta_ID=? AND data>=? AND data <=?", new String[] {contaID,dataInicio,dataFim});
        return cursor;
    }

    public Cursor getTotalRendimentosCat(String contaID, String catRendID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoRendimento WHERE conta_ID=? AND cat_rend_ID=? AND data>=? AND data <=?", new String[] {contaID,catRendID,dataInicio,dataFim});
        return cursor;
    }

    public ArrayList<String> queryRendimentos(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> rendimentos = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoRendimento WHERE conta_ID=? AND data>=? AND data<=? GROUP BY cat_rend_ID ORDER BY data", new String[] {contaID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            rendimentos.add(cursor.getString(0));
        }
        cursor.close();
        return rendimentos;

    }

    public ArrayList<String> queryCatRendID(String contaID, String data_Inicio, String data_Fim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> catRend_ID = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT cat_rend_ID FROM registoRendimento WHERE conta_ID=? AND data>=? AND data<=? GROUP BY cat_rend_ID ORDER BY data", new String[] {contaID, data_Inicio, data_Fim});
        while (cursor.moveToNext()){
            catRend_ID.add(cursor.getString(0));
        }
        cursor.close();
        return catRend_ID;
    }

    public Cursor getDesignacaoCatRend(String catRend_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT designacao FROM categoriaRendimento WHERE cat_rend_ID=?", new String[] {catRend_ID});
        return cursor;
    }

    public ArrayList<String> queryRendimentosCat(String contaID, String dataInicio, String dataFim, String catID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> rendimentosCat = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoRendimento WHERE conta_ID=? AND data>=? AND data<=? AND cat_rend_ID=? GROUP BY data", new String[] {contaID, dataInicio, dataFim, catID});
        while (cursor.moveToNext()){
            rendimentosCat.add(cursor.getString(0));
        }
        cursor.close();
        return rendimentosCat;
    }

    public ArrayList<String> queryDatasRendCat(String contaID, String catID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> datas = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT data FROM registoRendimento WHERE conta_ID=? AND cat_rend_ID=? AND data>=? AND data<=? GROUP BY data", new String[] {contaID, catID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            datas.add(cursor.getString(0));
        }
        cursor.close();
        return datas;

    }

    public Cursor getTotalTransferencias(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoTransferencias WHERE conta_ID=? AND dataTransf>=? AND dataTransf <=?", new String[] {contaID,dataInicio,dataFim});
        return cursor;
    }

    public Cursor getTotalTransferenciasCat(String contaID, String dataInicio, String dataFim, String contaDestinoID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoTransferencias WHERE conta_ID=? AND contaDestino_ID=? AND dataTransf>=? AND dataTransf<=?", new String[] {contaID, contaDestinoID, dataInicio, dataFim});
        return cursor;
    }

    public ArrayList<String> queryTransferencias(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> transferencias = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoTransferencias WHERE conta_ID=? AND dataTransf>=? AND dataTransf<=? GROUP BY contaDestino_ID ORDER BY dataTransf", new String[] {contaID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            transferencias.add(cursor.getString(0));
        }
        cursor.close();
        return transferencias;
    }

    public ArrayList<String> queryContaDestinoID(String contaID, String data_Inicio, String data_Fim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> contasDestino = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT contaDestino_ID FROM registoTransferencias WHERE conta_ID=? AND dataTransf>=? AND dataTransf<=? GROUP BY contaDestino_ID ORDER BY dataTransf", new String[] {contaID, data_Inicio, data_Fim});
        while (cursor.moveToNext()){
            contasDestino.add(cursor.getString(0));
        }
        cursor.close();
        return contasDestino;
    }

    public Cursor getContaDestinoDesignacao(String contaDestino_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT designacao FROM conta WHERE ID=?", new String[] {contaDestino_ID});
        return cursor;
    }

    public ArrayList<String> queryTransferenciaContaDest(String contaID, String dataInicio, String dataFim, String contaDestino_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> transfContaDestino = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoTransferencias WHERE conta_ID=? AND dataTransf>=? AND dataTransf<=? AND contaDestino_ID=? GROUP BY dataTransf", new String[] {contaID, dataInicio, dataFim, contaDestino_ID});
        while (cursor.moveToNext()){
            transfContaDestino.add(cursor.getString(0));
        }
        cursor.close();
        return transfContaDestino;
    }

    public ArrayList<String> queryDatasTransfContaDestino(String contaID, String contaDestino_ID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> datas = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT dataTransf FROM registoTransferencias WHERE conta_ID=? AND contaDestino_ID=? AND dataTransf>=? AND dataTransf<=? GROUP BY dataTransf", new String[] {contaID, contaDestino_ID, dataInicio, dataFim});
        while (cursor.moveToNext()){
            datas.add(cursor.getString(0));
        }
        cursor.close();
        return datas;
    }

    public long addLimiteDespesas(String designacao, String limite, String dataInicio, String dataLimite, String catID, String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designacao", designacao);
        contentValues.put("limite", limite);
        contentValues.put("data_Inicio", dataInicio);
        contentValues.put("data_Fim", dataLimite);
        contentValues.put("cat_ID", catID);
        contentValues.put("contaID", contaID);
        long res = db.insert("excedenciaDespesas", null, contentValues);
        db.close();
        return res;
    }

    public boolean checkLimitesCriados(String contaID, String catID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM excedenciaDespesas WHERE contaID=? AND cat_ID=? AND data_Inicio=? AND data_Fim=?", new String[] {contaID, catID, dataInicio, dataFim});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return true;
        } else {
            return false;
        }
    }

    public Integer deleteLimiteDataFim(String dataAtual, String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("excedenciaDespesas", "data_Fim<=? AND contaID=?", new String[] {dataAtual,contaID});
        return res;
    }

    public boolean checkLimitesDesp(String contaID, String catID, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM excedenciaDespesas WHERE contaID=? AND cat_ID=? AND data_Inicio<=? AND data_Fim>=?", new String[] {contaID, catID, data, data});
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0){
            return  true;
        }else {
            return  false;
        }
    }

    public Cursor getLimitesDesp(String contaID, String catID, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT limite FROM excedenciaDespesas WHERE contaID=? AND cat_ID=? AND data_Inicio<=? AND data_Fim>=?", new String[] {contaID, catID, data, data});
        return cursor;
    }

    public Cursor checkCatLimiteDesp(String contaID, String data){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cat_ID FROM excedenciaDespesas WHERE contaID=? AND data_Inicio<=? AND data_Fim>=?", new String[] {contaID, data, data});
        return cursor;
    }

    public Cursor getLimiteID(String contaID, String catID, String limite, String data){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT exc_desp_id FROM excedenciaDespesas WHERE contaID=? AND cat_ID=? AND limite=? AND data_Inicio<=? AND data_Fim>=?", new String[] {contaID,catID,limite,data,data});
        return cursor;
    }

    public Cursor getDatasLimite(String limiteDespID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT data_Inicio, data_Fim FROM excedenciaDespesas WHERE exc_desp_id=?", new String[] {limiteDespID});
        return cursor;
    }

    public Cursor getTotalDespesasLimite(String contaID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND data>=? AND data<=?", new String[] {contaID, dataInicio, dataFim});
        return cursor;
    }

    public Cursor getTotalDespesasLimiteCat(String contaID, String catID, String dataInicio, String dataFim){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM registoDespesa WHERE conta_ID=? AND cat_ID=? AND data>=? AND data<=?", new String[] {contaID, catID, dataInicio, dataFim});
        return cursor;
    }

    public Integer deleteContaFin(String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("conta", "ID=?", new String[] {contaID});
        return res;
    }

    public Integer deleteDespesaConta(String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoDespesa", "conta_ID=?", new String[] {contaID});
        return res;
    }

    public Integer deleteRendimentoConta(String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoRendimento", "conta_ID=?", new String[] {contaID});
        return res;
    }

    public Integer deleteExcDespesaConta(String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("excedenciaDespesas", "contaID=?", new String[] {contaID});
        return res;
    }

    public Integer deleteTransferenciaConta(String contaID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoTransferencias", "conta_ID=?", new String[] {contaID});
        return res;
    }

    public Integer deleteLimiteDespesas(String limiteID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("excedenciaDespesas", "exc_desp_id=?", new String[] {limiteID});
        return res;
    }

    public Integer deleteDespesa(String regDespID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoDespesa", "regDesp_ID=?", new String[] {regDespID});
        return res;
    }

    public Integer deleteRendimento(String regRendID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoRendimento", "regRend_ID=?", new String[] {regRendID});
        return res;
    }

    public Integer deleteTransferencia(String regTransfID){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("registoTransferencias", "regTransf_id=?", new String[] {regTransfID});
        return res;
    }









}
