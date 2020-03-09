package br.com.cozinheirodelivery.domotica_new.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.cozinheirodelivery.domotica_new.Objects.PiscinaController;
import br.com.cozinheirodelivery.domotica_new.R;


public class PiscinaFragment extends Fragment {
    private static final String ARG_PARAM1 = "iIDUsuario";

    // TODO: Rename and change types of parameters
    private int iIDUsuario;


    public PiscinaFragment() {
        // Required empty public constructor
    }
    public static PiscinaFragment newInstance(int param1) {
        PiscinaFragment fragment = new PiscinaFragment();
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
    TextView cLuminosidadePosAtual;
    EditText cLuminosidadeSetpoint;
    AppCompatSeekBar iLuminosidadeSetpoint;
    CheckBox chkLuminosidadeControle;
    SwitchCompat tetoSwitch,bLuminosidadeManualOn;
    LinearLayout luminosidadeManual;
    CheckBox chkAvancado;
    Button btnConfigurar;
    PiscinaController oPiscina;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_piscina, container, false);
        oPiscina = new PiscinaController(getActivity(), iIDUsuario, new PiscinaController.onPiscinaBDResponse() {
            @Override
            public void onCheckResponse() {
                setPiscinaData();
            }

            @Override
            public void onTetoJobResponse(boolean isSuccess) {
                if(!isSuccess){
                    if(oPiscina.getoTeto().getiPos() ==1 ) {
                        tetoSwitch.setChecked(true);
                    }else{
                        tetoSwitch.setChecked(false);
                    }
                    oPiscina.getoTeto().setiSetpoint(oPiscina.getoTeto().getiPos());
                }
            }

            @Override
            public void onLuminosidadeJobResponse(boolean isSuccess) {
                if(!isSuccess){
                    iLuminosidadeSetpoint.setProgress(oPiscina.getoLuminosidade().getiSensor());
                }
            }
        });
        initComponents(v);
        oPiscina.checkBD(true);
        return v;
    }
    private void setTetoData(){
        if(oPiscina.getoTeto().getiSetpoint() == 1){ //Abrir
            tetoSwitch.setChecked(true);
        }else if(oPiscina.getoTeto().getiSetpoint() == 0){//Fechar
            tetoSwitch.setChecked(false);
        }
        if(oPiscina.getoTeto().getbAvancado() == 1){
            chkAvancado.setChecked(true);
        }else if (oPiscina.getoTeto().getbAvancado() == 0){
            chkAvancado.setChecked(false);
        }
        //Fazer lógica para o deviceStatus.
    }
    private void updateLuminosidadeData(){
        cLuminosidadePosAtual.setText(oPiscina.getoLuminosidade().getiSensor()+"%");
    }
    private void setLuminosidadeData(){
        updateLuminosidadeData();
        iLuminosidadeSetpoint.setProgress(oPiscina.getoLuminosidade().getiSetpoint());
        cLuminosidadeSetpoint.setText(oPiscina.getoLuminosidade().getiSetpoint()+"%");
        if(oPiscina.getoLuminosidade().getbControle() == 1){
            chkLuminosidadeControle.setChecked(true);
        }else{
            chkLuminosidadeControle.setChecked(false);
        }
        if(oPiscina.getoLuminosidade().getbManualOn() == 1){
            bLuminosidadeManualOn.setChecked(true);
        }else{
            bLuminosidadeManualOn.setChecked(false);
        }

        if(chkLuminosidadeControle.isChecked()){
            luminosidadeManual.setVisibility(View.VISIBLE);
            iLuminosidadeSetpoint.setEnabled(false);

        }else{
            luminosidadeManual.setVisibility(View.GONE);
            iLuminosidadeSetpoint.setEnabled(true);
        }
    }

    private void setPiscinaData(){
        setTetoData();
        setLuminosidadeData();
    }
    private void initComponents(View v){
        tetoSwitch = (SwitchCompat) v.findViewById(R.id.teto_switch);
        chkAvancado = (CheckBox) v.findViewById(R.id.teto_avancado_check);
        btnConfigurar = (Button) v.findViewById(R.id.teto_avancado_button);
        cLuminosidadePosAtual = (TextView) v.findViewById(R.id.luminosidade_posAtual);
        cLuminosidadeSetpoint = (EditText) v.findViewById(R.id.luminosidade_setpoint_text);
        iLuminosidadeSetpoint = (AppCompatSeekBar) v.findViewById(R.id.luminosidade_setpoint);
        chkLuminosidadeControle = (CheckBox) v.findViewById(R.id.luminosidade_manual_check);
        bLuminosidadeManualOn = (SwitchCompat) v.findViewById(R.id.luminosidade_manual_switch);
        luminosidadeManual = (LinearLayout) v.findViewById(R.id.luminosidade_manual);
        initTetoComponents();
        initLuminosidadeComponents();
    }
    private void initTetoComponents(){
        tetoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tetoSwitch.isChecked()){
                    oPiscina.getoTeto().setiSetpoint(1); //1 é o código de abrir
                }else{
                    oPiscina.getoTeto().setiSetpoint(0);
                }
                oPiscina.getoTeto().sendJob();
            }
        });
        chkAvancado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkAvancado.isChecked()){
                    oPiscina.getoTeto().setbAvancado(1);
                }else{
                    oPiscina.getoTeto().setbAvancado(0);
                }
                oPiscina.getoTeto().sendJob();
            }
        });
    }
    private void initLuminosidadeComponents(){
        iLuminosidadeSetpoint.incrementProgressBy(10);
        cLuminosidadeSetpoint.setEnabled(false);
        iLuminosidadeSetpoint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress / 10;
                progress = progress * 10;
                cLuminosidadeSetpoint.setText(progress+"%");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getActivity(), "Parou em "+seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                oPiscina.getoLuminosidade().setiSetpoint(seekBar.getProgress());
                oPiscina.getoLuminosidade().sendJob();
            }
        });
        chkLuminosidadeControle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkLuminosidadeControle.isChecked()){
                    oPiscina.getoLuminosidade().setbControle(1);
                    luminosidadeManual.setVisibility(View.VISIBLE);
                    iLuminosidadeSetpoint.setEnabled(false);

                }else{
                    oPiscina.getoLuminosidade().setbControle(0);
                    luminosidadeManual.setVisibility(View.GONE);
                    iLuminosidadeSetpoint.setEnabled(true);
                }
                oPiscina.getoLuminosidade().sendJob();
            }
        });
        bLuminosidadeManualOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bLuminosidadeManualOn.isChecked()){
                    oPiscina.getoLuminosidade().setbManualOn(1);
                }else{
                    oPiscina.getoLuminosidade().setbManualOn(0);
                }
                oPiscina.getoLuminosidade().sendJob();
            }
        });
    }
    @Override
    public String toString(){
        return "Controle Piscina";
    }

    @Override
    public void onPause() {
        super.onPause();
        oPiscina.cancelRunningTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(oPiscina != null) {
            if(!oPiscina.isTaskRunning()) {
                oPiscina.checkBD(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oPiscina.cancelRunningTasks();
    }
}
