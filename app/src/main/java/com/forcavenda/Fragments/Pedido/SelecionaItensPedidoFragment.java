package com.forcavenda.Fragments.Pedido;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.forcavenda.Adapters.ListItensVendaAdapter;
import com.forcavenda.Adapters.SpinnerClienteAdapter;
import com.forcavenda.Entidades.Cliente;
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
import java.util.Map;

/**
 * Created by Leo on 26/02/2017.
 */

public class SelecionaItensPedidoFragment extends Fragment {
    ListItensVendaAdapter adapterItens;
    List<Cliente> listaclientes = new ArrayList<Cliente>();
    List<ItemPedido> listaItens = new ArrayList<ItemPedido>();
    Cliente cliente;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itens_pedido, container, false);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ListView listView = (ListView) view.findViewById(R.id.lista);
        TextView lblItens = (TextView) view.findViewById(R.id.lbl_Itens);
        TextView lblCliente = (TextView) view.findViewById(R.id.lbl_cliente);
        final Spinner cboCliente = (Spinner) view.findViewById(R.id.cboCliente);
        final SpinnerClienteAdapter adapterCliente = new SpinnerClienteAdapter(getActivity().getApplicationContext(), listaclientes);
        cboCliente.setAdapter(adapterCliente);

        if (cliente != null && cliente.getAdmin()) {
            DatabaseReference tabCliente = banco.getReference("cliente");
            Query resultadoCliente = tabCliente.orderByChild("isAdmin").equalTo(false);
            resultadoCliente.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            resultadoCliente.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Map<String, Object> cli = (Map<String, Object>) snapshot.getValue();
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    cliente.setAdmin((Boolean) cli.get("isAdmin"));
                    listaclientes.add(cliente);
                    adapterCliente.notifyDataSetChanged();
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

        } else {
            if (cliente != null) {
                listaclientes.add(cliente);
                adapterCliente.notifyDataSetChanged();
                lblCliente.setVisibility(View.GONE);
                lblItens.setVisibility(View.GONE);
                cboCliente.setVisibility(View.GONE);
            }
        }

        cboCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cliente clienteSelecionado = (Cliente) cboCliente.getSelectedItem();
                setCliente(clienteSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatabaseReference tabProdutos = banco.getReference("produto");
        Query resultado = tabProdutos.orderByChild("ativo").equalTo(true);

        adapterItens = new ListItensVendaAdapter(getActivity(),
                R.layout.lista_itens_venda, listaItens);
        listView.setAdapter(adapterItens);

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
                listaItens.add(new ItemPedido(produto, (long) 1));
                adapterItens.notifyDataSetChanged();
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
        return adapterItens.getSelectedItens();
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getClienteSelecionado() {
        return cliente;
    }
}
