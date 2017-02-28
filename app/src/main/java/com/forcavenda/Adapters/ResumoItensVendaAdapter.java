package com.forcavenda.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class ResumoItensVendaAdapter extends ArrayAdapter {

    private List<ItemPedido> itens;

    public ResumoItensVendaAdapter(Context context, List<ItemPedido> itens) {
        super(context, R.layout.lista_resumo_pedido, itens);
        this.itens = itens;
    }

    public double getValorTotal() {
        double valorTotal = 0;
        for (ItemPedido item : itens) {
            valorTotal += item.getQuantidade() * item.getProduto().getPreco();
        }
        return valorTotal;
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
            convertView = View.inflate(getContext(), R.layout.lista_resumo_pedido, null);

        final ItemPedido itemPedido = (ItemPedido) getItem(position);

        TextView txt1 = (TextView) convertView.findViewById(R.id.lblQtde);
        TextView txt2 = (TextView) convertView.findViewById(R.id.lbl_nomeProduto);
        TextView txt3 = (TextView) convertView.findViewById(R.id.lbl_valorTotal);

        txt1.setText(String.valueOf(itemPedido.getQuantidade()));
        txt2.setText(itemPedido.getProduto().getNome());

        double valorTotal = itemPedido.getQuantidade() * itemPedido.getProduto().getPreco();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        txt3.setText(formatter.format(valorTotal));

        return convertView;
    }
}
