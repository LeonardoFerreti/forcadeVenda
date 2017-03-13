package com.forcavenda.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.forcavenda.Enums.Status;
import com.forcavenda.R;

import java.util.List;

/**
 * Created by Leo on 12/03/2017.
 */

public class SpinnerStatusAdapter extends BaseAdapter implements SpinnerAdapter {
    private final Context context;
    private List<Status> lista;

    public SpinnerStatusAdapter(Context context, List<Status> status) {
        this.context = context;
        this.lista = status;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_button, 0);
        txt.setText(lista.get(position).getDescricao());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(lista.get(position).getDescricao());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}
