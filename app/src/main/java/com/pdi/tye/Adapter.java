package com.pdi.tye;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;

    private OnItemClickedListener mListener;

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener){
        mListener = listener;
    }

    public Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.conta_row, parent, false);
        return new Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Model model = arrayList.get(position);
        String id = model.getId();
        String designacao = model.getDesignacao();
        String saldo = model.getSaldo();

        holder.designacao.setText(designacao);
        holder.saldo.setText(saldo+"€");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView designacao;
        TextView saldo;


        public Holder(@NonNull View itemView, final OnItemClickedListener listener) {
            super(itemView);

            designacao = itemView.findViewById(R.id.designacao);
            saldo = itemView.findViewById(R.id.saldo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
