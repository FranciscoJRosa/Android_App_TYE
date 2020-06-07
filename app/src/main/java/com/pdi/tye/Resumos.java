package com.pdi.tye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Resumos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_TEXT = "com.pdi.tye.EXTRA_TEXT";
    public static final String EXTRA_TEXT1 = "com.pdi.tye.EXTRA_TEXT1";

    public static final String TEXT_DESP = "com.pdi.tye.TEXT_DESP";
    public static final String TEXT_DESP_ID = "com.pdi.tye.TEXT_DESP";

    DataBaseHelper db;

    LineChart lineChart;
    PieChart pieChart;
    RecyclerView rv_despesas;
    ArrayList<ModelDespesas> modelDespesasArrayList;
    AdapterDespesas adapterDespesas;
    RecyclerView rv_rendimentos;
    ArrayList<ModelRendimentos> modelRendimentosArrayList;
    AdapterRendimentos adapterRendimentos;
    RecyclerView rv_Transferencias;
    ArrayList<ModelTransferencias> modelTransferenciasArrayList;
    AdapterTransferencias adapterTransferencias;

    private Spinner spn_resumos;
    private Spinner spn_categoria;
    private TextView txt_dataInicio;
    private TextView txt_dataFim;
    private Button btn_visualizar;
    private TextView txt_Total;
    private DatePickerDialog.OnDateSetListener dateSetListenerInicio;
    private DatePickerDialog.OnDateSetListener dateSetListenerFim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumos);

        getSupportActionBar().setElevation(0);

        db = new DataBaseHelper(this);

        Intent res_intent = getIntent();
        final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);

        spn_resumos = findViewById(R.id.spn_Resumo);
        spn_categoria = findViewById(R.id.spn_categoria);
        txt_dataInicio = findViewById(R.id.txt_dataInicio);
        txt_dataFim = findViewById(R.id.txt_dataFim);
        btn_visualizar = findViewById(R.id.btn_visualizar);
        txt_Total = findViewById(R.id.txt_Total);
        lineChart = (LineChart) findViewById(R.id.mp_lineChart);
        pieChart = (PieChart) findViewById(R.id.mp_pieChart);
        rv_despesas = findViewById(R.id.rv_despesas);
        modelDespesasArrayList = new ArrayList<>();
        rv_rendimentos = findViewById(R.id.rv_rendimentos);
        modelRendimentosArrayList = new ArrayList<>();
        rv_Transferencias = findViewById(R.id.rv_transferencias);
        modelTransferenciasArrayList = new ArrayList<>();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_resumos.setAdapter(adapter);
        spn_resumos.setOnItemSelectedListener(this);

        txt_dataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogInicio = new DatePickerDialog(Resumos.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListenerInicio,
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
                    txt_dataInicio.setText(date);
                } else if (dayOfMonth < 10){
                    String date = year + "/" + month + "/0" + dayOfMonth;
                    txt_dataInicio.setText(date);
                } else if (month <10){
                    String date = year + "/0" + month + "/" + dayOfMonth;
                    txt_dataInicio.setText(date);
                } else {
                    String date = year + "/" + month + "/" + dayOfMonth;
                    txt_dataInicio.setText(date);
                }
            }
        };

        txt_dataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogFim = new DatePickerDialog(Resumos.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListenerFim,
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
                    txt_dataFim.setText(date);
                } else if (dayOfMonth < 10){
                    String date = year + "/" + month + "/0" + dayOfMonth;
                    txt_dataFim.setText(date);
                } else if (month <10){
                    String date = year + "/0" + month + "/" + dayOfMonth;
                    txt_dataFim.setText(date);
                } else {
                    String date = year + "/" + month + "/" + dayOfMonth;
                    txt_dataFim.setText(date);
                }
            }
        };

        btn_visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoResumo = spn_resumos.getSelectedItem().toString();
                String categoria = spn_categoria.getSelectedItem().toString();
                final String dataInicio = txt_dataInicio.getText().toString();
                final String dataFim = txt_dataFim.getText().toString();
                if (dataInicio.isEmpty() || dataFim.isEmpty()) {
                    Toast.makeText(Resumos.this, "Campos por preencher!", Toast.LENGTH_SHORT).show();
                } else{
                    if(tipoResumo.equals("Despesas")){
                        if(categoria.equals("Todas")){
                            final Cursor cursor = db.getTotalDespesas(contaID, dataInicio, dataFim);
                            while (cursor.moveToNext()){
                                String total = cursor.getString(0);
                                if (total==null){
                                    txt_Total.setText("Total de Despesas = 0€");
                                    Toast.makeText(Resumos.this, "Não existem despesas registadas!", Toast.LENGTH_SHORT).show();
                                } else {
                                    txt_Total.setText("Total de Despesas = "+total+"€");
                                }
                            }
                            pieChart.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            rv_despesas.setVisibility(View.VISIBLE);
                            rv_rendimentos.setVisibility(View.GONE);
                            rv_Transferencias.setVisibility(View.GONE);
                            final ArrayList<String> yData = db.queryDespesa(contaID, dataInicio, dataFim);
                            final ArrayList<String> xData = db.queryCatID(contaID, dataInicio, dataFim);
                            addDataToPieGraph(xData, yData, tipoResumo);
                            modelDespesasArrayList.clear();
                            showDespesas(contaID, dataInicio, dataFim);
                            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    try {
                                        PieEntry pe = (PieEntry) e;
                                        Toast.makeText(Resumos.this, "Valor= "+pe.getValue()+"€; Categoria: "+pe.getLabel(), Toast.LENGTH_LONG).show();
                                    } catch (Exception a) {
                                        Toast.makeText(Resumos.this, "Erro:"+a, Toast.LENGTH_LONG).show();
                                    }
                                }


                                @Override
                                public void onNothingSelected() {

                                }
                            });
                        } else{
                            Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
                            while (utilizadorID_cursor.moveToNext()){
                                String utilizadorID = utilizadorID_cursor.getString(0);
                                Cursor cursorCatID = db.getCatID(categoria, utilizadorID);
                                while (cursorCatID.moveToNext()){
                                    String catID = cursorCatID.getString(0);
                                    Cursor cursor = db.getTotalDespesasCat(contaID,catID,dataInicio,dataFim);
                                    while (cursor.moveToNext()){
                                        String total = cursor.getString(0);
                                        if (total==null){
                                            txt_Total.setText("Total de Despesas = 0€");
                                            Toast.makeText(Resumos.this, "Não existem despesas registadas!", Toast.LENGTH_SHORT).show();
                                            rv_despesas.setVisibility(View.GONE);

                                        } else {
                                            txt_Total.setText("Total de Despesas = " + total + "€");
                                        }
                                    }
                                    lineChart.setVisibility(View.VISIBLE);
                                    pieChart.setVisibility(View.GONE);
                                    rv_despesas.setVisibility(View.VISIBLE);
                                    rv_rendimentos.setVisibility(View.GONE);
                                    rv_Transferencias.setVisibility(View.GONE);
                                    final ArrayList<String> yData = db.queryDespesaCat(contaID, dataInicio, dataFim, catID);
                                    final ArrayList<String> xData = db.queryDatasDespCat(contaID,catID, dataInicio, dataFim);
                                    addDataToLineGraph(xData, yData);
                                    modelDespesasArrayList.clear();
                                    showDespesasCat(contaID, dataInicio, dataFim, catID);
                                    lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                        @Override
                                        public void onValueSelected(Entry e, Highlight h) {
                                            int x = lineChart.getData().getDataSetForEntry(e).getEntryIndex((Entry)e);
                                            String despesa = yData.get(x);
                                            String data = xData.get(x);
                                            Toast.makeText(Resumos.this, "Soma de Depesas= "+despesa+"€; data: "+data, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onNothingSelected() {

                                        }
                                    });

                                }
                            }

                        }
                    } else if (tipoResumo.equals("Rendimentos")){
                        if (categoria.equals("Todas")){
                            final Cursor cursor = db.getTotalRendimentos(contaID, dataInicio, dataFim);
                            while (cursor.moveToNext()){
                                String total = cursor.getString(0);
                                if (total==null){
                                    txt_Total.setText("Total de Rendimentos = 0€");
                                    Toast.makeText(Resumos.this, "Não existem rendimentos registados!", Toast.LENGTH_SHORT).show();
                                } else {
                                    txt_Total.setText("Total de Rendimentos = "+total+"€");
                                }
                            }
                            pieChart.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            rv_despesas.setVisibility(View.GONE);
                            rv_rendimentos.setVisibility(View.VISIBLE);
                            rv_Transferencias.setVisibility(View.GONE);
                            final ArrayList<String> yData = db.queryRendimentos(contaID, dataInicio,dataFim);
                            final ArrayList<String> xData = db.queryCatRendID(contaID, dataInicio, dataFim);
                            addDataToPieGraph(xData, yData, tipoResumo);
                            modelRendimentosArrayList.clear();
                            showRendimentos(contaID, dataInicio, dataFim);
                            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    PieEntry pe = (PieEntry) e;
                                    Toast.makeText(Resumos.this, "Valor= "+pe.getValue()+"€; Categoria: "+pe.getLabel(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNothingSelected() {

                                }
                            });
                        } else {
                            Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
                            while (utilizadorID_cursor.moveToNext()){
                                String utilizadorID = utilizadorID_cursor.getString(0);
                                Cursor cursorCatRendID = db.getCatRendID(categoria, utilizadorID);
                                while (cursorCatRendID.moveToNext()){
                                    String catRendID = cursorCatRendID.getString(0);
                                    Cursor cursor = db.getTotalRendimentosCat(contaID,catRendID,dataInicio,dataFim);
                                    while (cursor.moveToNext()){
                                        String total = cursor.getString(0);
                                        if (total==null){
                                            txt_Total.setText("Total de Rendimentos = 0€");
                                            Toast.makeText(Resumos.this, "Não existem rendimentos registados", Toast.LENGTH_SHORT).show();
                                            rv_rendimentos.setVisibility(View.GONE);
                                        } else {
                                            txt_Total.setText("Total de Rendimentos = " + total + "€");
                                        }
                                    }
                                    lineChart.setVisibility(View.VISIBLE);
                                    pieChart.setVisibility(View.GONE);
                                    rv_despesas.setVisibility(View.GONE);
                                    rv_rendimentos.setVisibility(View.VISIBLE);
                                    rv_Transferencias.setVisibility(View.GONE);
                                    final ArrayList<String> yData = db.queryRendimentosCat(contaID, dataInicio, dataFim, catRendID);
                                    final ArrayList<String> xData = db.queryDatasRendCat(contaID, catRendID, dataInicio, dataFim);
                                    addDataToLineGraph(xData, yData);
                                    modelRendimentosArrayList.clear();
                                    showRendimentosCat(contaID, dataInicio, dataFim, catRendID);
                                    lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                        @Override
                                        public void onValueSelected(Entry e, Highlight h) {
                                            int x = lineChart.getData().getDataSetForEntry(e).getEntryIndex((Entry)e);
                                            String rendimento = yData.get(x);
                                            String data = xData.get(x);
                                            Toast.makeText(Resumos.this, "Soma de Rendimentos= "+rendimento+"€; data: "+data, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onNothingSelected() {

                                        }
                                    });
                                }
                            }
                        }
                    } else if (tipoResumo.equals("Transferências")){
                        if (categoria.equals("Todas")){
                            final Cursor cursor = db.getTotalTransferencias(contaID, dataInicio, dataFim);
                            while (cursor.moveToNext()) {
                                String total = cursor.getString(0);
                                if (total==null){
                                    txt_Total.setText("Total de Transferências = 0€");
                                    Toast.makeText(Resumos.this, "Não existem transferências efetuadas!", Toast.LENGTH_SHORT).show();
                                } else {
                                    txt_Total.setText("Total de Transferências = " + total + "€");
                                }
                            }
                            pieChart.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            rv_despesas.setVisibility(View.GONE);
                            rv_rendimentos.setVisibility(View.GONE);
                            rv_Transferencias.setVisibility(View.VISIBLE);
                            final ArrayList<String> yData = db.queryTransferencias(contaID, dataInicio, dataFim);
                            final ArrayList<String> xData = db.queryContaDestinoID(contaID, dataInicio, dataFim);
                            addDataToPieGraph(xData, yData, tipoResumo);
                            modelTransferenciasArrayList.clear();
                            showTransferencias(contaID, dataInicio, dataFim);
                            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    PieEntry pe = (PieEntry) e;
                                    Toast.makeText(Resumos.this, "Valor= "+pe.getValue()+"€; Conta Destino: "+pe.getLabel(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNothingSelected() {

                                }
                            });
                        } else {
                            Cursor cursorUtilizadorID = db.getUtilizadorID(contaID);
                            while (cursorUtilizadorID.moveToNext()){
                                String utilizador_ID = cursorUtilizadorID.getString(0);
                                Cursor cursorContaDestinoID = db.getContaID(categoria, utilizador_ID);
                                while (cursorContaDestinoID.moveToNext()){
                                    String contaDestinoID = cursorContaDestinoID.getString(0);
                                    Cursor cursor = db.getTotalTransferenciasCat(contaID,dataInicio,dataFim,contaDestinoID);
                                    while (cursor.moveToNext()){
                                        String total = cursor.getString(0);
                                        if (total==null){
                                            txt_Total.setText("Total de Transferências = 0€");
                                            Toast.makeText(Resumos.this, "Não existem transferências efetuadas!", Toast.LENGTH_SHORT).show();
                                            rv_Transferencias.setVisibility(View.GONE);
                                        } else {
                                            txt_Total.setText("Total de Transferências = " + total + "€");
                                        }
                                    }
                                    lineChart.setVisibility(View.VISIBLE);
                                    pieChart.setVisibility(View.GONE);
                                    rv_despesas.setVisibility(View.GONE);
                                    rv_rendimentos.setVisibility(View.GONE);
                                    rv_Transferencias.setVisibility(View.VISIBLE);
                                    final ArrayList<String> yData = db.queryTransferenciaContaDest(contaID, dataInicio, dataFim, contaDestinoID);
                                    final ArrayList<String> xData = db.queryDatasTransfContaDestino(contaID, contaDestinoID, dataInicio, dataFim);
                                    addDataToLineGraph(xData, yData);
                                    modelTransferenciasArrayList.clear();
                                    showTransferenciasContaDestino(contaID, dataInicio, dataFim, contaDestinoID);
                                    lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                        @Override
                                        public void onValueSelected(Entry e, Highlight h) {
                                            int x = lineChart.getData().getDataSetForEntry(e).getEntryIndex((Entry)e);
                                            String transferencia = yData.get(x);
                                            String data = xData.get(x);
                                            Toast.makeText(Resumos.this, "Valor da Transferência= "+transferencia+"€; data: "+data, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onNothingSelected() {

                                        }
                                    });

                                }
                            }
                        }

                    }
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_despesas);

        ItemTouchHelper itemTouchHelperRend = new ItemTouchHelper(simpleCallbackRend);
        itemTouchHelperRend.attachToRecyclerView(rv_rendimentos);

        ItemTouchHelper itemTouchHelperTransf = new ItemTouchHelper(simpleCallbackTransf);
        itemTouchHelperTransf.attachToRecyclerView(rv_Transferencias);

    }

    private void showDespesas(String contaID, String dataInicio, String dataFim) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoDespesa WHERE conta_ID=? AND data>=? AND data<=? ORDER BY data", new String[] {contaID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor categoria_cursor = dataBaseHelper.getDesignacaoCat(cursor.getString(2));
                        while (categoria_cursor.moveToNext()){
                            modelDespesasArrayList.add(new ModelDespesas(cursor.getString(0),
                                    cursor.getString(1),
                                    "Categoria: "+categoria_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    "Designação: "+cursor.getString(5)));
                        }
                    }
                    adapterDespesas = new AdapterDespesas(modelDespesasArrayList);
                    rv_despesas.hasFixedSize();
                    rv_despesas.setLayoutManager(new LinearLayoutManager(this));
                    rv_despesas.setAdapter(adapterDespesas);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDespesasCat(String contaID, String dataInicio, String dataFim, String catID) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoDespesa WHERE conta_ID=? AND cat_ID=? AND data>=? AND data<=? ORDER BY data", new String[] {contaID, catID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor categoria_cursor = dataBaseHelper.getDesignacaoCat(cursor.getString(2));
                        while (categoria_cursor.moveToNext()){
                            modelDespesasArrayList.add(new ModelDespesas(cursor.getString(0),
                                    cursor.getString(1),
                                    "Categoria: "+categoria_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    "Designação: "+cursor.getString(5)));
                        }
                    }
                    adapterDespesas = new AdapterDespesas(modelDespesasArrayList);
                    rv_despesas.hasFixedSize();
                    rv_despesas.setLayoutManager(new LinearLayoutManager(this));
                    rv_despesas.setAdapter(adapterDespesas);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ModelDespesas despesaApagada = null;

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
                despesaApagada = modelDespesasArrayList.get(position);
                modelDespesasArrayList.remove(position);
                adapterDespesas.notifyItemRemoved(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Resumos.this);
                builder.setTitle("AVISO");
                builder.setMessage("Tem a certeza que pretende eliminar permanentemente a despesa com "+despesaApagada.getDesignacaoDespesa()+"?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor valorDespesa_cursor = db.getValorDespesa(despesaApagada.getRegDespID());
                        while (valorDespesa_cursor.moveToNext()){
                            String valorDespesa = valorDespesa_cursor.getString(0);
                            Cursor saldoConta_cursor = db.getSaldoContaID(despesaApagada.getContaID());
                            while (saldoConta_cursor.moveToNext()){
                                String saldoConta = saldoConta_cursor.getString(0);
                                int res = db.deleteDespesa(despesaApagada.getRegDespID());
                                Boolean atualizarSaldo = db.somaRend(despesaApagada.getContaID(), saldoConta, valorDespesa);
                                if (res > 0){
                                    Toast.makeText(Resumos.this, "Despesa eliminada com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelDespesasArrayList.add(position, despesaApagada);
                        adapterDespesas.notifyItemInserted(position);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showRendimentos(String contaID, String dataInicio, String dataFim) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoRendimento WHERE conta_ID=? AND data>=? AND data<=? ORDER BY data", new String[] {contaID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor categoriaRend_cursor = dataBaseHelper.getDesignacaoCatRend(cursor.getString(2));
                        while (categoriaRend_cursor.moveToNext()){
                            modelRendimentosArrayList.add(new ModelRendimentos(cursor.getString(0),
                                    cursor.getString(1),
                                    "Categoria: "+categoriaRend_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    "Designação: "+cursor.getString(5)));
                        }
                    }
                    adapterRendimentos = new AdapterRendimentos(modelRendimentosArrayList);
                    rv_rendimentos.hasFixedSize();
                    rv_rendimentos.setLayoutManager(new LinearLayoutManager(this));
                    rv_rendimentos.setAdapter(adapterRendimentos);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showRendimentosCat(String contaID, String dataInicio, String dataFim, String catID) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoRendimento WHERE conta_ID=? AND cat_rend_ID=? AND data>=? AND data<=? ORDER BY data", new String[] {contaID, catID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor categoria_cursor = dataBaseHelper.getDesignacaoCat(cursor.getString(2));
                        while (categoria_cursor.moveToNext()){
                            modelRendimentosArrayList.add(new ModelRendimentos(cursor.getString(0),
                                    cursor.getString(1),
                                    "Categoria: "+categoria_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    "Designação: "+cursor.getString(5)));
                        }
                    }
                    adapterRendimentos = new AdapterRendimentos(modelRendimentosArrayList);
                    rv_rendimentos.hasFixedSize();
                    rv_rendimentos.setLayoutManager(new LinearLayoutManager(this));
                    rv_rendimentos.setAdapter(adapterRendimentos);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ModelRendimentos rendimentoApagado = null;

    ItemTouchHelper.SimpleCallback simpleCallbackRend = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            db = new DataBaseHelper(getApplicationContext());
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT){
                rendimentoApagado = modelRendimentosArrayList.get(position);
                modelRendimentosArrayList.remove(position);
                adapterRendimentos.notifyItemRemoved(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Resumos.this);
                builder.setTitle("AVISO");
                builder.setMessage("Tem a certeza que pretende eliminar permanentemente o rendimento com "+rendimentoApagado.getDesignacaoRend()+"?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor valorRend_cursor = db.getvalorRend(rendimentoApagado.getRegRendID());
                        while (valorRend_cursor.moveToNext()){
                            String valorRend = valorRend_cursor.getString(0);
                            Cursor saldoConta_cursor = db.getSaldoContaID(rendimentoApagado.getContaID());
                            while (saldoConta_cursor.moveToNext()){
                                String saldoConta = saldoConta_cursor.getString(0);
                                int res = db.deleteRendimento(rendimentoApagado.getRegRendID());
                                Boolean atualizarSaldo = db.SubtDespesa(rendimentoApagado.getContaID(), saldoConta, valorRend);
                                if (res > 0){
                                    Toast.makeText(Resumos.this, "Rendimento eliminado com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelRendimentosArrayList.add(position, rendimentoApagado);
                        adapterRendimentos.notifyItemInserted(position);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showTransferencias(String contaID, String dataInicio, String dataFim) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoTransferencias WHERE conta_ID=? AND dataTransf>=? AND dataTransf<=? ORDER BY dataTransf", new String[] {contaID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor contaDestino_cursor = dataBaseHelper.getContaDestinoDesignacao(cursor.getString(2));
                        while (contaDestino_cursor.moveToNext()){
                            modelTransferenciasArrayList.add(new ModelTransferencias(cursor.getString(0),
                                    "Designação: "+cursor.getString(1),
                                    "Conta Destino: "+contaDestino_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    cursor.getString(5)));
                        }
                    }
                    adapterTransferencias = new AdapterTransferencias(modelTransferenciasArrayList);
                    rv_Transferencias.hasFixedSize();
                    rv_Transferencias.setLayoutManager(new LinearLayoutManager(this));
                    rv_Transferencias.setAdapter(adapterTransferencias);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showTransferenciasContaDestino(String contaID, String dataInicio, String dataFim, String contaDestinoID) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            if (sqLiteDatabase!=null){
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM registoTransferencias WHERE conta_ID=? AND contaDestino_ID=? AND dataTransf>=? AND dataTransf<=? ORDER BY dataTransf", new String[] {contaID, contaDestinoID, dataInicio, dataFim});
                if (cursor.getCount()==0) {
                    Toast.makeText(this, "Não existe informação", Toast.LENGTH_SHORT).show();
                }else {
                    while (cursor.moveToNext()) {
                        Cursor contaDestino_cursor = dataBaseHelper.getContaDestinoDesignacao(cursor.getString(2));
                        while (contaDestino_cursor.moveToNext()){
                            modelTransferenciasArrayList.add(new ModelTransferencias(cursor.getString(0),
                                    "Designação: "+cursor.getString(1),
                                    "Conta Destino: "+contaDestino_cursor.getString(0),
                                    "Valor= "+cursor.getString(3)+"€",
                                    "Data: "+cursor.getString(4),
                                    cursor.getString(5)));
                        }
                    }
                    adapterTransferencias = new AdapterTransferencias(modelTransferenciasArrayList);
                    rv_Transferencias.hasFixedSize();
                    rv_Transferencias.setLayoutManager(new LinearLayoutManager(this));
                    rv_Transferencias.setAdapter(adapterTransferencias);
                }
            } else {
                Toast.makeText(this, "Base Dados null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ShowValuesFromDB: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ModelTransferencias transferenciaApagada = null;

    ItemTouchHelper.SimpleCallback simpleCallbackTransf = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            db = new DataBaseHelper(getApplicationContext());
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT){
                transferenciaApagada = modelTransferenciasArrayList.get(position);
                modelTransferenciasArrayList.remove(position);
                adapterTransferencias.notifyItemRemoved(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Resumos.this);
                builder.setTitle("AVISO");
                builder.setMessage("Tem a certeza que pretende eliminar permanentemente a transferência com "+transferenciaApagada.getDesignacaoTransf()+"?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor valorTransf_cursor = db.getValorTransf(transferenciaApagada.getRegTransfID());
                        while (valorTransf_cursor.moveToNext()){
                            String valorTransf = valorTransf_cursor.getString(0);
                            Cursor saldoConta_cursor = db.getSaldoContaID(transferenciaApagada.getContaID());
                            while (saldoConta_cursor.moveToNext()){
                                String saldoConta = saldoConta_cursor.getString(0);
                                Cursor contaDestinoID_cursor = db.getContaDestinoID(transferenciaApagada.getRegTransfID());
                                while (contaDestinoID_cursor.moveToNext()){
                                    String contaDestinoID = contaDestinoID_cursor.getString(0);
                                    Cursor saldoContaDestino_cursor = db.getSaldoContaID(contaDestinoID);
                                    while (saldoContaDestino_cursor.moveToNext()){
                                        String saldoContaDestino = saldoContaDestino_cursor.getString(0);
                                        int res = db.deleteTransferencia(transferenciaApagada.getRegTransfID());
                                        Boolean atualizarSaldoContaOrigem = db.transfContaDestino(transferenciaApagada.getContaID(), valorTransf, saldoConta);
                                        Boolean atualizarSaldoContaDestino = db.transfContaOrigem(contaDestinoID, valorTransf, saldoContaDestino);
                                        if (res > 0){
                                            Toast.makeText(Resumos.this, "Tranferência eliminada com sucesso", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelTransferenciasArrayList.add(position, transferenciaApagada);
                        adapterTransferencias.notifyItemInserted(position);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Resumos.this, R.color.colorPrimaryDark))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String tipoResumo = parent.getItemAtPosition(position).toString();
        if(tipoResumo.equals("Despesas")){
            Intent res_intent = getIntent();
            final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);

            Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
            while (utilizadorID_cursor.moveToNext()){
                String utilizadorID = utilizadorID_cursor.getString(0);
                List<String> listaCatDesp = new ArrayList<>();
                listaCatDesp.add("Selecione a Categoria..");
                Cursor categoriasDesp = db.getDataCatDespesa(utilizadorID);

                if(categoriasDesp.getCount()==0){
                    Toast.makeText(Resumos.this, "Não existem categorias definidas", Toast.LENGTH_SHORT).show();
                }else {
                    while (categoriasDesp.moveToNext()){
                        listaCatDesp.add(categoriasDesp.getString(1));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, listaCatDesp){
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
                        spn_categoria.setAdapter(adapter);
                    }
                }
            }

        } else if(tipoResumo.equals("Rendimentos")){
            Intent res_intent = getIntent();
            final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);

            Cursor utilizadorID_cursor = db.getUtilizadorID(contaID);
            while (utilizadorID_cursor.moveToNext()){
                String utilizadorID = utilizadorID_cursor.getString(0);
                List<String> listaCatRend = new ArrayList<>();
                listaCatRend.add("Selecione a Categoria...");
                listaCatRend.add("Todas");
                Cursor categoriasRend = db.getDataCatRendimento(utilizadorID);

                if(categoriasRend.getCount()==0){
                    Toast.makeText(Resumos.this, "Não existem categorias definidas", Toast.LENGTH_SHORT).show();
                }else {
                    while (categoriasRend.moveToNext()){
                        listaCatRend.add(categoriasRend.getString(1));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, listaCatRend){
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
                        spn_categoria.setAdapter(adapter);
                    }
                }
            }

        } else if (tipoResumo.equals("Transferências")){
            Intent res_intent = getIntent();
            final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);
            List<String> listaContas = new ArrayList<>();
            listaContas.add("Selecione a Conta Destino...");
            listaContas.add("Todas");
            Cursor cursorUserID = db.getUtilizadorID(contaID);
            while (cursorUserID.moveToNext()){
                String utilizadorID = cursorUserID.getString(0);
                Cursor contas = db.getContasTransf(contaID, utilizadorID);
                if(contas.getCount()==0){
                    Toast.makeText(Resumos.this, "Não existem contas definidas", Toast.LENGTH_SHORT).show();
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
                        spn_categoria.setAdapter(adapter);
                    }
                }

            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addDataToLineGraph(ArrayList<String> xData, ArrayList<String> yData){
        db = new DataBaseHelper(this);
        final ArrayList<Entry> yVals = new ArrayList<Entry>();

        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.setScaleEnabled(true);
        lineChart.animateY(1000, Easing.EaseInOutCubic);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.size(); i++){
            xVals.add(xData.get(i));
        }

        for (int i = 0; i < yData.size(); i++){
            Entry newEntry = new Entry(i, Float.parseFloat(yData.get(i)));
            yVals.add(newEntry);
        }

        LineDataSet dataSet = new LineDataSet(yVals, null);

        dataSet.setColor(Color.GRAY);
        dataSet.setLineWidth(3);
        dataSet.setCircleRadius(5);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setValueTextSize(14);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        ArrayList<ILineDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(dataSet);
        LineData data = new LineData(dataSets1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setSpaceMin(0.2f);
        xAxis.setSpaceMax(0.2f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(75f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        lineChart.setData(data);
        lineChart.invalidate();

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + " €";
            }
        });
    }



    public void addDataToPieGraph(ArrayList<String> xData, ArrayList<String> yData, String tipoResumo){

        db = new DataBaseHelper(this);
        final ArrayList<PieEntry> yEntry = new ArrayList<PieEntry>();
        final ArrayList<PieEntry> xEntry = new ArrayList<>();
        final ArrayList<String> designacaoCat = new ArrayList<>();

        pieChart.getDescription().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000, Easing.EaseInOutCubic);


        for (int i = 0; i < xData.size(); i++){
            String catID = xData.get(i);
            if (tipoResumo.equals("Despesas")){
                Cursor cursor = db.getDesignacaoCat(catID);
                while(cursor.moveToNext()){
                    designacaoCat.add(cursor.getString(0));
                }
            } else if (tipoResumo.equals("Rendimentos")) {
                Cursor cursor = db.getDesignacaoCatRend(catID);
                while (cursor.moveToNext()) {
                    designacaoCat.add(cursor.getString(0));
                }
            } else if (tipoResumo.equals("Transferências")) {
                Cursor cursor = db.getContaDestinoDesignacao(catID);
                while (cursor.moveToNext()) {
                    designacaoCat.add(cursor.getString(0));
                }
            }
        }

        for (int i = 0; i < yData.size(); i++){
            yEntry.add(new PieEntry(Float.parseFloat(yData.get(i)), designacaoCat.get(i)));
        }

        for (int i = 0; i < xData.size(); i++){
            xEntry.add(new PieEntry(Float.parseFloat(xData.get(i))));
        }

        String[] rainbow = getApplicationContext().getResources().getStringArray(R.array.rainbow);
        List<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < rainbow.length; i++) {
            int newColor = Color.parseColor(rainbow[i]);
            colors.add(newColor);
        }

        PieDataSet piedataSet = new PieDataSet(yEntry, null);
        piedataSet.setValueTextSize(12);
        piedataSet.setSliceSpace(3);
        piedataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(14);
        legend.setWordWrapEnabled(true);


        PieData pieData = new PieData(piedataSet);
        pieData.setValueTextSize(20);
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setData(pieData);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setHighlightEnabled(true);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value +" €";
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent res_intent = getIntent();
        final String contaID = res_intent.getStringExtra(EXTRA_TEXT1);
        final String desp = "despesa";

        Intent intentResumos = new Intent(Resumos.this, MenuFuncionalidades.class);
        intentResumos.putExtra(TEXT_DESP, desp);
        intentResumos.putExtra(TEXT_DESP_ID, contaID);
        startActivity(intentResumos);
        finish();
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
                        Intent logout_intent = new Intent(Resumos.this, MainActivity.class);
                        startActivity(logout_intent);
                        finish();
                        return true;
                    case R.id.carteiraContasItem:
                        Intent carteira_intent = new Intent(Resumos.this, ContaFinanceira.class);
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
}
