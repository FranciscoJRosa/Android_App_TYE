package com.pdi.tye;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AdicionarCatDialog extends AppCompatDialogFragment {
    private EditText et_designacaoCategoria;
    private AdicionarCatDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.adicionar_categoria, null);

        builder.setView(view)
                .setTitle("Adicionar Categoria")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String designacaoCat = et_designacaoCategoria.getText().toString();
                        listener.applyText(designacaoCat);
                    }
                });
        et_designacaoCategoria = view.findViewById(R.id.et_designacaoCat);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AdicionarCatDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "Tem de implementar AdicionarCatDialogListener");
        }
    }

    public interface AdicionarCatDialogListener {
        void applyText(String designacaoCat);
    }
}
