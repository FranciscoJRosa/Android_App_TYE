package com.pdi.tye;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterDespesas extends RecyclerView.Adapter<AdapterDespesas.viewHolder> {

    ArrayList<ModelDespesas> modelDespesasArrayList;

    public AdapterDespesas(ArrayList<ModelDespesas> modelDespesasArrayList) {
        this.modelDespesasArrayList = modelDespesasArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View despesasRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.despesas_row, parent, false);
        return new viewHolder(despesasRow);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelDespesas modelDespesas = modelDespesasArrayList.get(position);

        String regDespID = modelDespesas.getRegDespID();
        String contaID = modelDespesas.getContaID();
        String catID = modelDespesas.getCatID();
        String valor = modelDespesas.getValor();
        String data = modelDespesas.getData();
        String designacaoDespesa = modelDespesas.getDesignacaoDespesa();

        holder.designacaoDespesa.setText(designacaoDespesa);
        holder.valorDespesa.setText(valor);
        holder.dataDespesa.setText(data);
        holder.categoriaDespesa.setText(catID);

    }

    @Override
    public int getItemCount() {
        return modelDespesasArrayList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        TextView designacaoDespesa;
        TextView valorDespesa;
        TextView dataDespesa;
        TextView categoriaDespesa;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            designacaoDespesa = itemView.findViewById(R.id.txt_designacaoDespesa1);
            valorDespesa = itemView.findViewById(R.id.txt_valorDespesa1);
            dataDespesa = itemView.findViewById(R.id.txt_data_Despesa1);
            categoriaDespesa = itemView.findViewById(R.id.txt_categoriaDespesa);
        }
    }
}
