package com.forcavenda.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Entidades.Pedido;
import com.forcavenda.R;
import com.forcavenda.ViewHolders.PedidoViewHolder;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Leo on 05/03/2017.
 */
public class ListaPedidoRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Pedido> pedidos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pedido pedido);
    }

    public ListaPedidoRecyclerAdapter(Context context, List<Pedido> pedidos, OnItemClickListener listener) {
        this.pedidos = pedidos;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_row_pedido, parent, false);
        PedidoViewHolder holder = new PedidoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PedidoViewHolder holder = (PedidoViewHolder) viewHolder;
        holder.bind(pedidos.get(position), listener);
        Pedido pedido = pedidos.get(position);
        holder.txtIdPedido.setText("PEDIDO: "+ pedido.getIdPedido().toString());
        holder.txtNomeCiente.setText(pedido.getCliente().getNome().toUpperCase());
        holder.txtStatus.setText("STATUS: "+ pedido.getStatus().getDescricao().toUpperCase());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.txtValor.setText(formatter.format(pedido.getValorTotal()));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
}
