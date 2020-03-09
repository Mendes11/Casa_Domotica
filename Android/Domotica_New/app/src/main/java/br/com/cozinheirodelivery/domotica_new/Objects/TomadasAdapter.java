package br.com.cozinheirodelivery.domotica_new.Objects;

import android.content.Context;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import br.com.cozinheirodelivery.domotica_new.R;

/**
 * Created by Mendes on 09/03/2017.
 */

public class TomadasAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<TomadaController> tomadaList;
    TomadasListener mListener;
    Context c;
    AsyncTaskCompat mCheckTask;
    public interface TomadasListener{
        void onTomadasStateChange(TomadaController oTomada);
    }

    public TomadasAdapter(Context c, List<TomadaController> tomadaList,TomadasListener mListener){
        this.c = c;
        inflater = LayoutInflater.from(c);
        this.tomadaList = tomadaList;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return tomadaList.size();
    }

    @Override
    public Object getItem(int i) {
        return tomadaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tomadaList.get(i).getiIDTomada();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tomadas_list, null);
            view.setTag(holder);
            holder.cNome = (TextView) view.findViewById(R.id.cNome);
            holder.bHabilitado = (SwitchCompat) view.findViewById(R.id.tomada_manual_switch);
            holder.bHabilitado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.bHabilitado.isChecked() && tomadaList.get(i).getbStatus() == 0){
                        //Houve mudança.
                        tomadaList.get(i).setbStatus(1);
                        mListener.onTomadasStateChange(tomadaList.get(i));
                    }else if((!holder.bHabilitado.isChecked()) && tomadaList.get(i).getbStatus() == 1){
                        tomadaList.get(i).setbStatus(0);
                        mListener.onTomadasStateChange(tomadaList.get(i));
                    }

                }
            });
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.cNome.setText(tomadaList.get(i).cNome);
        int status = tomadaList.get(i).getbStatus();
        if(status == 1){
            holder.bHabilitado.setChecked(true);
        }else{
            holder.bHabilitado.setChecked(false);
        }
        return view;
    }

    public class ViewHolder{
        TextView cNome;
        SwitchCompat bHabilitado;
    }
}
