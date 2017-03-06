package com.forcavenda.Fragments.Listas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaClienteAdapter;
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


public class ClienteFragment extends Fragment {

    List<Cliente> listaClientes = new ArrayList<Cliente>();
    private OnFragmentInteractionListener mListener;

    public ClienteFragment() {
    }

    public static ClienteFragment newInstance() {
        ClienteFragment fragment = new ClienteFragment();
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

        Query resultado = tabClientes.orderByChild("nome");

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
            // ....
        });

        FloatingActionButton btn_adicionar = (FloatingActionButton) view.findViewById(R.id.btn_adicionar);

//        btn_adicionar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListaClienteActivity.this, CadastroClienteActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i("Detach", "sas");
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
