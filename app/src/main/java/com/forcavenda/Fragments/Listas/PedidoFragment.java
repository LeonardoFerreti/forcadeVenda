package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Adapters.ListaPedidoAdapter;
import com.forcavenda.Adapters.ListaPedidoRecyclerAdapter;
import com.forcavenda.Entidades.Pedido;
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
 * Created by Leo on 03/03/2017.
 */
public class PedidoFragment extends Fragment {
    List<Pedido> listaPedidos = new ArrayList<Pedido>();

    public PedidoFragment() {
    }

    public static PedidoFragment newInstance() {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_lista, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        final ListaPedidoRecyclerAdapter adapter = new ListaPedidoRecyclerAdapter(getActivity().getApplicationContext(), listaPedidos, new ListaPedidoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Toast.makeText(getActivity(), pedido.getIdPedido().toString() + " " + pedido.getCliente().getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabClientes = banco.getReference("pedido");
        Query resultado = tabClientes.orderByChild("status");

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
                Pedido pedido = snapshot.getValue(Pedido.class);
                listaPedidos.add(pedido);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.VISIBLE);
                Pedido pedido = dataSnapshot.getValue(Pedido.class);
                for (Pedido pedidoLista : listaPedidos) {
                    if (pedidoLista.getChave().equals(pedido.getChave())) {
                        pedidoLista.setIdPedido(pedido.getIdPedido());
                        pedidoLista.setCliente(pedido.getCliente());
                        pedidoLista.setFormaPgto(pedido.getFormaPgto());
                        pedidoLista.setListaItens(pedido.getListaItens());
                        pedidoLista.setStatus(pedido.getStatus());
                        pedido.setValorPago(pedido.getValorPago());
                        pedido.setDesconto(pedido.getDesconto());
                        pedido.setValorPago(pedido.getValorPago());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
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
            // ....
        });

        return view;
    }
}
