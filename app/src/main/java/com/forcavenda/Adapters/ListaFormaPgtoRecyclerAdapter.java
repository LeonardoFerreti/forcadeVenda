package com.forcavenda.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;
import com.forcavenda.ViewHolders.FormaPgtoViewHolder;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Leo on 06/03/2017.
 */

public class ListaFormaPgtoRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<FormaPgto> formaPgtos;
    private final ListaFormaPgtoRecyclerAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FormaPgto formaPgto);
    }

    public ListaFormaPgtoRecyclerAdapter(Context context, List<FormaPgto> formaPgtos, ListaFormaPgtoRecyclerAdapter.OnItemClickListener listener) {
        this.formaPgtos = formaPgtos;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_row_formapgto, parent, false);
        FormaPgtoViewHolder holder = new FormaPgtoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FormaPgtoViewHolder holder = (FormaPgtoViewHolder) viewHolder;
        holder.bind(formaPgtos.get(position), listener);
        FormaPgto formaPgto = formaPgtos.get(position);
        holder.txtNome.setText(formaPgto.getNome().toString());
        holder.txtAtivo.setText((formaPgto.getAtivo()) ? "Ativo":"Inativo");

    }

    @Override
    public int getItemCount() {
        return formaPgtos.size();
    }



}
