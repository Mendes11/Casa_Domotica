package br.com.cozinheirodelivery.domotica_new.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import br.com.cozinheirodelivery.domotica_new.Dialogs.ChangeCloseTime;
import br.com.cozinheirodelivery.domotica_new.Objects.DeviceStatus;
import br.com.cozinheirodelivery.domotica_new.Objects.EntradaController;
import br.com.cozinheirodelivery.domotica_new.R;

public class EntradaFragment extends Fragment implements ChangeCloseTime.onClick {

    private static final String ARG_PARAM1 = "iIDUsuario";

    private int iIDUsuario;
    EntradaController oEntrada;
    public EntradaFragment() {
        // Required empty public constructor
    }

    public static EntradaFragment newInstance(int param1) {
        EntradaFragment fragment = new EntradaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            iIDUsuario = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_entrada, container, false);
        oEntrada = new EntradaController(getActivity(), iIDUsuario, new EntradaController.onEntradaBDResponse() {
            @Override
            public void onCheckResponse() {
                setPortaData();
                setPortaoData();
                if(oEntrada.getMigue() == 1){
                    migue.setChecked(true);
                }else{
                    migue.setChecked(false);
                }
            }

            @Override
            public void onPortaJobResponse(boolean isSuccess) {
                if(!isSuccess){

                }
            }

            @Override
            public void onPortaoJobResponse(boolean isSuccess) {
                if(!isSuccess){
                    oEntrada.getoPortao().setbOpen(oEntrada.getoPortao().getbOpen()^1);
                    setPortaoData();
                }
            }

            @Override
            public void onMigueJobresponse(boolean isSuccess) {
                if(!isSuccess){
                    migue.setChecked(!migue.isChecked());
                }
            }
        });
        initComponents(v);
        initPortaComponents();
        initPortaoComponents();
        oEntrada.checkBD(true);
        return v;
    }
    private void setPortaData(){
        porta_txtStatus.setText(DeviceStatus.getDeviceStatus(oEntrada.getoPorta().getiIDDeviceStatus()));
        porta_txtTimeClose.setText(String.format("%.0f segundos",oEntrada.getoPorta().getfTimeAutoClose()));
    }
    private void setPortaoData(){
        if(oEntrada.getoPortao().getbOpen() == 0){
            portao_btnAbrir.setText("Abrir");
        }else{
            portao_btnAbrir.setText("Fechar");
        }
        portao_txtStatus.setText(DeviceStatus.getDeviceStatus(oEntrada.getoPortao().getiIDDeviceStatus()));
        portao_txtTimeClose.setText(String.format("%.0f segundos",oEntrada.getoPortao().getfTimeAutoClose()));
    }

    TextView porta_txtStatus,porta_txtTimeClose,portao_txtStatus,portao_txtTimeClose;
    AppCompatButton porta_btnAbrir,portao_btnAbrir;
    Button porta_btnAlterar,portao_btnAlterar;
    SwitchCompat migue;
    private void initComponents(View v) {
        porta_txtStatus = (TextView) v.findViewById(R.id.porta_txtStatus);
        porta_txtTimeClose = (TextView) v.findViewById(R.id.porta_txtTempo);
        porta_btnAbrir = (AppCompatButton) v.findViewById(R.id.porta_btnAbrir);
        porta_btnAlterar = (Button) v.findViewById(R.id.porta_btnAlterar);
        portao_txtStatus = (TextView) v.findViewById(R.id.portao_txtStatus);
        portao_txtTimeClose = (TextView) v.findViewById(R.id.portao_txtTempo);
        portao_btnAbrir = (AppCompatButton) v.findViewById(R.id.portao_btnAbrir);
        portao_btnAlterar = (Button) v.findViewById(R.id.portao_btnAlterar);
        migue = (SwitchCompat) v.findViewById(R.id.entrada_migue);
        migue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(migue.isChecked()){
                    migue.setChecked(false);
                    oEntrada.setMigue(0);
                }else{
                    migue.setChecked(true);
                    oEntrada.setMigue(1);
                }
                oEntrada.sendMigueJob();
            }
        });
    }
    private void initPortaComponents(){
        porta_btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oEntrada.getoPorta().getbOpen() != 1){
                    oEntrada.getoPorta().setbOpen(1);
                    oEntrada.getoPorta().sendJob();
                }
            }
        });
        porta_btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment d = ChangeCloseTime.newInstance("Porta");
                d.setTargetFragment(EntradaFragment.this,0);
                d.show(getFragmentManager(),"ChangeCloseTime");
            }
        });
    }
    private void initPortaoComponents(){
        portao_btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oEntrada.getoPortao().getbOpen() != 1){
                    oEntrada.getoPortao().setbOpen(1);
                    oEntrada.getoPortao().sendJob();
                    portao_btnAbrir.setText("Fechar");
                }else{
                    oEntrada.getoPorta().setbOpen(0);
                    portao_btnAbrir.setText("Abrir");
                    oEntrada.getoPortao().sendJob();
                }
            }
        });
        portao_btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment d = ChangeCloseTime.newInstance("Portao");
                d.setTargetFragment(EntradaFragment.this,0);
                d.show(getFragmentManager(),"ChangeCloseTime");
            }
        });
    }
    @Override
    public String toString(){
        return "Porta/Garagem";
    }

    @Override
    public void onPause() {
        super.onPause();
        oEntrada.cancelRunningTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(oEntrada != null) {
            if(!oEntrada.isTaskRunning()) {
                oEntrada.checkBD(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oEntrada.cancelRunningTasks();
    }

    @Override
    public void onConfirmClick(Double fTime, String caller) {
        if(caller.equals("Porta")){
            oEntrada.getoPorta().setfTimeAutoClose(fTime);
            oEntrada.getoPorta().sendJob();
            setPortaData();
        }else{
            oEntrada.getoPortao().setfTimeAutoClose(fTime);
            oEntrada.getoPortao().sendJob();
            setPortaoData();
        }
    }
}
