package com.forcavenda.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.R;
import com.forcavenda.ViewHolders.ClienteViewHolder;
import com.forcavenda.ViewHolders.PedidoViewHolder;

import java.util.List;

/**
 * Created by Leo on 05/03/2017.
 */

public class ListaClienteRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Cliente> clientes;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cliente cliente);
    }

    public ListaClienteRecyclerAdapter(Context context, List<Cliente> clientes, OnItemClickListener listener) {
        this.clientes = clientes;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_row_cliente, parent, false);
        ClienteViewHolder holder = new ClienteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ClienteViewHolder holder = (ClienteViewHolder) viewHolder;
        holder.bind(clientes.get(position), listener);
        Cliente cliente = clientes.get(position);
        holder.txtNome.setText(cliente.getNome().toString());
        holder.txtEmail.setText(cliente.getEmail());
        holder.txtTelefone.setText(  cliente.getTelefone());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}
