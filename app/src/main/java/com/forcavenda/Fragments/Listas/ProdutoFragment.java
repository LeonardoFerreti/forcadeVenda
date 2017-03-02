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

import com.forcavenda.Adapters.ListaProdutoAdapter;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.Fragments.Cadastros.CadastroProdutoFragment;
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

public class ProdutoFragment extends Fragment {
    List<Produto> listaProdutos = new ArrayList<Produto>();

    public ProdutoFragment() {
    }

    public static ProdutoFragment newInstance() {
        ProdutoFragment fragment = new ProdutoFragment();
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
        DatabaseReference tabClientes = banco.getReference("produto");

        Query resultado = tabClientes.orderByChild("nome");

        final ListaProdutoAdapter adapter = new ListaProdutoAdapter(getActivity().getApplicationContext(),
                listaProdutos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produto = (Produto) parent.getItemAtPosition(position);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CadastroProdutoFragment fragmentFormaPgto = CadastroProdutoFragment.newInstance(produto);
                fragmentFormaPgto.show(fm, "Cadastrar produto");
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
                Produto produto = snapshot.getValue(Produto.class);
                listaProdutos.add(produto);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                for (Produto produtolista: listaProdutos) {
                    if (produtolista.getId().equals(produto.getId())){
                        produtolista.setNome(produto.getNome());
                        produtolista.setPreco(produto.getPreco());
                        produtolista.setDescricao(produto.getDescricao());
                        produtolista.setAtivo(produto.getAtivo());
                        break;
                    }
                }
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

        return view;
    }
}
