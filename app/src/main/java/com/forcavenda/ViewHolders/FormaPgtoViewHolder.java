package com.forcavenda.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forcavenda.Adapters.ListaFormaPgtoRecyclerAdapter;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;

/**
 * Created by Leo on 06/03/2017.
 */

public class FormaPgtoViewHolder extends RecyclerView.ViewHolder {

    public final TextView txtNome;
    public final TextView txtAtivo;

    public FormaPgtoViewHolder(View itemView) {
        super(itemView);
        txtNome = (TextView) itemView.findViewById(R.id.txt_1);
        txtAtivo = (TextView) itemView.findViewById(R.id.txt_2);
    }

    public void bind(final FormaPgto formaPgto, final ListaFormaPgtoRecyclerAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(formaPgto);
            }
        });
    }

}
