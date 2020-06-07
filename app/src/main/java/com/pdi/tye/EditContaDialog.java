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

public class EditContaDialog extends AppCompatDialogFragment {
    private EditText txt_editDesignaçãoConta;
    private EditText txt_editSaldoConta;
    private EditContaDialogListener listener2;

    public static EditContaDialog newInstance(String contaID, String info, String saldoInfo){
        EditContaDialog dialog = new EditContaDialog();
        Bundle bundle = new Bundle();
        bundle.putString("contaInfo", info);
        bundle.putString("saldoInfo", saldoInfo);
        bundle.putString("contaID", contaID);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_conta_info, null);

        builder.setView(view)
                .setTitle("Editar Informação da Conta")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener2.applyTexts("Cancelar", null, null);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String designacaoConta = txt_editDesignaçãoConta.getText().toString();
                        String saldoConta = txt_editSaldoConta.getText().toString();
                        listener2.applyTexts(getArguments().getString("contaID"),designacaoConta,saldoConta);

                    }
                });
        txt_editDesignaçãoConta = view.findViewById(R.id.txt_editDesignacaoConta);
        txt_editSaldoConta = view.findViewById(R.id.txt_editSaldoConta);
        txt_editDesignaçãoConta.setText(getArguments().getString("contaInfo"));
        txt_editSaldoConta.setText(getArguments().getString("saldoInfo"));

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener2 = (EditContaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implemente DialogListener");
        }


    }

    public interface EditContaDialogListener{
        void applyTexts(String contaID, String designacaoConta, String saldoConta);
    }
}
