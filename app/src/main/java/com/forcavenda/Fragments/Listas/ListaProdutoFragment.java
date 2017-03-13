package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaProdutoRecyclerAdapter;
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

public class ListaProdutoFragment extends Fragment {
    List<Produto> listaProdutos = new ArrayList<Produto>();

    public ListaProdutoFragment() {
    }

    public static ListaProdutoFragment newInstance() {
        ListaProdutoFragment fragment = new ListaProdutoFragment();
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
        final ListaProdutoRecyclerAdapter adapter = new ListaProdutoRecyclerAdapter(getActivity().getApplicationContext(), listaProdutos, new ListaProdutoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Produto produto) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CadastroProdutoFragment fragmentFormaPgto = CadastroProdutoFragment.newInstance(produto);
                fragmentFormaPgto.show(fm, "Alterar produto");
            }
        });

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabClientes = banco.getReference("produto");

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
