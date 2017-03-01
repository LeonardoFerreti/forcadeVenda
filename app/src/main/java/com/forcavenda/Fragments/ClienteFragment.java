package com.forcavenda.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaClienteAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroClienteActivity;
import com.forcavenda.Telas.Listas.ListaClienteActivity;
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

    public static ClienteFragment newInstance( ) {
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

        View view = inflater.inflate(R.layout.activity_lista, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ListView listView = (ListView) view.findViewById(R.id.lista);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabClientes = banco.getReference("cliente");

        Query resultado = tabClientes.orderByChild("nome");

        final ListaClienteAdapter adapter = new ListaClienteAdapter(getActivity().getApplicationContext(),
                listaClientes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cliente cliente = (Cliente) parent.getItemAtPosition(position);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CadastroClienteFragment fragment = CadastroClienteFragment.newInstance(cliente);
                fragment.show(fm, "Alterar cliente");
            }
        });

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
                listaClientes.remove(cliente);
                listaClientes.add(cliente);
                adapter.notifyDataSetChanged();
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
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
