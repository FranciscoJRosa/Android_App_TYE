package com.pdi.tye;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterLimites extends RecyclerView.Adapter<AdapterLimites.viewHolder> {

    ArrayList<ModelLimites> modelLimitesArrayList;

    public AdapterLimites(ArrayList<ModelLimites> modelLimitesArrayList) {
        this.modelLimitesArrayList = modelLimitesArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View limiteRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.limite_row,parent, false);
        return new viewHolder(limiteRow);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelLimites modelLimites = modelLimitesArrayList.get(position);

        String exc_desp_id = modelLimites.getLimiteID();
        String designacaoLimite = modelLimites.getDesignacaoLimite();
        String valorLimite = modelLimites.getValorLimite();
        String dataInicioLimite = modelLimites.getDataInicioLimite();
        String dataFimLimite = modelLimites.getDataFimLimite();
        String categoriaLimite = modelLimites.getCategoriaLimite();
        String contaIDLimite = modelLimites.getContaIDLimite();

        holder.designacaoLimite.setText(designacaoLimite);
        holder.valorLimite.setText(valorLimite);
        holder.dataInicioLimite.setText(dataInicioLimite);
        holder.dataFimLimite.setText(dataFimLimite);
        holder.categoriaLimite.setText(categoriaLimite);
    }

    @Override
    public int getItemCount() {
        return modelLimitesArrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView designacaoLimite;
        TextView valorLimite;
        TextView dataInicioLimite;
        TextView dataFimLimite;
        TextView categoriaLimite;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            designacaoLimite = itemView.findViewById(R.id.txt_designacaoLimite1);
            valorLimite = itemView.findViewById(R.id.txt_valorLimite1);
            dataInicioLimite = itemView.findViewById(R.id.txt_data_InicioLimite1);
            dataFimLimite = itemView.findViewById(R.id.txt_data_FimLimite1);
            categoriaLimite = itemView.findViewById(R.id.txt_categoriaLimite1);
        }
    }
}
