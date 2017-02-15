package com.forcavenda.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.R;

import java.util.List;

/**
 * Created by Leo on 12/02/2017.
 */

public class ListItensVendaAdapter extends ArrayAdapter<ItemPedido> {
    private List<ItemPedido> items;

    public List<ItemPedido> getItems() {
        return items;
    }

    public ListItensVendaAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListItensVendaAdapter(Context context, int resource, List<ItemPedido> items) {
        super(context, resource, items);
        this.items = items;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lista_itens_venda, null);
        }

        final ItemPedido itemPedido = getItem(position);

        if (itemPedido != null) {
            TextView txt_nome = (TextView) v.findViewById(R.id.txt_nome);
            final TextView txt_preco = (TextView) v.findViewById(R.id.txt_preco);
            final TextView txt_qtde = (TextView) v.findViewById(R.id.txt_qtde);
            ImageView btn_adiciona = (ImageView) v.findViewById(R.id.btn_adicionaQtde);
            btn_adiciona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qtdeAtual = Integer.valueOf(txt_qtde.getText().toString());
                    qtdeAtual += 1;
                    Double valor = itemPedido.getProduto().getPreco() * qtdeAtual;
                    txt_qtde.setText(String.valueOf(qtdeAtual));
                    txt_preco.setText(String.valueOf(valor));
                }
            });

            ImageView btn_diminui = (ImageView) v.findViewById(R.id.btn_diminuiQtde);
            btn_diminui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qtdeAtual = Integer.valueOf(txt_qtde.getText().toString());
                    if (qtdeAtual > 1) {
                        qtdeAtual -= 1;
                        Double valor = itemPedido.getProduto().getPreco() * qtdeAtual;
                        txt_qtde.setText(String.valueOf(qtdeAtual));
                        txt_preco.setText(String.valueOf(valor));
                    }
                }
            });

            ImageView btn_delete = (ImageView) v.findViewById(R.id.btn_remover);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.remove(position);
                    atualizalista();
                }
            });

            if (txt_nome != null) {
                txt_nome.setText(itemPedido.getProduto().getNome());
            }

            if (txt_preco != null) {
                txt_preco.setText(String.valueOf(itemPedido.getProduto().getPreco()));
            }

            if (txt_qtde != null) {
                txt_qtde.setText(String.valueOf(itemPedido.getQuantidade()));
            }
        }

        return v;
    }

    private void atualizalista() {
        this.notifyDataSetChanged();
    }
}
