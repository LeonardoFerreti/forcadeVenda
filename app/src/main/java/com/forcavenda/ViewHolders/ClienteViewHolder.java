package com.forcavenda.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forcavenda.Adapters.ListaClienteRecyclerAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.R;

/**
 * Created by Leo on 05/03/2017.
 */

public class ClienteViewHolder extends RecyclerView.ViewHolder {

    public final TextView txtNome;
    public final TextView txtEmail;
    public final TextView txtTelefone;

    public ClienteViewHolder(View itemView) {
        super(itemView);
        txtNome = (TextView) itemView.findViewById(R.id.txt_1);
        txtEmail = (TextView) itemView.findViewById(R.id.txt_2);
        txtTelefone = (TextView) itemView.findViewById(R.id.txt_3);
    }

    public void bind(final Cliente cliente, final ListaClienteRecyclerAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(cliente);
            }
        });
    }

}
