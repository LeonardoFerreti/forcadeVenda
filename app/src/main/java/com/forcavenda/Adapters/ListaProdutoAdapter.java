package com.forcavenda.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Leo on 12/02/2017.
 */

public class ListaProdutoAdapter extends ArrayAdapter<Produto> {

    public ListaProdutoAdapter(Context context, List<Produto> produtos) {
        super(context, R.layout.layout_lista_produto, produtos);
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
                    R.layout.layout_lista_produto,
                    null);
        TextView txt1 = (TextView) convertView.findViewById(R.id.txt_1);
        txt1.setText(getItem(position).getNome());
        TextView txt2 = (TextView) convertView.findViewById(R.id.txt_2);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        txt2.setText(formatter.format(getItem(position).getPreco()));
        return convertView;
    }
}
