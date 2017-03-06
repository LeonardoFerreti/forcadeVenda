package com.forcavenda.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.forcavenda.ViewHolders.ProdutoViewHolder;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Leo on 05/03/2017.
 */

public class ListaProdutoRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Produto> produtos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Produto produto);
    }

    public ListaProdutoRecyclerAdapter(Context context, List<Produto> produtos, OnItemClickListener listener) {
        this.produtos = produtos;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_row_produto, parent, false);
        ProdutoViewHolder holder = new ProdutoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ProdutoViewHolder holder = (ProdutoViewHolder) viewHolder;
        holder.bind(produtos.get(position), listener);
        Produto produto = produtos.get(position);
        holder.txtNome.setText(produto.getNome().toString());
        holder.txtDescricao.setText(produto.getDescricao());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.txtPreco.setText(formatter.format(produto.getPreco()));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

}
