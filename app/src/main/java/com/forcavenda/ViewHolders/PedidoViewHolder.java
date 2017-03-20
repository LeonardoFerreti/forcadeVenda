package com.forcavenda.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forcavenda.Adapters.ListaPedidoRecyclerAdapter;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.R;

import static android.R.attr.name;

/**
 * Created by Leo on 05/03/2017.
 */

public class PedidoViewHolder extends RecyclerView.ViewHolder {

    public final TextView txtIdPedido;
    public final TextView txtNomeCiente;
    public final TextView txtStatus;
    public final TextView txtValor;

    public PedidoViewHolder(View itemView) {
        super(itemView);
        txtIdPedido = (TextView) itemView.findViewById(R.id.txt_1);
        txtNomeCiente = (TextView) itemView.findViewById(R.id.txt_2);
        txtStatus = (TextView) itemView.findViewById(R.id.txt_3);
        txtValor = (TextView) itemView.findViewById(R.id.txt_4);
    }

    public void bind(final Pedido pedido, final ListaPedidoRecyclerAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pedido);
            }
        });
    }


}
