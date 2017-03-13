package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaClienteRecyclerAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Fragments.Cadastros.CadastroClienteFragment;
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


public class ListaClienteFragment extends Fragment {

    List<Cliente> listaClientes = new ArrayList<Cliente>();

    public ListaClienteFragment() {
    }

    public static ListaClienteFragment newInstance() {
        ListaClienteFragment fragment = new ListaClienteFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_recycler_lista, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        final ListaClienteRecyclerAdapter adapter = new ListaClienteRecyclerAdapter(getActivity().getApplicationContext(), listaClientes, new ListaClienteRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cliente cliente) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CadastroClienteFragment fragment = CadastroClienteFragment.newInstance(cliente);
                fragment.show(fm, "Alterar cliente");
            }
        });

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabClientes = banco.getReference("cliente");

        Query resultado = tabClientes.orderByChild("isAdmin").equalTo(false);

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
                Cliente cliente = snapshot.getValue(Cliente.class);
                listaClientes.add(cliente);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Cliente cliente = dataSnapshot.getValue(Cliente.class);
                for (Cliente cli : listaClientes) {
                    if (cli.getId().equals(cliente.getId())) {
                        cli.setNome(cliente.getNome());
                        cli.setTelefone(cliente.getTelefone());
                        cli.setEndereco(cli.getEndereco());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Cliente cliente = dataSnapshot.getValue(Cliente.class);
                listaClientes.remove(cliente);
                adapter.notifyDataSetChanged();
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
}
