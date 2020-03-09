package br.com.cozinheirodelivery.domotica_new.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.cozinheirodelivery.domotica_new.Objects.LavanderiaController;
import br.com.cozinheirodelivery.domotica_new.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LavanderiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LavanderiaFragment extends Fragment {
    private static final String ARG_PARAM1 = "iIDUsuario";

    private int iIDUsuario;

    TextView iUsuariosComodo;
    SwitchCompat tetoSwitch;
    CheckBox chkAvancado;
    Button btnConfigurar;

    LavanderiaController oLavanderia;
    public LavanderiaFragment() {
        // Required empty public constructor
    }

    public static LavanderiaFragment newInstance(int param1) {
        LavanderiaFragment fragment = new LavanderiaFragment();
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
        View v = inflater.inflate(R.layout.fragment_lavanderia, container, false);
        initComponents(v);
        oLavanderia = new LavanderiaController(getActivity(), iIDUsuario, new LavanderiaController.onLavanderiaBDResponse() {
            @Override
            public void onCheckResponse() {
                setLavanderiaData();
            }

            @Override
            public void onTetoJobResponse(boolean isSuccess) {

            }
        });
        oLavanderia.checkBD(true);
        return v;
    }

    private void initComponents(View v){
        iUsuariosComodo = (TextView) v.findViewById(R.id.lavanderia_pessoas_comodo);
        tetoSwitch = (SwitchCompat) v.findViewById(R.id.teto_switch);
        chkAvancado = (CheckBox) v.findViewById(R.id.teto_avancado_check);
        btnConfigurar = (Button) v.findViewById(R.id.teto_avancado_button);
        tetoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tetoSwitch.isChecked()){
                    oLavanderia.getoTeto().setiSetpoint(1); //1 é o código de abrir
                }else{
                    oLavanderia.getoTeto().setiSetpoint(0);
                }
                oLavanderia.getoTeto().sendJob();
            }
        });
        chkAvancado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkAvancado.isChecked()){
                    oLavanderia.getoTeto().setbAvancado(1);
                }else{
                    oLavanderia.getoTeto().setbAvancado(0);
                }
                oLavanderia.getoTeto().sendJob();
            }
        });
    }

    private void setTetoData(){
        if(oLavanderia.getoTeto().getiSetpoint() == 1){ //Abrir
            tetoSwitch.setChecked(true);
        }else if(oLavanderia.getoTeto().getiSetpoint() == 0){//Fechar
            tetoSwitch.setChecked(false);
        }
        if(oLavanderia.getoTeto().getbAvancado() == 1){
            chkAvancado.setChecked(true);
        }else if (oLavanderia.getoTeto().getbAvancado() == 0){
            chkAvancado.setChecked(false);
        }
        //Fazer lógica para o deviceStatus.
    }
    private void setLavanderiaData(){
        iUsuariosComodo.setText(oLavanderia.getiUserCont()+"");
        setTetoData();
    }
    @Override
    public String toString(){
        return "Controle Lavanderia";
    }

    @Override
    public void onPause() {
        super.onPause();
        oLavanderia.cancelRunningTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(oLavanderia != null) {
            if(!oLavanderia.isTaskRunning()) {
                oLavanderia.checkBD(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        oLavanderia.cancelRunningTasks();
    }
}
