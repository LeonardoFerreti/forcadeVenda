package com.forcavenda.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 12/02/2017.
 */

public class ListItensVendaAdapter extends ArrayAdapter<ItemPedido> {
    private List<ItemPedido> items;
    private List<ItemPedido> itensSelecionados = new ArrayList<>();
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public List<ItemPedido> getSelectedItens() {
        return itensSelecionados;
    }

    public ListItensVendaAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListItensVendaAdapter(Context context, int resource, List<ItemPedido> items) {
        super(context, resource, items);
        this.items = items;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView,@NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lista_itens_venda, null);
        }

        final AppCompatCheckBox checkBox = (AppCompatCheckBox) v.findViewById(R.id.chk);
        final ItemPedido itemPedido = getItem(position);

        if (itemPedido != null) {
            TextView txt_nome = (TextView) v.findViewById(R.id.txt_nome);
            final TextView txt_descricao = (TextView) v.findViewById(R.id.txt_descricao);
            final TextView txt_preco = (TextView) v.findViewById(R.id.txt_preco);
            final EditText txt_qtde = (EditText) v.findViewById(R.id.txt_qtde);
            txt_qtde.setEnabled(false);

            final ImageView btn_adiciona = (ImageView) v.findViewById(R.id.btn_adicionaQtde);
            btn_adiciona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        int qtdeAtual = Integer.valueOf(txt_qtde.getText().toString());
                        qtdeAtual += 1;
                        itemPedido.setQuantidade((long) qtdeAtual);
                        Double valor = itemPedido.getProduto().getPreco() * qtdeAtual;
                        txt_qtde.setText(String.valueOf(qtdeAtual));
                        txt_preco.setText(formatter.format(valor));
                    }
                }
            });

            final ImageView btn_diminui = (ImageView) v.findViewById(R.id.btn_diminuiQtde);
            btn_diminui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        int qtdeAtual = Integer.valueOf(txt_qtde.getText().toString());
                        if (qtdeAtual > 1) {
                            qtdeAtual -= 1;
                            itemPedido.setQuantidade((long) qtdeAtual);
                            Double valor = itemPedido.getProduto().getPreco() * qtdeAtual;
                            txt_qtde.setText(String.valueOf(qtdeAtual));
                            txt_preco.setText(formatter.format(valor));
                        }
                    }
                }
            });


            final View finalView = v;

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        btn_adiciona.setVisibility(View.VISIBLE);
                        btn_diminui.setVisibility(View.VISIBLE);
                        txt_qtde.setVisibility(View.VISIBLE);
                        finalView.setBackgroundColor(Color.argb(100, 208, 215, 212));
                        if (!itensSelecionados.contains(itemPedido)) {
                            itensSelecionados.add(itemPedido);
                        }
                    } else {
                        btn_adiciona.setVisibility(View.GONE);
                        btn_diminui.setVisibility(View.GONE);
                        txt_qtde.setVisibility(View.GONE);
                        finalView.setBackgroundColor(Color.TRANSPARENT);
                        if (itensSelecionados.size() > 0 && itensSelecionados.contains(itemPedido)) {
                            itensSelecionados.remove(itemPedido);
                        }
                    }

                }
            });

            if (itensSelecionados.contains(itemPedido)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            if (txt_nome != null) {
                txt_nome.setText(itemPedido.getProduto().getNome().toUpperCase());
            }

            if (txt_descricao != null) {
                txt_descricao.setText(itemPedido.getProduto().getDescricao());
            }

            if (txt_preco != null) {
                txt_preco.setText(formatter.format(itemPedido.getProduto().getPreco()));
            }

            txt_qtde.setText(String.valueOf(itemPedido.getQuantidade()));
        }

        return v;
    }

}
