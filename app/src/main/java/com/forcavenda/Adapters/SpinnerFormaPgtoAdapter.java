package com.forcavenda.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;

import java.util.List;

/**
 * Created by Leo on 11/02/2017.
 */

public class SpinnerFormaPgtoAdapter extends ArrayAdapter<FormaPgto> {

    public SpinnerFormaPgtoAdapter(Context context, List<FormaPgto> formaPgtos) {
        super(context, R.layout.layout_spinner, formaPgtos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return initView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    private View initView(int position, View convertView) {
        if (convertView == null)
            convertView = View.inflate(getContext(),
                    R.layout.layout_spinner,
                    null);
        TextView tvText1 = (TextView) convertView.findViewById(R.id.txt_1);
        tvText1.setText(getItem(position).getNome());

        return convertView;
    }

}
