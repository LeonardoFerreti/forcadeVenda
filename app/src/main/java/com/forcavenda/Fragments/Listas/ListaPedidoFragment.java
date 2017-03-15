package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Adapters.ListaPedidoRecyclerAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.Fragments.Pedido.PedidoFragment;
import com.forcavenda.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 03/03/2017.
 */
public class ListaPedidoFragment extends Fragment {
    private static final String PARAM_CLIENTE = "cliente";
    List<Pedido> listaPedidos = new ArrayList<Pedido>();
    private Cliente clienteLogado;

    public ListaPedidoFragment() {
    }

    public static ListaPedidoFragment newInstance(Cliente clienteLogado) {
        ListaPedidoFragment fragment = new ListaPedidoFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_CLIENTE, clienteLogado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteLogado = (Cliente) getArguments().getSerializable(PARAM_CLIENTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_lista, container, false);
        final boolean Admin = clienteLogado.getAdmin();

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        final ListaPedidoRecyclerAdapter adapter = new ListaPedidoRecyclerAdapter(getActivity().getApplicationContext(), listaPedidos, new ListaPedidoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                PedidoFragment fragment = PedidoFragment.newInstance(pedido,clienteLogado);
                fragment.show(fm, "Alterar pedido");

            }
        });

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        Query resultado = banco.getReference("pedido").orderByChild("idPedido");

        resultado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                java.util.Collections.reverse(listaPedidos);
                adapter.notifyDataSetChanged();
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
                Map<String, Object> mapPedido = (Map<String, Object>) snapshot.getValue();

                List<ItemPedido> itens = new ArrayList<ItemPedido>();
                if (mapPedido.get("itens") != null) {
                    Map<String, Object> mapListItens = (Map<String, Object>) mapPedido.get("itens");
                    for (Object item : mapListItens.values()) {
                        Map<String, Object> mapItemList = (Map<String, Object>) item;
                        for (Object pedidoItem : mapItemList.values()) {
                            Map<String, Object> mapItem = (Map<String, Object>) pedidoItem;
                            ItemPedido itemPedido = new ItemPedido();
                            itemPedido.setQuantidade((Long) mapItem.get("quantidade"));
                            Map<String, Object> mapProduto = (Map<String, Object>) mapItem.get("produto");
                            Produto produto = new Produto(mapProduto.get("id").toString(), mapProduto.get("nome").toString(),
                                    Double.valueOf(mapProduto.get("preco").toString()), (Boolean) mapProduto.get("ativo"),
                                    mapProduto.get("descricao").toString());
                            itemPedido.setProduto(produto);
                            itens.add(itemPedido);
                        }
                    }
                    pedido.setListaItens(itens);
                }

                if ((Admin == true) || (Admin == false && pedido.getCliente().getId().equals(clienteLogado.getId())))
                    listaPedidos.add(pedido);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.VISIBLE);
                Pedido pedido = dataSnapshot.getValue(Pedido.class);
                Map<String, Object> mapPedido = (Map<String, Object>) dataSnapshot.getValue();

                List<ItemPedido> itens = new ArrayList<ItemPedido>();
                if (mapPedido.get("itens") != null) {
                    Map<String, Object> mapListItens = (Map<String, Object>) mapPedido.get("itens");
                    for (Object item : mapListItens.values()) {
                        Map<String, Object> mapItemList = (Map<String, Object>) item;
                        for (Object pedidoItem : mapItemList.values()) {
                            Map<String, Object> mapItem = (Map<String, Object>) pedidoItem;
                            ItemPedido itemPedido = new ItemPedido();
                            itemPedido.setQuantidade((Long) mapItem.get("quantidade"));
                            Map<String, Object> mapProduto = (Map<String, Object>) mapItem.get("produto");
                            Produto produto = new Produto(mapProduto.get("id").toString(), mapProduto.get("nome").toString(),
                                    Double.valueOf(mapProduto.get("preco").toString()), (Boolean) mapProduto.get("ativo"),
                                    mapProduto.get("descricao").toString());
                            itemPedido.setProduto(produto);
                            itens.add(itemPedido);
                        }
                    }
                    pedido.setListaItens(itens);
                }

                if ((Admin == true) || (Admin == false && pedido.getCliente().getId().equals(clienteLogado.getId())))
                    listaPedidos.add(pedido);
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
