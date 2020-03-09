package br.com.cozinheirodelivery.domotica_new.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import br.com.cozinheirodelivery.domotica_new.R;

/**
 * Created by Mendes on 11/03/2017.
 */

public class ChangeCloseTime extends DialogFragment {
    onClick mListener;
    public interface onClick{
        void onConfirmClick(Double fTime,String caller);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (onClick) getTargetFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static ChangeCloseTime newInstance(String caller){
        ChangeCloseTime frag = new ChangeCloseTime();
        Bundle args = new Bundle();
        args.putString("caller",caller);
        frag.setArguments(args);
        return frag;
    }
    String caller = "";
    EditText fTempo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(savedInstanceState != null){
            caller = savedInstanceState.getString("caller");
        }
        if(getArguments()!= null){
            caller = getArguments().getString("caller");
        }
        if(caller.equals("Porta")){
            builder.setTitle("Permanência Destravada");
        }else{
            builder.setTitle("Permanência Aberta");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View inflator = inflater.inflate(R.layout.dialog_close_time,null);
        builder.setView(inflator);

        builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        fTempo = (EditText)inflator.findViewById(R.id.close_time);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog!=null){
            Button positive = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean wantToClose = false;
                    if(!(fTempo.getText().toString().trim().equals(""))) {
                        try {
                            Double.parseDouble(fTempo.getText().toString());
                            wantToClose = true;
                        }catch (Exception e) {
                            Toast.makeText(getActivity(), "Atenção, dado em formato inválido.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Preencha o campo.", Toast.LENGTH_SHORT).show();
                    }
                    if(wantToClose) {
                        mListener.onConfirmClick(Double.parseDouble(fTempo.getText().toString()),caller);
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
