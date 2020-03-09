package br.com.cozinheirodelivery.domotica_new.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import java.util.List;

import br.com.cozinheirodelivery.domotica_new.Database.AsyncRequest;
import br.com.cozinheirodelivery.domotica_new.Objects.JanelaController;
import br.com.cozinheirodelivery.domotica_new.Objects.SalaController;
import br.com.cozinheirodelivery.domotica_new.Objects.Tomada;
import br.com.cozinheirodelivery.domotica_new.Objects.TomadaController;
import br.com.cozinheirodelivery.domotica_new.Objects.TomadasAdapter;
import br.com.cozinheirodelivery.domotica_new.R;


public class SalaFragment extends Fragment {

    TextView cPessoasComodo,cLuminosidadePosAtual,cJanelaPosAtual;
    EditText cJanelaSetpoint,cLuminosidadeSetpoint;
    AppCompatSeekBar iJanelaSetpoint,iLuminosidadeSetpoint;
    CheckBox chkJanelaAvancado,chkLuminosidadeControle;
    Button btnJanelaConfigurar;
    SwitchCompat bLuminosidadeManualOn;
    ExpandableHeightListView tomadasListView;
    LinearLayout luminosidadeManual;
    List<Tomada> mTomadaList;
    boolean setData = true;
    int iIDUsuario;
    //JanelaController janelaController;
    SalaController oSalaController;
    TomadasAdapter tomadasAdapter;

    public SalaFragment() {
        // Required empty public constructor
    }

    public static SalaFragment newInstance(int iIDUsuario) {
        SalaFragment fragment = new SalaFragment();
        Bundle args = new Bundle();
        args.putInt("iIDUsuario",iIDUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            iIDUsuario = getArguments().getInt("iIDUsuario");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_sala, container, false);
        init(v);
            oSalaController = new SalaController(getActivity(), iIDUsuario, new SalaController.onSalaBDResponse() {
                @Override
                public void onCheckResponse() {
                    if(setData){
                        setJanelaData();
                        setTomadaData();
                        setSalaData();
                        setLuminosidadeData();
                        //dialog.dismiss();
                        setData = false;
                    }else {
                        //updateJanelaData();
                        //updateTomadaData();
                        //updateLuminosidadeData();
                        //setSalaData();
                        setJanelaData();
                        setTomadaData();
                        setSalaData();
                        setLuminosidadeData();
                    }
                }

                @Override
                public void onLuminosidadeJobResponse(boolean isSuccess) {
                        if(!isSuccess){
                            iLuminosidadeSetpoint.setProgress(oSalaController.getoLuminosidade().getiSensor());
                        }
                }

                @Override
                public void onJanelaJobResponse(boolean isSuccess) {
                    if(!isSuccess){ //Solução para retornar ao setpoint inicial ao haver um erro no envio do job.
                        iJanelaSetpoint.setProgress(oSalaController.getoJanela().getiPosition());
                    }
                }

                @Override
                public void onTomadaJobResponse(TomadaController t,boolean isSuccess) {
                    if(!isSuccess){
                        t.setbStatus((t.getbStatus()^1));
                        tomadasAdapter.notifyDataSetChanged();
                    }
                }
            });
        initJanelaComponents();
        initLuminosidadeComponents();

        //mAsync.cancel(true); //MODIFICARR ELE TA TRAVANDO AQUI POR ISSO... TINHA QUE VERIFICAR SE TA RODANDO.
        oSalaController.checkBD(true);
        return v;
    }

    private void updateJanelaData(){
        cJanelaPosAtual.setText(oSalaController.getoJanela().getiPosition()+"%");
    }
    private void setJanelaData(){
        updateJanelaData();
        iJanelaSetpoint.setProgress(oSalaController.getoJanela().getiSetpoint());
        cJanelaSetpoint.setText(oSalaController.getoJanela().getiSetpoint()+"%");
        if(oSalaController.getoJanela().getbAvancado() == 1){
            chkJanelaAvancado.setChecked(true);
        }else{
            chkJanelaAvancado.setChecked(false);
        }
    }
    private void updateLuminosidadeData(){
        cLuminosidadePosAtual.setText(oSalaController.getoLuminosidade().getiSensor()+"%");
    }
    private void setLuminosidadeData(){
        updateLuminosidadeData();
        iLuminosidadeSetpoint.setProgress(oSalaController.getoLuminosidade().getiSetpoint());
        cLuminosidadeSetpoint.setText(oSalaController.getoLuminosidade().getiSetpoint()+"%");
        if(oSalaController.getoLuminosidade().getbControle() == 1){
            chkLuminosidadeControle.setChecked(true);
        }else{
            chkLuminosidadeControle.setChecked(false);
        }
        if(oSalaController.getoLuminosidade().getbManualOn() == 1){
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
    private void init(View v){
        cPessoasComodo = (TextView) v.findViewById(R.id.sala_pessoas_comodo);
        cLuminosidadePosAtual = (TextView) v.findViewById(R.id.luminosidade_posAtual);
        cJanelaPosAtual = (TextView) v.findViewById(R.id.janela_posAtual);
        cJanelaSetpoint = (EditText) v.findViewById(R.id.janela_setpoint_text);
        cLuminosidadeSetpoint = (EditText) v.findViewById(R.id.luminosidade_setpoint_text);
        iJanelaSetpoint = (AppCompatSeekBar) v.findViewById(R.id.janela_setpoint);
        iLuminosidadeSetpoint = (AppCompatSeekBar) v.findViewById(R.id.luminosidade_setpoint);
        chkJanelaAvancado = (CheckBox) v.findViewById(R.id.janela_avancado_check);
        chkLuminosidadeControle = (CheckBox) v.findViewById(R.id.luminosidade_manual_check);
        btnJanelaConfigurar = (Button) v.findViewById(R.id.janela_avancado_button);
        bLuminosidadeManualOn = (SwitchCompat) v.findViewById(R.id.luminosidade_manual_switch);
        tomadasListView = (ExpandableHeightListView) v.findViewById(R.id.lista);
        luminosidadeManual = (LinearLayout) v.findViewById(R.id.luminosidade_manual);

    }
    private void initJanelaComponents(){
        iJanelaSetpoint.incrementProgressBy(10);
        cJanelaSetpoint.setEnabled(false);
        iJanelaSetpoint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress / 10;
                progress = progress * 10;
                cJanelaSetpoint.setText(progress+"%");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "Parou em "+seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                oSalaController.getoJanela().setiSetpoint(seekBar.getProgress());
                oSalaController.getoJanela().sendJob(seekBar.getProgress());
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
                oSalaController.getoLuminosidade().setiSetpoint(seekBar.getProgress());
                oSalaController.getoLuminosidade().sendJob();
            }
        });
        chkLuminosidadeControle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkLuminosidadeControle.isChecked()){
                    oSalaController.getoLuminosidade().setbControle(1);
                    luminosidadeManual.setVisibility(View.VISIBLE);
                    iLuminosidadeSetpoint.setEnabled(false);

                }else{
                    oSalaController.getoLuminosidade().setbControle(0);
                    luminosidadeManual.setVisibility(View.GONE);
                    iLuminosidadeSetpoint.setEnabled(true);
                }
                oSalaController.getoLuminosidade().sendJob();
            }
        });
        bLuminosidadeManualOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bLuminosidadeManualOn.isChecked()){
                    oSalaController.getoLuminosidade().setbManualOn(1);
                }else{
                    oSalaController.getoLuminosidade().setbManualOn(0);
                }
                oSalaController.getoLuminosidade().sendJob();
            }
        });
    }
    private void setTomadaData(){
        tomadasAdapter = new TomadasAdapter(getActivity(), oSalaController.getTomadaList(), new TomadasAdapter.TomadasListener() {
            @Override
            public void onTomadasStateChange(TomadaController oTomada) {
                oTomada.sendJob();
            }
        });
        tomadasListView.setAdapter(tomadasAdapter);
        tomadasListView.setExpanded(true);
    }
    private void updateTomadaData(){
        if(tomadasAdapter!=null){
            tomadasAdapter.notifyDataSetChanged();
        }else{
            setTomadaData();
        }
    }
    private void setSalaData(){
        cPessoasComodo.setText(oSalaController.getiUserCont()+"");
    }
    @Override
    public String toString(){
        return "Controle Sala";
    }

    @Override
    public void onPause() {
        super.onPause();
        oSalaController.cancelRunningTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(oSalaController != null) {
            if(!oSalaController.isTaskRunning()) {
                oSalaController.checkBD(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oSalaController.cancelRunningTasks();
    }
}
