package com.forcavenda.Fragments.Pedido;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListItensVendaAdapter;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class SelecionaItensPedidoFragment extends Fragment {
    ListItensVendaAdapter adapter;
    List<ItemPedido> listaItens = new ArrayList<ItemPedido>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itens_pedido, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ListView listView = (ListView) view.findViewById(R.id.lista);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabProdutos = banco.getReference("produto");

        Query resultado = tabProdutos.orderByChild("nome");

        adapter = new ListItensVendaAdapter(getActivity(),
                R.layout.lista_itens_venda, listaItens);
        listView.setAdapter(adapter);

        resultado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        resultado.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Produto produto = snapshot.getValue(Produto.class);
                listaItens.add(new ItemPedido(produto, 1));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return view;
    }

    public List<ItemPedido> getItensVenda() {
        return adapter.getSelectedItens();
    }
}
