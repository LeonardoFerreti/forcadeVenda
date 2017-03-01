package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.forcavenda.Adapters.ResumoItensVendaAdapter;
import com.forcavenda.Adapters.SpinnerFormaPgtoAdapter;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class FinalizaPedidoFragment extends Fragment {
    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();
    List<ItemPedido> listaItensPedido = new ArrayList<ItemPedido>();
    Spinner cboFormaPgto;
    ProgressBar progressBar;
    ResumoItensVendaAdapter resumoItensVendaAdapter;
    ListView listaResumoItens;
    TextView lblValorTotal;
    Button btn_salvar;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public void setListaItensPedido(List<ItemPedido> listaItensPedido) {
        this.listaItensPedido = listaItensPedido;
        if (resumoItensVendaAdapter != null) {
            resumoItensVendaAdapter = new ResumoItensVendaAdapter(getActivity().getApplicationContext(),
                    listaItensPedido);
            listaResumoItens.setAdapter(resumoItensVendaAdapter);
            resumoItensVendaAdapter.notifyDataSetChanged();
            lblValorTotal.setText(formatter.format(resumoItensVendaAdapter.getValorTotal()));
            btn_salvar.setEnabled(resumoItensVendaAdapter.getCount() > 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finaliza_pedido, container, false);

        cboFormaPgto = (Spinner) view.findViewById(R.id.cboFormapgto);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listaResumoItens = (ListView) view.findViewById(R.id.lista_resumo_pedido);
        btn_salvar = (Button) view.findViewById(R.id.btn_salvar);

        View header = (View) getLayoutInflater(savedInstanceState).inflate(R.layout.lista_resumo_pedido_header, null);
        View footer = (View) getLayoutInflater(savedInstanceState).inflate(R.layout.lista_resumo_pedido_footer, null);

        lblValorTotal = (TextView) footer.findViewById(R.id.lbl_valorTotal);
        lblValorTotal.setText(formatter.format(0));

        listaResumoItens.addHeaderView(header);
        listaResumoItens.addFooterView(footer);

        progressBar.setVisibility(View.VISIBLE);

        DefineAdapterResumo();

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabFormaPgto = banco.getReference("formaPgto");
        Query resultadoFormaPgto = tabFormaPgto.orderByChild("nome");
        final SpinnerFormaPgtoAdapter adapterFormaPgto = new SpinnerFormaPgtoAdapter(getActivity().getApplicationContext(), listaFormaPgto);
        cboFormaPgto.setAdapter(adapterFormaPgto);

        resultadoFormaPgto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultadoFormaPgto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                FormaPgto formaPgto = snapshot.getValue(FormaPgto.class);
                listaFormaPgto.add(formaPgto);
                adapterFormaPgto.notifyDataSetChanged();
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

    private void DefineAdapterResumo() {
        if (resumoItensVendaAdapter == null) {
            resumoItensVendaAdapter = new ResumoItensVendaAdapter(getActivity().getApplicationContext(),
                    listaItensPedido);
            listaResumoItens.setAdapter(resumoItensVendaAdapter);
            resumoItensVendaAdapter.notifyDataSetChanged();
        }
    }
}
