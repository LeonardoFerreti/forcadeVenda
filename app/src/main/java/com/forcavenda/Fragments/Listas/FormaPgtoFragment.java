package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaFormaPgtoAdapter;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.Fragments.Cadastros.CadastroFormaPgtoFragment;
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
 * Created by Leo on 23/02/2017.
 */

public class FormaPgtoFragment extends Fragment {
    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();

    public FormaPgtoFragment() {
    }

    public static FormaPgtoFragment newInstance() {
        FormaPgtoFragment fragment = new FormaPgtoFragment();
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
        DatabaseReference tabProdutos = banco.getReference("formaPgto");

        Query resultado = tabProdutos.orderByChild("nome");

        final ListaFormaPgtoAdapter adapter = new ListaFormaPgtoAdapter(getActivity().getApplicationContext(),
                listaFormaPgto);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FormaPgto formaPgto = (FormaPgto) parent.getItemAtPosition(position);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CadastroFormaPgtoFragment fragmentFormaPgto = CadastroFormaPgtoFragment.newInstance(formaPgto);
                fragmentFormaPgto.show(fm, "Cadastrar forma de pagamento");
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
                FormaPgto formaPgto = snapshot.getValue(FormaPgto.class);
                listaFormaPgto.add(formaPgto);
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
}

