package com.pdi.tye;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRendimentos extends RecyclerView.Adapter<AdapterRendimentos.viewHolder> {

    ArrayList<ModelRendimentos> modelRendimentosArrayList;

    public AdapterRendimentos(ArrayList<ModelRendimentos> modelRendimentosArrayList) {
        this.modelRendimentosArrayList = modelRendimentosArrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rendimentosRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.despesas_row, parent, false);
        return new viewHolder(rendimentosRow);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelRendimentos modelRendimentos = modelRendimentosArrayList.get(position);

        String regRendID = modelRendimentos.getRegRendID();
        String contaID = modelRendimentos.getContaID();
        String catRendID = modelRendimentos.getCatRendID();
        String valorRend = modelRendimentos.getValorRend();
        String dataRend = modelRendimentos.getDataRend();
        String designacaoRendimento = modelRendimentos.getDesignacaoRend();

        holder.designacaoRendimento.setText(designacaoRendimento);
        holder.valorRendimento.setText(valorRend);
        holder.dataRendimento.setText(dataRend);
        holder.categoriaRendimento.setText(catRendID);
    }

    @Override
    public int getItemCount() {
        return modelRendimentosArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView designacaoRendimento;
        TextView valorRendimento;
        TextView dataRendimento;
        TextView categoriaRendimento;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            designacaoRendimento = itemView.findViewById(R.id.txt_designacaoDespesa1);
            valorRendimento = itemView.findViewById(R.id.txt_valorDespesa1);
            dataRendimento = itemView.findViewById(R.id.txt_data_Despesa1);
            categoriaRendimento = itemView.findViewById(R.id.txt_categoriaDespesa);
        }
    }
}
