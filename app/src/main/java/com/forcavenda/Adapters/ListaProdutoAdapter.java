package com.forcavenda.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Produto;

import java.util.List;

/**
 * Created by Leo on 12/02/2017.
 */

public class ListaProdutoAdapter extends ArrayAdapter<Produto> {

    public ListaProdutoAdapter(Context context, List<Produto> produtos) {
        super(context, android.R.layout.simple_list_item_1, produtos);
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
                    android.R.layout.simple_list_item_1,
                    null);
        TextView tvText1 = (TextView) convertView.findViewById(android.R.id.text1);
        tvText1.setText(getItem(position).getNome());
        return convertView;
    }
}
