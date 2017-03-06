package com.forcavenda.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forcavenda.Entidades.Pedido;
import com.forcavenda.R;

import java.util.List;

/**
 * Created by Leo on 04/03/2017.
 */

public class ListaPedidoAdapter extends ArrayAdapter<Pedido> {

    public ListaPedidoAdapter(Context context, List<Pedido> pedidos) {
        super(context, R.layout.layout_lista_produto, pedidos);
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
                    R.layout.layout_lista_pedido,
                    null);
        TextView txt1 = (TextView) convertView.findViewById(R.id.txt_1);
        txt1.setText(getItem(position).getIdPedido().toString());
        TextView txt2 = (TextView) convertView.findViewById(R.id.txt_2);
        txt2.setText(getItem(position).getCliente().getNome());
        TextView txt3 = (TextView) convertView.findViewById(R.id.txt_3);
        txt3.setText(getItem(position).getStatus().toString());
        return convertView;
    }

}
