package com.pdi.tye;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTransferencias extends RecyclerView.Adapter<AdapterTransferencias.viewHolder> {
    ArrayList<ModelTransferencias> modelTransferenciasArrayList;

    public AdapterTransferencias(ArrayList<ModelTransferencias> modelTransferenciasArrayList) {
        this.modelTransferenciasArrayList = modelTransferenciasArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View transferenciasRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.despesas_row, parent, false);
        return new viewHolder(transferenciasRow);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelTransferencias modelTransferencias = modelTransferenciasArrayList.get(position);

        String regTransfID = modelTransferencias.getRegTransfID();
        String contaID = modelTransferencias.getContaID();
        String contaDestinoID = modelTransferencias.getContaDestinoID();
        String valorTransf = modelTransferencias.getValorTransf();
        String dataTransf = modelTransferencias.getDataTransf();
        String designacaoTransf = modelTransferencias.getDesignacaoTransf();

        holder.designacaoTransf.setText(designacaoTransf);
        holder.valorTransf.setText(valorTransf);
        holder.dataTransf.setText(dataTransf);
        holder.contaDestino.setText(contaDestinoID);
    }

    @Override
    public int getItemCount() {
        return modelTransferenciasArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView designacaoTransf;
        TextView valorTransf;
        TextView dataTransf;
        TextView contaDestino;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            designacaoTransf = itemView.findViewById(R.id.txt_designacaoDespesa1);
            valorTransf = itemView.findViewById(R.id.txt_valorDespesa1);
            dataTransf = itemView.findViewById(R.id.txt_data_Despesa1);
            contaDestino = itemView.findViewById(R.id.txt_categoriaDespesa);
        }
    }
}
