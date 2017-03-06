package com.forcavenda.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forcavenda.Adapters.ListaProdutoRecyclerAdapter;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;

/**
 * Created by Leo on 05/03/2017.
 */

public class ProdutoViewHolder extends RecyclerView.ViewHolder {
    public final TextView txtNome;
    public final TextView txtDescricao;
    public final TextView txtPreco;


    public ProdutoViewHolder(View itemView) {
        super(itemView);
        txtNome = (TextView) itemView.findViewById(R.id.txt_1);
        txtDescricao = (TextView) itemView.findViewById(R.id.txt_2);
        txtPreco = (TextView) itemView.findViewById(R.id.txt_3);
    }

    public void bind(final Produto produto, final ListaProdutoRecyclerAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(produto);
            }
        });
    }
}
